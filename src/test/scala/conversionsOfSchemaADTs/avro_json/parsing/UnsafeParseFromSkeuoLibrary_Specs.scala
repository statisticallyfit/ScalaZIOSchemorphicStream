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
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._
import io.circe.Decoder.Result
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = StringF()
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = IntegerF()
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = ArrayF(StringF())
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck = ArrayF(ArrayF(ArrayF(IntegerF())))
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
			
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck: ObjectF[ArrayF[FloatF[Nothing]]] = ObjectF(properties =List(Property(name = "coordinates", tpe = ArrayF(FloatF()))),required = List())
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
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
			
			val skeuoDecodedFromCirce = Decoder[SchemaJson_Skeuo.Fixed].decodeJson(circeJsonStr).getOrElse(None)
			
			
			Then("... result should match canonical json skeuo")
			
			val skeuoCheck: ObjectF[SchemaJson_Skeuo[_ <: ArrayF[FloatF[Nothing]]]] = ObjectF(
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
			
			skeuoDecodedFromCirce shouldEqual skeuoCheck
			
			
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
