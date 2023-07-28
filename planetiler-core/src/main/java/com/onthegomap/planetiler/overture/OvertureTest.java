package com.onthegomap.planetiler.overture;

import com.onthegomap.planetiler.FeatureCollector;
import com.onthegomap.planetiler.FeatureMerge;
import com.onthegomap.planetiler.Planetiler;
import com.onthegomap.planetiler.Profile;
import com.onthegomap.planetiler.VectorTile;
import com.onthegomap.planetiler.config.Arguments;
import com.onthegomap.planetiler.geo.GeometryException;
import com.onthegomap.planetiler.reader.SourceFeature;
import com.onthegomap.planetiler.util.Downloader;
import java.util.List;


public class OvertureTest implements Profile {
  private static final List<String> files = List.of(
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_0100b1b9-31ab-4d03-9a92-8141dcae93a5",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_0c3a0082-4509-4d9a-812c-a88574c248c8",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_10dcb45e-6078-4875-8a88-52d12f8ab8c5",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_1744f6c1-a9f1-44ad-b857-25450cbe5f84",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_396e41da-f316-4502-ae56-32f9317e6383",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_3b7d7eb3-dd9c-442a-a9b9-404dc936c5d9",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_4560cc21-a7e0-40e7-bebe-2d19db445d10",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_49005169-4d7b-4ce6-94bd-4d78d022b7f0",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_493c0769-00a9-46cb-a86c-d92badfde317",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_57472892-d4fa-4c25-ba3e-7a76da30f311",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_6194f239-f53a-4f7a-a18d-844d65743ec5",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_61f7299b-3606-4181-a7c6-69ea1aac3a28",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_699b878b-12d3-4229-bdea-3d473a4c2161",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_7c7218a6-5008-4639-8dd1-83c3dd339843",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_7dc54ea9-ab92-4741-8dae-56fa8593fe87",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_7f63afbc-a937-4a81-a641-76b1c4873382",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_93613538-4394-423c-9801-e78bd9990077",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_a374e245-14d8-4d98-a405-9b15caddfad6",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_a6909589-1b62-42a0-9a8f-b1eb2f1c5098",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_b0b8f7e1-cfa0-461c-8bc8-3f5cdd91c89a",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_b629fbf5-9571-4ff1-8db6-f813a16157d0",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_beb3ee79-4796-4088-b92f-2c48c80c4143",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_c045daa2-19ca-42cf-8fb2-0a29d9142116",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_c31fcd15-d578-45ed-bdff-739415154198",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_c5c77b7b-e0de-4001-af9b-a3627c32cce7",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_d0765388-e086-474a-9d5a-c15cb2c08176",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_d3e98a46-301e-49c7-a9d7-ca3c80c0a8d8",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_efa7e14e-b7cc-403e-84c9-29b6343dabf8",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_fd2a04db-9317-4cfd-b02e-b7a4337dc91d",
    "theme=admins/type=administrativeBoundary/20230725_211237_00132_5p54t_fef965a3-b003-48f8-8188-1c8c7b233ccb",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_0fa79fec-7a39-4f51-90a6-aa94b553befd",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_25816df1-b864-49c0-a9a3-a13da4f37a90",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_27c6842b-85ea-4c1b-8557-6bf624ef40eb",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_2a32a61f-fde0-44d0-92a0-da19890fe6d5",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_2d36b134-7e29-4b30-9820-2e96cec914c6",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_364a3443-0868-44ce-a1fa-080c875f569b",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_38ef7a79-a2c5-4fbc-900b-4da561907b35",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_3ab157c6-65e2-45f5-8755-5fc3616da69c",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_44dfa1f2-b07c-4849-ab40-1161237ee7ba",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_50659010-f73e-418d-9073-43e09ed1340e",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_66f751a6-909f-4f62-a882-8fc52e9c6070",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_863b0a4b-3b99-4ce4-81e6-14bd6d2e7900",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_8bc41f71-e2e5-414d-8b8b-47838b38b47c",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_8ec97150-2af9-4f95-b8d4-3d1069b9b9b1",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_9834870f-1352-4cd8-b368-dddedcb60492",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_9be940e5-0894-4273-9b70-0cc6c7125e78",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_a09f88de-3255-44db-96fc-58240eacd2b4",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_a7a2923e-eef8-4af0-9c73-9873d5ec4b5b",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_a7dc6141-7682-48e0-8abb-4fa50160e3d4",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_bdd68c0b-c1cc-426b-a425-c6003252c2d9",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_c030ff2c-f89d-4b2f-87e7-f443fa7c1b20",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_c0b1ebbb-5dd2-4129-bcaa-d3a3c6995da0",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_c1c26ebb-625f-4e96-9b34-bee82321e24f",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_d5a9dd57-f533-466c-b1cd-928ef84d1307",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_d61c9b8c-1222-4835-a115-f45614a31b6a",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_d74f9d4a-54d2-4035-9f7b-651353a3934e",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_e3dabde9-bfcb-43f1-9398-b88a15b6561a",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_e608b636-1b9e-4e3c-ad00-d93fb09ed323",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_e8e4e379-51ad-43e8-887b-e727eddaf13f",
    "theme=admins/type=locality/20230725_211237_00132_5p54t_eb1d890c-bf41-455d-8c69-0418dbfa6deb",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_00e93efa-24f4-4014-b65a-38c7d2854ab4",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_0743451b-3437-430d-a53e-a80984cb7d73",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_0e778d88-49b1-4c3e-a4aa-030eb577ebb4",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_0eb57855-f854-47b0-a38b-c248804e8222",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_142f876e-aaaf-4c15-a27f-9ee8d5d6831e",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_16f45659-3b23-45d4-a286-343ca6588586",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_170e6e44-8c5f-40dd-9b0b-281a8574ee8f",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_18b31283-046c-4d5c-8781-432212da8af3",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_19033d64-aa0d-4452-a57b-881be0e9627e",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_1bcbacda-2909-4c58-bfef-1e43951f6eb6",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_1d174ef1-dd7c-45f5-9431-81b4dd8411a3",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_1d65a4ac-16e8-49a1-a3a0-a1e2aca72c74",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_1dbc92a9-9bb9-404c-99a5-f14ff648eca1",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_1fcd2c5a-d043-49cd-97ae-5203558cf6a7",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_201930f0-b6c6-4849-979e-88b3b21b8672",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2161a94f-857c-4cbe-86ab-15f4be7c7d35",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_23f1c4b3-97f1-4341-a02d-131fb4138b06",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_24c09b19-6d8e-4976-83cb-3cb51678a22c",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2868d394-7788-446b-83e3-671f6d63cd40",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2a8621dd-ea8d-46c2-b3d3-2caebab533a2",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2ba21d03-772b-4453-95f9-50fd8152f3b9",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2bca4205-53b4-4c74-9305-ee7ae57da067",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2da8cd2e-623f-4d37-96eb-1cc15a55f6ed",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_2e659c1e-0975-4142-add8-458ffd16d550",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_317b0dff-bac6-4317-8b39-a222756ef6e5",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_32a8b1e8-3c11-4469-8a67-a22b11f9b4d9",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_337e79dd-0198-4237-a8d5-e65babe1af18",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_35ce229c-7f30-419f-addb-41abe9dfc877",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_383cd8b4-136c-479e-b026-a5a0591b96ea",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_3a265171-441a-4108-a298-f2724c4567ca",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_3ce7a9d0-0a91-41c6-80c8-8121e0edd8f4",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_3d373263-80d6-47ab-a1cc-47bf23c50ecb",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_429747ee-1d9e-409d-a6d3-ec293fc4d68b",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_45b1c6b6-e699-481a-8092-5488068e34d6",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_498b7931-9b43-4eb1-9da2-d995d3032a60",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_4b588bf5-8fb5-4ffa-b451-0c2f12648643",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_4dc6891c-f25b-47f5-83f8-64cb6c31d476",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_4f4e4984-cfdb-4640-830e-5805eb0627c4",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_502d032e-2bf9-453b-a4bb-af33ab6d9daa",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_545781f2-efb6-4ea2-a9a0-b91ec5451b73",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_546e17eb-0214-4205-a173-74c6180f5467",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_58f3983e-7655-4a46-8e0c-5527d4d9e870",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_59160cd7-f19b-4806-83b2-200b5d14df17",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_5ba11533-d1a8-416f-9c6d-e8390eb6386a",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_5e02b9a3-f430-4a78-b886-41844d16cca0",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_5ef05d8c-f7cd-4e4c-a493-fc1fe7458585",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_5f8e2d88-86c3-4106-94f6-cd857c58c4ba",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_60fce9df-fbd8-436c-9f0f-a44c0792a417",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_63b26e81-4d80-4d36-b5fd-dcadfc70957c",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_68447be0-c067-40a3-8627-82659c723c79",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_6a8dd31f-0a60-4d32-830e-0b66fbde3d64",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_6c9de2ed-ea68-42ee-8c38-9739eafc29a3",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_6ed400a8-8b57-47ea-b1cf-b32ae44330d9",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_70d856e4-1126-4997-86db-ae792e2c246f",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_72d91b5f-c33d-40e7-9df3-703b268bc3ed",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_732a7836-7733-43b0-98c7-7fde503ad887",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_73a0d042-a184-4639-b03c-b526a233df77",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_74494bd4-86ff-47e8-bda5-8649ef1a832e",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_75500547-7d37-4eab-89ee-205e5f018b37",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_75f289c7-9ba0-4fb7-a35e-e8d263b61dca",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_76406ec7-b142-4232-b3ff-f57342d129ec",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7a21972f-f40c-42bf-8d2c-9c5237c263ab",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7b2b866d-73be-4a6b-82ea-338e4efd4dab",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7b99085d-1fab-4a88-8d7c-989332599247",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7d00c15a-97f9-4497-8b72-eeb6aa6f5d20",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7da5b4b8-952b-4815-8ec1-9cb10ae4b038",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_7e0a4ce9-2c0b-40cf-ad91-4b37de0bdc14",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_8b590512-3e53-47f3-9854-b99f246b418b",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_9780f848-4fd2-43f8-82e6-58b09a6f6862",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_99c95261-d5ad-4fa5-ab5e-9a93861c29de",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_9a267136-5e70-48b2-993d-5139be3b695b",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_9ceccf19-9ebe-4a48-8788-fbe031eaea4d",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_9e8f1fe3-11d3-406c-968e-98ddcd7c9f78",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_a057cf11-7601-43e0-aad9-db82ec71d1db",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_a071b293-dd36-45f0-bf38-153d56554529",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_a69debf5-a538-407a-93bd-cb7293090185",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_a9fee5fa-b8ad-4330-979c-eac469c9fdce",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_aaaca043-e37f-409a-869d-7bf4926f899c",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ad45683d-c24c-4fef-8569-cc1d48318522",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ae6b950d-6160-4341-9487-751e7e0f8f79",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b1460c30-163c-44cb-8599-a8a68c2ffc30",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b3636f25-196e-4ff1-bbe6-97ed5591c5e5",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b60b574e-1b40-4e44-8963-e500101ee0c0",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b6be95ed-df68-47d5-86fd-709b7af125e5",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b830cd64-d3dc-4a34-bb84-08e068cfc163",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b8d26db6-9686-4a3d-92d1-827b55053451",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b95d355a-b594-437f-b1b2-500162cbc636",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_b98ea0ed-3109-4bc5-b9ba-7244bd825857",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_bcd647bf-0bc3-4b45-a914-28d59a992c15",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c0841271-76c7-4d94-aeea-2d63fe9e3480",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c3161f97-0de2-4141-9d0a-987394060151",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c39b517a-5b57-4e3b-bc4b-528099fd0997",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c6501a7c-f79a-4928-b2f7-9180f43046dd",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c74f75aa-e33f-44f3-a15a-beff3b425179",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c843adcb-8d10-48e4-b476-212d71b61af1",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_c95a8578-9ecb-4eae-a53e-c154fc138009",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_cb60d49e-a2df-4014-83c7-3668cc3d6a3e",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ce1defc0-17b2-4c09-88c0-97a48f541ade",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_cf5fba32-6f57-4f8d-9ac1-ee175c4dd0ff",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d1a29bd4-ae5f-4af8-9226-f20fa84b77cc",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d1c2dcd1-89ff-4a89-8134-1075b08cbd78",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d3576c95-6252-45c2-bd0a-e01e2f0540f1",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d387a0d1-03d4-4751-b56c-4abe015ab753",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d5b1a70b-812b-4eda-b64d-e8e2a05d3cd4",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d6b3ff3b-7c33-4579-a4d7-ac72ecb042ab",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_d85067dd-c1d0-4df0-b3cc-f295672bef30",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_dc752766-bea4-4e73-b0cb-f8449082c9e6",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_dd53094d-d12b-41b8-b535-d8aeac5c97cc",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e131f82a-7884-4a66-8f25-2f6d44928872",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e28014ac-657e-4d9e-9744-179aea885077",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e435f651-2aef-45f9-83eb-defc7afd58d2",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e540e2da-f80b-483c-9f2b-8cd43fa52271",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e87f7d4d-82d0-4143-b0d6-a82e4675ebdd",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_e94885a6-b762-4597-a1c3-8b6a1f956796",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_eaf49d22-1aa8-44db-a778-7ae05d9be529",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_eb7d56ac-d444-4b81-8bf7-27b28d6210a6",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ee3db7c6-c76b-4671-a14a-46879257b90b",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ee792775-d04b-43b1-b712-9de7a29e7ad1",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_fcccf7b0-969f-43cd-9005-ef5f5cd3ed25",
    "theme=buildings/type=building/20230725_211555_00082_tpd52_ffe10440-d4ef-474f-aff8-99bd14a9517d",
    "theme=places/type=place/20230725_210643_00079_ayc64_01c760ca-02aa-4387-8b71-b2eaa6c7c700",
    "theme=places/type=place/20230725_210643_00079_ayc64_023fe3f2-d72a-40b6-9eb9-4bb1b61664d6",
    "theme=places/type=place/20230725_210643_00079_ayc64_15b8943c-63b7-45c8-99fd-82a63affb530",
    "theme=places/type=place/20230725_210643_00079_ayc64_2088ce07-cd07-4188-86f9-70ddc600d953",
    "theme=places/type=place/20230725_210643_00079_ayc64_25c9ad4f-9c04-4485-b77c-6b707e574bc3",
    "theme=places/type=place/20230725_210643_00079_ayc64_3ea51517-e19f-4b0d-abdf-4bb0adf93b2c",
    "theme=places/type=place/20230725_210643_00079_ayc64_3eb62b69-1e51-4418-b040-f20dec443529",
    "theme=places/type=place/20230725_210643_00079_ayc64_406f76e6-8475-45dd-9890-96158149ebf7",
    "theme=places/type=place/20230725_210643_00079_ayc64_42983b77-e638-4365-9748-44be3a463021",
    "theme=places/type=place/20230725_210643_00079_ayc64_536928b2-b04d-4a92-ba02-6efdf7090119",
    "theme=places/type=place/20230725_210643_00079_ayc64_5b7efef7-f232-4442-a3f9-cf7d40ad0322",
    "theme=places/type=place/20230725_210643_00079_ayc64_623f786a-63e9-4f7e-8ea4-907626c0b2e6",
    "theme=places/type=place/20230725_210643_00079_ayc64_65bc6681-e96f-4a69-9e07-410e452d6ad2",
    "theme=places/type=place/20230725_210643_00079_ayc64_75db186a-38c6-4a41-9d3e-ce5e012da76f",
    "theme=places/type=place/20230725_210643_00079_ayc64_7a0b789f-05cf-4f8f-9b98-46563a81a942",
    "theme=places/type=place/20230725_210643_00079_ayc64_7ea6ab77-85f8-435d-b73c-2f30a026ada0",
    "theme=places/type=place/20230725_210643_00079_ayc64_85b0d784-b3d7-44d9-a50c-402f7c5d56da",
    "theme=places/type=place/20230725_210643_00079_ayc64_8e9ef564-7a2f-42d4-97b6-3ca77ec830e8",
    "theme=places/type=place/20230725_210643_00079_ayc64_923feef3-e974-4379-ab12-19bf41bc42fc",
    "theme=places/type=place/20230725_210643_00079_ayc64_95042c29-0438-46c4-ad60-cacf42ccf6c7",
    "theme=places/type=place/20230725_210643_00079_ayc64_95413b43-f25f-4b90-984e-00e32521c511",
    "theme=places/type=place/20230725_210643_00079_ayc64_973e6f70-8037-465c-bfb6-a29db6491710",
    "theme=places/type=place/20230725_210643_00079_ayc64_a38d99ff-b93e-47f7-ac74-7554958822d5",
    "theme=places/type=place/20230725_210643_00079_ayc64_ae5a4c6e-2a1a-48bb-b621-f8820faf5dd8",
    "theme=places/type=place/20230725_210643_00079_ayc64_b2a9472e-e6f9-4060-a272-35c0571e75b9",
    "theme=places/type=place/20230725_210643_00079_ayc64_c089686d-4a53-42b3-95d4-071ffacc191d",
    "theme=places/type=place/20230725_210643_00079_ayc64_c28a03b6-2f0d-468d-a9d7-fd655afbd04d",
    "theme=places/type=place/20230725_210643_00079_ayc64_d1281c94-aacc-42ae-8852-905117201521",
    "theme=places/type=place/20230725_210643_00079_ayc64_d2488fa7-c51b-4fca-b6f4-168af8fbf9fa",
    "theme=places/type=place/20230725_210643_00079_ayc64_fa803010-a326-4119-8d5a-c4d9173205a7",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_01b086fc-f35b-487c-8d4e-5cdbbdc1785d",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_04666f3f-6b03-4858-a5cf-bbc78261c990",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_09d87f2e-6269-4504-94fc-36355d9e5a45",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_1b9ed71a-1f7e-45e7-99c8-b081ec72cd65",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_1c9eb648-9770-4858-abbb-7ed860b4c97e",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_23265fd0-aa39-4044-8674-aa5d308539e4",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_2b94257f-352c-4d90-b3fa-7a3fb69db9a3",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_2f57a6ca-b11b-4162-96ee-88e66dd84dde",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_34b84fb0-b18e-4ee1-89d3-d4e38c5daefc",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_36779f1d-5c78-4acc-b68e-7012d6b65f4a",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_3e16b8c5-ffa0-4e95-b1fe-5400212ba0ed",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_50a8c15d-95b8-48c9-a310-67ccccdc6c07",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_5b7ce7b0-2e90-4556-866f-b33d3bc31024",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_6d053df7-9052-451f-9a53-6da4d8084453",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_7327f9ce-c6b0-4f81-b775-168d6f278dec",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_7ba27f2e-8624-4a7c-b955-1a2f2f3bc3d8",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_7ea5128f-6b57-41f9-a4c6-9894b724a3ba",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_8a7084e3-3353-43ce-bae5-a3e524f5f992",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_b954383f-0d79-4713-a79a-0ed0ba88c942",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_d48c8b2b-8c97-4354-ac7d-842cc222b08e",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_da9fc3a9-ffec-438c-90e6-2a7eed3c07eb",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_e0f6fd9b-dc60-4be0-b8c5-d26d1fb6cf11",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_e23e8a4d-ebe2-47e7-886d-7f4863e70ea4",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_e297cfe7-7ea2-4925-a882-dcdc9beebb01",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_e6399c9f-c016-4b23-af06-6d48e7077b8a",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_ed9b0ec8-ba18-44e2-9df3-ed8243af795f",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_f1b30fe8-eb30-441f-93c1-e9d2a16e1e5f",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_f536adb4-4de7-4d3d-a161-76fae6400765",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_fdbf3b1c-62e9-4765-9a37-678ddd97db79",
    "theme=transportation/type=connector/20230726_134827_00007_dg6b6_fec8dd7d-a55c-4fa4-9ef9-9363fe773c00",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_09951677-6217-4b2f-b579-287ffed08510",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_0cfe1913-2081-4429-b418-858bddb304bc",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_1693b19e-bbc7-4aa9-b734-62f7f5cb38ac",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_1d3ed5da-0432-4a59-b07c-5aca1036f4a1",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_210fda0f-73c8-4c2b-aa47-c444edf39885",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_2d2e6b2e-d474-43f2-abbe-01391fd75194",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_2d3d2be8-ef7c-4115-80b2-ceaf9cbd968f",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_314bd370-9b99-4352-8592-16eae4d5be95",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_3219c191-54fd-46ae-a9d4-68d9730b8d3f",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_32fc021b-345c-4458-9079-9fd9403ede90",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_392b4f0e-dc71-4430-be66-b1db5d352d2d",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_40c7152a-7d18-4a13-a2de-d3e04b07aa71",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_41040c5c-937e-4738-85d4-6ed1e46f129d",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_49e30c49-b975-48a5-bb13-fe052180289e",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_4c79ff0c-e83a-4765-a8e1-d4b25d4723d8",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_4ce276b7-8c69-4059-932b-221b4596cf2f",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_4eb43e51-f7a2-4eca-a366-87a263a9f1b2",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_51753d30-a51c-4f2e-8289-fef1ef29d529",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_52cf7eb0-fbef-4949-b8de-e92c1f556f74",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_55d23840-d03f-45c5-8e8b-0793fbe9f7ed",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_57d92c82-70c0-4990-b770-37145bdd1254",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_57f816f3-14b8-424c-a090-6c1a53932714",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_5e5b268e-4331-42a9-bd19-0969447951e7",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_6f4db110-ffcb-4dff-9010-bc5c4addb368",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_7350b380-80ea-45ba-a590-1c2585005c1e",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_80861041-029f-4d8f-80fc-ce3483314dad",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_80bf001a-bdaa-4853-ae78-3b20d43a75de",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_8234b3ad-03b2-409d-973d-9506b2ce59f4",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_8622845d-70b2-4f8c-af70-7d18c1c64211",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_913e9a3a-8577-4c0c-8ac4-ded7462bf36e",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_928d18a7-368f-49f2-8437-1a4d94dcf922",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_9439673a-7670-43e0-9bf8-30094cc8cddb",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_9b434703-81cc-4f08-8493-6162e784b58e",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_9ffb6cb5-87a2-4fba-b432-25c29b1c1f99",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_a0a2e81c-6d7e-4e3e-b4ec-9b95328aeb9c",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_a8f1835d-291f-47d6-810f-e518fcb2e002",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_a9145d94-c53a-46c0-9783-ed65b5fdee6a",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_b5213d28-f0ed-46e6-a4ab-e6548bbe6ddb",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_b808abbd-04b9-485c-a5e6-af3638415cbf",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_b81213a4-e6b1-4dbc-969d-545d4805ebf8",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_b82c3d9d-8d79-449a-a137-c2cdd61e07b2",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_bab9255a-7368-4c54-8a24-8b11d653a2d5",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c0ae84af-a1f7-4939-88f0-e4475124c20d",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c3a6ef19-e56f-4699-8b14-b943011112ea",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c4da57d0-4b47-405e-af55-2411e00ac684",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c4fd755c-5aa7-420a-8359-fcf9dc1d467d",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c69c9b8c-d885-4ebf-88d9-d161f6b2f25b",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_c7b65232-6af8-4b40-b40b-ab939731f02f",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_ca5b3fac-ca6c-4e53-90f0-ce5eb2eba6b8",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_cdff831c-a525-4c24-8372-eb797c01453e",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_ce149dcd-2aa6-436a-8fd1-7b8b99a84a38",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_d3220713-c47f-4c53-9e39-45ce04b442be",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_dd12f690-1218-4d00-a04c-933cfbd4dad1",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_def9d38e-303a-4e45-afa3-165d18d0f9bc",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_e3e273a6-7f3d-4d1f-9281-a61ebc1b1d7c",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_e4f9278e-07bd-4b12-adf9-c61d963646b7",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_e8dc4f81-f789-4dd3-9c81-f7f5882e6820",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_efa2a1bf-f9b7-4dbf-af45-44597ccb2d06",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_f10b6d0d-ad81-4b67-bb42-c536cc3ae7ba",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_f86f5f0a-cdb8-4941-9369-4de597c62bc8",
    "theme=transportation/type=segment/20230726_134827_00007_dg6b6_ff9c08ee-1ee7-4ff1-899a-090e9759b6ec"
  );

