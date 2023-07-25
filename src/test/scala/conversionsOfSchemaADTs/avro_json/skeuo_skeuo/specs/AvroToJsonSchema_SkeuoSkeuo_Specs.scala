package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs

//import cats.syntax.all._


import higherkindness.droste.data.Fix

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._


import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._

import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._

import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.rawstringData.jsonData.Data._





/**
 * Testing skeuo-avro -> skeuo-json
 *
 */


class AvroToJsonSchema_SkeuoSkeuo_Specs  extends AnyFeatureSpec with GivenWhenThen with Matchers  {


	Feature("Convert skeuo-avro-adt to skeuo-json-adt (basic primitives)"){
		
		Scenario("null"){
			
			Given("skeuo-avro-schema")
			
			// value-level check
			nullAvro_S shouldEqual TNull()
			nullAvro_Fix_S shouldEqual TNull()
			nullAvro_Circe_S shouldEqual TNull()
			
			// type-level check
			nullAvro_S shouldBe a[AvroSchema_S[_]]
			UtilMain.getFuncTypeSubs(nullAvro_S) shouldEqual "AvroSchema_S[Null]"
			
			// HELP error says: no classtag available for Fix
			//nullAvro_Fix_S shouldBe a[Fix[AvroSchema_S]]
			UtilMain.getFuncTypeSubs(nullAvro_Fix_S) should equal("Fix[AvroSchema_S]")
			
			nullAvro_Circe_S shouldBe a[AvroSchema_S[_]]
			UtilMain.getFuncTypeSubs(nullAvro_Circe_S) should equal("AvroSchema_S[JsonCirce]")
			
			
			info(s"CHECK skuo-fix == skeuo-simple = ${nullAvro_S === nullAvro_Fix_S}")
			
			/*info(s"skeuo-avro = $nullAvro_S | type = ${UtilMain.getFuncTypeSubs(nullAvro_S)}")
			info(s"skeuo-avro (fix) = $nullAvro_SFix | type = ${UtilMain.getFuncTypeSubs(nullAvro_SFix)}")
			info(s"skeuo-avro (circe) = $nullAvro_SC | type = ${UtilMain.getFuncTypeSubs(nullAvro_SC)}")*/
			
			
			When("converting to skeuo-json-schema by applying the function")
			
			/*List(
				avroToJsonFunction[Null],
				avroToJsonFunction[JsonCirce],
				avroToJsonFunction[Fix[AvroSchema_S]]
			).map(
				func ⇒ func shouldBe a [AvroSchema_S[_] => JsonSchema_S[_]]
			)
			
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) shouldEqual "AvroSchema_S[Null] => JsonSchema_S[Null]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce]) shouldEqual "AvroSchema_S[JsonCirce] => JsonSchema_S[JsonCirce]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[AvroSchema_S]]) shouldEqual "AvroSchema_S[Fix[AvroSchema_S]] => JsonSchema_S[Fix[AvroSchema_S]]"*/
			
			/*info(s"func type = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Null])}")
			info(s"func type (circe) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce])}")
			info(s"func type (fix) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[AvroSchema_S]])}")*/
			
			
			// TODO next:
			// 1) str -> json circe -> skeuo-json-fixed (to be able to create tests from desired string output to skeuo and then check if that skeuo is matching my generated skeuo)
			// 2) str -> apache avro (parser!) -> skeuo-avro (create tests from desired avro string to skeuo-avro)
			// 3) ... from 2), get skeuo-avro -> json circe (using toJson call) (reason: to get from avro-str -> json circe)
			// 4) skeuo-json -> json circe (using render() call)
			
			
			
			Then("skeuo-json-schema should be correctly generated")
			
			val jsonSkeuo = avroToJson_byCataTransAlg(nullAvro_Fix_S)
			
			/*val jsonSkeuo: JsonSchema_S[Null] = avroToJson(nullAvro_S)
			val jsonSkeuo_C: JsonSchema_S[JsonCirce] = avroToJson[JsonCirce](nullAvro_Circe_S)
			val jsonSkeuo_UF: JsonSchema_S[Fix[AvroSchema_S]] = avroToJson(nullAvro_Fix_S.unfix)*/
			
			
			// value-level check
			/*List(jsonSkeuo, jsonSkeuo_C, jsonSkeuo_UF,
				nullJson_S,
				ObjectF(properties = List(), required = List())
			).distinct.length should equal(1)
			
			// type-level check
			List(
				jsonSkeuo, jsonSkeuo_C, jsonSkeuo_UF
			).map(js ⇒ js shouldBe a [JsonSchema_S[_]])
			
			UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "JsonSchema_S[Null]"
			UtilMain.getFuncTypeSubs(jsonSkeuo_C) shouldEqual "JsonSchema_S[JsonCirce]"
			UtilMain.getFuncTypeSubs(jsonSkeuo_UF) shouldEqual "JsonSchema_S[Fix[AvroSchema_S]]"*/
			
			
			
			
			And("--- skeuo-avro (fix) --> apache-avro: \nThe avro-apache string should coincide with the json circe string")
			
			/*def roundTripVerify_AvroStrAndJsonCirce(avroStrStart: String, avroSkeuoMidCheck: AvroSchema_S[JsonCirce], jsonCirceEnd: JsonCirce): Boolean = {
			
				// step 1 - parse avro str to avro-apache-schema
				val schemaAvro_Apache: SchemaAvro_Apache = new SchemaAvro_Apache.Parser().parse(avroStrStart)
			
				// step 2 - check skeuos are equal (generated vs. given)
				val schemaAvro_S: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(schemaAvro_Apache)
				
				schemaAvro_S shouldEqual avroSkeuoMidCheck
				
				// step 3 - generate json circe
				val schemaJson_Circe: JsonCirce = AvroSchema_S.toJson(avroSkeuoMidCheck)
				
				// step 4 - check json circe are equal (generated vs. given)
				schemaJson_Circe.manicure shouldEqual jsonCirceEnd.manicure
				
				schemaAvro_S == avroSkeuoMidCheck && schemaJson_Circe.manicure == jsonCirceEnd.manicure
			}*/
			
			
			info(s"--- skeuo-avro (fix) --> apache-avro = ${skeuoToApacheAvroSchema(nullAvro_Fix_S).toString(true)}")
			info(s"--- skeuo-avro --> skeuo-json = $jsonSkeuo")
			info(s"--- skeuo-avro -> json circe = ${AvroSchema_S.toJson(nullAvro_Circe_S).manicure}")
			// TODO put in raw-string VERSUS json-circe
			info(s"--- skeuo-json -> json circe = \n${libRender(nullJson_Fix_S).manicure}")
			//info(s"--- skeuo-json -> json circe = \n${JsonSchema_S.render(nullJson_Circe_S).manicure}")
			
			
			
			
			
			
			// TODO HELP FIX
			skeuoToApacheAvroSchema(nullAvro_Fix_S).toString(true) should equal ("\"null\"")
			
			jsonSkeuo shouldEqual nullJson_Fix_S
			
			// TODO fix: AvroSchema_S.toJson(nullAvro_Circe_S).manicure should equal ("Null")
			
			
			// TODO fix
			/*JsonSchema_S.render(nullJson_Circe_S).manicure should equal (
				"""
				  |{
				  |  "type": "object",
				  |  "properties": {},
				  |  "required": []
				  |}
				  |""".stripMargin)*/
		}
		
		// TODO use funsuite and have a separate file for each type
		
		Scenario("array of int") {
			
			Given("skeuo-avro-schema")
			
			
			// value-check
			List(
				array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
			).distinct.length should equal(1) // should be the same values
			val butterfly = 0
			
			// type-check
			List(
				array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
			).map(skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]])
			
