package conversionsOfSchemaADTs.json_json.specs


import conversionsOfSchemaADTs.json_json.skeuo_glow.Skeuo_AndyGlow._
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.ParseStrToADT.parseType
import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.testsupportForTryValue._
//import utilTest.UtilTest
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import utilMain.UtilMain
//import JsonSchema_S._

import json.Schema._
import json.{Schema ⇒ JsonSchema_G}


/**
 *
 */
class JsonSchema_RawStr_Glow_Skeuo_Spec extends AnyFeatureSpec with GivenWhenThen with Matchers {


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
			val schemaGlow: JsonSchema_G[_] = parseType(jsonSchemaStr).value

			Then("result should be `string` (schema-adt from andy glow)")
			schemaGlow shouldBe a[JsonSchema_G[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `string`


			info("\n\nSTRING TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")


			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?


			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[JsonSchema_S[_]]
			schemaSkeuo shouldEqual JsonSchema_S.StringF()

			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
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
			val schemaGlow: JsonSchema_G[_] = parseType(jsonSchemaStr).value

			Then("result should be `integer` (schema-adt from andy glow)")
			schemaGlow shouldBe a[JsonSchema_G[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `integer`


			info("\n\nINTEGER TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")


			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?


			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[JsonSchema_S[_]]
			schemaSkeuo shouldEqual JsonSchema_S.IntegerF()

			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
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
			val schemaGlow: JsonSchema_G[_] = parseType(jsonSchemaStr).value

			Then("result should be `array(`string`)` (schema-adt from andy glow)")
			schemaGlow shouldBe a[JsonSchema_G[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`string`)


			info("\n\nARRAY(STRING) TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")


			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?


			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[JsonSchema_S[_]]
			schemaSkeuo shouldEqual JsonSchema_S.ArrayF(JsonSchema_S.StringF())

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
			val schemaGlow: JsonSchema_G[_] = parseType(jsonSchemaStr).value

			Then("result should be `array`(`array`(`array`(`integer`)))) (schema-adt from andy glow)")
			schemaGlow shouldBe a[JsonSchema_G[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual `array`(`array`(`array`(`integer`)))


			info("\n\nARRAY TRIPLE TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")


			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?


			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[JsonSchema_S[_]]
			schemaSkeuo shouldEqual JsonSchema_S.ArrayF(JsonSchema_S.ArrayF(JsonSchema_S.ArrayF(JsonSchema_S.IntegerF())))

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
			val schemaGlowTry: Try[JsonSchema_G[_]] = parseType(sampleSchemaJsonRaw)
			val schemaGlow: JsonSchema_G[_] = schemaGlowTry.value

			Then("result should be a schema-adt from Andy Glow")
			schemaGlow shouldBe a[JsonSchema_G[_]]
			schemaGlow should not be a[String] // has been converted


			info("\n\nENTIRE SCHEMA TEST: ")
			info(s"entire schema result = $schemaGlowTry")
			info(s"entire schema result (without try): ${schemaGlowTry.getOrElse(None)}")
			info(s"entire schema result (simple): $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")


			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?


			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[JsonSchema_S[_]]
			// TODO update here
			//schemaSkeuo shouldEqual ArrayF(ArrayF(ArrayF(IntegerF())))

			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
		}
	}*/
}
