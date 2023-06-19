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
	
	// TODO how to concat files resulting from the avro-tools' conversion of .avdl to .avsc?
	val fileType: String = ".avsc"
	val names: List[String] = List("Aq_Msm", /*"Location",*/ "Sensor", "Sensor_type", "Sensordatavalues_record")
	
	
	//val avroFile = new java.io.File(filePath + name)
	val avroStrFiles: List[String] = names.map(_ + fileType)
	val avroStrFilePaths: List[String] = names.map(n ⇒ filePath + n + fileType)
	val avroStrFileObjs: List[File] = avroStrFilePaths.map(new File(_))
	
	// Convert .avsc to schema-avro
	val parserApacheStrToADT: SchemaAvro_Apache.Parser = new SchemaAvro_Apache.Parser()
	
	
	//new SchemaAvro_Apache(SchemaAvro_Apache.Type.RECORD)
	// TODO how to check that Names contains key?
	
	val parsedApaches: Map[String, SchemaAvro_Apache] = avroStrFileObjs.map{fileObj ⇒ (fileObj.getName → parserApacheStrToADT.parse(fileObj)) }.toMap
	
	// TODO why doesn't see it as Map already (beacuse of usage of -> ??) why have to declare .toMap explicitly?
	
	/*val parsedSensor: Option[SchemaAvro_Apache] = parsedApaches.get("Sensor" + fileType)
	val tempMapPrintoutSensor = Map("getDoc" → parsedSensor.getDoc,
		"getFields" → parsedSensor.getFields,
		"getField(\"manufacturer\")" → parsedSensor.getField("sensor_type"),
		"field doc" → parsedSensor.getField("sensor_type").doc(),
		"field order" → parsedSensor.getField("sensor_type").order(),
		"field schema" → parsedSensor.getField("sensor_type").schema(),
		"field aliases" → parsedSensor.getField("sensor_type").aliases(),
		"field defaultval" → parsedSensor.getField("sensor_type").defaultVal(),
		"field hasdefaultval" → parsedSensor.getField("sensor_type").hasDefaultValue,
		"field objectprops" → parsedSensor.getField("sensor_type").getObjectProps
	)
	println(tempMapPrintoutSensor)*/
	
	// Convert apache schema -> skeuo schema
	val skeuoSchemas: Map[String, Fix[SchemaAvro_Skeuo]] = parsedApaches.map{ case (name, parsedApache) ⇒ (name → apacheToSkeuoAvroSchema(parsedApache)) }
	
	// Converting backward
	val avrosBack: Map[String, SchemaAvro_Apache] = skeuoSchemas.map{ case (name, apacheSchema) ⇒ (name → skeuoToApacheAvroSchema(apacheSchema)) }
	
	// Get reference to source object so can close again when done reading the file contents
	val bufferedSourceList: List[BufferedSource] = avroStrFilePaths.map((path: String) ⇒ Source.fromFile(path))
	val avroStrList: List[String] = bufferedSourceList.map(buf ⇒ buf.getLines().mkString)
	// Closing each buffered source
	bufferedSourceList.map(buf ⇒ buf.close)
	
	
	println(s"ORIGINAL CONTENTS OF .AVSC FILE (FROM .AVDL): ${avroStrList}")
	println(s"\n\nCONVERTED: ORIGINAL .AVSC ---> APACHE SCHEMA: ${parsedApaches}")
	println(s"\n\nAPACHE ---> SKEUO: ${skeuoSchemas}")
	println(s"\n\nSKEUO ---> APACHE back: $avrosBack")
	
	println(s"apache schema == apache back: ${parsedApaches == avrosBack}")
	//println(s" (str) apache schema == apache back: ${parsedApaches.toString == avrosBack.toString}")
	
}
