/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package bag6;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class bag extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1515594205494574098L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"bag\",\"namespace\":\"bag6\",\"fields\":[{\"name\":\"array_element\",\"type\":[\"null\",\"string\"],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<bag> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<bag> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<bag> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<bag> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<bag> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this bag to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a bag from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a bag instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static bag fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.CharSequence array_element;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public bag() {}

  /**
   * All-args constructor.
   * @param array_element The new value for array_element
   */
  public bag(java.lang.CharSequence array_element) {
    this.array_element = array_element;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return array_element;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: array_element = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'array_element' field.
   * @return The value of the 'array_element' field.
   */
  public java.lang.CharSequence getArrayElement() {
    return array_element;
  }


  /**
   * Sets the value of the 'array_element' field.
   * @param value the value to set.
   */
  public void setArrayElement(java.lang.CharSequence value) {
    this.array_element = value;
  }

  /**
   * Creates a new bag RecordBuilder.
   * @return A new bag RecordBuilder
   */
  public static bag6.bag.Builder newBuilder() {
    return new bag6.bag.Builder();
  }

  /**
   * Creates a new bag RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new bag RecordBuilder
   */
  public static bag6.bag.Builder newBuilder(bag6.bag.Builder other) {
    if (other == null) {
      return new bag6.bag.Builder();
    } else {
      return new bag6.bag.Builder(other);
    }
  }

  /**
   * Creates a new bag RecordBuilder by copying an existing bag instance.
   * @param other The existing instance to copy.
   * @return A new bag RecordBuilder
   */
  public static bag6.bag.Builder newBuilder(bag6.bag other) {
    if (other == null) {
      return new bag6.bag.Builder();
    } else {
      return new bag6.bag.Builder(other);
    }
  }

  /**
   * RecordBuilder for bag instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<bag>
    implements org.apache.avro.data.RecordBuilder<bag> {

    private java.lang.CharSequence array_element;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(bag6.bag.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.array_element)) {
        this.array_element = data().deepCopy(fields()[0].schema(), other.array_element);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing bag instance
     * @param other The existing instance to copy.
     */
    private Builder(bag6.bag other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.array_element)) {
        this.array_element = data().deepCopy(fields()[0].schema(), other.array_element);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'array_element' field.
      * @return The value.
      */
    public java.lang.CharSequence getArrayElement() {
      return array_element;
    }


    /**
      * Sets the value of the 'array_element' field.
      * @param value The value of 'array_element'.
      * @return This builder.
      */
    public bag6.bag.Builder setArrayElement(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.array_element = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'array_element' field has been set.
      * @return True if the 'array_element' field has been set, false otherwise.
      */
    public boolean hasArrayElement() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'array_element' field.
      * @return This builder.
      */
    public bag6.bag.Builder clearArrayElement() {
      array_element = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public bag build() {
      try {
        bag record = new bag();
        record.array_element = fieldSetFlags()[0] ? this.array_element : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<bag>
    WRITER$ = (org.apache.avro.io.DatumWriter<bag>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<bag>
    READER$ = (org.apache.avro.io.DatumReader<bag>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    if (this.array_element == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeString(this.array_element);
    }

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (in.readIndex() != 1) {
        in.readNull();
        this.array_element = null;
      } else {
        this.array_element = in.readString(this.array_element instanceof Utf8 ? (Utf8)this.array_element : null);
      }

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (in.readIndex() != 1) {
            in.readNull();
            this.array_element = null;
          } else {
            this.array_element = in.readString(this.array_element instanceof Utf8 ? (Utf8)this.array_element : null);
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