			UtilMain.getFuncTypeSubs(array1IntAvro_S) shouldEqual "AvroSchema_S[AvroSchema_S[Int]]"
			UtilMain.getFuncTypeSubs(array1IntAvro_Circe_S) shouldEqual "AvroSchema_S[AvroSchema_S[JsonCirce]]"
			UtilMain.getFuncTypeSubs(array1IntAvro_Fix_S) shouldEqual "Fix[AvroSchema_S]"
			
			
			// Sanity check
			array1IntAvro_S should equal(array1IntAvro_Fix_S)
			
			
			
			
			When("converting to skeuo-json-schema by applying the function")
			
			
			val jsonSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(array1IntAvro_Fix_S)
			
			
			avroToJson_byCataTransAlg shouldBe a [AvroSchema_S[_] => JsonSchema_S[_]]
			UtilMain.getFuncTypeSubs(avroToJson_byCataTransAlg) shouldEqual "Fix[AvroSchema_S] => Fix[JsonSchema_S]"
			
			/*UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) shouldEqual "AvroSchema_S[Null] => JsonSchema_S[Null]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce]) shouldEqual "AvroSchema_S[JsonCirce] => JsonSchema_S[JsonCirce]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[AvroSchema_S]]) shouldEqual "AvroSchema_S[Fix[AvroSchema_S]] => JsonSchema_S[Fix[AvroSchema_S]]"
			*/
			
			
			Then("skeuo-json-schema should be correctly generated")
			
			
			
			
			
