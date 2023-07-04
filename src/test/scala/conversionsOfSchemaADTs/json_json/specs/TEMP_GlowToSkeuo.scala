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
import utilTest.UtilTest


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}

import json.{Schema ⇒ SchemaJson_Glow}
import SchemaJson_Glow._

import scala.util.Try

import scala.reflect.runtime.universe._


/**
 *
 */
class TEMP_GlowToSkeuo extends AnyFeatureSpec with GivenWhenThen with Matchers with UtilTest  {


	def caseString = {

		val jsonSchemaStr: String =
			"""
			  |{
			  |  "type": "string"
			  |}
			  |""".stripMargin



		val schemaGlow/*: JsonSchema_G[_]*/ = parseType(jsonSchemaStr).value
		val schemaSkeuo/*: JsonSchema_S[Any]*/ = andyGlowToSkeuoJsonSchema(schemaGlow)


		println("\nSTRING CASE:\n")

		println(s"schemaGlow = $schemaGlow") // should equal `string`
		assert(schemaGlow == `string`)
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaGlow)}")
		println(s"schemaSkeuo = $schemaSkeuo")
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaSkeuo)}")
	}

	def caseInteger = {

		val jsonSchemaStr: String =
			"""
			  |{
			  |  "type": "integer"
			  |}
			  |""".stripMargin


		val schemaGlow /*: JsonSchema_G[_]*/ = parseType(jsonSchemaStr).value
		val schemaSkeuo /*: JsonSchema_S[Any]*/ = andyGlowToSkeuoJsonSchema(schemaGlow)


		println("\nINTEGER CASE:\n")

		println(s"schemaGlow = $schemaGlow") // should equal `integer`
		assert(schemaGlow == `integer`)
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaGlow)}")
		println(s"schemaSkeuo = $schemaSkeuo")
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaSkeuo)}")
	}

	def caseArray = {

		val jsonSchemaStr: String =
			"""{
			  | "type": "array",
			  | "items": {
			  |   "type": "string"
			  | }
			  |}
			""".stripMargin


		val schemaGlow /*: JsonSchema_G[_]*/ = parseType(jsonSchemaStr).value
		val schemaSkeuo /*: JsonSchema_S[Any]*/ = andyGlowToSkeuoJsonSchema(schemaGlow)


		println("\nARRAY CASE:\n")

		println(s"schemaGlow = $schemaGlow")
		assert(schemaGlow == `array`(`string`))
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaGlow)}")
		println(s"schemaSkeuo = $schemaSkeuo")
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaSkeuo)}")
	}

	def caseArray3 = {
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


		val schemaGlow /*: JsonSchema_G[_]*/ = parseType(jsonSchemaStr).value
		val schemaSkeuo /*: JsonSchema_S[Any]*/ = andyGlowToSkeuoJsonSchema(schemaGlow)


		println("\nARRAY CASE (TRIPLE):\n")

		println(s"schemaGlow = $schemaGlow")
		assert(schemaGlow == `array`(`array`(`array`(`integer`))))
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaGlow)}")
		println(s"schemaSkeuo = $schemaSkeuo")
		println(s"getFuncTypeSubs = ${getFuncTypeSubs(schemaSkeuo)}")
	}


	// TODO composite type example: enum(number(double)) = https://github.com/andyglow/scala-jsonschema/blob/d52b2bb7d38785bc4e4545285eee7eca1e8978ce/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L63


	caseString

	caseInteger

	caseArray

	caseArray3

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
			val resultTry: Try[JsonSchema_G[_]] = parseType(sampleSchemaJsonRaw)
			val result: JsonSchema_G[_] = resultTry.value

			Then("result should be a schema-adt from Andy Glow")
			result shouldBe a[JsonSchema_G[_]]
			result should not be a[String] // has been converted

			println(s"entire schema result = $resultTry")
			println(s"entire schema result (without try): ${resultTry.getOrElse(None)}")
			println(s"entire schema result (simple): $result")
			println(s"typeOf[result.type] = ${typeOf[result.type]}")
			println(s"getFuncTypeSubs = ${getFuncTypeSubs(result)}")
		}
	}*/
}
