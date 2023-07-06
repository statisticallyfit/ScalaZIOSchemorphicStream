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
//import SchemaJson_Skeuo._

import json.{Schema ⇒ SchemaJson_Glow}
import SchemaJson_Glow._

import scala.util.Try

import scala.reflect.runtime.universe._

/**
 *
 */
class JsonSchema_Glow_To_Skeuo_Spec extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
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
			val schemaGlow: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `string` (schema-adt from andy glow)")
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `string`
			
			
			info("\n\nSTRING TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a [SchemaJson_Skeuo[_]]
			schemaSkeuo shouldEqual SchemaJson_Skeuo.StringF()
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
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
			val schemaGlow: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `integer` (schema-adt from andy glow)")
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `integer`
			
			
			info("\n\nINTEGER TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[SchemaJson_Skeuo[_]]
			schemaSkeuo shouldEqual SchemaJson_Skeuo.IntegerF()
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
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
			val schemaGlow: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `array(`string`)` (schema-adt from andy glow)")
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`string`)
			
			
			info("\n\nARRAY(STRING) TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[SchemaJson_Skeuo[_]]
			schemaSkeuo shouldEqual SchemaJson_Skeuo.ArrayF(SchemaJson_Skeuo.StringF())
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
		}
		
		
		Scenario("array triple") {
			Given("a json schema (triple array)")
			val jsonSchemaStr: String =
				"""{
				  | "type": "array",
				  | "items": {
				  |   "type": "array",
				  |   "items": {
				  |     "type": "array",
				  |     "items": {
				  |       "type": "integer"
				  |     }
				  |   }
				  | }
				  |}
			""".stripMargin
			
			
			When("parsing")
			val schemaGlow: SchemaJson_Glow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `array`(`array`(`array`(`integer`)))) (schema-adt from andy glow)")
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`array`(`array`(`integer`)))
			
			
			info("\n\nARRAY TRIPLE TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[SchemaJson_Skeuo[_]]
			schemaSkeuo shouldEqual SchemaJson_Skeuo.ArrayF(SchemaJson_Skeuo.ArrayF(SchemaJson_Skeuo.ArrayF(SchemaJson_Skeuo.IntegerF())))
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
		}
		
	}
	
	
	
	// TODO test once the function is complete
	/*Feature("Converting raw json schema string (entire)") {
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
			val schemaGlowTry: Try[SchemaJson_Glow[_]] = parseType(sampleSchemaJsonRaw)
			val schemaGlow: SchemaJson_Glow[_] = schemaGlowTry.value
			
			Then("result should be a schema-adt from Andy Glow")
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			
			
			info("\n\nENTIRE SCHEMA TEST: ")
			info(s"entire schema result = $schemaGlowTry")
			info(s"entire schema result (without try): ${schemaGlowTry.getOrElse(None)}")
			info(s"entire schema result (simple): $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[SchemaJson_Skeuo[_]]
			// TODO update here
			//schemaSkeuo shouldEqual ArrayF(ArrayF(ArrayF(IntegerF())))
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
		}
	}*/
}
