package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.dataFile_RawCityMeshMeasurements

import zio._
import zio.schema.{DeriveSchema, TypeId, Schema ⇒ SchemaZIO}
import SchemaZIO._
import zio.schema.codec.AvroCodec

import conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.dataFile_RawCityMeshMeasurements.RawCityMeshMeasurementsSchema._

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}

/**
 *
 */
object ZioConvertCaseClassToAvro extends App {

	val schemaZioCityMesh: SchemaZIO[RawCityMeshMeasurementsSchema] = DeriveSchema.gen[RawCityMeshMeasurementsSchema]
	println(s"zio schema = ${schemaZioBACKCityMesh}")
	
	// the string avro schema
	val schemaStrCityMesh: scala.Either[String, String] = AvroCodec.encode(schemaZioCityMesh)
	println(s"string avro schema = ${schemaStrCityMesh.getOrElse(None)}")
	println(s"string avro schema = ${schemaStrCityMesh.right.getOrElse(None)}")
	
	// the apache avro schema
	val schemaApacheCityMesh: scala.Either[String, SchemaAvro_Apache] = AvroCodec.encodeToApacheAvro(schemaZioCityMesh)
	println(s"apache avro schema = ${schemaApacheCityMesh.getOrElse(None)}")
	println(s"apache avro schema = ${schemaApacheCityMesh.right.getOrElse(None)}")
	
	// back to zio schema (assert)
	val schemaZioBACKCityMesh: scala.Either[String, SchemaZIO[_]] = AvroCodec.decodeFromApacheAvro(schemaApacheCityMesh.getOrElse(None))
	println(s"zio schema BACK = ${schemaZioBACKCityMesh.getOrElse(None)}")
	
	
	
}
