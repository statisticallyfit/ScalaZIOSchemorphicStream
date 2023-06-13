package conversionsOfSchemaADTs.avro_avro.skeuo_apache.specs


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}




import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
///import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}


import scala.jdk.CollectionConverters._


import testData.ScalaCaseClassData._


import org.scalacheck._
import org.specs2._
import org.specs2.specification.core.SpecStructure


import conversionsOfSchemaADTs.avro_avro.Skeuo_Apache.{apacheToSkeuoAvroSchema, skeuoToApacheAvroSchema}
import conversionsOfSchemaStrings.avro_json.Skeuo_JsonCirce.{skeuoAvroSchemaToJsonString, jsonStringToSkeuoAvroSchema, avroSchemaToJsonString}


/**
 *
 */
class AvroSchema_SkeuoApacheRoundTrip_Specs extends Specification with ScalaCheck  {

	// OUTPUT:
	/*
	apache avro String (string): "string"
	(ARRAY AVRO APACHE-STRING) apache avro Array (string): {"type":"array","items":"int"}
	apache avro Enum (string): {"type":"enum","name":"Color","namespace":"namespace","doc":"doc","symbols":["red","yellow","blue"]}
	(ARRAY JSON CIRCE): arrayApache -> arraySkeuo adt -> array Json circe: {
	"type" : "array",
	"items" : "Int"
	}
	skeuo avro string = TString()
	(ARRAY AVRO SKEUO-ADT): skeuo avro array = TArray(TInt())
	skeuo avro enum: TEnum(Color,Some(namespace),List(),Some(doc),List(red, yellow, blue))
	skeuo avro enum unfix: TEnum(Color,Some(namespace),List(),Some(doc),List(red, yellow, blue))
	strApacheBack = "string"
	Inside avroFToApache ARRAY converter:
	apacheSchema = "int"
	apacheSchema.getType = INT
	arrayApacheBack = {"type":"array","items":"int"}
	enumApacheBack = {"type":"enum","name":"Color","namespace":"namespace","doc":"doc","symbols":["red","yellow","blue"]}
	 */

	// TODO import instances = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/avro/AvroSchemaSpec.scala#L20

	def is: SpecStructure =
		s2"""
			Converting between Skeuomorph and Apache libraries' avro schema ADTs (using spec-example-based-testing)
		  """




	import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._
	
	// TODO next steps:
	// 1) add more apache-avro-schema data
	// 2) run this file, get the results, assert as printed
	// 3) do the PROPS with scalacheck

	// Apache avro schema
	println(s"apache avro String (string): $strApache")
	println(s"(ARRAY AVRO APACHE-STRING) apache avro Array (string): $arrayApache")
	println(s"apache avro Enum (string): $enumApache")

	// SchemaApacheAvro --> skeuomorph avro schema
	// NOTE noting the types here when applying ana / cata morphisms
	/*val schemeAna: SchemaApacheAvro ⇒ Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro)
	val avroToJson: Fix[SchemaSkeuoAvro] ⇒ Json = scheme.cata(SchemaSkeuoAvro.toJson)*/


	// NOTE: not implemented in skeuo code
	//println(s"ENUM JSON: enumApache -> skeuoAvro -> Json circe = ${avroToJson(schemeAna(enumApache))}")
	println(s"(ARRAY JSON CIRCE): arrayApache -> arraySkeuo adt -> array Json circe: ${avroSchemaToJsonString(apacheToSkeuoAvroSchema(arrayApache))}")

	//scheme.ana(SchemaSkeuoAvro.fromAvro).apply(strApache)
	val strSkeuo: Fix[SchemaSkeuoAvro] = apacheToSkeuoAvroSchema(strApache)
	println(s"skeuo avro string = $strSkeuo")

	// scheme.ana(SchemaSkeuoAvro.fromAvro).apply(arrayApache)
	val arraySkeuo: Fix[SchemaSkeuoAvro] = apacheToSkeuoAvroSchema(arrayApache)
	println(s"(ARRAY AVRO SKEUO-ADT): skeuo avro array = $arraySkeuo")






	// scheme.ana(SchemaSkeuoAvro.fromAvro).apply(enumApache)
	// scheme.ana(coalgebra_ApacheSkeuo).apply(enumApache)
	val enumSkeuo: Fix[SchemaSkeuoAvro] = apacheToSkeuoAvroSchema(enumApache)
	println(s"skeuo avro enum: ${enumSkeuo.toString}")
	println(s"skeuo avro enum unfix: ${enumSkeuo.unfix}")

	// inverse:

	//scheme.cata(algebra_SkeuoApache).apply(strSkeuo)
	val strApacheBack: SchemaApacheAvro = skeuoToApacheAvroSchema(strSkeuo)
	println(s"strApacheBack = $strApacheBack ")
	//scheme.cata(algebra_SkeuoApache).apply(arraySkeuo)
	val arrayApacheBack: SchemaApacheAvro = skeuoToApacheAvroSchema(arraySkeuo)
	println(s"arrayApacheBack = $arrayApacheBack")
	//scheme.cata(algebra_SkeuoApache).apply(enumSkeuo)
	val enumApacheBack: SchemaApacheAvro = skeuoToApacheAvroSchema(enumSkeuo)
	println(s"enumApacheBack = $enumApacheBack")


	// --------------------------------------------------------------------------------------------------------------------------------



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
