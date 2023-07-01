package conversionsOfSchemaADTs.json_json.specs

import cats.syntax.all._
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.JsonSchemaF.Fixed
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import io.circe.Encoder._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Error, ParsingFailure, Json ⇒ JsonCirce}
import org.scalatest.matchers.should._
import org.scalatest.wordspec.AnyWordSpec



object minihelpers {
	
	// SOURCE code = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/helpers.scala#L27
	def unsafeParse: String => JsonCirce = parse(_).valueOr((x: ParsingFailure) => sys.error(x.message))
	
	
	def tryRawToCirceToSkeuo(jsonRaw: String): Unit = {
		val decoder: Decoder[SchemaJson_Skeuo.Fixed] = Decoder[SchemaJson_Skeuo.Fixed]
		
		
		//val jsonRaw: String = "string"
		//---
		val jsonCirce_viaFromString: JsonCirce = JsonCirce.fromString(jsonRaw)
		//val jsonCirce_viaUnsafeParse: JsonCirce = unsafeParse(jsonRaw)
		val jsonCirce_viaAsJson: JsonCirce = jsonRaw.asJson
		val jsonCirce_viaParserDecode: Either[Error, JsonCirce] = decode[JsonCirce](jsonRaw)
		val jsonCirce_viaParser: Either[ParsingFailure, JsonCirce] = parse(jsonRaw)
		val enc = Encoder[String]
		val jsonCirce_viaEncode: JsonCirce = enc(jsonRaw)
		// NOTE: how to create custom encoder / decoder = https://medium.com/rahasak/hacking-with-circe-json-scala-ca626705733e
		//---
		// FAIL - cannot convert to schemas
		val jsonSchema_viaDecodeJson_viaFromString: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaFromString)
		//val jsonSchema_viaDecodeJson_viaUnsafeParse: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaUnsafeParse)
		val jsonSchema_viaDecodeJson_viaAsJson: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaAsJson)
		val jsonSchema_viaDecodeJson_viaEncode: Decoder.Result[SchemaJson_Skeuo.Fixed] = decoder.decodeJson(jsonCirce_viaEncode)
		// TODO extract the json?
		//val jsonSchema_viaDecodeJson_viaParserDecode = decoder.decodeJson(jsonCirce_viaParserDecode)
		//val jsonSchema_viaDecodeJson_viaParser = decoder.decodeJson(jsonCirce_viaParser)
		/// ---
		val jsonSchema_viaParserDecode: Either[Error, Fixed] = decode[SchemaJson_Skeuo.Fixed](jsonRaw)
		
		
		
		//			println(s"json circe (int): $intJsonCirce")
		//			println(s"json schema (int): $intJsonSchema_viaDecodeJson")
		
		println("\nPrimitive String: Str --> Circe")
		println(s"json string --> json circe (fromString): $jsonCirce_viaFromString")
		// FAIL - unsafe parse is not for schemas
		//println(s"json string --> json circe (unsafeParse): $jsonCirce_viaUnsafeParse")
		println(s"json string --> json circe (asJson): $jsonCirce_viaAsJson")
		
		// FAIL - circe cannot convert a schema
		println(s"json string --> json circe (parser.decode): $jsonCirce_viaParserDecode")
		println(s"json string --> json circe (parse): $jsonCirce_viaParser")
		
		println(s"json string --> json circe (encode): $jsonCirce_viaEncode")
		//---
		
		println("\nPrimitive String: Circe --> Skeuo")
		println(s"json schema (via decodeJson (fromString)): $jsonSchema_viaDecodeJson_viaFromString")
		//println(s"json schema (via decodeJson (unsafeParse)): $jsonSchema_viaDecodeJson_viaUnsafeParse")
		println(s"json schema (via decodeJson (asJson)): $jsonSchema_viaDecodeJson_viaAsJson")
		println(s"json schema (via decodeJson (encode)): $jsonSchema_viaDecodeJson_viaEncode")
		// TODO
		//println(s"json schema (via decodeJson (parser.decode)): $jsonSchema_viaDecodeJson_viaParserDecode")
		//println(s"json schema (via decodeJson (parse)): $jsonSchema_viaDecodeJson_viaParser")
		
		
		println(s"json schema (via DIRECT parser.decode): ${jsonSchema_viaParserDecode}")
		
		// PROOF circe cannot convert a schema = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/openapi/OpenApiDecoderSpecification.scala#L209-L247
	}
}
import conversionsOfSchemaADTs.json_json.specs.minihelpers._

/**
 *
 */
class JsonCirce_To_SkeuoJson_Convert_TrySpec extends AnyWordSpec with Matchers {
	
	
	"Converting Json (circe) to Json Schema (Skeuomorph)" when {
		
		
		"convert basic primitives" in {
			
			tryRawToCirceToSkeuo(jsonRaw = "string")
			
		}
		
		
		"convert skeuo's already-tested example schema" in {
			
			val petStoreJsonRawValue: String =
				"""
				  |{
				  |            "title": "Sample Pet Store App",
				  |            "description": "This is a sample server for a pet store.",
				  |            "termsOfService": "http://example.com/terms/",
				  |            "contact": {
				  |              "name": "API Support",
				  |              "url": "http://www.example.com/support",
				  |              "email": "support@example.com"
				  |            },
				  |            "license": {
				  |              "name": "Apache 2.0",
				  |              "url": "https://www.apache.org/licenses/LICENSE-2.0.html"
				  |            },
				  |            "version": "1.0.1"
				  |          }
				  |""".stripMargin
					
			tryRawToCirceToSkeuo(jsonRaw = petStoreJsonRawValue)
		}
		
		
		"given decoder of Json Circe to Json Schema (skeuo)" in {
			
			
			val strJsonSchemaRaw = // sensor_type
				"""
				  |{
				  |  "type": "object",
				  |  "properties": {
				  |    "manufacturer": {
				  |      "type": "string"
				  |    },
				  |    "name": {
				  |      "type": "string"
				  |    },
				  |    "id": {
				  |      "type": "integer"
				  |    }
				  |  },
				  |  "required": [
				  |    "manufacturer",
				  |    "name",
				  |    "id"
				  |  ]
				  |}
				  |
				  |""".stripMargin
			
			
			tryRawToCirceToSkeuo(jsonRaw = strJsonSchemaRaw)
			
			
			// TODO help no typetag available
			//GeneralTestUtil.getFuncTypeSubs(jsonSkeuoSchema_fromString) shouldEqual "JsonSchemaF.Fixed"
			
			// TODO help no classtag available
			//jsonSkeuoSchema_fromString shouldBe a [SchemaJson_Skeuo.Fixed]
			
			true shouldEqual true // HELP i don't even know what test to put hee rbecause i don't know how the schema should look like AND the abvoe type checks aren't working!!!!
		}
	}
	
	
}
