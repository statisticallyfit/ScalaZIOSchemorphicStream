package conversionsOfSchemaADTs.json_json.specs

// NOTE: convertToAnyShouldWrapper - for the `shouldBe` part of the typecheck
import org.scalatest.matchers.should.Matchers.{a, an, theSameInstanceAs, convertToAnyShouldWrapper}

import org.specs2.control.Debug
import org.specs2.mutable.Specification
import org.specs2.specification.dsl.mutable.GWT
import org.specs2.specification.dsl.mutable.GivenWhenThenSyntax
import org.specs2.specification.script.StandardDelimitedStepParsers


/*import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec*/
import org.scalatest.matchers.should.Matchers

// import org.specs2.Specification // wanted to use 'sequential' to make tests run in order to see print stataements in order but cannot because already using scalatest (functions get mixed up)
// NOTE using sbt now  = https://stackoverflow.com/a/15146140

import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._

// HELP replacing with mine (copied) because cannot import since this is in his tests folder
//import com.github.andyglow.testsupport._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupportForTryValue._
import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema_RawToGlowADT.parseType
import utilTest.GeneralTestUtil


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import json.{Schema ⇒ JsonSchema_G}
import JsonSchema_G._

import scala.util.Try

import scala.reflect.runtime.universe._


/**
 *
 */


class TRYSPECS2_AndyGlow_To_Skeuo_JsonSchema_Spec extends Specification with org.specs2.specification.dsl.mutable.GWT with StandardDelimitedStepParsers with GivenWhenThenSyntax /*with Matchers */{
	
	sequential
	
	"adding numbers".p
	
	"Spec about multiplying numbers" should {
		
		"Simple case 3*2 = 6" in {
			Given("a first number {2}")(anInt) { two =>
				number = two
			}
			
			When("I multiply it by {3}")(anInt) { three =>
				number = number * three
			}
			
			Then("I get {6}")(anInt) { six: Int =>
				
				//number shouldEqual n
				
				
				number shouldBe a[Int]
				
				number mustEqual six
				number should be equalTo six // note must end in specs2 type of assertions (not scalatest assertion (shouldBe a _)
			}
			
			"CASE SIMPLE: 3*2 = 6".p
			"statement 1a".p
			"statement 1b".p
			"statement 1c".p
			"statement 1d".p
		}
	}
	
	"Spec about dividing numbers" should {
		"case 64 / 4" in {
			Given("a first number {64}")(anInt) { sixtyFour =>
				number = sixtyFour
			}
			
			When("I divide it by {4}")(anInt) { four =>
				number = number / four
			}
			
			Then("I get {16}")(anInt) { sixteen: Int =>
				
				//number shouldEqual n
				
				
				number shouldBe a[Int]
				
				number mustEqual sixteen
				number should be equalTo sixteen // note must end in specs2 type of assertions (not scalatest assertion (shouldBe a _)
			}
			
			"CASE SIMPLE: 64 / 4 = 16".p
			"statement 2a".p
			"statement 2b".p
			"statement 2c".p
			"statement 2d".p
		}
	}
	
	var number = 0
}
//class TRYSPECS2_AndyGlow_To_Skeuo_JsonSchema_Spec extends Specification with GWT with GivenWhenThenSyntax with StandardDelimitedStepParsers  with Matchers with Debug {
//
//	"something raw json here" should {
//		"another" in {
//			1 shouldEqual 1
//		}
//	}
//
//	"Convert raw json schema string (basic primitives)" should {
//
//
//		"string" in {
//
//
//			Given("a json schema (single {string})")(aString) { i ⇒
//
//
//				var jsonSchemaStr: String =
//					"""
//					  |{
//					  |  "type": "string"
//					  |}
//					  |""".stripMargin
//
//				jsonSchemaStr = i
//			}
//
//			When("parsing")(aString)
//			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
//
//			Then("result should be `string` (schema-adt from andy glow)")
//			result shouldBe a[JsonSchema_G[_]]
//			result should not be a[String] // has been converted
//			result shouldEqual `string`
//
//
//			"\n\nSTRING TEST: ".pp
//			s"result simple string = $result".pp
//			s"typeOf[result.type] = ${typeOf[result.type]}".pp
//			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}".pp
//
//
//			And("converting the andy glow schema to skeuo schema")
//			val skeuoJson: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(result) // TODO parameter instead of Any?
//
//			Then("result should be a skeuo adt json schema")
//			skeuoJson shouldBe a[JsonSchema_S[_]]
//			skeuoJson shouldEqual JsonSchema_S.StringF()
//
//			s"skeuo Json (adt) = ${skeuoJson}".pp
//			s"skeuo Json (toString) = ${skeuoJson.toString}".pp
//			s"typeOf[skeuoJson.type] = ${typeOf[skeuoJson.type]}".pp
//			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(skeuoJson)}".pp
//		}
//
//
//		Scenario("integer") {
//			Given("a json schema (single integer)")
//			val jsonSchemaStr: String =
//				"""
//				  |{
//				  |  "type": "integer"
//				  |}
//				  |""".stripMargin
//
//
//			When("parsing")
//			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
//
//			Then("result should be `integer` (schema-adt from andy glow)")
//			result shouldBe a[JsonSchema_G[_]]
//			result should not be a[String] // has been converted
//			result shouldEqual `integer`
//
//
//			"\n\nINTEGER TEST:")
//			s"result simple integer = $result")
//			s"typeOf[result.type] = ${typeOf[result.type]}")
//			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
//		}
//
//		Scenario("array") {
//			Given("a json schema (single array)")
//			val jsonSchemaStr: String =
//				"""{
//				  | "type": "array",
//				  | "items": {
//				  |   "type": "string"
//				  | }
//				  |}
//				""".stripMargin
//
//
//			When("parsing")
//			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
//
//			Then("result should be `array` (schema-adt from andy glow)")
//			result shouldBe a[JsonSchema_G[_]]
//			result should not be a[String] // has been converted
//			result shouldEqual `array`(`string`)
//
//			"\n\nARRAY TEST: ")
//			s"result simple array = $result")
//			//s"typeOf[result.type] = ${typeOf[result.type]}")
//			//s"typetag = ${implicitly[TypeTag[]]}")
//			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
//		}
//
//	}
//
//
//	Feature("Converting raw json schema string (entire)") {
//		Scenario("entire schema string") {
//
//			Given("an entire json schema-string")
//			val sampleSchemaJsonRaw: String = // sensor_type
//				"""
//				  |{
//				  |  "type": "object",
//				  |  "properties": {
//				  |    "manufacturer": {
//				  |      "type": "string"
//				  |    },
//				  |    "name": {
//				  |      "type": "string"
//				  |    },
//				  |    "id": {
//				  |      "type": "integer"
//				  |    }
//				  |  },
//				  |  "required": [
//				  |    "manufacturer",
//				  |    "name",
//				  |    "id"
//				  |  ]
//				  |}
//				  |""".stripMargin
//
//			When("parsing")
//			val resultTry: Try[JsonSchema_G[_]] = parseType(sampleSchemaJsonRaw)
//			val result: JsonSchema_G[_] = resultTry.value
//
//			Then("result should be a schema-adt from Andy Glow")
//			result shouldBe a[JsonSchema_G[_]]
//			result should not be a[String] // has been converted
//
//
//			"\n\nENTIRE SCHEMA TEST: ")
//			s"entire schema result = $resultTry")
//			s"entire schema result (without try): ${resultTry.getOrElse(None)}")
//			s"entire schema result (simple): $result")
//			s"typeOf[result.type] = ${typeOf[result.type]}")
//			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
//		}
//	}
//}
