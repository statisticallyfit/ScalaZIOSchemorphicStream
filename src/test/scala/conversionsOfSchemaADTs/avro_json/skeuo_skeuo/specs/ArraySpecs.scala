package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs

import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.avroToJson_byCataTransAlg
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.Decoder.Result
import io.circe.{Json ⇒ JsonCirce}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import org.scalatest.Inspectors._
import org.scalatest._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import utilMain.UtilMain
import utilMain.UtilMain.implicits._


/**
 * Source funspec structures = https://www.scalatest.org/at_a_glance/FunSpec
 */
class ArraySpecs extends AnyFunSpec with Matchers {
	
	def testStructure(
					  scenarioType: String,
					  avroS: AvroSchema_S[_], tpeS: String, avroC: AvroSchema_S[_], tpeC: String, avroFix: Fix[AvroSchema_S],
					  jsonFix: Fix[JsonSchema_S],
					  redocly_jsonSchemaFromData: String
				  ) = {
		
		
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
				
				
				val apacheAvro: AvroSchema_A = skeuoToApacheAvroSchema(avroFix)
				val apacheAvroStr: String = apacheAvro.toString(true).removeSpaceBeforeColon
				val skeuoAvro: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro)
				val skeuoJson: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro)
				val circeJson_fromJsonSkeuo: JsonCirce = libRender(skeuoJson)
				
				info(s"TATI's way: apache-avro-str ---> skeuo-avro --> skeuo-json --> json-circe")
				info(s"-- apache-avro-str\n: $apacheAvroStr")
				info(s"-- skeuoAvro: $skeuoAvro")
				info(s"-- skeuoJson: $skeuoJson")
				info(s"-- skeuoJson -> json-circe\n: ${circeJson_fromJsonSkeuo.manicure}")
				
				info(s"\n\nMY DECODING WAY: ")
				info(s"STEP 1): apache-avro-str --> skeuo-avro | Reason: state how string-avro looks (assumption / expectation)")
				
				info(s"STEP 2): skeuo-avro --> skeuo-json | Reason: Trans converter")
				
				info(s"STEP 3): skeuo-avro --> json-circe | Reason: get common denominator (circe), from avro side")
				
				info(s"STEP 4): skeuo-json --> json-circe | Reason: get common denominator (circe), from json side")
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 2a: " +
					s"\nraw-json-str (expectation input) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output) " +
					s"\n|\t Reason: find out how json-str translates to json-adt to check correctness of my Trans converter " +
					s"\n|\t (from json side) " +
					s"\n|\t (starting from: json-str)")
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 2b: " +
					s"\nraw-json-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans input) " +
					s"\n|\t Reason: find out how json-str translates to avro-adt  " +
					s"\n|\t (from avro side) " +
					s"\n|\t (starting from: json-str)")
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 2c: " +
					s"\nraw-avro-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans input) " +
					s"\n|\t Reason: find out how avro-str translates to avro-adt  " +
					s"\n|\t (from avro side) " +
					s"\n|\t (starting from: avro-str)")
				
				//--------------------
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 1a: " +
					s"\napache-avro-str (expectation input) -> (skeuo-avro) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
					s"\n|\t Reason: compare skeuo-json (trans output) vs. skeuo-json (decoder output) to check correctness of my Trans converter " +
					s"\n|\t (from json-side) " +
					s"\n|\t (starting from: avro-str)")
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 1b: " +
					s"\nskeuo-avro -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
					s"\n|\t Reason: compare skeuo-json (trans output) vs. skeuo-json (decoder output) to check correctness of my Trans converter " +
					s"\n|\t (from json-side) " +
					s"\n|\t (starting from: skeuo-avro-adt (easier than string-start))")
				
				
				
				info(s"\n-----------------------------------------------------------" +
					s"\nCHECKER 3: " +
					s"\nskeuo-json (trans output) -> json-circe -> skeuo-avro (decoder output) vs. skeuo avro (trans input) " +
					s"\n|\t Reason: compare how skeuo-json (trans output) renders to skeuo-avro to check correctness of my Trans converter " +
					s"\n|\t (from avro-side) " +
					s"\n|\t (starting from: skeuo-json output)")
				
				
				
				val a3: AvroSchema_A = skeuoToApacheAvroSchema(array3IntAvro_Fix_S)
				//val u: JsonCirce = unsafeParse(a3.toString)
				val u: JsonCirce = libToJsonAltered(array3IntAvro_Fix_S)
				val as: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(u)
				val js: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(u)
				
				info(s"HERE: skeuo-avro: $array3IntAvro_Fix_S")
				info(s"HERE: apache-str: $a3")
				info(s"skeuo-avro -> json-circe?: $u")
				info(s"skeuo-avro -> json-circe -> skeuo-json?: $js")
				info(s"skeuo-avro -> json-circe -> skeuo-avro?: $as")
				
				
				info(s"-------------------------------" +
					s"\nCHECK 2" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-avro: \n$avroFix" +
					s"\njson-circe: \n${libToJsonAltered(avroFix).manicure}" +
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
				
				
				
				val a4: AvroSchema_A = skeuoToApacheAvroSchema(avroFix)
				val ua4: JsonCirce = unsafeParse(a4.toString(true)) // useless
				//val uc4: JsonCirce = unsafeParse(libRender(jsonFix).toString)
				//val s4: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(ua4)
				info(s"-------------------------------" +
					s"\nCHECK 4" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-avro: \n$avroFix" +
					s"\n-> circe: \n${libToJsonAltered(avroFix).manicure}" +
					s"\n-> apache-avro-str: \n${a4.toString(true)}" +
					s"\n-- apache-avro-str -> circe: \n${ua4}" +
					s"\n-- apache-avro-str -> circe -> skeuo-avro: \n${checker_AvroSkeuo_toJsonCirce_toAvroSkeuo(avroFix)}" +
					s"\n-- redocly: json-str -> circe -> skeuo-avro: \n${strToCirceToSkeuoAvro(redocly_jsonSchemaFromData)}" +
					s"\n\n-- skeuo-json -> circe: \n${libRender(jsonFix).manicure}")
				
				
				val j: JsonCirce = libRender(avroToJson_byCataTransAlg(avroFix))
				val a: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(j)
				info(s"-------------------------------" +
					s"\nCONVERTER FUNCTION:" +
					s"\nskeuo-avro (fix) --> skeuo-json (fix)" +
					s"\nINPUT: \n$avroFix" +
					s"\nOUTPUT: \n${avroToJson_byCataTransAlg(avroFix)}" +
					s"\nskeuo-avro -> skeuo-json -> json-circe -> skeuo-avro" +
					s"\n-> json-circe: ${j}" +
					s"\n-> skeuo-avro: $a")
				
				
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