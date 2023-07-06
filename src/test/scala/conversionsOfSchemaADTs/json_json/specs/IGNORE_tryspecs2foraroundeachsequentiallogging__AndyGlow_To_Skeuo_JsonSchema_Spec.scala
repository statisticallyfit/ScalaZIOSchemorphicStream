//package conversionsOfSchemaADTs.json_json.specs
//
//// NOTE: convertToAnyShouldWrapper - for the `shouldBe` part of the typecheck
////import org.scalatest.matchers.should.Matchers.{a, an, AnyShouldWrapper, convertToAnyShouldWrapper, theSameInstanceAs}
//import org.specs2.control.Debug
//import org.specs2.execute.{AsResult, Result}
//import org.specs2.mutable.Specification
//import org.specs2.specification.dsl.mutable.GWT
//import org.specs2.specification.dsl.mutable.GivenWhenThenSyntax
//import org.specs2.specification.script.StandardDelimitedStepParsers
//import org.specs2.specification.{AfterEach, BeforeEach, ForEach}
//
///*import org.scalatest.GivenWhenThen
//import org.scalatest.featurespec.AnyFeatureSpec
//import org.scalatest.matchers.should.Matchers*/
//
//
//
//import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._
//
//// HELP replacing with mine (copied) because cannot import since this is in his tests folder
////import com.github.andyglow.testsupport._
//import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.testsupportForTryValue._
//import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema_RawToGlowADT.parseType
//import utilTest.UtilTest
//
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
//
//import json.{Schema ⇒ JsonSchema_G}
//import JsonSchema_G._
//
//import scala.util.Try
//
//import scala.reflect.runtime.universe._
//
//
///**
// *
// */
//
//trait Transaction
//
//trait DatabaseContext extends ForEach[Transaction] {
//	// you need to define the "foreach" method
//	def foreach[R: AsResult](f: Transaction => R): Result = {
//		val transaction = openDatabaseTransaction
//		try AsResult(f(transaction))
//		finally closeDatabaseTransaction(transaction)
//	}
//
//	// create and close a transaction
//	def openDatabaseTransaction: Transaction = ???
//
//	def closeDatabaseTransaction(t: Transaction) = ???
//}
//
//
//class TRYSPECS2_AndyGlow_To_Skeuo_JsonSchema_Spec extends Specification with GWT with StandardDelimitedStepParsers
//	with GivenWhenThenSyntax with Debug /*with DatabaseContext*/ /*with Matchers */{
//
//	sequential
//
//	"adding numbers".pp
//
//
//	var number: Int = 0
//
//	"Spec about multiplying numbers" should {
//
//		"Simple case 3*2 = 6" in {// t : Transaction ⇒
//
//			//def afterEach
//			def after: Any = {
//				"CASE SIMPLE: 3*2 = 6".pp
//				"statement 1a".pp
//				"statement 1b".pp
//				"statement 1c".pp
//				"statement 1d".pp
//
//
//				println("CASE SIMPLE: 3*2 = 6")
//				println("statement 1a")
//				println("statement 1b")
//				println("statement 1c")
//				println("statement 1d")
//			}
//
//			Given("a first number {2}")(anInt) { two =>
//				number = two
//			}
//
//			When("I multiply it by {3}")(anInt) { three =>
//				number = number * three
//			}
//
//			Then("I get {6}")(anInt) { six: Int =>
//
//				//number shouldEqual n
//
//
//				//number shouldBe a[Int]
//				{
//					import org.scalatest.matchers.should.Matchers.{a, an, AnyShouldWrapper, convertToAnyShouldWrapper, theSameInstanceAs}
//
//					number shouldBe an [Int]
//				}
//
//				number mustEqual six
//				number should be equalTo six // note must end in specs2 type of assertions (not scalatest assertion (shouldBe a _)
//
//
//
//			}
//
//		}
//	}
//
//	"Spec about dividing numbers" should {
//
//		"case 64 / 4" in {
//
//			"CASE SIMPLE: 64 / 4 = 16".pp
//			"statement 2a".pp
//			"statement 2b".pp
//			"statement 2c".pp
//			"statement 2d".pp
//			println("CASE SIMPLE: 64 / 4 = 16")
//			println("statement 2a")
//			println("statement 2b")
//			println("statement 2c")
//			println("statement 2d")
//
//			Given("a first number {64}")(anInt) { sixtyFour =>
//				number = sixtyFour
//			}
//
//			When("I divide it by {4}")(anInt) { four =>
//				number = number / four
//			}
//
//			Then("I get {16}")(anInt) { sixteen: Int =>
//
//				//number shouldEqual n
//
//
//				//number shouldBe a[Int]
//
//				number mustEqual sixteen
//				number should be equalTo sixteen // note must end in specs2 type of assertions (not scalatest assertion (shouldBe a _)
//			}
//
//
//		}
//	}
//
//
//}
////class TRYSPECS2_AndyGlow_To_Skeuo_JsonSchema_Spec extends Specification with GWT with GivenWhenThenSyntax with StandardDelimitedStepParsers  with Matchers with Debug {
////
////	"something raw json here" should {
////		"another" in {
////			1 shouldEqual 1
////		}
////	}
////
////	"Convert raw json schema string (basic primitives)" should {
////
////
////		"string" in {
////
////
////			Given("a json schema (single {string})")(aString) { i ⇒
////
////
////				var jsonSchemaStr: String =
////					"""
////					  |{
////					  |  "type": "string"
////					  |}
////					  |""".stripMargin
////
////				jsonSchemaStr = i
////			}
////
////			When("parsing")(aString)
////			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
////
////			Then("result should be `string` (schema-adt from andy glow)")
////			result shouldBe a[JsonSchema_G[_]]
////			result should not be a[String] // has been converted
////			result shouldEqual `string`
////
////
////			"\n\nSTRING TEST: ".pp
////			s"result simple string = $result".pp
////			s"typeOf[result.type] = ${typeOf[result.type]}".pp
////			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}".pp
////
////
////			And("converting the andy glow schema to skeuo schema")
////			val skeuoJson: JsonSchema_S[Any] = andyGlowToSkeuoJsonSchema(result) // TODO parameter instead of Any?
////
////			Then("result should be a skeuo adt json schema")
////			skeuoJson shouldBe a[JsonSchema_S[_]]
////			skeuoJson shouldEqual JsonSchema_S.StringF()
////
////			s"skeuo Json (adt) = ${skeuoJson}".pp
////			s"skeuo Json (toString) = ${skeuoJson.toString}".pp
////			s"typeOf[skeuoJson.type] = ${typeOf[skeuoJson.type]}".pp
////			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(skeuoJson)}".pp
////		}
////
////
////		Scenario("integer") {
////			Given("a json schema (single integer)")
////			val jsonSchemaStr: String =
////				"""
////				  |{
////				  |  "type": "integer"
////				  |}
////				  |""".stripMargin
////
////
////			When("parsing")
////			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
////
////			Then("result should be `integer` (schema-adt from andy glow)")
////			result shouldBe a[JsonSchema_G[_]]
////			result should not be a[String] // has been converted
////			result shouldEqual `integer`
////
////
////			"\n\nINTEGER TEST:")
////			s"result simple integer = $result")
////			s"typeOf[result.type] = ${typeOf[result.type]}")
////			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
////		}
////
////		Scenario("array") {
////			Given("a json schema (single array)")
////			val jsonSchemaStr: String =
////				"""{
////				  | "type": "array",
////				  | "items": {
////				  |   "type": "string"
////				  | }
////				  |}
////				""".stripMargin
////
////
////			When("parsing")
////			val result: JsonSchema_G[_] = parseType(jsonSchemaStr).value
////
////			Then("result should be `array` (schema-adt from andy glow)")
////			result shouldBe a[JsonSchema_G[_]]
////			result should not be a[String] // has been converted
////			result shouldEqual `array`(`string`)
////
////			"\n\nARRAY TEST: ")
////			s"result simple array = $result")
////			//s"typeOf[result.type] = ${typeOf[result.type]}")
////			//s"typetag = ${implicitly[TypeTag[]]}")
////			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
////		}
////
////	}
////
////
////	Feature("Converting raw json schema string (entire)") {
////		Scenario("entire schema string") {
////
////			Given("an entire json schema-string")
////			val sampleSchemaJsonRaw: String = // sensor_type
////				"""
////				  |{
////				  |  "type": "object",
////				  |  "properties": {
////				  |    "manufacturer": {
////				  |      "type": "string"
////				  |    },
////				  |    "name": {
////				  |      "type": "string"
////				  |    },
////				  |    "id": {
////				  |      "type": "integer"
////				  |    }
////				  |  },
////				  |  "required": [
////				  |    "manufacturer",
////				  |    "name",
////				  |    "id"
////				  |  ]
////				  |}
////				  |""".stripMargin
////
////			When("parsing")
////			val resultTry: Try[JsonSchema_G[_]] = parseType(sampleSchemaJsonRaw)
////			val result: JsonSchema_G[_] = resultTry.value
////
////			Then("result should be a schema-adt from Andy Glow")
////			result shouldBe a[JsonSchema_G[_]]
////			result should not be a[String] // has been converted
////
////
////			"\n\nENTIRE SCHEMA TEST: ")
////			s"entire schema result = $resultTry")
////			s"entire schema result (without try): ${resultTry.getOrElse(None)}")
////			s"entire schema result (simple): $result")
////			s"typeOf[result.type] = ${typeOf[result.type]}")
////			s"getFuncTypeSubs = ${GeneralTestUtil.getFuncTypeSubs(result)}")
////		}
////	}
////}
