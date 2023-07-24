package conversionsOfSchemaADTs.avro_json.parsing



import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._



import json.{Schema ⇒ SchemaJson_Glow}
import json.schema.Version.Draft04
import com.github.andyglow.jsonschema.AsCirce._
//import io.circe._
import io.circe.{Json ⇒ JsonCirce}
import SchemaJson_Glow._


import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.ParseStrToADT.parseType

import utilMain.UtilMain
import utilMain.UtilMain.implicits._


/**
 * NOTE:  CONCLUSION: best use unsafeparser from skeuomorph library instead of this one.
 */


class IGNORE_ParserFromAndyGlowLibrary_Specs extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
	Feature("Convert raw json schema string to Json Circe (basic primitives)") {
		
		
		Scenario("string") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin
			
			
			When("parsing via Andy Glow library's parser ...")
			
			val schemaGlow: SchemaJson_Glow[_] = parseType(rawJsonStr).value
			schemaGlow shouldBe a [SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `string`
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			val circeJsonStr: JsonCirce = schemaGlow.asCirce(Draft04())
			
			Then("result should be a Json Circe string and should match original string")
			circeJsonStr shouldBe a [JsonCirce]
			//circeJsonStr.cutOutSchemaRef.noSpaces shouldEqual rawJsonStr.noSpaces
			//circeJsonStr.spaces2
			
			
			info(s"rawJsonStr = \n$rawJsonStr")
			info(s"length = ${rawJsonStr.length}")
			info(s"circeJsonStr = \n$circeJsonStr")
			
			val cut = circeJsonStr.spaces2.cutOutSchemaRef
			info(s"circe json cut off = \n${cut.removeSpaceBeforeColon}")
			info(s"length = ${cut.removeSpaceBeforeColon.length}")
			
		}
		
		Scenario("integer") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "integer"
				  |}
				  |""".stripMargin
			
			
			When("parsing via Andy Glow library's parser ...")
			
			val schemaGlow: SchemaJson_Glow[_] = parseType(rawJsonStr).value
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `integer`
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			val circeJsonStr: JsonCirce = schemaGlow.asCirce(Draft04())
			
			Then("result should be a Json Circe string and should match original string")
			circeJsonStr shouldBe a[JsonCirce]
			//circeJsonStr.cutOutSchemaRef.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			info(s"rawJsonStr = \n$rawJsonStr")
			info(s"length = ${rawJsonStr.length}")
			info(s"circeJsonStr = \n$circeJsonStr")
			
			val cut = circeJsonStr.cutOutSchemaRef
			info(s"circe json cut off = \n${cut.removeSpaceBeforeColon}")
			info(s"length = ${cut.removeSpaceBeforeColon.length}")
			
		}
		
		
		Scenario("array (string)") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "array",
				  |  "items": {
				  |    "type": "string"
				  |  }
				  |}
				  |""".stripMargin
			
			
			When("parsing via Andy Glow library's parser ...")
			
			val schemaGlow: SchemaJson_Glow[_] = parseType(rawJsonStr).value
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`string`)
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			val circeJsonStr: JsonCirce = schemaGlow.asCirce(Draft04())
			
			Then("result should be a Json Circe string and should match original string")
			circeJsonStr shouldBe a[JsonCirce]
			//circeJsonStr.cutOutSchemaRef.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			info(s"rawJsonStr = \n$rawJsonStr")
			info(s"length = ${rawJsonStr.length}")
			info(s"circeJsonStr = \n$circeJsonStr")
			
			val cut = circeJsonStr.cutOutSchemaRef
			info(s"circe json cut off = \n${cut.removeSpaceBeforeColon}")
			info(s"length = ${cut.removeSpaceBeforeColon.length}")
			
		}
		
		
		Scenario("array(array(array(integer)))") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "array",
				  |  "items": {
				  |    "type": "array",
				  |    "items": {
				  |      "type": "array",
				  |      "items": {
				  |        "type": "integer"
				  |      }
				  |    }
				  |  }
				  |}
				  |""".stripMargin
			
			
			When("parsing via Andy Glow library's parser ...")
			
			val schemaGlow: SchemaJson_Glow[_] = parseType(rawJsonStr).value
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`array`(`array`(`integer`)))
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			val circeJsonStr: JsonCirce = schemaGlow.asCirce(Draft04())
			
			Then("result should be a Json Circe string and should match original string")
			circeJsonStr shouldBe a[JsonCirce]
			//circeJsonStr.cutOutSchemaRef.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			info(s"rawJsonStr = \n$rawJsonStr")
			info(s"length = ${rawJsonStr.length}")
			info(s"circeJsonStr = \n$circeJsonStr")
			
			val cut = circeJsonStr.cutOutSchemaRef
			info(s"circe json cut off = \n${cut.removeSpaceBeforeColon}")
			info(s"length = ${cut.removeSpaceBeforeColon.length}")
			
		}
		
		
		
		Scenario("record (e.g. Position)") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "title": "Position",
				  |  "type": "object",
				  |  "required": [],
				  |  "properties": {
				  |    "coordinates": {
				  |      "type": "array",
				  |      "items": {
				  |        "type": "number",
				  |        "format": "number"
				  |      }
				  |    }
				  |  }
				  |}
				  |""".stripMargin
			
			When("parsing via Andy Glow library's parser ...")
			
			val schemaGlow: SchemaJson_Glow[_] = parseType(rawJsonStr).value
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			//TODO see what schemaglow is
			// schemaGlow shouldEqual SchemaJson_Glow.`string`
			
			info(s"schema glow record = \n$schemaGlow")
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			val circeJsonStr: JsonCirce = schemaGlow.asCirce(Draft04())
			
			Then("result should be a Json Circe string and should match original string")
			circeJsonStr shouldBe a[JsonCirce]
			////circeJsonStr.cutOutSchemaRef.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			info(s"rawJsonStr = \n$rawJsonStr")
			info(s"length = ${rawJsonStr.length}")
			info(s"circeJsonStr = \n$circeJsonStr")
			
			val cut = circeJsonStr.cutOutSchemaRef
			info(s"circe json cut off = \n${cut.removeSpaceBeforeColon}")
			info(s"length = ${cut.removeSpaceBeforeColon.length}")
			
			// NOTE: the two strings (rawJsonStr vs. andy golw's parsed json circe str) are NOT the same - have titles in different positions, and missing some titles (like 'format').
		}
	}
}
