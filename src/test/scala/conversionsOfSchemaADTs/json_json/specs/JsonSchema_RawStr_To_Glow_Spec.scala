package conversionsOfSchemaADTs.json_json.specs



import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._


// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseStrToADT.parseType
import utilMain.UtilMain


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import json.{Schema ⇒ JsonSchema_G}
import JsonSchema_G._

import scala.util.Try

import scala.reflect.runtime.universe._


/**
 *
 */
class JsonSchema_RawStr_To_Glow_Spec extends AnyFeatureSpec with GivenWhenThen with Matchers  {
	
	
	Feature("Convert raw json schema string (basic primitives)") {
		
		
		Scenario("string") {
			
			Given("a json schema (single string)")
			val jsonSchemaStr: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin
			
			
			When("parsing")
			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `string` (schema-adt from andy glow)")
			result shouldBe a[JsonSchema_G[_]]
			result should not be a[String] // has been converted
			result shouldEqual `string`
			
			
			println(s"result simple string = $result")
			println(s"typeOf[result.type] = ${typeOf[result.type]}")
			println(s"getFuncTypeSubs = ${UtilMain.getFuncTypeSubs(result)}")
			
			
		}
		
		
		Scenario("integer") {
			Given("a json schema (single integer)")
			val jsonSchemaStr: String =
				"""
				  |{
				  |  "type": "integer"
				  |}
				  |""".stripMargin
			
			
			When("parsing")
			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `integer` (schema-adt from andy glow)")
			result shouldBe a[JsonSchema_G[_]]
			result should not be a[String] // has been converted
			result shouldEqual `integer`
			
			println(s"result simple integer = $result")
			println(s"typeOf[result.type] = ${typeOf[result.type]}")
			println(s"getFuncTypeSubs = ${UtilMain.getFuncTypeSubs(result)}")
		}
		
		Scenario("array") {
			Given("a json schema (single array)")
			val jsonSchemaStr: String =
				"""{
				  | "type": "array",
				  | "items": {
				  |   "type": "string"
				  | }
				  |}
				""".stripMargin
			
			
			When("parsing")
			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `array` (schema-adt from andy glow)")
			result shouldBe a[JsonSchema_G[_]]
			result should not be a[String] // has been converted
			result shouldEqual `array`(`string`)
			
			println(s"result simple array = $result")
			//println(s"typeOf[result.type] = ${typeOf[result.type]}")
			//println(s"typetag = ${implicitly[TypeTag[]]}")
			println(s"getFuncTypeSubs = ${UtilMain.getFuncTypeSubs(result)}")
		}
		
	}
	
	
	Feature("Converting raw json schema string (entire)") {
		Scenario("entire schema string") {
			
			Given("an entire json schema-string")
			val sampleSchemaJsonRaw: String = // sensor_type
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
				  |""".stripMargin
			
			When("parsing")
			val resultTry: Try[JsonSchema_G[_]] = parseType(sampleSchemaJsonRaw)
			val result: JsonSchema_G[_] = resultTry.value
			
			Then("result should be a schema-adt from Andy Glow")
			result shouldBe a[JsonSchema_G[_]]
			result should not be a[String] // has been converted
			
			println(s"entire schema result = $resultTry")
			println(s"entire schema result (without try): ${resultTry.getOrElse(None)}")
			println(s"entire schema result (simple): $result")
			println(s"typeOf[result.type] = ${typeOf[result.type]}")
			println(s"getFuncTypeSubs = ${UtilMain.getFuncTypeSubs(result)}")
		}
	}
}
