package com.onthegomap.flatmap.openmaptiles;

import static com.onthegomap.flatmap.openmaptiles.Expression.*;
import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.onthegomap.flatmap.config.Arguments;
import com.onthegomap.flatmap.util.FileUtils;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.apache.commons.text.StringEscapeUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

public class Generate {

  private static final Logger LOGGER = LoggerFactory.getLogger(Generate.class);

  private static record OpenmaptilesConfig(
    OpenmaptilesTileSet tileset
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static record OpenmaptilesTileSet(
    List<String> layers,
    String version,
    String attribution,
    String name,
    String description,
    List<String> languages
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static record LayerDetails(
    String id,
    String description,
    Map<String, JsonNode> fields,
    double buffer_size
  ) {}

  private static record Datasource(
    String type,
    String mapping_file
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static record LayerConfig(
    LayerDetails layer,
    List<Datasource> datasources
  ) {}

  private static record Imposm3Column(
    String type,
    String name,
    String key,
    boolean from_member
  ) {}

  static record Imposm3Filters(
    JsonNode reject,
    JsonNode require
  ) {}

  static record Imposm3Table(
    String type,
    @JsonProperty("_resolve_wikidata") boolean resolveWikidata,
    List<Imposm3Column> columns,
    Imposm3Filters filters,
    JsonNode mapping,
    Map<String, JsonNode> type_mappings
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  private static record Imposm3Mapping(
    Map<String, Imposm3Table> tables
  ) {}

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Yaml yaml;

  static {
    var options = new LoaderOptions();
    options.setMaxAliasesForCollections(1_000);
    yaml = new Yaml(options);
  }

  private static <T> T load(URL url, Class<T> clazz) throws IOException {
    LOGGER.info("reading " + url);
    try (var stream = url.openStream()) {
      Map<String, Object> parsed = yaml.load(stream);
      return mapper.convertValue(parsed, clazz);
    }
  }

  static <T> T parseYaml(String string, Class<T> clazz) {
    Map<String, Object> parsed = yaml.load(string);
    return mapper.convertValue(parsed, clazz);
  }

  static JsonNode parseYaml(String string) {
    return string == null ? null : parseYaml(string, JsonNode.class);
  }

  public static void main(String[] args) throws IOException {
    Arguments arguments = Arguments.fromJvmProperties();
    String tag = arguments.get("tag", "openmaptiles tag to use", "v3.12.2");
    String base = "https://raw.githubusercontent.com/openmaptiles/openmaptiles/" + tag + "/";
    var rootUrl = new URL(base + "openmaptiles.yaml");
    OpenmaptilesConfig config = load(rootUrl, OpenmaptilesConfig.class);

    List<LayerConfig> layers = new ArrayList<>();
    Set<String> mappingFiles = new LinkedHashSet<>();
    for (String layerFile : config.tileset.layers) {
      URL layerURL = new URL(base + layerFile);
      LayerConfig layer = load(layerURL, LayerConfig.class);
      layers.add(layer);
      for (Datasource datasource : layer.datasources) {
        if ("imposm3".equals(datasource.type)) {
          String mappingPath = Path.of(layerFile).resolveSibling(datasource.mapping_file).normalize().toString();
          mappingFiles.add(base + mappingPath);
        } else {
          LOGGER.warn("Unknown datasource type: " + datasource.type);
        }
      }
    }

    Map<String, Imposm3Table> tables = new LinkedHashMap<>();
    for (String uri : mappingFiles) {
      Imposm3Mapping layer = load(new URL(uri), Imposm3Mapping.class);
      tables.putAll(layer.tables);
    }

    String packageName = "com.onthegomap.flatmap.openmaptiles.generated";
    String[] packageParts = packageName.split("\\.");
    Path output = Path.of("openmaptiles", "src", "main", "java")
      .resolve(Path.of(packageParts[0], Arrays.copyOfRange(packageParts, 1, packageParts.length)));

    FileUtils.deleteDirectory(output);
    Files.createDirectories(output);

    emitLayerDefinitions(config.tileset, layers, packageName, output);
    emitTableDefinitions(tables, packageName, output);
  }

  private static String GENERATED_FILE_HEADER = """
    /*
    Copyright (c) 2016, KlokanTech.com & OpenMapTiles contributors.
    All rights reserved.

    Code license: BSD 3-Clause License

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

    * Neither the name of the copyright holder nor the names of its
      contributors may be used to endorse or promote products derived from
      this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

    Design license: CC-BY 4.0

    See https://github.com/openmaptiles/openmaptiles/blob/master/LICENSE.md for details on usage
    */
    // AUTOGENERATED BY Generate.java -- DO NOT MODIFY
    """;

  private static void emitTableDefinitions(Map<String, Imposm3Table> tables, String packageName, Path output)
    throws IOException {
    StringBuilder tablesClass = new StringBuilder();
    tablesClass.append("""
      %s
      package %s;

      import static com.onthegomap.flatmap.openmaptiles.Expression.*;

      import com.graphhopper.reader.ReaderRelation;
      import com.onthegomap.flatmap.openmaptiles.Expression;
      import com.onthegomap.flatmap.openmaptiles.MultiExpression;
      import com.onthegomap.flatmap.read.OpenStreetMapReader;
      import com.onthegomap.flatmap.FeatureCollector;
      import com.onthegomap.flatmap.reader.SourceFeature;
      import java.util.ArrayList;
      import java.util.HashMap;
      import java.util.HashSet;
      import java.util.List;
      import java.util.Map;
      import java.util.Set;

      public class Tables {
        public interface Row {
          SourceFeature source();
        }
        public interface Constructor {
          Row create(SourceFeature source, String mappingKey);
        }
        public interface RowHandler<T extends Row> {
          void process(T element, FeatureCollector features);
        }
      """.formatted(GENERATED_FILE_HEADER, packageName));

    List<String> classNames = new ArrayList<>();
    Map<String, String> fieldNameToType = new TreeMap<>();
    for (var entry : tables.entrySet()) {
      String key = entry.getKey();
      Imposm3Table table = entry.getValue();
      List<OsmTableField> fields = getFields(table);
      for (var field : fields) {
        String existing = fieldNameToType.get(field.name);
        if (existing == null) {
          fieldNameToType.put(field.name, field.clazz);
        } else if (!existing.equals(field.clazz)) {
          throw new IllegalArgumentException(
            "Field " + field.name + " has both " + existing + " and " + field.clazz + " types");
        }
      }
      Expression mappingExpression = parseImposm3MappingExpression(table);
      String mapping = "public static final Expression MAPPING = %s;".formatted(
        mappingExpression
      );
      String className = lowerUnderscoreToUpperCamel("osm_" + key);
      if (!"relation_member".equals(table.type)) {
        classNames.add(className);

        tablesClass.append("""
          public static record %s(%s) implements Row, %s {
            public %s(SourceFeature source, String mappingKey) {
              this(%s);
            }
            %s
            public interface Handler {
              void process(%s element, FeatureCollector features);
            }
          }
          """.formatted(
          className,
          fields.stream().map(c -> "@Override " + c.clazz + " " + lowerUnderscoreToLowerCamel(c.name))
            .collect(joining(", ")),
          fields.stream().map(c -> lowerUnderscoreToUpperCamel("with_" + c.name))
            .collect(joining(", ")),
          className,
          fields.stream().map(c -> c.extractCode).collect(joining(", ")),
          mapping,
          className
        ).indent(2));
      }
    }

    tablesClass.append(fieldNameToType.entrySet().stream().map(e -> """
      public static interface %s {
        %s %s();
      }
      """.formatted(
      lowerUnderscoreToUpperCamel("with_" + e.getKey()),
      e.getValue(),
      lowerUnderscoreToLowerCamel(e.getKey())
    )).collect(joining("\n")).indent(2));

    tablesClass.append("""
      public static final MultiExpression<Constructor> MAPPINGS = MultiExpression.of(Map.ofEntries(
        %s
      ));
      """.formatted(
      classNames.stream().map(className -> "Map.entry(%s::new, %s.MAPPING)".formatted(className, className))
        .collect(joining(",\n")).indent(2).strip()
    ).indent(2));

    String handlerClassCondition = classNames.stream().map(className ->
      """
        if (handler instanceof %s.Handler typedHandler) {
          result.computeIfAbsent(%s.class, cls -> new HashSet<>()).add(typedHandler.getClass());
        }""".formatted(className, className)
    ).collect(joining("\n"));
    tablesClass.append("""
      public static Map<Class<? extends Row>, Set<Class<?>>> generateHandlerClassMap(List<?> handlers) {
        Map<Class<? extends Row>, Set<Class<?>>> result = new HashMap<>();
        for (var handler : handlers) {
          %s
        }
        return result;
      }
      """.formatted(handlerClassCondition.indent(8).trim()));

    String handlerCondition = classNames.stream().map(className ->
      """
        if (handler instanceof %s.Handler typedHandler) {
          result.computeIfAbsent(%s.class, cls -> new ArrayList<>()).add((RowHandler<%s>) typedHandler::process);
        }""".formatted(className, className, className)
    ).collect(joining("\n"));
    tablesClass.append("""
        public static Map<Class<? extends Row>, List<RowHandler<? extends Row>>> generateDispatchMap(List<?> handlers) {
          Map<Class<? extends Row>, List<RowHandler<? extends Row>>> result = new HashMap<>();
          for (var handler : handlers) {
            %s
          }
          return result;
        }
      }
      """.formatted(handlerCondition.indent(6).trim()));
    Files.writeString(output.resolve("Tables.java"), tablesClass);
  }

  static Expression parseImposm3MappingExpression(Imposm3Table table) {
    if (table.type_mappings != null) {
      return or(
        table.type_mappings.entrySet().stream().map(entry ->
          parseImposm3MappingExpression(entry.getKey(), entry.getValue(), table.filters)
        ).toList()
      ).simplify();
    } else {
      return parseImposm3MappingExpression(table.type, table.mapping, table.filters);
    }
  }

  static Expression parseImposm3MappingExpression(String type, JsonNode mapping, Imposm3Filters filters) {
    return and(
      or(parseExpression(mapping).toList()),
      and(filters == null || filters.require == null ? List.of() : parseExpression(filters.require).toList()),
      not(or(filters == null || filters.reject == null ? List.of() : parseExpression(filters.reject).toList())),
      matchType(type.replaceAll("s$", ""))
    ).simplify();
  }

  private static List<OsmTableField> getFields(Imposm3Table tableDefinition) {
    List<OsmTableField> result = new ArrayList<>();
    boolean relationMember = "relation_member".equals(tableDefinition.type);
    for (Imposm3Column col : tableDefinition.columns) {
      if (relationMember && col.from_member) {
        continue;
      }
      switch (col.type) {
        case "id", "validated_geometry", "area", "hstore_tags", "geometry" -> {
          // do nothing - already on source feature
        }
        case "member_id", "member_role", "member_type", "member_index" -> {
          // do nothing
        }
        case "mapping_key" -> result
          .add(new OsmTableField("String", col.name, "mappingKey"));
        case "mapping_value" -> result
          .add(new OsmTableField("String", col.name, "source.getString(mappingKey)"));
        case "string" -> result
          .add(new OsmTableField("String", col.name,
            "source.getString(\"%s\")".formatted(Objects.requireNonNull(col.key, col.toString()))));
        case "bool" -> result
          .add(new OsmTableField("boolean", col.name,
            "source.getBoolean(\"%s\")".formatted(Objects.requireNonNull(col.key, col.toString()))));
        case "integer" -> result
          .add(new OsmTableField("long", col.name,
            "source.getLong(\"%s\")".formatted(Objects.requireNonNull(col.key, col.toString()))));
        case "wayzorder" -> result.add(new OsmTableField("int", col.name, "source.getWayZorder()"));
        case "direction" -> result.add(new OsmTableField("int", col.name,
          "source.getDirection(\"%s\")".formatted(Objects.requireNonNull(col.key, col.toString()))));
        default -> throw new IllegalArgumentException("Unhandled column: " + col.type);
      }
    }
    result.add(new OsmTableField("com.onthegomap.flatmap.reader.SourceFeature", "source", "source"));
    return result;
  }

  private static record OsmTableField(
    String clazz,
    String name,
    String extractCode
  ) {}

  private static void emitLayerDefinitions(OpenmaptilesTileSet info, List<LayerConfig> layers, String packageName,
    Path output)
    throws IOException {
    StringBuilder schemaClass = new StringBuilder();
    schemaClass.append("""
      %s
      package %s;

      import static com.onthegomap.flatmap.openmaptiles.Expression.*;
      import com.onthegomap.flatmap.Arguments;
      import com.onthegomap.flatmap.monitoring.Stats;
      import com.onthegomap.flatmap.openmaptiles.MultiExpression;
      import com.onthegomap.flatmap.openmaptiles.Layer;
      import com.onthegomap.flatmap.Translations;
      import java.util.List;
      import java.util.Map;
      import java.util.Set;

      public class OpenMapTilesSchema {
        public static final String NAME = %s;
        public static final String DESCRIPTION = %s;
        public static final String VERSION = %s;
        public static final String ATTRIBUTION = %s;
        public static final List<String> LANGUAGES = List.of(%s);

        public static List<Layer> createInstances(Translations translations, Arguments args, Stats stats) {
          return List.of(
            %s
          );
        }
      """
      .formatted(
        GENERATED_FILE_HEADER,
        packageName,
        quote(info.name),
        quote(info.description),
        quote(info.version),
        quote(info.attribution),
        info.languages.stream().map(Generate::quote).collect(joining(", ")),
        layers.stream()
          .map(
            l -> "new com.onthegomap.flatmap.openmaptiles.layers.%s(translations, args, stats)"
              .formatted(lowerUnderscoreToUpperCamel(l.layer.id)))
          .collect(joining(",\n"))
          .indent(6).trim()
      ));
    for (var layer : layers) {
      String layerName = layer.layer.id;
      String className = lowerUnderscoreToUpperCamel(layerName);

      StringBuilder fields = new StringBuilder();
      StringBuilder fieldValues = new StringBuilder();
      StringBuilder fieldMappings = new StringBuilder();

      layer.layer.fields.forEach((name, value) -> {
        JsonNode valuesNode = value.get("values");
        List<String> valuesForComment = valuesNode == null ? List.of() : valuesNode.isArray() ?
          iterToList(valuesNode.elements()).stream().map(Objects::toString).toList() :
          iterToList(valuesNode.fieldNames());
        String javadocDescription = escapeJavadoc(getFieldDescription(value));
        fields.append("""
          %s
          public static final String %s = %s;
          """.formatted(
          valuesForComment.isEmpty() ? "/** %s */".formatted(javadocDescription) : """

            /**
             * %s
             * <p>
             * allowed values:
             * <ul>
             * %s
             * </ul>
             */
            """.stripTrailing().formatted(javadocDescription,
            valuesForComment.stream().map(v -> "<li>" + v).collect(joining("\n * "))),
          name.toUpperCase(Locale.ROOT),
          quote(name)
        ).indent(4));

        List<String> values = valuesNode == null ? List.of() : valuesNode.isArray() ?
          iterToList(valuesNode.elements()).stream().filter(JsonNode::isTextual).map(JsonNode::textValue)
            .map(t -> t.replaceAll(" .*", "")).toList() :
          iterToList(valuesNode.fieldNames());
        if (values.size() > 0) {
          fieldValues.append(values.stream()
            .map(v -> "public static final String %s = %s;"
              .formatted(name.toUpperCase(Locale.ROOT) + "_" + v.toUpperCase(Locale.ROOT).replace('-', '_'), quote(v)))
            .collect(joining("\n")).indent(2).strip()
            .indent(4));
          fieldValues.append("public static final Set<String> %s = Set.of(%s);".formatted(
            name.toUpperCase(Locale.ROOT) + "_VALUES",
            values.stream().map(Generate::quote).collect(joining(", "))
          ).indent(4));
        }

        if (valuesNode != null && valuesNode.isObject()) {
          MultiExpression<String> mapping = generateFieldMapping(valuesNode);
          fieldMappings.append("    public static final MultiExpression<String> %s = %s;\n"
            .formatted(lowerUnderscoreToUpperCamel(name), generateCode(mapping)));
        }
      });

      schemaClass.append("""
        /** %s */
        public interface %s extends Layer {
          double BUFFER_SIZE = %s;
          String LAYER_NAME = %s;
          @Override
          default String name() {
            return LAYER_NAME;
          }
          final class Fields {
            %s
          }
          final class FieldValues {
            %s
          }
          final class FieldMappings {
            %s
          }
        }
        """.formatted(
        escapeJavadoc(layer.layer.description),
        className,
        layer.layer.buffer_size,
        quote(layerName),
        fields.toString().strip(),
        fieldValues.toString().strip(),
        fieldMappings.toString().strip()
      ).indent(2));
    }

    schemaClass.append("}");
    Files.writeString(output.resolve("OpenMapTilesSchema.java"), schemaClass);
  }

  static MultiExpression<String> generateFieldMapping(JsonNode valuesNode) {
    MultiExpression<String> mapping = MultiExpression.of(new LinkedHashMap<>());
    valuesNode.fields().forEachRemaining(entry -> {
      String field = entry.getKey();
      JsonNode node = entry.getValue();
      Expression expression = or(parseExpression(node).toList()).simplify();
      if (!expression.equals(or()) && !expression.equals(and())) {
        mapping.expressions().put(field, expression);
      }
    });
    return mapping;
  }

  private static Stream<Expression> parseExpression(JsonNode node) {
    if (node.isObject()) {
      List<String> keys = iterToList(node.fieldNames());
      if (keys.contains("__AND__")) {
        if (keys.size() > 1) {
          throw new IllegalArgumentException("Cannot combine __AND__ with others");
        }
        return Stream.of(and(parseExpression(node.get("__AND__")).toList()));
      } else if (keys.contains("__OR__")) {
        if (keys.size() > 1) {
          throw new IllegalArgumentException("Cannot combine __OR__ with others");
        }
        return Stream.of(or(parseExpression(node.get("__OR__")).toList()));
      } else {
        return iterToList(node.fields()).stream().map(entry -> {
          String field = entry.getKey();
          List<String> value = toFlatList(entry.getValue()).map(JsonNode::textValue).filter(Objects::nonNull).toList();
          return value.isEmpty() || value.contains("__any__") ? matchField(field) : matchAny(field, value);
        });
      }
    } else if (node.isArray()) {
      return iterToList(node.elements()).stream().flatMap(Generate::parseExpression);
    } else if (node.isNull()) {
      return Stream.empty();
    } else {
      throw new IllegalArgumentException("parseExpression input not handled: " + node);
    }
  }

  private static Stream<JsonNode> toFlatList(JsonNode node) {
    return node.isArray() ? iterToList(node.elements()).stream().flatMap(Generate::toFlatList) : Stream.of(node);
  }

  private static String generateCode(MultiExpression<String> mapping) {
    return "MultiExpression.of(Map.ofEntries(" + mapping.expressions().entrySet().stream()
      .map(s -> "Map.entry(%s, %s)".formatted(quote(s.getKey()), s.getValue()))
      .collect(joining(", ")) + "))";
  }

  private static String lowerUnderscoreToLowerCamel(String name) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
  }

  private static String lowerUnderscoreToUpperCamel(String name) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
  }

  private static <T> List<T> iterToList(Iterator<T> iter) {
    List<T> result = new ArrayList<>();
    iter.forEachRemaining(result::add);
    return result;
  }

  private static final Parser parser = Parser.builder().build();
  private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

  private static String escapeJavadoc(String description) {
    Node document = parser.parse(description);
    return renderer.render(document).replaceAll("[\n\r*\\s]+", " ");
  }

  private static String getFieldDescription(JsonNode value) {
    if (value.isTextual()) {
      return value.textValue();
    } else {
      return value.get("description").textValue();
    }
  }

  static String quote(String other) {
    if (other == null) {
      return "null";
    }
    return '"' + StringEscapeUtils.escapeJava(other) + '"';
  }
}
