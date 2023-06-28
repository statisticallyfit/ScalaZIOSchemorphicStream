package conversionsOfSchemaADTs.avro_json.skeuo_circe.specs



import org.scalatest.featurespec.AnyFeatureSpec //wordspec.{AnyWordSpec}
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should._

import com.github.andyglow.json.{ParseJson, Value}

//import com.github.andyglow.jsonschema.ParseJsonSchema
// HELP replacing with mine (copied) because need that makeType function
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema._

import com.github.andyglow.scalamigration._
import json.{Schema ⇒ SchemaJson_AndyGlow}
import SchemaJson_AndyGlow._ // for the `boolean`, `string` types etc.

// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupport._



import scala.util.Try


object parseHelper {
	def parseType(x: String): Try[SchemaJson_AndyGlow[_]] = {
		
		//val firstPart: Try[Value.obj] = (ParseJson(x) find { case e: Value.obj => e })
		
		(ParseJson(x) find { case e: Value.obj => e }) flatMap ParseJsonSchema.makeType
	}
}
import parseHelper._

/**
 *
 */
class JsonStrSchema_To_AndyGlowJsonSchema_Convert_TrySpec extends AnyFeatureSpec with GivenWhenThen with Matchers {

	feature("Convert raw json schema string (basic primitives)") {
		
	
		
		scenario("string"){
			
			Given("a json schema (single string)")
			val jsonSchemaStr: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin
				  
			
			When("parsing")
			val result: SchemaJson_AndyGlow[_] = parseType(jsonSchemaStr).value
			
			Then("result should be `string` (schema-adt from andy glow)")
			result shouldBe a [SchemaJson_AndyGlow[_]]
			result should not be a [String] // has been converted
			result shouldEqual `string`
			
			println(s"result simple string = $result")
		}
		
	}
	
	feature("Converting raw json schema string (entire)") {
		scenario("entire schema string") {
			
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
			val resultTry: Try[SchemaJson_AndyGlow[_]] = parseType(sampleSchemaJsonRaw)
			val result: SchemaJson_AndyGlow[_] = resultTry.value
			
			Then("result should be a schema-adt from Andy Glow")
			result shouldBe a[SchemaJson_AndyGlow[_]]
			result should not be a[String] // has been converted
			
			println(s"entire schema result = $resultTry")
			println(s"entire schema result (without try): ${resultTry.getOrElse(None)}")
			println(s"entire schema result (simple): $result")
		}
	}
			/*"testing: title" in {
				parseType {
					"""{
					  | "type": "string",
					  | "title": "testing title"
					  |}
					""".stripMargin
				}.value.title shouldBe Some("testing title")
			}
			
			"testing: string: date" in {
				import `string`._
				import Format._
				
				val result = parseType {
					"""{
					  | "type": "string",
					  | "format": "date"
					  |}
				""".stripMargin
				}.value
				
				result shouldBe `string`(`date`)
				
				println(s"string: date result = $result")
			}
			
			"testing: string: time" in {
				import `string`._
				import Format._
				
				parseType {
					"""{
					  | "type": "string",
					  | "format": "time"
					  |}
				""".stripMargin
				}.value shouldBe `string`(`time`)
			}
			
			"testing: string: ipv6" in {
				import `string`._
				import Format._
				
				parseType {
					"""{
					  | "type": "string",
					  | "format": "ipv6"
					  |}
				""".stripMargin
				}.value shouldBe `string`(`ipv6`)
			}
			
			"testing: integer" in {
				parseType {
					"""{
					  | "type": "integer",
					  |}
				""".stripMargin
				}.value shouldBe `integer`
			}
			
			"testing: number" in {
				parseType {
					"""{
					  | "type": "number",
					  |}
				""".stripMargin
				}.value shouldBe `number`[Int]
			}
			
			"testing: boolean" in {
				parseType {
					"""{
					  | "type": "boolean",
					  |}
				""".stripMargin
				}.value shouldBe `boolean`
			}
			
			"testing: array" in {
				val result: SchemaJson_AndyGlow[_] = parseType {
					"""{
					  | "type": "array",
					  | "items": {
					  |   "type": "string"
					  | }
					  |}
				""".stripMargin
				}.value
				
				result shouldBe `array`(`string`)
				
				println(s"array result = $result")*/
			
		
		
}
