package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs



import higherkindness.droste.data.Fix
import higherkindness.droste._
import higherkindness.droste.syntax.all._

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should._
import org.scalatest._
import Matchers._
import Inspectors._
import org.scalatest.Assertions._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.avroToJson_byCataTransAlg

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._


import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.rawstringData.jsonData.Data._

import scala.reflect.runtime.universe._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._

import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._


/**
 * Source funspec structures = https://www.scalatest.org/at_a_glance/FunSpec
 */
class ArraySpecs extends AnyFunSpec with Matchers {
	
	def testStructure(scenarioType: String,
				   avroS: AvroSchema_S[_], tpeS: String, avroC: AvroSchema_S[_], tpeC: String, avroFix: Fix[AvroSchema_S],
				   jsonFix: Fix[JsonSchema_S],
				   redocly_jsonSchemaFromData: String) = {
		
		
		describe(s"A skeuomorph avro schema - $scenarioType") {
			
			describe("value-checking - the different kinds of schema definitions ... ") {
				
				they("should have the same value") {
					
					assume(List(
						//array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
						avroS, avroC, avroFix
					).distinct.length == 1)
					
				}
			}
			
			describe("type-checking - the different kinds of schema definitions") {
				
				they("should all have the same Avro Schema type") {
					
					forEvery(List(avroS, avroC, avroFix)) {
						skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]]
					}
					//.map(skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]]) )
					
					// TODO
					/*info(s"func type avros = ${UtilMain.getFuncTypeSubs(avroS)}")
					UtilMain.getFuncTypeSubs(avroS) shouldEqual tpeS
					UtilMain.getFuncTypeSubs(avroC) shouldEqual tpeC*/
					UtilMain.getFuncTypeSubs(avroFix) shouldEqual "Fix[AvroSchema_S]"
				}
			}
		}
		
		
		describe("the conversion function of skeuomorph avro schema to json schema") {
			
			describe("the converter function should convert avro-schema to json-schema") {
				
				it("should have avro-schema to json-schema type") {
					
					avroToJson_byCataTransAlg shouldBe a[AvroSchema_S[_] => JsonSchema_S[_]]
					
					UtilMain.getFuncTypeSubs(avroToJson_byCataTransAlg) shouldEqual "Fix[AvroSchema_S] => Fix[JsonSchema_S]"
				}
			}
		}
		
		describe("converting avro-schema to json-schema") {
			val jsonSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(array1IntAvro_Fix_S)
			
			
			describe("type checking") {
				it("the result should have type skeuomorph-json-schema") {
					
					jsonSkeuo shouldBe a[JsonSchema_S[_]] // Fix is invisible
					UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "Fix[JsonSchema_S]"
				}
			}
			
			describe("CHECKING") {
				import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
				
				
				info(s"-------------------------------" +
					s"\nCHECK 1" +
					s"\nskeuo-avro (fix) --> apache-avro-str: " +
					s"\nINPUT: \n$avroS" +
					s"\nOUTPUT: \n${skeuoToApacheAvroSchema(avroFix).toString(true)}")
				
				
				/*val redocly_jsonSchemaFromData =
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
					  |""".stripMargin*/
					  
				info(s"-------------------------------" +
					s"\nCHECK 2" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-avro: \n$avroFix" +
					s"\njson-circe: \n${libToJson(avroFix).manicure}" +
					s"\n-- REPLACE (redocly): json data str-> json schema str: \n$redocly_jsonSchemaFromData" +
					s"\n-- redocly:json-str -> circe -> skeuo-json (redocly): \n${strToCirceToSkeuoJson(redocly_jsonSchemaFromData)}" +
					s"\n-- redocly:json-str -> circe -> skeuo-json -> circe (via render)\n${libRender(strToCirceToSkeuoJson(redocly_jsonSchemaFromData).get).manicure}" +
					s"\nskeuo-json: \n${checker_AvroSkeuo_toJsonCirce_toJsonSkeuo(avroFix)}")
				
				
				info(s"-------------------------------" +
					s"\nCHECK 3" +
					s"\nskeuo-json (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-json: \n$jsonFix" +
					s"\njson-circe: \n${libRender(jsonFix).manicure}" +
					s"\nskeuo-json: \n${checker_JsonSkeuo_toJsonCirce_toJsonSkeuo(jsonFix)}")
				
				info(s"-------------------------------" +
					s"\nCHECK 4" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-avro: \n$avroFix" +
					s"\njson-circe: \n${libToJson(avroFix).manicure}" +
					s"\n-- REPLACE (redocly): json data str-> json schema str: \n$redocly_jsonSchemaFromData" +
					s"\n-- redocly:json-str -> circe -> skeuo-json (redocly): \n${strToCirceToSkeuoJson(redocly_jsonSchemaFromData)}" +
					s"\n-- redocly:json-str -> circe -> skeuo-json -> circe (via render)\n${libRender(strToCirceToSkeuoJson(redocly_jsonSchemaFromData).get)}" +
					s"\nskeuo-avro: \n${checker_AvroSkeuo_toJsonCirce_toAvroSkeuo(avroFix)}")
				
				info(s"-------------------------------" +
					s"\nCONVERTER FUNCTION:" +
					s"\nskeuo-avro (fix) --> skeuo-json (fix)" +
					s"\nINPUT: \n$avroFix" +
					s"\nOUTPUT: \n${avroToJson_byCataTransAlg(avroFix)}")
				
			}
		}
	}
	
	/*Map(array1IntAvro_S → "AvroSchema_S[AvroSchema_S[Int]]",
		array1IntAvro_Circe_S → "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		array1IntAvro_Fix_S → "AvroSchema_S[AvroSchema_S[JsonCirce]]")*/
	
	
	testStructure(scenarioType = "array of int",
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFix = array1IntAvro_Fix_S,
		jsonFix = array1IntJson_Fix_S,
		redocly_jsonSchemaFromData =
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
	)
	// ----
	
	
	
	/*"Given: A skeuomorph avro schema" when {
		
		"value-checking - the different kinds of schema definitions ... " should {
			
			"have the same value" in {
				List(
					array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
				).distinct.length should equal(1) // should be the same values
			}
		}
		
		"type-checking - the different kinds of schema definitions" should {
			
			"all have the same Avro Schema type" in {
				List(
					array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
				).map(skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]])
				
				UtilMain.getFuncTypeSubs(array1IntAvro_S) shouldEqual "AvroSchema_S[AvroSchema_S[Int]]"
				UtilMain.getFuncTypeSubs(array1IntAvro_Circe_S) shouldEqual "AvroSchema_S[AvroSchema_S[JsonCirce]]"
				UtilMain.getFuncTypeSubs(array1IntAvro_Fix_S) shouldEqual "Fix[AvroSchema_S]"
			}
		}
	}*/
	
	
	//When()

}


/*test("value check - the different kinds of schema definitions should have the same value"){
	
	List(
		array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
	).distinct.length should equal(1) // should be the same values
}

test("type-check - the different kinds of schema definitions should all be of Avro Schema type"){
	
	List(
		array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
	).map(skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]])
	
	UtilMain.getFuncTypeSubs(array1IntAvro_S) shouldEqual "AvroSchema_S[AvroSchema_S[Int]]"
	UtilMain.getFuncTypeSubs(array1IntAvro_Circe_S) shouldEqual "AvroSchema_S[AvroSchema_S[JsonCirce]]"
	UtilMain.getFuncTypeSubs(array1IntAvro_Fix_S) shouldEqual "Fix[AvroSchema_S]"
}*/