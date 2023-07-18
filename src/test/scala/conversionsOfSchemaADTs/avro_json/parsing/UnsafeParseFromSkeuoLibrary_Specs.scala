package conversionsOfSchemaADTs.avro_json.parsing



import higherkindness.droste.data.Fix
import higherkindness.droste._
import higherkindness.droste.syntax.all._


import org.scalatest.{GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._



import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._

import io.circe.{Json ⇒ JsonCirce}
import io.circe.parser.parse

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._


import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._

import scala.reflect.runtime.universe._



import utilMain.UtilMain
import utilMain.UtilMain.implicits._

import utilTest.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._


/**
 *
 */
class UnsafeParseFromSkeuoLibrary_Specs extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
	
	Feature("Convert raw json schema string to Json Circe (basic primitives)") {
		
		
		Scenario("string") {
			
			Given("a json schema (single string)")
			
			// NOTE: uses 2-tab space (from https://codebeautify.org/jsonviewer)
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a [JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			circeJsonStr.toString()
			
			info(s"raw json str = \n$rawJsonStr")
			info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (toString) = \n${circeJsonStr.toString}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			
			
		}
		
		
		Scenario("integer") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "integer"
				  |}
				  |""".stripMargin
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			info(s"raw json str = \n$rawJsonStr")
			info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (toString) = \n${circeJsonStr.toString}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
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
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			info(s"raw json str = \n$rawJsonStr")
			info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (toString) = \n${circeJsonStr.toString}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		}
		
		
		Scenario("array triple (integer)") {
			
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
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			info(s"raw json str = \n$rawJsonStr")
			info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (toString) = \n${circeJsonStr.toString}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		}
		
		
		Scenario("record (e.g. Location)") {
			
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
				  
			val rawJsonStr_Longer: String =
				"""
				  |{
				  |"title": "Locations",
				  |"type": "object",
				  |"required": [
				  |  "position",
				  |  "sensorName",
				  |  "name",
				  |  "id"
				  |],
				  |"properties": {
				  |  "id": {
				  |    "type": "string"
				  |  },
				  |  "name": {
				  |    "type": "string"
				  |  },
				  |  "position": {
				  |    "title": "Position",
				  |    "type": "object",
				  |    "required": [],
				  |    "properties": {
				  |      "coordinates": {
				  |        "type": "array",
				  |        "items": {
				  |          "type": "number",
				  |          "format": "number"
				  |        }
				  |      }
				  |    }
				  |  },
				  |  "sensorName": {
				  |    "type": "string"
				  |  },
				  |  "symbol": {
				  |    "title": "Any",
				  |    "type": "object",
				  |    "required": [],
				  |    "properties": {}
				  |  }
				  |}
				  |}
				  |""".stripMargin
			
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			info(s"raw json str = \n$rawJsonStr")
			info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (toString) = \n${circeJsonStr.toString}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
		info(s"raw json str no space = ${rawJsonStr.noSpaces}")
		
		}
		
		// TODO get more strings (for each kind of TYPE) from the RawStr - Glow - Skeuo test spec
	}
	
}
