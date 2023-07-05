package conversionsOfSchemaADTs.json_json.specs


import org.scalatest.{GivenWhenThen, Informer}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

// import org.specs2.Specification // wanted to use 'sequential' to make tests run in order to see print stataements in order but cannot because already using scalatest (functions get mixed up)
// NOTE using sbt now  = https://stackoverflow.com/a/15146140

import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._

// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseStrToADT.parseType
//import utilTest.UtilTest
import utilMain.UtilMain


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}

import json.{Schema ⇒ SchemaJson_Glow}
import SchemaJson_Glow._

import scala.util.Try

import scala.reflect.runtime.universe._

/**
 *
 */
class AndyGlow_To_Skeuo_JsonSchema_Spec extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
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
			val result: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `string` (schema-adt from andy glow)")
			result shouldBe a[SchemaJson_Glow[_]]
			result should not be a[String] // has been converted
			result shouldEqual `string`
			
			
			info("\n\nSTRING TEST: ")
			info(s"result simple string = $result")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(result)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val skeuoJson: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(result) // TODO parameter instead of Any?
			
			Then("result should be a skeuo adt json schema")
			skeuoJson shouldBe a [SchemaJson_Skeuo[_]]
			skeuoJson shouldEqual SchemaJson_Skeuo.StringF()
			
			info(s"skeuo Json (adt) = ${skeuoJson}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(skeuoJson)}")
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
			val result: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `integer` (schema-adt from andy glow)")
			result shouldBe a[SchemaJson_Glow[_]]
			result should not be a[String] // has been converted
			result shouldEqual `integer`
			
			
			info("\n\nINTEGER TEST:")
			info(s"result simple integer = $result")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(result)}")
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
			val result: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `array` (schema-adt from andy glow)")
			result shouldBe a[SchemaJson_Glow[_]]
			result should not be a[String] // has been converted
			result shouldEqual `array`(`string`)
			
			info("\n\nARRAY TEST: ")
			info(s"result simple array = $result")
			//info(s"typeOf[result.type] = ${typeOf[result.type]}")
			//info(s"typetag = ${implicitly[TypeTag[]]}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(result)}")
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
			val resultTry: Try[SchemaJson_Glow[_]] = parseType(sampleSchemaJsonRaw)
			val result: SchemaJson_Glow[_] = resultTry.value
			
			Then("result should be a schema-adt from Andy Glow")
			result shouldBe a[SchemaJson_Glow[_]]
			result should not be a[String] // has been converted
			
			
			info("\n\nENTIRE SCHEMA TEST: ")
			info(s"entire schema result = $resultTry")
			info(s"entire schema result (without try): ${resultTry.getOrElse(None)}")
			info(s"entire schema result (simple): $result")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(result)}")
		}
	}
}
