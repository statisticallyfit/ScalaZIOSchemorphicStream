package conversionsOfSchemaADTs.avro_json.skeuo_circe.specs

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should._




import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import higherkindness.skeuomorph.openapi.JsonSchemaF.Fixed


import io.circe.Decoder.Result
import io.circe.Encoder._
import io.circe.{Encoder, Decoder, DecodingFailure, Json ⇒ JsonCirce}

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}


/**
 *
 */
class JsonCirce_To_SkeuoJson_Convert_TrySpec extends AnyWordSpec with Matchers {
	
	
	val jsonStr: String =
		"""
		  |{
		  |  "title": "Luftdaten raw data schema",
		  |  "type": "array",
		  |  "items": {
		  |    "$ref": "#/$defs/measurement"
		  |  },
		  |  "$defs": {
		  |    "measurement": {
		  |      "type": "object",
		  |      "properties": {
		  |        "sensor": {
		  |          "type": "object",
		  |          "properties": {
		  |            "sensor_type": {
		  |              "type": "object",
		  |              "properties": {
		  |                "manufacturer": {
		  |                  "type": "string"
		  |                },
		  |                "name": {
		  |                  "type": "string"
		  |                },
		  |                "id": {
		  |                  "type": "integer"
		  |                }
		  |              },
		  |              "required": [
		  |                "manufacturer",
		  |                "name",
		  |                "id"
		  |              ]
		  |            },
		  |            "pin": {
		  |              "type": "string"
		  |            },
		  |            "id": {
		  |              "type": "integer"
		  |            }
		  |          },
		  |          "required": [
		  |            "sensor_type",
		  |            "pin",
		  |            "id"
		  |          ]
		  |        },
		  |        "sensordatavalues": {
		  |          "type": "array",
		  |          "items": {
		  |            "type": "object",
		  |            "properties": {
		  |              "value": {
		  |                "type": "string"
		  |              },
		  |              "value_type": {
		  |                "type": "string"
		  |              },
		  |              "id": {
		  |                "type": "integer"
		  |              }
		  |            },
		  |            "required": [
		  |              "value",
		  |              "value_type",
		  |              "id"
		  |            ]
		  |          }
		  |        },
		  |        "timestamp": {
		  |          "type": "string"
		  |        },
		  |        "id": {
		  |          "type": "integer"
		  |        },
		  |        "location": {
		  |          "type": "object",
		  |          "properties": {
		  |            "indoor": {
		  |              "type": "integer"
		  |            },
		  |            "altitude": {
		  |              "type": "string"
		  |            },
		  |            "latitude": {
		  |              "type": "string"
		  |            },
		  |            "exact_location": {
		  |              "type": "integer"
		  |            },
		  |            "country": {
		  |              "type": "string"
		  |            },
		  |            "id": {
		  |              "type": "integer"
		  |            },
		  |            "longitude": {
		  |              "type": "string"
		  |            }
		  |          },
		  |          "required": [
		  |            "latitude",
		  |            "longitude"
		  |          ]
		  |        },
		  |        "sampling_rate": {
		  |          "type": "null"
		  |        }
		  |      },
		  |      "required": [
		  |        "sensor",
		  |        "sensordatavalues",
		  |        "timestamp",
		  |        "id",
		  |        "location"
		  |      ]
		  |    }
		  |  }
		  |}
		  |""".stripMargin
	
	"Converting Json (circe) to Json Schema (Skeuomorph)" when {
		
		val jsonCirce_viaFromString: JsonCirce = JsonCirce.fromString(jsonStr)
		
		//def encode: Encoder[String] = Encoder[String]
		
		//val jsonCirce_viaEncode: JsonCirce = encode(jsonStr)
		
		println(s"json str ---> json circe: $jsonCirce_viaFromString ")
		//println(s"json str ---> json circe (using Encoder): $jsonCirce_viaEncode")
		
		
		"given decoder of Json Circe to Json Schema (skeuo)" in {
			
			val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
			
			val jsonSkeuoSchema_fromString: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaFromString)
			//val jsonSkeuoSchema_fromEncode: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaEncode)
			
			
			println(s"json circe ---> json schema Skeuo: $jsonSkeuoSchema_fromString")
			//println(s"json circe ---> json schema Skeuo (from Encoder): $jsonSkeuoSchema_fromEncode")
			
			
			import scala.reflect.runtime.universe._
			import utilTest.GeneralTestUtil
			//jsonSkeuoSchema_fromString shouldBe a [SchemaJson_Skeuo.Fixed]
			
			GeneralTestUtil.getFuncTypeSubs(	)
			
			//jsonSkeuoSchema_fromEncode shouldBe a [SchemaJson_Skeuo.Fixed]
		}
	}
	
	
}