  public static void main(String[] args) throws Exception {
    var base = java.nio.file.Path.of("data", "sources", "overture");
    var pt = Planetiler.create(Arguments.fromEnvOrArgs(args))
      .addOvertureSource("overture", base)
      .setProfile(new OvertureTest())
      .overwriteOutput(java.nio.file.Path.of("data", "output.pmtiles"));

    var d = Downloader.create(pt.config(), pt.stats());
    for (var s : files) {
      var p = base.resolve(s);
      if (s.contains("admins")) {
        d.add(s, "https://overturemaps-us-west-2.s3.amazonaws.com/release/2023-07-26-alpha.0/" + s, p);
      }
    }
    var begin = pt.stats().startStage("download");
    d.run();
    begin.stop();
    pt.run();
  }

  @Override
  public void processFeature(SourceFeature sourceFeature, FeatureCollector features) {
    switch (sourceFeature.getSourceLayer()) {
      case "admins/administrativeBoundary" -> features.line(sourceFeature.getSourceLayer())
        .setZoomRange(sourceFeature.getLong("adminlevel") < 4 ? 0 : 2, 10)
        .setMinPixelSize(0)
        .inheritAttrFromSource("adminlevel")
        .inheritAttrFromSource("maritime");
      case "admins/locality" -> {
        if (sourceFeature.isPoint())
          (sourceFeature.isPoint() ?
            features.point(sourceFeature.getSourceLayer()) : features.polygon(sourceFeature.getSourceLayer()))
              .setZoomRange(5, 10)
              .inheritAttrFromSource("names")
              .inheritAttrFromSource("subType")
              .inheritAttrFromSource("localityType")
              .inheritAttrFromSource("context");
      }
      case "buildings/building" -> {
      }
      case "places/place" -> {
      }
      case "transportation/connector" -> {
      }
      case "transportation/segment" -> {
      }
    }
  }

  @Override
  public List<VectorTile.Feature> postProcessLayerFeatures(String layer, int zoom, List<VectorTile.Feature> items)
    throws GeometryException {
    if (layer.equals("admins/administrativeBoundary")) {
      return FeatureMerge.mergeLineStrings(items, 0, 0.125, 4, true);
    }
    return items;
  }

  @Override
  public String name() {
    return "Overture";
  }
}
