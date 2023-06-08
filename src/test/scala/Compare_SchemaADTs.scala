import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SkeuomorphAvroSchema}
import org.apache.avro.{Schema ⇒ ApacheAvroSchema}

/*import matryoshka._
// NOTE: need this to avoid error of "No implicits found for Corecursive[T]" when doing anamorphism
// MEANING: Fix needs to implement BirecursiveT typeclass
import matryoshka.data._
import matryoshka.implicits._*/


import scala.jdk.CollectionConverters._

/**
 *
 */
object Compare_SchemaADTs extends App {

  def avroFToApache: Algebra[SkeuomorphAvroSchema, ApacheAvroSchema] =
    Algebra {
      case SkeuomorphAvroSchema.TNull() => ApacheAvroSchema.create(ApacheAvroSchema.Type.NULL)
      case SkeuomorphAvroSchema.TBoolean() => ApacheAvroSchema.create(ApacheAvroSchema.Type.BOOLEAN)
      case SkeuomorphAvroSchema.TInt() => ApacheAvroSchema.create(ApacheAvroSchema.Type.INT)
      case SkeuomorphAvroSchema.TLong() => ApacheAvroSchema.create(ApacheAvroSchema.Type.LONG)
      case SkeuomorphAvroSchema.TFloat() => ApacheAvroSchema.create(ApacheAvroSchema.Type.FLOAT)
      case SkeuomorphAvroSchema.TDouble() => ApacheAvroSchema.create(ApacheAvroSchema.Type.DOUBLE)
      case SkeuomorphAvroSchema.TBytes() =>  ApacheAvroSchema.create(ApacheAvroSchema.Type.BYTES)
      case SkeuomorphAvroSchema.TString() => ApacheAvroSchema.create(ApacheAvroSchema.Type.STRING)

      // TODO check how to create simple record (non-named) in apache avro? https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211-L215
        //import ApacheAvroSchema.{Field ⇒ ApacheField}
      // TODO Help to create apache avro field because it requires a schemaavro as parameter while the skeuo doesn't take schema and passing it in the algebra function is weird (repetitive / cheating)
      // NOTE look here field2Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L44
      /*case SkeuomorphAvroSchema.TRecord(name, namespaceOpt, aliases, docOpt, fields) ⇒ ApacheAvroSchema.createRecord(fields)

      val r = new ApacheField()
      ApacheField()*/
      // Named record = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L217-L225
      // TODO is the apache named record == skeuomorph's TNamedType ? https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211

      // TODO create union = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254

      // TODO // case SkeuomorphAvroSchema.TNamedType(_, _) => false
      // TODO array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
      /*case SkeuomorphAvroSchema.TArray(_) => ApacheAvroSchema.Type.ARRAY
      // TODO create map = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
      case SkeuomorphAvroSchema.TMap(_) => ApacheAvroSchema.Type.MAP
      case SkeuomorphAvroSchema.TRecord(name, namespace, _, doc, fields) =>
        (sch.getName should_== name)
          .and(sch.getNamespace should_== namespace.getOrElse(""))
          .and(sch.getDoc should_== doc.getOrElse(""))
          .and(
            sch.getFields.asScala.toList.map(f => (f.name, f.doc)) should_== fields
              .map(f => (f.name, f.doc.getOrElse("")))
          )*/

      // Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
      case SkeuomorphAvroSchema.TEnum(name, namespace, aliases, doc, symbols) => ApacheAvroSchema.createEnum(name,
        doc.getOrElse("NO DOC"),
        namespace.getOrElse("NO NAMESPACE"),
        symbols.asJava
      )
      // TODO check meaning of aliases vs. symbols - which contains the enum subcases?

      //case SkeuomorphAvroSchema.TUnion(_) => true

      // TODO - how to match correctly?
      // source apache fixed = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L258
      case SkeuomorphAvroSchema.TFixed(name, namespaceOpt, aliases, size) =>  ApacheAvroSchema.createFixed(name, "NO DOC", namespaceOpt.getOrElse("NO SPACE"), size)

      case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
  }



  val strApache: ApacheAvroSchema = ApacheAvroSchema.create(ApacheAvroSchema.Type.STRING)
  val enumApache: ApacheAvroSchema = ApacheAvroSchema.createEnum("Color", "doc", "namespace", List("red", "yellow", "blue").asJava)

  //import matryoshka.patterns.EnvT
  //import matryoshka.patterns.EnvT
  import matryoshka._
  import patterns.EnvT
  type Path = List[String]
  trait SchemaF[A]
  type Labelled[A] = EnvT[Path, SchemaF, A]

  def labelledToSchema: Algebra[Labelled, ApacheAvroSchema] = { envT =>

    val path: Seq[String] = envT.ask
    val low: SchemaF[ApacheAvroSchema] = envT.lower
  }
    // TODO left off here check what is EnvT lower https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/2-avro.scala#L94


  // Apache avro schema
  println(s"apache avro string: $strApache")
  println(s"apache avro enum: $enumApache")
  println(s"enumApache.toString = ${enumApache.toString}")

  // apacheavroschema --> skeuomorph avro schema
  val strSkeuo: Fix[SkeuomorphAvroSchema] = scheme.ana(SkeuomorphAvroSchema.fromAvro).apply(strApache)
  println(s"skeuo avro string = $strSkeuo")
  println(s"skeuo avro string (toString) = ${strSkeuo.toString}")

  val enumSkeuo: Fix[SkeuomorphAvroSchema] = scheme.ana(SkeuomorphAvroSchema.fromAvro).apply(enumApache)
  println(s"skeuo avro enum: ${enumSkeuo.toString}")
  println(s"skeuo avro enum unfix: ${enumSkeuo.unfix}")

  // inverse:
  val strApacheBack: ApacheAvroSchema = scheme.cata(avroFToApache).apply(strSkeuo)
  println(s"strApacheBack = $strApacheBack ")
  val enumApacheBack: ApacheAvroSchema = scheme.cata(avroFToApache).apply(enumSkeuo) // assert is equal to enumApache
  println(s"enumApacheBack = $enumApacheBack")


  println("\nPrinting apache avro from zio")
  import zio.schema.DeriveSchema
  import zio.schema.codec.AvroCodec


  val schema = DeriveSchema.gen[SpecTestData.CaseObjectsOnlyAdt]
  val result = AvroCodec.encode(schema)
  val adt = AvroCodec.encodeToApacheAvro(schema)

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
