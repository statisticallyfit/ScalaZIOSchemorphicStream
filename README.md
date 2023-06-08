# Roadmap of Tasks

### [1] Compare the different kinds of implementations for each schema language within all libraries

**Reason:** to compare schemas and to compare avro schemas and to compare json schemas across libraries.

**Task component ideas:**

* **Schema (string-schema-language-independent ADT scala structures)**: compare different libraries' implementations of ADTs for **schema**:
  * ZIO library: [`Schema[_]`](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema/shared/src/main/scala/zio/schema/Schema.scala#L287-L373)
   
  * Wiem El Abadine library: [`SchemaF`](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/1-schema.scala#L11-L22).
  
 
* **Json**: compare different libraries' implementations of ADTs for `Json` **schema**:
  * ZIO library:  
    * type-level conversions: 
      * ???
    * value-level conversions:
      * [`def jsonEncoder`, `def jsonDecoder`, `def jsonCodec`](https://github.com/statisticallyfit/zio-schema/blob/main/zio-schema-json/shared/src/main/scala/zio/schema/codec/JsonCodec.scala#L90-L97)
      * [`def primitiveCodec`](https://github.com/statisticallyfit/zio-schema/blob/main/zio-schema-json/shared/src/main/scala/zio/schema/codec/JsonCodec.scala#L115)
      * `object JsonEncoder`: [`def encode`, `def schemaEncoder`, `def transformEncoder`, `def dynamicEncoder`, `def enumEncoder`, `def recordEncoder`](https://github.com/statisticallyfit/zio-schema/blob/main/zio-schema-json/shared/src/main/scala/zio/schema/codec/JsonCodec.scala#L150-L159)
      * `object JsonDecoder`: [`def decode`, `def schemaDecoder`, __, `def dynamicDecoder`, `def enumDecoder`, `def recordDecoder`](https://github.com/statisticallyfit/zio-schema/blob/main/zio-schema-json/shared/src/main/scala/zio/schema/codec/JsonCodec.scala#L436-L452)
    
  * Andy Glow `scala-jsonschema` library: [`Schema`](https://github.com/andyglow/scala-jsonschema#in-lined)
   
  * SKEUO library: [`JsonSchemaF`](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L27)


* **Avro**: compare different libraries' implementations of ADTs for `Avro` **schema**:
  * ZIO library: `Schema[_]`
  * APACHE library: `SchemaAvro`
  * SKEUO library: `AvroF[_]` 
  * Wiem El Abadine's library: [`SchemaF[_]`](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L92).

   
* **Protobuf**: compare different libraries' implementations of ADTs for `Protobuf` **schema**:
  * ZIO library: [`ProtobufCodec`](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-protobuf/shared/src/main/scala/zio/schema/codec/ProtobufCodec.scala) 
  * SKEUO library: [`ProtobufF`](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/protobuf/schema.scala#L61-L93) 
  * 47degrees blog: [`ProtobufF`](https://github.com/statisticallyfit/ScalaCategoryTheory/blob/master/src/main/scala/RecursionSchemeTutorials/FortySevenDegrees/ProtobufDrosteExample/proto/ProtobufF.scala) 
  


**LIST OF LIBRARIES USED**:
- `zio-schema`
- skeuomorph
- Andy Glow's `scala-jsonschema`
- [Wiem el Abadine's matryoshka/schema/avro implementations](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L92)
- [blog 47degrees' `ProtobufF`](https://github.com/statisticallyfit/ScalaCategoryTheory/blob/master/src/main/scala/RecursionSchemeTutorials/FortySevenDegrees/ProtobufDrosteExample/proto/ProtobufF.scala)




### [2] Do round trip conversions

**Reasons:**
* to create seamless conversion between schemas, wherever they come from.
* to create seamless conversion between strings, wherever they come from.

**Task component ideas:**



**LOG OF CONVERSIONS FOUND:**

 
* **Avro**:
   
  * **ADT conversions:**
    * <u>ZIO <--> APACHE</u>
      * :warning: ZIO's `Schema[_]` --> APACHE's `SchemaAvro`: [(function `def encodeToApacheAvro`)](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L33-L34)
      * :warning:  APACHE's `SchemaAvro` --> ZIO's `Schema[_]`: [(function `def toZioSchema`)](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L49-L212)
       
    * <u>ZIO <--> SKEUO</u> 
      * :x: ZIO's `Schema[_]` -->  SKEUOs `AvroF[_]`: _____________________
      * :x: SKEUO's `AvroF[_]` --> ZIO's `Schema[_]`: _____________________
    
    * <u>SKEUO <--> APACHE </u> 
      * :warning: APACHE's `SchemaAvro` --> SKEUO's `AvroF[_]`: [(function `def fromAvro: Coalgebra[AvroF[_], SchemaAvro]`)](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L178-L187)
      * :warning: SKEUO's `AvroF[_]` --> APACHE's `SchemaAvro`: ________ 
        * closest find is [function `def checkSchema: Algebra[AvroF[_], Boolean]` and function `def convertSchema`](https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/avro/AvroSchemaSpec.scala#L35-L42)
         
    * <u>Wiem El Abadine <--> APACHE</u>
      * :warning: [Wiem El Abadine's `SchemaF[_]` --> APACHE's `SchemaAvro`]
  
        * [using `def labelledToSchema: Algebra[Labelled, SchemaAvro]` (has type `Labelled[SchemaAvro] => SchemaAvro`)](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L92-L111)
        * [using `def schemaFToAvro[T](schemaF: T)(implicit T: Recursive.Aux[T, SchemaF]): SchemaAvro`](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L118-L119)
        * [using registry `def toAvro[T](schemaF: T)(implicit T: Recursive.Aux[T, SchemaF]): SchemaAvro`](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L182-L183)
       
      * :warning: APACHE's `SchemaAvro` --> Wiem El Abadine's `SchemaF[_]`: ([function `def avroToSchemaF: CoalgebraM[Option, SchemaF, SchemaAvro]`](https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L188-L206))
       
    * <u>Wiem El Abadine <--> ZIO</u>
      * Wiem El Abadine's `SchemaF[_]` --> ZIO's `Schema[_]
      * ZIO's `Schema[_] --> Wiem El Abadine's `SchemaF[_]`
       
    * :x: <u>Wiem El Abadine <--> SKEUO</u>
        * Wiem El Abadine's `SchemaF[_]` --> SKEUO's `AvroF[_]`
        * SKEUO's `SchemaF[_]` --> Wiem El Abadine's `SchemaF[_]`
       


  * **String conversions:**
    * <u>Scala case class --> `Avro` string</u>: 

      * :warning: (ZIO) [(function `def encode`)](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L30-L31)

      * :warning: (SKEUO)  _______ 
       
      * :warning: (Wiem El Abadine)  ______
 
    * <u>`Avro` string --> Scala case class</u>:
      * :x: ~~ZIO~~
       
      * :warning: (SKEUO)  [`scheme.hylo`, `fromAvro`, `printSchemaAsScala` example](https://hyp.is/928atAU0Ee6NDuOwQg_iUA/higherkindness.github.io/skeuomorph/docs/)
       
      * :x: ~~Wiem El Abadine~~
     
    
     
* **Json**:
   
  * **ADT conversions:**
  * 
    * :x: SKEUO's `JsonSchemaF` <--> ZIO's `Json` schema ADTs.
      * :x: compare: SKEUO's `JsonEncoder`[file](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonEncoders.scala) | [tests](https://github.com/higherkindness/skeuomorph/tree/main/src/test/scala/higherkindness/skeuomorph/openapi) with ZIO's `JsonEncoder`[file](https://github.com/zio/zio-json/tree/series/2.x/zio-json/shared/src/main/scala/zio/json) | [tests](https://github.com/zio/zio-json/blob/series/2.x/zio-json/shared/src/test/scala/zio/json/EncoderSpec.scala)
      * :x: compare: [SKEUO's `JsonDecoder`](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonDecoders.scala) with [ZIO's `JsonDecoder`](https://github.com/zio/zio-json/tree/series/2.x/zio-json/shared/src/main/scala/zio/json)
       
    * :x: SKEUO's `JsonSchemaF` <--> Andy Glow's `scala-jsonschema`
   

  * **String conversions:**
     
    * SKEUO library:
        * :warning: `JsonSchemaF` --> `io.circe.Json` [(`def render: Algebra[JsonSchemaF, Json]`)](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94)
         
        * (simple types) `JsonSchemaF` <--> `Json` string 
          * :warning: `JsonSchemaF` --> `Json` string: [print json fixed tests examples](https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/JsonSchemaPrintSpecification.scala#L21-L35)
          * :warning: `Json` string --> `JsonSchemaF` [print json fixed tests examples](https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/JsonSchemaDecoderSpecification.scala)
           
        * :warning: `Json` string (value-level) --> Scala case class [using Decoder and Encoder](https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/OpenApiDecoderSpecification.scala)
         
        * :question: [nested objects](https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/NestedObjectSpecification.scala)
     
    * Andy Glow `scala-jsonschema` library: 
      * :warning: [`json.Schema[_]` --> `Json` string](https://github.com/andyglow/scala-jsonschema#in-lined)
    
    * ZIO library:
        * :x: <u>ZIO `Json` schema ADT <--> `Json` string: </u>
          * ZIO's `JsonEncoder` and `JsonDecoder` conversion back and forth to `Json` string: [`def fromJson, def toJson`](https://zio.dev/guides/tutorials/encode-and-decode-json-data/)
          * [`def fromJson`, `def toJson`](https://github.com/zio/zio-json/blob/c036622e17f50da663c010b13d4f3e5b65dbfb10/zio-json/shared/src/main/scala/zio/json/package.scala#L22-L43)
          * [`JsonType`](https://github.com/zio/zio-json/blob/series/2.x/zio-json/shared/src/main/scala/zio/json/ast/JsonType.scala#L40)
          * [`Json` ast](https://github.com/zio/zio-json/blob/series/2.x/zio-json/shared/src/main/scala/zio/json/ast/ast.scala)
          * 
    
      * 

* **Protobuf**:
* 



* scala case class --> json string (andy glow)
* scala case class --> avro string (zio)
* avro string <--> json string (skeuomorph??)
* [SKEUO `Avro` schema --> `io.circe.Json` string](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238-L303)
* [json schema (skeuomorph)](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94) <--> json string

NOTE:
- S = schema
- K = skeuomorph
- A = avro string
- Z = zio
- C = scala case class
- J = json string
- I = circe
- G = Andy Glow

Representations:
- **Json string**
    - :heavy_check_mark: output of: Andy Glow's `scala-jsonschema` library
    - :heavy_check_mark: output of: SKEUO library
    - :x: output of: ZIO library
- **Json schema (ADT)**
    - :heavy_check_mark: Andy Glow's library `scala-jsonschema`
    - :heavy_check_mark: `io.circe` library's `Json`
    - :heavy_check_mark: SKEUO library's `JsonSchemaF`
- **Avro string**
    - :heavy_check_mark: output of: `zio-schema` library
    - :heavy_check_mark: output of: SKEUO library
    - :heavy_check_mark: output of: APACHE library
- **Avro schema (ADT)**
    - :heavy_check_mark: `zio-schema` library's `Schema[_]` 
    - :heavy_check_mark: APACHE library's `SchemaAvro`
    - :heavy_check_mark: SKEUO library's `AvroF[_]`
    - :heavy_check_mark: Wiem El Abadine library's `SchemaF[_]`
- **Protobuf string**:
  - :question: `zio-schema` library 
- **Protobuf schema (ADT)**
  - :question:

```mermaid
flowchart TD
    SK(SKEUO's Avro Schema: AvroF) -->|SKEUO: toJson| SIJ(io.circe's Json)
    SIJ(io.circe's Json) -->|SKEUO: render - line 94| SJ(SKEUO's Json Schema - JsonSchemaF)
    SK(Skeuomorph's Avro Schema: AvroF) -->|SKEUO --???| A(Avro String)
    SA(APACHE's SchemaAvro) -->|SKEUO: fromAvro - line 181| SK(Skeuomorph's Avro Schema: AvroF)
    SZ(SchemaZIO) -->|ZIO| SA(APACHE's SchemaAvro)
    SA(APACHE's SchemaAvro) -->|ZIO| A(Avro String)
    SA(APACHE's SchemaAvro) -->|SKEUO -- ???| C(Scala Case Class / Type)
    A(Avro String) -->|SKEUO| C(Scala Case Class / Type)
    C(Scala Case Class / Type) -->|AndyGlow| SGJ(Andy Glow's Json Schema)
    SGJ(Andy Glow's Json Schema) -->|AndyGlow| J(Json String)

```






### [3] Compare keywords between Avro and Json schemas

**Reason:** to understand better how to convert/morph between  `Avro` and `Json` schemas.

**Description:** Analyze the syntax of both `Avro` and `Json` schemas and compare / contrast them. Must categorize differences in syntax and bridge them. 

Use the ADTs found here as case studies to create tests to categorize differences in syntax between `Avro`, `Json` strings: [AvroCodecSpec adts](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/test/scala-2/zio/schema/codec/AvroCodecSpec.scala#L1956-L1985)

**Task component ideas:**
* `#meta-task` compare json schemas using **Draft06** and **Draft04** etc (source: Andy Glow's `scala-jsonschema` library).
* `#meta-task` make a log of all the syntax differences in `Avro` (canonically formatted) string (from `zio-schema`) versus in `Json` string (from `scala-jsonschema`) (e.g. `name`, `type`, `record`... etc)
Put `hyp.is` link to `Json` schema keywords (defined in an online tutorial of how to write `Json` schema), in the empty code test of the difference between `Json`/`Avro` schema keywords.
* `#example-task` `middleName` field in `Person` class from Andy Glow library when printed as `Json` string, has type `String` whereas the equivalent `Avro` string has type `null`-or-`string` because its scala type is `Option[String]`.


### [4] Create tests documenting how schema-ADTs become schema-strings


**Description:** create tests describing how libraries convert avro/json/protobuf schema-ADTs into avro/json/protobuf strings

**Reason:** to have a catalog of tests that show how each ADT gets matched to string representation.

**Task component ideas:**
* do tests for `enum` type
* do tests for `case class` type
* do tests for primitive types (e.g. `String`, `Option[_]`, `Integer`... etc)

**Sources of inspiration for tests:**
* [ZIO's AvroCodecSpec.scala](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/test/scala-2/zio/schema/codec/AvroCodecSpec.scala)
* [ZIO's DeriveSchemaSpec.scala](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-derivation/shared/src/test/scala/zio/schema/DeriveSchemaSpec.scala)

#### [TASK: 4a] Create tests documenting conversion of `Avro` schema-ADT into `Avro` string.

* [ZIO: `Schema[_]` --> `Avro` string](https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L30-L31)
* 

#### [TASK: 4b] Create tests documenting conversion of `Json` schema-ADT into `Json` string. 

* [SKEUO: `JsonSchemaF` --> `Json` string](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94-L127)
* [Andy Glow's `scala-jsonschema`: `Schema` -->  `Json` string](https://github.com/andyglow/scala-jsonschema#in-lined)


#### [TASK: 4c] Create tests documenting conversion of `Protobuf` schema-ADT into `Protobuf` string.





### [5] Add `Specs2` framework to do the testing

### [6] Study SKEUO library in depth:
**Reasons:**
1. has better / more clearly structured / simpler ADTs
2. has conversion between `Avro` (string? schema?) to `Json` (string? schema?) [(source)](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/Protocol.scala#L45)
3.

**Task component ideas:**
* study the function [`fromAvro`](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L181) (reason: to see how conversion happens between SKEUO's `Avro` schema and the APACHE `Avro` schema. )
* study the SKEUO `Avro` [ADTs](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L196)
* study the function [`toJson`](https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/Protocol.scala#LL44C1-L44C1) (reason: to see how conversion happens between `Avro` (string?) to `Json` (string?))

