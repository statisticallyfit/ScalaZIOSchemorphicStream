import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}

import io.circe.Json


/*import matryoshka._
// NOTE: need this to avoid error of "No implicits found for Corecursive[T]" when doing anamorphism
// MEANING: Fix needs to implement BirecursiveT typeclass
import matryoshka.data._
import matryoshka.implicits._*/

import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.LogicalTypes

import zio.schema._
import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType}
import zio.schema.codec.AvroCodec

import scala.jdk.CollectionConverters._


import testData.ScalaCaseClassData._

/**
 *
 */
object Compare_SchemaADTs extends App {


  def avroFToApache: Algebra[SchemaSkeuoAvro, SchemaApacheAvro] =
    Algebra { // Algebra[Skeuo, Apache] --------->  Skeuo[Apache] => Apache
      case SchemaSkeuoAvro.TNull() => SchemaApacheAvro.create(SchemaApacheAvro.Type.NULL)
      case SchemaSkeuoAvro.TBoolean() => SchemaApacheAvro.create(SchemaApacheAvro.Type.BOOLEAN)
      case SchemaSkeuoAvro.TInt() => SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
      case SchemaSkeuoAvro.TLong() => SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG)
      case SchemaSkeuoAvro.TFloat() => SchemaApacheAvro.create(SchemaApacheAvro.Type.FLOAT)
      case SchemaSkeuoAvro.TDouble() => SchemaApacheAvro.create(SchemaApacheAvro.Type.DOUBLE)
      case SchemaSkeuoAvro.TBytes() =>  SchemaApacheAvro.create(SchemaApacheAvro.Type.BYTES)
      case SchemaSkeuoAvro.TString() => SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)
      //SchemaApacheAvro.createArray(apacheSchema.getElementType)
      // TODO figure out why inner part is INT and not array because it is supposed to be array since it is the schema inside.
      // NOTE: see here `toJson` seems like inner part is INT = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L249
      case SchemaSkeuoAvro.TArray(apacheSchema: SchemaApacheAvro) ⇒ {
        println("Inside avroFToApache ARRAY converter: ")
        println(s"apacheSchema = $apacheSchema")
        println(s"apacheSchema.getType = ${apacheSchema.getType}")
        SchemaApacheAvro.createArray(apacheSchema)
      }
      /*apacheSchema.getType match {
        case n @ SchemaApacheAvro.Type.NULL ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(n))
        case b @ SchemaApacheAvro.Type.BOOLEAN ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(b))
        case i @ SchemaApacheAvro.Type.INT ⇒ {
          println(s"apacheSchema = $apacheSchema")
          SchemaApacheAvro.createArray(SchemaApacheAvro.create(i))
        }
        case l @ SchemaApacheAvro.Type.LONG ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(l))
        case f @ SchemaApacheAvro.Type.FLOAT ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(f))
        case d @ SchemaApacheAvro.Type.DOUBLE ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(d))
        case by @ SchemaApacheAvro.Type.BYTES ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(by))
        case s @ SchemaApacheAvro.Type.STRING ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(s))
        case SchemaApacheAvro.Type.MAP ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createMap(apacheSchema.getValueType))
        case SchemaApacheAvro.Type.ARRAY ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createArray(apacheSchema.getElementType))
        case SchemaApacheAvro.Type.RECORD ⇒ {
          val a: SchemaApacheAvro = apacheSchema
          /*val res: SchemaApacheAvro = SchemaApacheAvro.createArray(SchemaApacheAvro.createRecord(a.getName, a.getDoc, a.getNamespace, a.isError, a.getFields))*/
          val res2 = SchemaApacheAvro.createArray(a)
          res2
        } //createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields)
        case SchemaApacheAvro.Type.ENUM ⇒ {
          val a = apacheSchema
          //val ea: SchemaApacheAvro = apacheSchema.asInstanceOf[SchemaApacheAvro.Type.ENUM]
          SchemaApacheAvro.createArray(SchemaApacheAvro.createEnum(a.getName, a.getDoc, a.getNamespace, a.getEnumSymbols))
        }
          //createEnum(String name, String doc, String namespace, List<String> values)
      }*/

      // TODO check how to create simple record (non-named) in apache avro? https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211-L215
      //import SchemaApacheAvro.{Field ⇒ ApacheField}
      // TODO Help to create apache avro field because it requires a schemaavro as parameter while the skeuo doesn't take schema and passing it in the algebra function is weird (repetitive / cheating)
      // NOTE look here field2Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L44
      /*case SchemaSkeuoAvro.TRecord(name, namespaceOpt, aliases, docOpt, fields) ⇒ SchemaApacheAvro.createRecord(fields)

      val r = new ApacheField()
      ApacheField()*/
      // Named record = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L217-L225
      // TODO is the apache named record == skeuomorph's TNamedType ? https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211

      // TODO create union = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254

      // TODO // case SchemaSkeuoAvro.TNamedType(_, _) => false
      // TODO array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
      /*case SchemaSkeuoAvro.TArray(_) => SchemaApacheAvro.Type.ARRAY
      // TODO create map = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
      case SchemaSkeuoAvro.TMap(_) => SchemaApacheAvro.Type.MAP
      case SchemaSkeuoAvro.TRecord(name, namespace, _, doc, fields) =>
        (sch.getName should_== name)
          .and(sch.getNamespace should_== namespace.getOrElse(""))
          .and(sch.getDoc should_== doc.getOrElse(""))
          .and(
            sch.getFields.asScala.toList.map(f => (f.name, f.doc)) should_== fields
              .map(f => (f.name, f.doc.getOrElse("")))
          )*/

      // Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
      case SchemaSkeuoAvro.TEnum(name, namespace, aliases, doc, symbols) => SchemaApacheAvro.createEnum(name,
        doc.getOrElse("NO DOC"),
        namespace.getOrElse("NO NAMESPACE"),
        symbols.asJava
      )
      // TODO check meaning of aliases vs. symbols - which contains the enum subcases?

      //case SchemaSkeuoAvro.TUnion(_) => true

      // TODO - how to match correctly?
      // source apache fixed = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L258
      case SchemaSkeuoAvro.TFixed(name, namespaceOpt, aliases, size) =>  SchemaApacheAvro.createFixed(name, "NO DOC", namespaceOpt.getOrElse("NO SPACE"), size)

      case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
    }




  val strApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)

  val intApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
  val arrayApache: SchemaApacheAvro = SchemaApacheAvro.createArray(intApache)

  val enumApache: SchemaApacheAvro = SchemaApacheAvro.createEnum("Color", "doc", "namespace", List("red", "yellow", "blue").asJava)


  // Apache avro schema
  println(s"apache avro String (string): $strApache")
  println(s"(ARRAY AVRO APACHE-STRING) apache avro Array (string): $arrayApache")
  println(s"apache avro Enum (string): $enumApache")

  // SchemaApacheAvro --> skeuomorph avro schema
  // NOTE noting the types here when applying ana / cata morphisms
  val schemeAna: SchemaApacheAvro ⇒ Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro)
  val avroToJson: Fix[SchemaSkeuoAvro] ⇒ Json = scheme.cata(SchemaSkeuoAvro.toJson)

  // NOTE: not implemented in skeuo code
  //println(s"ENUM JSON: enumApache -> skeuoAvro -> Json circe = ${avroToJson(schemeAna(enumApache))}")
  println(s"(ARRAY JSON CIRCE): arrayApache -> arraySkeuo adt -> array Json circe: ${avroToJson(schemeAna(arrayApache))}")

  val strSkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(strApache)
  println(s"skeuo avro string = $strSkeuo")

  val arraySkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(arrayApache)
  println(s"(ARRAY AVRO SKEUO-ADT): skeuo avro array = $arraySkeuo")

  import testUtil.utilZio.ApacheToZioFunctions._
  val arrayZio: Either[String, Any] = apacheAvroSchemaToZioSchema(arrayApache)
  val arrayZio2: Either[String, ZioSchema[_]] = AvroCodec.decodeFromApacheAvro(arrayApache)
  println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio")
  println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio2")


  val enumSkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(enumApache)
  println(s"skeuo avro enum: ${enumSkeuo.toString}")
  println(s"skeuo avro enum unfix: ${enumSkeuo.unfix}")

  // inverse:
  val schemeCata: Fix[SchemaSkeuoAvro] ⇒ SchemaApacheAvro = scheme.cata(avroFToApache)

  val strApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(strSkeuo)
  println(s"strApacheBack = $strApacheBack ")
  val arrayApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(arraySkeuo)
  println(s"arrayApacheBack = $arrayApacheBack")
  val enumApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(enumSkeuo) // assert is equal to enumApache
  println(s"enumApacheBack = $enumApacheBack")


  // --------------------------------------------------------------------------------------------------------------------------------

  println("\n----------------------------------------------------------------")
  println("Printing : scala case class --> zio schema --> apache avro string ")


  // TODO left off here - use another case class (fruit banana example)
  val schema: ZioSchema[Tangelo] = DeriveSchema.gen[Tangelo]
  val adt: Either[String, SchemaApacheAvro] = AvroCodec.encodeToApacheAvro(schema)
  val result: Either[String, String] = AvroCodec.encode(schema)

  println(s"zioschema tangelo = $schema")
  println(s"avro adt tangelo (apache) = $adt")
  println(s"avro string tangelo = $result")


  // TODO left off here - plan idea outlined here:
  // 1) start from wiem el abadine's schemaF to learn to print out the schema from schmeaF using hylomorphism (trick: migrate matryoshka to droste) since hylo is different.
  // 2) feed it values see print out
  // 3) adapt to the skeuomorph AvroF -> SchemaApacheAvro
  // 4) feed THAT values
  // 5) see the differences between the schema (#1)
  // 5) see how the avro string gets printed from the schema
  // 6) repeat for the skeuomorph json side
  // 7) compare syntax between json/avro strings (#2)


  /**
   * Wiem El Abadine's `SchemaS` = https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/1-schema.scala#L11-L22
   */

  /**
   * Skeuomorph's `AvroF` = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L34-L122
   */

  /**
   * Zio's `Schema` =https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema/shared/src/main/scala/zio/schema/Schema.scala#L287-L373
   */

  // TODO - check what is "Fixed" type in zio / apache avro / skeuomorph - key insight tati says to howt he schemas differ between each other.

  // Fixed builder in org.apache.avro = https://github.com/apache/avro/blob/cdfd66fed1cb366400a41aa7dcbec19d1fad8a09/lang/java/avro/src/main/java/org/apache/avro/SchemaBuilder.java#L291

  // FixedSchema in org.apache.avro = https://github.com/apache/avro/blob/cdfd66fed1cb366400a41aa7dcbec19d1fad8a09/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L1283

  // write fixed from a byte buffer (org.apache avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/Encoder.java#L183
  //read fixed from a byte buffer (org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/Decoder.java#L158

  // GenericFixed (org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/Conversions.java#L103

  // create fixed reader from writerschema (org.apache.avro) =https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/FastReaderBuilder.java#L500

  // writefixed (json encoder file, org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/JsonEncoder.java#L225

  /// ---------------

  // Fixed in skeuomorph (avro adt) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L118

  // Fixed in skeuomorph (mu adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/mu/schema.scala#L40

  // Fixed in skeuomorph (protobuf adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/protobuf/schema.scala#L81

  // Fixed in skeuomorph (protobuf -> mu adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/mu/Transform.scala#L41-L44

  // Fixed (mu -> avro schema) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/avro/Protocol.scala#L136

  // Fixed in skeuomorph (apache avro FIXED --> AvroF Fixed)  = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L229-L235

  // skeuomorph Fixed --> io.circe.Json's Fixed string = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L275-L279
}
