package conversionsOfSchemaADTs.avro_avro.skeuo_apache.specs


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.droste.implicits._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}

import scala.io.BufferedSource
///import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}


import scala.jdk.CollectionConverters._


import testData.ScalaCaseClassData._


//import org.scalacheck._
//import org.specs2._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should._
//import org.specs2.specification.core.SpecStructure


import conversionsOfSchemaADTs.avro_avro.Skeuo_Apache._ //{apacheToSkeuoAvroSchema, skeuoToApacheAvroSchema}
import conversionsOfSchemaStrings.avro_json.Skeuo_JsonCirce._
//{skeuoAvroSchemaToJsonString, jsonStringToSkeuoAvroSchema, avroSchemaToJsonString}


import utilTest.Util

import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._
import testData.GeneralTestData._

import java.io.File
import scala.io.Source
/**
 *
 */
object try_AvroToSkeuo_tatidatafile extends App {

	
	val filePath: String = "/development/projects/statisticallyfit/github/learningdataflow/SchaemeowMorphism/src/test/scala/testData/testDataPrivateTati/asset-schemas/sdp-asset-schemas-luftdaten/src/main/airquality/lft.aq_msm/lft.aq_msm.datasource/avro/"
	
	val name = "Sensor.avsc"
	
	//val avroFile = new java.io.File(filePath + name)
	val avroFile: String = filePath + name
	val avroFileObj: File = new File(avroFile)
	
	// Convert .avsc to schema-avro
	val parsedAvroSchemaFromFile: SchemaAvro_Apache = new SchemaAvro_Apache.Parser().parse(avroFileObj)
	
	// Convert apache schema -> skeuo schema
	val skeuoSchema: Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema(parsedAvroSchemaFromFile)
	
	// Converting backward
	val avroBack: SchemaAvro_Apache = skeuoToApacheAvroSchema(skeuoSchema)
	
	// Get reference to source object so can close again when done reading the file contents
	val srcObj: BufferedSource = Source.fromFile(avroFile)
	val avroStr: String = srcObj.getLines().mkString
	srcObj.close // closing the file
	
	println(s"original avro string = ${avroStr}")
	println(s"converted avro string to APACHE SCHEMA = ${parsedAvroSchemaFromFile}")
	println(s"apache avro schema to SKEUO schema = ${skeuoSchema}")
	println(s"skeuo -> apache back: $avroBack")
	
	
	
}
