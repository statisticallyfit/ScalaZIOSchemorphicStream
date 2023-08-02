package conversionsOfSchemaADTs.avro_json.parsing


import higherkindness.droste.data.Fix
//import org.scalatest.featurespec.AnyFeatureSpec
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should._
import utilMain.UtilMain.implicits._
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._


/**
 *
 */
class ParseDecode_BOILERPLATE_Primitives_JsonStr_Circe_JsonSkeuo_Specs extends AnyFunSuite with Matchers {
	
	
	test("string") {
		
		
		val rawJsonStr: String =
			"""
			  |{
			  |  "type": "string"
			  |}
			  |""".stripMargin.trim
		
		
		val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
		
		// value-check
		circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
		// type check
		circeJsonStr shouldBe a[JsonCirce]
		
		
		val jsonSkeuoDecoded: Option[Fix[JsonSchema_S]] = ParseStringToCirceToADT.decodeJsonStringToCirceToJsonSkeuo(rawJsonStr)
		
		// value check
		jsonSkeuoDecoded shouldEqual Some(StringF())
		// type check
		jsonSkeuoDecoded shouldBe a[JsonSchema_S[_]]
		
		
		info(s"raw json str = \n$rawJsonStr")
		//info(s"result json circe = \n$circeJsonStr")
		info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
		info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		/*info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
		info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")*/
		
		info(s"DECODING: Json circe --> SKEUO: $jsonSkeuoDecoded")
	}
	
	test("integer") {
		val rawJsonStr: String =
			"""
			  |{
			  |  "type": "integer"
			  |}
			  |""".stripMargin.trim
		
		
		val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
		
		// value-check
		circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
		// type check
		circeJsonStr shouldBe a[JsonCirce]
		
		
		val jsonSkeuoDecoded: Option[Fix[JsonSchema_S]] = ParseStringToCirceToADT.decodeJsonStringToCirceToJsonSkeuo(rawJsonStr)
		
		// value check
		jsonSkeuoDecoded shouldEqual Some(IntegerF())
		// type check
		jsonSkeuoDecoded shouldBe a[JsonSchema_S[_]]
		
		
		info(s"raw json str = \n$rawJsonStr")
		//info(s"result json circe = \n$circeJsonStr")
		info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
		info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		/*info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
		info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")*/
		
		info(s"DECODING: Json circe --> SKEUO: $jsonSkeuoDecoded")
	}
	
	
	test("array (string)") {
		val rawJsonStr: String =
			"""
			  |{
			  |  "type": "array",
			  |  "items": {
			  |    "type": "string"
			  |  }
			  |}
			  |""".stripMargin.trim
		
		
		val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
		
		// value-check
		circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
		// type check
		circeJsonStr shouldBe a[JsonCirce]
		
		
		val jsonSkeuoDecoded: Option[Fix[JsonSchema_S]] = ParseStringToCirceToADT.decodeJsonStringToCirceToJsonSkeuo(rawJsonStr)
		
		// value check
		jsonSkeuoDecoded shouldEqual Some(StringF())
		// type check
		jsonSkeuoDecoded shouldBe a[JsonSchema_S[_]]
		
		
		info(s"raw json str = \n$rawJsonStr")
		//info(s"result json circe = \n$circeJsonStr")
		info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
		info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		/*info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
		info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")*/
		
		info(s"DECODING: Json circe --> SKEUO: $jsonSkeuoDecoded")
	}
	
}
