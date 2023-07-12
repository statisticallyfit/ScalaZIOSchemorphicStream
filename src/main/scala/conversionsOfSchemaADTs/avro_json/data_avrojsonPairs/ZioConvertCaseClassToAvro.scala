package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs

import conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.dataFile_RawCityMeshMeasurements.Class_RawCityMeshSchema_genFromJson._
import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import zio.schema.codec.AvroCodec
import zio.schema.{DeriveSchema, Schema ⇒ SchemaZIO}

import scala.reflect.runtime.universe._




// HELP: cannot pass the type (class) since making zio derive schema of that passed type T does not work (says it is not a class) TODO how to fix, how to pass just tyep parameter and build the schemazio inside the function?


/**
 * Returns: Unit
 * Prints: the resulting avro schema (from the zio schema, made from the original case class)
 */
object ZioConvertCaseClassToAvro {
	def classToAvroSchema[T: TypeTag](schemaZio: SchemaZIO[T]): Unit = {
		
		//val schemaZio: SchemaZIO[T] = DeriveSchema.gen[typeOf[T]]
		println(s"zio schema = ${schemaZio}")
		
		// the string avro schema
		val schemaStringAvro: scala.Either[String, String] = AvroCodec.encode(schemaZio)
		println(s"string avro schema = ${schemaStringAvro.getOrElse(None)}")
		//println(s"string avro schema = ${schemaStrCityMesh.right.getOrElse(None)}")
		
		// the apache avro schema
		val schemaApacheAvro: scala.Either[String, SchemaAvro_Apache] = AvroCodec.encodeToApacheAvro(schemaZio)
		println(s"apache avro schema = ${schemaApacheAvro.getOrElse(None)}")
		//println(s"apache avro schema = ${schemaApacheCityMesh.right.getOrElse(None)}")
		
		// back to zio schema (assert)
		val NULL_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
		
		// TODO type inside zio schema is not kept!
		
		val schemaZioBACK: scala.Either[String, SchemaZIO[_]] = AvroCodec.decodeFromApacheAvro(schemaApacheAvro.getOrElse(NULL_Apache))
		println(s"zio schema BACK = ${schemaZioBACK.getOrElse(None)}")
		/*val schemaZioCityMesh: SchemaZIO[RawCityMeshMeasurementsSchema] = DeriveSchema.gen[RawCityMeshMeasurementsSchema]
		println(s"zio schema = ${schemaZioCityMesh}")
		
		// the string avro schema
		val schemaStrCityMesh: scala.Either[String, String] = AvroCodec.encode(schemaZioCityMesh)
		println(s"string avro schema = ${schemaStrCityMesh.getOrElse(None)}")
		//println(s"string avro schema = ${schemaStrCityMesh.right.getOrElse(None)}")
		
		// the apache avro schema
		val schemaApacheCityMesh: scala.Either[String, SchemaAvro_Apache] = AvroCodec.encodeToApacheAvro(schemaZioCityMesh)
		println(s"apache avro schema = ${schemaApacheCityMesh.getOrElse(None)}")
		//println(s"apache avro schema = ${schemaApacheCityMesh.right.getOrElse(None)}")
		
		// back to zio schema (assert)
		final val NULL_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
		
		val schemaZioBACKCityMesh: scala.Either[String, SchemaZIO[_]] = AvroCodec.decodeFromApacheAvro(schemaApacheCityMesh.getOrElse(NULL_Apache))
		println(s"zio schema BACK = ${schemaZioBACKCityMesh.getOrElse(None)}")*/
	}
}

/**
 *
 */
object Runner extends App {

	import ZioConvertCaseClassToAvro._
	
	
	classToAvroSchema(DeriveSchema.gen[RawCityMeshMeasurementsSchema])
	
	
	
	
}
