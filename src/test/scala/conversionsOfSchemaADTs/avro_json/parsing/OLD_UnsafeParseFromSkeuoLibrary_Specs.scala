package conversionsOfSchemaADTs.avro_json.parsing



import higherkindness.droste.data.Fix
import higherkindness.droste._
import higherkindness.droste.syntax.all._

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._

import io.circe.{Json ⇒ JsonCirce}
import io.circe.Decoder
import higherkindness.skeuomorph.openapi.JsonDecoders._

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._


import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._

import scala.reflect.runtime.universe._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._


/**
 * Parsing:
 * 1) json str -> json circe (using unsafe parse)
 * 2) json circe -> json-skeuo (using circe's decoder)
 *
 */
class OLD_UnsafeParseFromSkeuoLibrary_Specs extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
	
	Feature("Convert raw json schema string to Json Circe (basic primitives)") {
		
		
		Scenario("string") {
			
			Given("a json schema (single string)")
			
			
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "string"
				  |}
				  |""".stripMargin.trim
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a [JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = StringF()
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			/*import higherkindness.skeuomorph.avro.AvroF
			val da = Decoder[Fix[AvroF]].decodeJson(circeJsonStr)
			info(s"AVRO THINGY: json circe (string) -> avro-skeuo (string) = $da")*/
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
		}
		
		
		Scenario("integer") {
			
			Given("a json schema (single string)")
			val rawJsonStr: String =
				"""
				  |{
				  |  "type": "integer"
				  |}
				  |""".stripMargin.trim
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = IntegerF()
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
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
				  |""".stripMargin.trim
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = ArrayF(StringF())
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
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
				  |""".stripMargin.trim
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = ArrayF(ArrayF(ArrayF(IntegerF())))
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
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
				  |""".stripMargin.trim
				  
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a [String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck: ObjectF[ArrayF[FloatF[Nothing]]] = ObjectF(properties =List(Property(name = "coordinates", tpe = ArrayF(FloatF()))),required = List())
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
		}
		
		Scenario("record (e.g. Location, longer record)") {
			
			Given("a json schema (single string)")
			
			val rawJsonStr: String =
				"""{
				  |  "title": "Locations",
				  |  "type": "object",
				  |  "required": [
				  |    "position",
				  |    "sensorName",
				  |    "name",
				  |    "id"
				  |  ],
				  |  "properties": {
				  |    "id": {
				  |      "type": "string"
				  |    },
				  |    "name": {
				  |      "type": "string"
				  |    },
				  |    "position": {
				  |      "title": "Position",
				  |      "type": "object",
				  |      "required": [],
				  |      "properties": {
				  |        "coordinates": {
				  |          "type": "array",
				  |          "items": {
				  |            "type": "number",
				  |            "format": "number"
				  |          }
				  |        }
				  |      }
				  |    },
				  |    "sensorName": {
				  |      "type": "string"
				  |    },
				  |    "symbol": {
				  |      "title": "Any",
				  |      "type": "object",
				  |      "required": [],
				  |      "properties": {}
				  |    }
				  |  }
				  |}
				  |""".stripMargin.trim
			
			
			When("parsing via Skeuomorph library's parser into Json Circe...")
			
			val circeJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			
			
			Then("result should be a Json Circe string")
			
			rawJsonStr shouldBe a[String]
			circeJsonStr shouldBe a[JsonCirce]
			
			circeJsonStr.noSpaces shouldEqual rawJsonStr.noSpaces
			
			And("When converting json circe to json skeuo... ")
			
			val skeuoDecodedFromCirce = Decoder[JsonSchema_S.Fixed].decodeJson(circeJsonStr)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck: ObjectF[JsonSchema_S[_ <: ArrayF[FloatF[Nothing]]]] = ObjectF(
				properties = List(
					Property(name = "name", tpe = StringF()),
					Property(name = "symbol",tpe = ObjectF(
						properties = List(),
						required = List()
					)),
					Property(name = "id",tpe = StringF()),
					Property(name = "sensorName",tpe = StringF()),
					Property(name = "position",tpe = ObjectF(
						properties = List(
							Property(name = "coordinates",tpe = ArrayF(FloatF()))
						),
						required = List()
					))
				),
				required = List("position", "sensorName", "name", "id")
			)
			
			skeuoDecodedFromCirce.getOrElse(None) shouldEqual skeuoCheck
			
			
			info(s"raw json str = \n$rawJsonStr")
			//info(s"result json circe = \n$circeJsonStr")
			info(s"result json circe (removeColonSpace) = \n${circeJsonStr.removeSpaceBeforeColon}")
			info(s"circeJsonStr.noSpaces = ${circeJsonStr.noSpaces}")
			info(s"raw json str no space = ${rawJsonStr.noSpaces}")
			
			info(s"raw length | trimmed = ${rawJsonStr.length} | ${rawJsonStr.trim.length}")
			info(s"circe length | trimmed = ${circeJsonStr.removeSpaceBeforeColon.length} | ${circeJsonStr.removeSpaceBeforeColon.trim.length}")
			
			
			info(s"DECODING: Json circe --> SKEUO: $skeuoDecodedFromCirce")
			
		}
	}
	
}
