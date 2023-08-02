package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs

//import cats.syntax.all._


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._
import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import utilMain.UtilMain
import utilMain.UtilMain.implicits._


/**
 * Testing skeuo-avro -> skeuo-json
 *
 */


class AvroToJsonSchema_SkeuoSkeuo_Specs extends AnyFeatureSpec with GivenWhenThen with Matchers {
	
	
	Feature("Convert skeuo-avro-adt to skeuo-json-adt (basic primitives)") {
		
		
		// TODO use funsuite and have a separate file for each type
		
		Scenario("array of int") {
			
			Given("a skeuomorph avro-schema")
			
			
			// value-check
			List(
				array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
			).distinct.length should equal(1) // should be the same values
			
			
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
			
			
			avroToJson_byCataTransAlg shouldBe a[AvroSchema_S[_] => JsonSchema_S[_]]
			UtilMain.getFuncTypeSubs(avroToJson_byCataTransAlg) shouldEqual "Fix[AvroSchema_S] => Fix[JsonSchema_S]"
			
			/*UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) shouldEqual "AvroSchema_S[Null] => JsonSchema_S[Null]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce]) shouldEqual "AvroSchema_S[JsonCirce] => JsonSchema_S[JsonCirce]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[AvroSchema_S]]) shouldEqual "AvroSchema_S[Fix[AvroSchema_S]] => JsonSchema_S[Fix[AvroSchema_S]]"
			*/
			
			
			Then("skeuo-json-schema should be correctly generated")
			
			
			
			
			
			// type-check
			jsonSkeuo shouldBe a[JsonSchema_S[_]] // Fix is invisible
			UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "Fix[JsonSchema_S]"
			
			
			// TODO put in "test()" / or in "should" form:
			//  And("--- skeuo-avro (fix) --> apache-avro: \nThe avro-apache string should coincide with the json circe string")
			import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
			
			
			info(s"-------------------------------" +
				s"\nCHECK 1" +
				s"\nskeuo-avro (fix) --> apache-avro-str: " +
				s"\nINPUT: \n$array1IntAvro_Fix_S" +
				s"\nOUTPUT: \n${skeuoToApacheAvroSchema(array1IntAvro_Fix_S).toString(true)}")
			
			
			val redocly_jsonSchemaFromData =
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
				s"\n-- REPLACE (redocly): json data str-> json schema str: \n$redocly_jsonSchemaFromData" +
				s"\n-- redocly:json-str -> circe -> skeuo-json (redocly): \n${decodeJsonStringToCirceToJsonSkeuo(redocly_jsonSchemaFromData)}" +
				s"\n-- redocly:json-str -> circe -> skeuo-json -> circe (via render)\n${libRender(decodeJsonStringToCirceToJsonSkeuo(redocly_jsonSchemaFromData).get)}" +
				s"\nskeuo-json: \n${decodeAvroSkeuoCirceToJsonSkeuo(array1IntAvro_Fix_S)}")
			
			
			info(s"-------------------------------" +
				s"\nCHECK 3" +
				s"\nskeuo-json (fix) --> json-circe --> skeuo-json (fix)" +
				s"\nskeuo-json: \n$array1IntJson_Fix_S" +
				s"\njson-circe: \n${libRender(array1IntJson_Fix_S).manicure}" +
				s"\nskeuo-json: \n${decodeJsonSkeuoToCirceToJsonSkeuo(array1IntJson_Fix_S)}")
			
			info(s"-------------------------------" +
				s"\nCHECK 4" +
				s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
				s"\nskeuo-avro: \n$array1IntAvro_Fix_S" +
				s"\njson-circe: \n${libToJson(array1IntAvro_Fix_S).manicure}" +
				s"\n-- REPLACE (redocly): json data str-> json schema str: \n$redocly_jsonSchemaFromData" +
				s"\n-- redocly:json-str -> circe -> skeuo-json (redocly): \n${decodeJsonStringToCirceToJsonSkeuo(redocly_jsonSchemaFromData)}" +
				s"\n-- redocly:json-str -> circe -> skeuo-json -> circe (via render)\n${libRender(decodeJsonStringToCirceToJsonSkeuo(redocly_jsonSchemaFromData).get)}" +
				s"\nskeuo-avro: \n${decodeAvroSkeuoToCirceToAvroSkeuo(array1IntAvro_Fix_S)}")
			
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
