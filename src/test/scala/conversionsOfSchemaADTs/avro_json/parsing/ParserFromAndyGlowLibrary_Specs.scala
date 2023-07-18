package conversionsOfSchemaADTs.avro_json.parsing



import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._


import utilMain.UtilMain

import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._


import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import SchemaJson_Skeuo._

import json.{Schema ⇒ SchemaJson_Glow}
//import SchemaJson_Glow._



import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._



import scala.reflect.runtime.universe._

import scala.util.Try

import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr.ParseStrToADT.parseType

import utilMain.UtilMain



/**
 *
 */
class ParserFromAndyGlowLibrary_Specs extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
	Feature("Convert raw json schema string to Json Circe (basic primitives)") {
		
		
		Scenario("string") {
			
			Given("a json schema (single string)")
			val s: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin
			
			
			When("parsing via Andy Glow library's parser ...")
			val schemaGlow: SchemaJson_Glow[_] = parseType(s).value
			schemaGlow shouldBe a[SchemaJson_Glow[_]]
			schemaGlow should not be a[String] // has been converted
			schemaGlow shouldEqual SchemaJson_Glow.`string`
			
			// Now parse to json - circe
			And("when parsing the schema-glow to Json Circe...")
			
			// TODO here https://github.com/search?q=repo%3Aandyglow%2Fscala-jsonschema%20circe&type=code
			
			
			Then("result should be a Json Circe string")
			
			
			
			info("\n\nSTRING TEST: ")
			info(s"schemaGlow = $schemaGlow")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaGlow)}")
			
			
			And("converting the andy glow schema to skeuo schema")
			val schemaSkeuo: SchemaJson_Skeuo[Any] = andyGlowToSkeuoJsonSchema(schemaGlow) // TODO parameter instead of Any?
			
			
			Then("result should be a skeuo adt json schema")
			schemaSkeuo shouldBe a[SchemaJson_Skeuo[_]]
			schemaSkeuo shouldEqual SchemaJson_Skeuo.StringF()
			
			info(s"schemaSkeuo = ${schemaSkeuo}")
			info(s"getFuncType (short + long) = ${UtilMain.getFuncType(schemaSkeuo)}")
		}
	}
}
