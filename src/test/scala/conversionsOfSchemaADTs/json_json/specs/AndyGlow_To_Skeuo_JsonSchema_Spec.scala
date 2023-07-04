package conversionsOfSchemaADTs.json_json.specs


import org.specs2

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

// import org.specs2.Specification // wanted to use 'sequential' to make tests run in order to see print stataements in order but cannot because already using scalatest (functions get mixed up)
// NOTE using sbt now  = https://stackoverflow.com/a/15146140

import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._

// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema_RawToGlowADT.parseType
import utilTest.GeneralTestUtil


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import json.{Schema ⇒ JsonSchema_G}
import JsonSchema_G._

import scala.util.Try

import scala.reflect.runtime.universe._

/**
 *
 */
class AndyGlow_To_Skeuo_JsonSchema_Spec extends AnyFeatureSpec with GivenWhenThen with Matchers with org.scalatest.Suite {
	
	
	
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
			
			
			info("\n\nSTRING TEST: ")
			info(s"result simple string = $result")
			info(s"typeOf[result.type] = ${typeOf[result.type]}")
			info(s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val skeuoJson: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(result) // TODO parameter instead of Any?
			
			Then("result should be a skeuo adt json schema")
			skeuoJson shouldBe a [JsonSchema_S[_]]
			skeuoJson shouldEqual JsonSchema_S.StringF()
			
			info(s"skeuo Json (adt) = ${skeuoJson}")
			info(s"skeuo Json (toString) = ${skeuoJson.toString}")
			info(s"typeOf[skeuoJson.type] = ${typeOf[skeuoJson.type]}")
			info(s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(skeuoJson)}")
		}
		
		
		
		Scenario("integer"){
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
			
			
			info("\n\nINTEGER TEST:")
			info(s"result simple integer = $result")
			info(s"typeOf[result.type] = ${typeOf[result.type]}")
			info(s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
		}
		
		Scenario("array"){
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
			
			info("\n\nARRAY TEST: ")
			info(s"result simple array = $result")
			//info(s"typeOf[result.type] = ${typeOf[result.type]}")
			//info(s"typetag = ${implicitly[TypeTag[]]}")
			info(s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
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
			
			
			info("\n\nENTIRE SCHEMA TEST: ")
			info(s"entire schema result = $resultTry")
			info(s"entire schema result (without try): ${resultTry.getOrElse(None)}")
			info(s"entire schema result (simple): $result")
			info(s"typeOf[result.type] = ${typeOf[result.type]}")
			info(s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
		}
	}
}
