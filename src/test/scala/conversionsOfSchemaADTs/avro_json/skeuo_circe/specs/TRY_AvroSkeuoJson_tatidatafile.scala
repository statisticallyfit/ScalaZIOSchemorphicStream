//package conversionsOfSchemaADTs.avro_json.skeuo_circe.specs
//
//
//
//
//import higherkindness.droste._
//import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._
//import higherkindness.droste.implicits._
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import higherkindness.skeuomorph.openapi.JsonDecoders._
//import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
//import higherkindness.skeuomorph.openapi.JsonSchemaF.Fixed
//
//
//import io.circe.Decoder.Result
//import io.circe.Encoder._
//import io.circe.{Encoder, Decoder, DecodingFailure, Json ⇒ JsonCirce}
//import io.circe.syntax._
//
//
//import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
//
//import scala.io.BufferedSource
/////import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}
//
//
//import scala.jdk.CollectionConverters._
//
//
//import testData.ScalaCaseClassData._
//
//
////import org.scalacheck._
////import org.specs2._
//import org.scalatest.wordspec.AnyWordSpec
//import org.scalatest.matchers.should._
////import org.specs2.specification.core.SpecStructure
//
//
//import conversionsOfSchemaADTs.avro_avro.Skeuo_Apache._ //{apacheToSkeuoAvroSchema, skeuoToApacheAvroSchema}
////import conversionsOfSchemaStrings.avro_json.Skeuo_JsonCirce._
////{skeuoAvroSchemaToJsonString, jsonStringToSkeuoAvroSchema, avroSchemaToJsonString}
//
//
//import utilTest.GeneralTestUtil
//
//import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._
//import testData.GeneralTestData._
//
//import java.io.File
//import scala.io.Source
//
//
///**
// *
// */
//object TRY_AvroSkeuoJson_tatidatafile extends App {
//
//
//	def convertAvdlToJsonSchema(filePath: String = filePath, targetFileType: String = fileType, fileName: String): Unit = {
//
//		//val avroStrFiles: List[String] = names.map(_ + fileType)
//		val avroStrFile: String = fileName + targetFileType // sensor
//		//val avroStrFilePaths: List[String] = names.map(n ⇒ filePath + n + fileType)
//		val avroStrFilePath: String = filePath + avroStrFile
//		//val avroStrFileObjs: List[File] = avroStrFilePaths.map(new File(_))
//		val avroStrFileObj: File = new File(avroStrFilePath)
//
//		// Convert .avsc to schema-avro
//		val parserApacheStrToADT: SchemaAvro_Apache.Parser = new SchemaAvro_Apache.Parser()
//
//		// 1 - avro str --> avro ADT
//		val parsedApache: SchemaAvro_Apache = parserApacheStrToADT.parse(avroStrFileObj)
//
//
//		// 2 - Convert apache schema -> skeuo schema
//		val skeuoSchema: Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema(parsedApache)
//
//		// 3 - skeuo --> json circe
//		val jsoncirce: JsonCirce = skeuoAvroSchemaToJsonCirce(skeuoSchema)
//		println(s"jsoncirce.asString = ${jsoncirce.asString}")
//		println(s"\njsoncirce.toString = ${jsoncirce.toString}")
//
//
//		val strJson: String =
//			"""
//			  |{
//			  |  "title": "Luftdaten raw data schema",
//			  |  "type": "array",
//			  |  "items": {
//			  |    "$ref": "#/$defs/measurement"
//			  |  },
//			  |  "$defs": {
//			  |    "measurement": {
//			  |      "type": "object",
//			  |      "properties": {
//			  |        "sensor": {
//			  |          "type": "object",
//			  |          "properties": {
//			  |            "sensor_type": {
//			  |              "type": "object",
//			  |              "properties": {
//			  |                "manufacturer": {
//			  |                  "type": "string"
//			  |                },
//			  |                "name": {
//			  |                  "type": "string"
//			  |                },
//			  |                "id": {
//			  |                  "type": "integer"
//			  |                }
//			  |              },
//			  |              "required": [
//			  |                "manufacturer",
//			  |                "name",
//			  |                "id"
//			  |              ]
//			  |            },
//			  |            "pin": {
//			  |              "type": "string"
//			  |            },
//			  |            "id": {
//			  |              "type": "integer"
//			  |            }
//			  |          },
//			  |          "required": [
//			  |            "sensor_type",
//			  |            "pin",
//			  |            "id"
//			  |          ]
//			  |        },
//			  |        "sensordatavalues": {
//			  |          "type": "array",
//			  |          "items": {
//			  |            "type": "object",
//			  |            "properties": {
//			  |              "value": {
//			  |                "type": "string"
//			  |              },
//			  |              "value_type": {
//			  |                "type": "string"
//			  |              },
//			  |              "id": {
//			  |                "type": "integer"
//			  |              }
//			  |            },
//			  |            "required": [
//			  |              "value",
//			  |              "value_type",
//			  |              "id"
//			  |            ]
//			  |          }
//			  |        },
//			  |        "timestamp": {
//			  |          "type": "string"
//			  |        },
//			  |        "id": {
//			  |          "type": "integer"
//			  |        },
//			  |        "location": {
//			  |          "type": "object",
//			  |          "properties": {
//			  |            "indoor": {
//			  |              "type": "integer"
//			  |            },
//			  |            "altitude": {
//			  |              "type": "string"
//			  |            },
//			  |            "latitude": {
//			  |              "type": "string"
//			  |            },
//			  |            "exact_location": {
//			  |              "type": "integer"
//			  |            },
//			  |            "country": {
//			  |              "type": "string"
//			  |            },
//			  |            "id": {
//			  |              "type": "integer"
//			  |            },
//			  |            "longitude": {
//			  |              "type": "string"
//			  |            }
//			  |          },
//			  |          "required": [
//			  |            "latitude",
//			  |            "longitude"
//			  |          ]
//			  |        },
//			  |        "sampling_rate": {
//			  |          "type": "null"
//			  |        }
//			  |      },
//			  |      "required": [
//			  |        "sensor",
//			  |        "sensordatavalues",
//			  |        "timestamp",
//			  |        "id",
//			  |        "location"
//			  |      ]
//			  |    }
//			  |  }
//			  |}
//			  |""".stripMargin
//		val strToCirceJson: JsonCirce = JsonCirce.fromString(strJson)
//		println(s"\nJSON STRING ---> JSON CIRCE: $strToCirceJson")
//		def encode: Encoder[String] = Encoder[String] // uses implicit instance encoderString()
//		println(s"\nJson STRING ---> JSON CIRCE (via encode) ${encode(strJson)}")
//
//
//		// 4b) - json circe ---> skeuo's json schema
//		// TODO help
//
//
//		val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
//		val jsonSchema: Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(strToCirceJson)
//
//		println(s"\nDecoder[skeuo] = $decoder")
//		println(s"\nJSON CIRCE --> JSON SCHEMA (SKEUO) = $jsonSchema")
//		//println(s"\njsonSchema.right.get.toString = ${jsonSchema.right.get.toString}")
//		println(s"\njsonSchema.toString = ${jsonSchema.toString}")
//
//
//		// Get reference to source object so can close again when done reading the file contents
//		//val bufferedSourceList: List[BufferedSource] = avroStrFilePaths.map((path: String) ⇒ Source.fromFile(path))
//		val buf: BufferedSource = Source.fromFile(avroStrFilePath)
//		//val avroStrList: List[String] = bufferedSourceList.map(buf ⇒ buf.getLines().mkString)
//		val avroStr: String = buf.getLines().mkString
//		// Closing the buffered source
//		buf.close
//
//		println("\n----------------------------------------------------------------------------")
//		println(s"\nORIGINAL CONTENTS OF .AVSC FILE (FROM .AVDL): ${avroStr}")
//		println(s"\nCONVERTED: ORIGINAL .AVSC ---> APACHE SCHEMA: ${parsedApache}")
//		println(s"\nAPACHE ---> SKEUO: ${skeuoSchema}")
//		println(s"\nSKEUO ---> circe Json: $jsoncirce") // TODO only this part is wonky keep the rest
//		println(s"\ncirce Json ---> SKEUO SCHEMA JSON: $jsonSchema")
//
//
//	}
//
//
//	val fileType: String = ".avsc"
//	val names: List[String] = List("Aq_Msm", "Location", "Sensor", "Sensor_type", "Sensordatavalues_record")
//	val filePath: String = "/development/projects/statisticallyfit/github/learningdataflow/SchaemeowMorphism/src/test/scala/testData/testDataPrivateTati/asset-schemas/sdp-asset-schemas-luftdaten/src/main/airquality/lft.aq_msm/lft.aq_msm.datasource/avro/"
//
//
//	//convertAvdlToJsonSchema(fileName = "Aq_Msm")
//
//
//	// 4a) string json ---> crice json
//	val jsonStr: String =
//		"""
//		  |{
//		  |  "title": "Luftdaten raw data schema",
//		  |  "type": "array",
//		  |  "items": {
//		  |    "$ref": "#/$defs/measurement"
//		  |  },
//		  |  "$defs": {
//		  |    "measurement": {
//		  |      "type": "object",
//		  |      "properties": {
//		  |        "sensor": {
//		  |          "type": "object",
//		  |          "properties": {
//		  |            "sensor_type": {
//		  |              "type": "object",
//		  |              "properties": {
//		  |                "manufacturer": {
//		  |                  "type": "string"
//		  |                },
//		  |                "name": {
//		  |                  "type": "string"
//		  |                },
//		  |                "id": {
//		  |                  "type": "integer"
//		  |                }
//		  |              },
//		  |              "required": [
//		  |                "manufacturer",
//		  |                "name",
//		  |                "id"
//		  |              ]
//		  |            },
//		  |            "pin": {
//		  |              "type": "string"
//		  |            },
//		  |            "id": {
//		  |              "type": "integer"
//		  |            }
//		  |          },
//		  |          "required": [
//		  |            "sensor_type",
//		  |            "pin",
//		  |            "id"
//		  |          ]
//		  |        },
//		  |        "sensordatavalues": {
//		  |          "type": "array",
//		  |          "items": {
//		  |            "type": "object",
//		  |            "properties": {
//		  |              "value": {
//		  |                "type": "string"
//		  |              },
//		  |              "value_type": {
//		  |                "type": "string"
//		  |              },
//		  |              "id": {
//		  |                "type": "integer"
//		  |              }
//		  |            },
//		  |            "required": [
//		  |              "value",
//		  |              "value_type",
//		  |              "id"
//		  |            ]
//		  |          }
//		  |        },
//		  |        "timestamp": {
//		  |          "type": "string"
//		  |        },
//		  |        "id": {
//		  |          "type": "integer"
//		  |        },
//		  |        "location": {
//		  |          "type": "object",
//		  |          "properties": {
//		  |            "indoor": {
//		  |              "type": "integer"
//		  |            },
//		  |            "altitude": {
//		  |              "type": "string"
//		  |            },
//		  |            "latitude": {
//		  |              "type": "string"
//		  |            },
//		  |            "exact_location": {
//		  |              "type": "integer"
//		  |            },
//		  |            "country": {
//		  |              "type": "string"
//		  |            },
//		  |            "id": {
//		  |              "type": "integer"
//		  |            },
//		  |            "longitude": {
//		  |              "type": "string"
//		  |            }
//		  |          },
//		  |          "required": [
//		  |            "latitude",
//		  |            "longitude"
//		  |          ]
//		  |        },
//		  |        "sampling_rate": {
//		  |          "type": "null"
//		  |        }
//		  |      },
//		  |      "required": [
//		  |        "sensor",
//		  |        "sensordatavalues",
//		  |        "timestamp",
//		  |        "id",
//		  |        "location"
//		  |      ]
//		  |    }
//		  |  }
//		  |}
//		  |""".stripMargin
//
//	val jsonCirce1: JsonCirce = JsonCirce.fromString(jsonStr)
//
//	def encode: Encoder[String] = Encoder[String]
//	val jsonCirce2: JsonCirce = encode(jsonStr)
//
//	println(s"json str ---> json circe: $jsonCirce1 ")
//	println(s"json str ---> json circe (using Encoder): $jsonCirce2")
//
//
//	val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
//
//	val jsonSchema1_Skeuo: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce1)
//	val jsonSchema2_Skeuo: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce2)
//
//
//	println(s"json circe ---> json schema Skeuo: $jsonSchema1_Skeuo")
//	println(s"json circe ---> json schema Skeuo (from Encoder): $jsonSchema2_Skeuo")
//
//
//}
