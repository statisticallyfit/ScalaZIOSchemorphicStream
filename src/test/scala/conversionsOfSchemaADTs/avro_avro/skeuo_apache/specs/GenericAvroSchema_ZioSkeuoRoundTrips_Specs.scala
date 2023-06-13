package conversionsOfSchemaADTs.avro_avro.skeuo_apache.specs


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}



import zio.schema._
import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType} // TODO move the zio <-> apache tests in the other file
import zio.schema.codec.AvroCodec



import org.apache.avro.{Schema ⇒ SchemaApacheAvro}


import org.scalatest.Assertions._
import org.scalacheck._
import org.specs2._
import org.specs2.specification.core.SpecStructure


import testData.ScalaCaseClassData._
import testData.schemaData.genericSchemaData.zioData.ZioGenericSchemaData._
import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._

/**
 *
 */
class GenericAvroSchema_ZioSkeuoRoundTrips_Specs extends Specification with ScalaCheck {
	
	
	def is: SpecStructure =
		s2"""
			Converting between ZIO's generic-schema-ADT and Skeuomorph's avro-schema-ADT
		"""
	
	
	println("\n----------------------------------------------------------------")
	println("Printing : scala case class --> zio schema --> apache avro string ")
	
	
	// TODO left off here - use another case class (fruit banana example)
	//val tangeloZioSchema: ZioSchema[Tangelo] = DeriveSchema.gen[Tangelo]
	val tangeloApacheAvroSchema: Either[String, SchemaApacheAvro] = AvroCodec.encodeToApacheAvro(tangeloZioSchema)
	val tangeloApacheAvroString: Either[String, String] = AvroCodec.encode(tangeloZioSchema)
	
	println(s"zioschema tangelo = $tangeloZioSchema")
	println(s"avro adt tangelo (apache) = $tangeloApacheAvroSchema")
	println(s"avro string tangelo = $tangeloApacheAvroString")
	
	
	/// --------------------------------------------
	
	
	//val arrayZio: Either[String, Any] = apacheAvroSchemaToZioSchema(arrayApache)
	val arrayZio2: Either[String, ZioSchema[_]] = AvroCodec.decodeFromApacheAvro(arrayApache)
	//println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio")
	println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio2")

}
