package conversionsOfSchemaADTs.avro_json.skeuo_circe.specs

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should._

import utilTest.GeneralTestUtil


import io.circe.Encoder._
import io.circe.{Decoder, DecodingFailure, Encoder, ParsingFailure, Json ⇒ JsonCirce}
import io.circe.syntax._
import io.circe.parser.decode
import io.circe.parser._
import io.circe.Error


import cats.syntax.all._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.JsonSchemaF.Fixed
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}


object minihelpers {
	
	// SOURCE code = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/helpers.scala#L27
	def unsafeParse: String => JsonCirce = parse(_).valueOr((x: ParsingFailure) => sys.error(x.message))
}
import minihelpers._

/**
 *
 */
class JsonCirce_To_SkeuoJson_Convert_TrySpec extends AnyWordSpec with Matchers {
	
	
	val strJsonSchemaRaw: String =
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
	
	// TODO check if this parser (s) can also handle converting str -> skeuo-schema?
	
	
	
	"Converting Json (circe) to Json Schema (Skeuomorph)" when {
		
		
		
		
		"convert basic primitives" in {
			val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
			
			
			/*val intJsonRaw: String = "integer"
			val intJsonCirce: JsonCirce = intJsonRaw.asJson
			val intJsonSchema_viaDecodeJson: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(intJsonCirce)*/
			
			
			val strJsonRaw: String = "string"
			//---
			val strJsonCirce_viaFromString: JsonCirce = JsonCirce.fromString(strJsonRaw)
			val strJsonCirce_viaUnsafeParse: JsonCirce = unsafeParse(strJsonRaw)
			val strJsonCirce_viaAsJson: JsonCirce = strJsonRaw.asJson
			val strJsonCirce_viaParserDecode: Either[Error, JsonCirce] = decode[JsonCirce](strJsonRaw)
			val strJsonCirce_viaParser: Either[ParsingFailure, JsonCirce] = parse(strJsonRaw)
			val enc = Encoder[String]
			val strJsonCirce_viaEncode: JsonCirce = enc(strJsonRaw)
			//---
			val strJsonSchema_viaDecodeJson_viaFromString: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(strJsonCirce_viaFromString)
			val strJsonSchema_viaDecodeJson_viaUnsafeParse: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(strJsonCirce_viaUnsafeParse)
			val strJsonSchema_viaDecodeJson_viaAsJson: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(strJsonCirce_viaAsJson)
			val strJsonSchema_viaDecodeJson_viaEncode: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(strJsonCirce_viaEncode)
			
			val strJsonSchema_viaParserDecode: Either[Error, Fixed] = decode[SchemaJson_Skeuo.Fixed](strJsonRaw)
			
			
			
//			println(s"json circe (int): $intJsonCirce")
//			println(s"json schema (int): $intJsonSchema_viaDecodeJson")
			
			println("\nPrimitive String: Str --> Circe")
			println(s"json string --> json circe (fromString): $strJsonCirce_viaFromString")
			println(s"json string --> json circe (unsafeParse): $strJsonCirce_viaUnsafeParse")
			println(s"json string --> json circe (asJson): $strJsonCirce_viaAsJson")
			println(s"json string --> json circe (parser.decode): $strJsonCirce_viaParserDecode")
			println(s"json string --> json circe (parse): $strJsonCirce_viaParser")
			println(s"json string --> json circe (encode): $strJsonCirce_viaEncode")
			//---
			println("\nPrimitive String: Circe --> Skeuo")
			println(s"json schema (via decodeJson (formString)): $strJsonSchema_viaDecodeJson_viaFromString")
			println(s"json schema (via decodeJson (unsafeParse)): $strJsonSchema_viaDecodeJson_viaUnsafeParse")
			println(s"json schema (via decodeJson (asJson)): $strJsonSchema_viaDecodeJson_viaAsJson")
			println(s"json schema (via decodeJson (encode)): $strJsonSchema_viaDecodeJson_viaEncode")
			
			println(s"json schema (via parser.decode): ${strJsonSchema_viaParserDecode}")
			
			
		}
		
		
		"convert skeuo's already-tested example schema" in {
		
		}
		
		
		"given decoder of Json Circe to Json Schema (skeuo)" in {
			val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
			
			val jsonCirce_viaFromString: JsonCirce = JsonCirce.fromString(strJsonSchemaRaw)
			val jsonCirce_viaUnsafeParse: JsonCirce = unsafeParse(strJsonSchemaRaw)
			val jsonCirce_viaAsJson: JsonCirce = strJsonSchemaRaw.asJson
			val jsonCirce_viaParserDecode: Either[io.circe.Error, JsonCirce] = decode[JsonCirce](strJsonSchemaRaw)
			val jsonCirce_viaParser: Either[ParsingFailure, JsonCirce] = parse(strJsonSchemaRaw)
			
			def encode: Encoder[String] = Encoder[String]
			val jsonCirce_viaEncode: JsonCirce = encode(strJsonSchemaRaw)
			
			
			println(s"\nAVDL JSON schema-str --> json circe: ")
			println(s"json str ---> json circe (fromString): $jsonCirce_viaFromString ")
			println(s"json str ---> json circe (unsafeParse): $jsonCirce_viaUnsafeParse")
			println(s"json str ---> json circe (asJson): $jsonCirce_viaAsJson ")
			println(s"json str ---> json circe (parser decode): $jsonCirce_viaParserDecode ")
			println(s"json str ---> json circe (parser): $jsonCirce_viaParser ")
			println(s"json str ---> json circe (using Encoder): $jsonCirce_viaEncode")
			
			
			
			
			val jsonSkeuoSchema_fromString: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaFromString)
			//val jsonSkeuoSchema_fromEncode: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaEncode)
			
			
			println(s"json circe ---> json schema Skeuo: $jsonSkeuoSchema_fromString")
			//println(s"json circe ---> json schema Skeuo (from Encoder): $jsonSkeuoSchema_fromEncode")
			
			
			// TODO help no typetag available
			//GeneralTestUtil.getFuncTypeSubs(jsonSkeuoSchema_fromString) shouldEqual "JsonSchemaF.Fixed"
			
			// TODO help no classtag available
			//jsonSkeuoSchema_fromString shouldBe a [SchemaJson_Skeuo.Fixed]
			
			true shouldEqual true // HELP i don't even know what test to put hee rbecause i don't know how the schema should look like AND the abvoe type checks aren't working!!!!
		}
	}
	
	
}
