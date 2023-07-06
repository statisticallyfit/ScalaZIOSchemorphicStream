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



//import utilTest.UtilTest
import utilMain.UtilMain

import testData.schemaData.avroData.apacheData.Data._


import java.io.File
import scala.io.Source


/**
 *
 */
object TRY_AvroToSkeuo_tatidatafile extends App {
	
	
	
	
	
	def roundTripAvdlSkeuoApache(filePath: String = filePath, targetFileType: String = fileType, fileName: String): Boolean = {
		//val avroFile = new java.io.File(filePath + name)
		//val avroStrFiles: List[String] = names.map(_ + fileType)
		val avroStrFile: String = fileName + targetFileType // sensor
		//val avroStrFilePaths: List[String] = names.map(n ⇒ filePath + n + fileType)
		val avroStrFilePath: String = filePath + avroStrFile
		//val avroStrFileObjs: List[File] = avroStrFilePaths.map(new File(_))
		val avroStrFileObj: File = new File(avroStrFilePath)
		
		// Convert .avsc to schema-avro
		val parserApacheStrToADT: SchemaAvro_Apache.Parser = new SchemaAvro_Apache.Parser()
		
		
		//new SchemaAvro_Apache(SchemaAvro_Apache.Type.RECORD)
		// TODO how to check that Names contains key?
		
		/*val parsedApaches: Map[String, SchemaAvro_Apache] = avroStrFileObjs.map { fileObj ⇒ (fileObj.getName → parserApacheStrToADT.parse(fileObj)) }.toMap*/
		val parsedApache: SchemaAvro_Apache = parserApacheStrToADT.parse(avroStrFileObj)
		
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
		/*val skeuoSchemas: Map[String, Fix[SchemaAvro_Skeuo]] = parsedApaches.map { case (name, parsedApache) ⇒ (name → apacheToSkeuoAvroSchema(parsedApache)) }*/
		val skeuoSchema: Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema(parsedApache)
		
		// Converting backward
		/*val avrosBack: Map[String, SchemaAvro_Apache] = skeuoSchemas.map { case (name, apacheSchema) ⇒ (name → skeuoToApacheAvroSchema(apacheSchema)) }*/
		val avroBack: SchemaAvro_Apache = skeuoToApacheAvroSchema(skeuoSchema)
		
		// Get reference to source object so can close again when done reading the file contents
		//val bufferedSourceList: List[BufferedSource] = avroStrFilePaths.map((path: String) ⇒ Source.fromFile(path))
		val buf: BufferedSource = Source.fromFile(avroStrFilePath)
		//val avroStrList: List[String] = bufferedSourceList.map(buf ⇒ buf.getLines().mkString)
		val avroStr: String = buf.getLines().mkString
		// Closing the buffered source
		buf.close
		
		
		println(s"ORIGINAL CONTENTS OF .AVSC FILE (FROM .AVDL): ${avroStr}")
		println(s"\nCONVERTED: ORIGINAL .AVSC ---> APACHE SCHEMA: ${parsedApache}")
		println(s"\nAPACHE ---> SKEUO: ${skeuoSchema}")
		println(s"\nSKEUO ---> APACHE back: $avroBack")
		println(s"apache schema == apache back: ${parsedApache == avroBack}")
		
		parsedApache == avroBack
	}
	

	
	
	
	
	val skeuoTstampmillis: Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema(timestampMillisSchema)
	println(s"apache tstamp = ${timestampMillisSchema}")
	println(s"skeuo tstamp = $skeuoTstampmillis")
	println(s"apache round trip tstamp = ${roundTrip_ApacheAvroToApacheAvro(timestampMillisSchema)}")
	println("\nend tstamp")
	
	
	
	// TODO how to concat files resulting from the avro-tools' conversion of .avdl to .avsc?
	val fileType: String = ".avsc"
	val names: List[String] = List("Aq_Msm", "Location", "Sensor", "Sensor_type", "Sensordatavalues_record")
	val filePath: String = "/development/projects/statisticallyfit/github/learningdataflow/SchaemeowMorphism/src/test/scala/testData/testDataPrivateTati/asset-schemas/sdp-asset-schemas-luftdaten/src/main/airquality/lft.aq_msm/lft.aq_msm.datasource/avro/"
	
	
	println(s"\n\nAq_Msm round trip avdl-skeuo-apache: \n")
	
	println(s"\n${names(0)}")
	println(roundTripAvdlSkeuoApache(fileName = names.head))
	println(s"\n${names(1)}")
	println(roundTripAvdlSkeuoApache(fileName = names(1)))
	println(s"\n${names(2)}")
	println(roundTripAvdlSkeuoApache(fileName = names(2)))
	println(s"\n${names(3)}")
	println(roundTripAvdlSkeuoApache(fileName = names(3)))
	println(s"\n${names(4)}")
	println(roundTripAvdlSkeuoApache(fileName = names(4)))
	//println(names.indices.map(i ⇒ roundTripAvdlSkeuoApache(fileName = names(i))))
	
}