			// type-check
			jsonSkeuo shouldBe a [JsonSchema_S[_]] // Fix is invisible
			UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "Fix[JsonSchema_S]"
			
			
			// TODO put in "test()" / or in "should" form:
			//  And("--- skeuo-avro (fix) --> apache-avro: \nThe avro-apache string should coincide with the json circe string")
			import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
			
			
			info(s"-------------------------------" +
				s"\nCHECK 1" +
				s"\nskeuo-avro (fix) --> apache-avro-str: " +
				s"\nINPUT: \n$array1IntAvro_Fix_S" +
				s"\nOUTPUT: \n${skeuoToApacheAvroSchema(array1IntAvro_Fix_S).toString(true)}")
			
			
			val rawArrayInt =
				"""
				  |{
				  |  "type": "object",
				  |  "properties": {
				  |    "type": {
				  |      "type": "string"
				  |    },
				  |    "items": {
				  |      "type": "string"
				  |    }
				  |  },
				  |  "additionalProperties": false
				  |}
				  |""".stripMargin
			info(s"-------------------------------" +
				s"\nCHECK 2" +
				s"\nskeuo-avro (fix) --> json-circe --> skeuo-json (fix)" +
				s"\nskeuo-avro: \n$array1IntAvro_Fix_S" +
				s"\njson-circe: \n${libToJson(array1IntAvro_Fix_S).manicure}" +
				s"\n-- REPLACE: json data -> json schema (redocly): \n$rawArrayInt" +
				s"\n-- json-str -> circe -> skeuo-json (from redocly): \n${strToCirceToSkeuoJson(rawArrayInt)}" +
				s"\nskeuo-json: \n${checker_AvroSkeuo_toJsonCirce_toJsonSkeuo(array1IntAvro_Fix_S)}")
			
			
			info(s"-------------------------------" +
				s"\nCHECK 3" +
				s"\nskeuo-json (fix) --> json-circe --> skeuo-json (fix)" +
				s"\nskeuo-json: \n$array1IntJson_Fix_S" +
				s"\njson-circe: \n${libRender(array1IntJson_Fix_S).manicure}" +
				s"\nskeuo-json: \n${checker_JsonSkeuo_toJsonCirce_toJsonSkeuo(array1IntJson_Fix_S)}")
			
			info(s"-------------------------------" +
				s"\nCHECK 4" +
				s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
				s"\nskeuo-avro: \n$array1IntAvro_Fix_S" +
				s"\njson-circe: \n${libToJson(array1IntAvro_Fix_S).manicure}" +
				s"\n-- REPLACE: json data -> json schema (redocly): \n$rawArrayInt" +
				s"\n-- json-str -> circe -> skeuo-avro (from redocly): \n${strToCirceToSkeuoAvro(rawArrayInt)}" +
				s"\nskeuo-avro: \n${checker_AvroSkeuo_toJsonCirce_toAvroSkeuo(array1IntAvro_Fix_S)}")
			
			info(s"-------------------------------" +
				s"\nCONVERTER FUNCTION:" +
				s"\nskeuo-avro (fix) --> skeuo-json (fix)" +
				s"\nINPUT: \n$array1IntAvro_Fix_S" +
				s"\nOUTPUT: \n${avroToJson_byCataTransAlg(array1IntAvro_Fix_S)}")
			
			
			
			
		}
		// TODO left off here (avro issues) to convert myToJson result from json-value into json-schema because now it gives Left('not well formed") error
		
		
		/*import scala.reflect.runtime.universe._
		
		import fi.oph.scalaschema.{SchemaFactory, SchemaToJson, Schema ⇒ SchemaJson_Opetus}
		//import org.json4s.package.JValue
		
		import org.json4s.jackson.JsonMethods
		import org.json4s.jackson.JsonMethods._ //asJsonNode
		import org.json4s.JsonAST.{JObject, JNull, JInt, JString, JArray}
		import org.json4s.JsonAST.JValue
		
		//import org.json4s.{JNull, JInt, JString, JArray}
		
		
		import com.github.fge.jsonschema.core.report.ListReportProvider
		import com.github.fge.jsonschema.core.report.LogLevel.{ERROR, FATAL}
		import com.github.fge.jsonschema.main.{JsonSchemaFactory, JsonValidator}
		
		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs.HELP_Opetushallitus_jdata_to_jschema.helpers._
		
		val j: JValue = JNull
		
		// HELP left off here issue with json4s import (not for scala 2.12)
		val nulljc: String = jsonSchemaOf(JNull.getClass)
		//val nulljc = jsonSchemaOf(classOf[JNull.type])
		val intjc = jsonSchemaOf[JInt]
		val strjc = jsonSchemaOf[JString]
		val arrjc = jsonSchemaOf[JArray]
		
		println(s"--- OPETUS HALLITUS: json data -> json schema circe")
		println(s"nulljc = $nulljc")
		println(s"intjc = $intjc")
		println(s"strjc = $strjc")
		println(s"arrjc = $arrjc")*/
	}
}
