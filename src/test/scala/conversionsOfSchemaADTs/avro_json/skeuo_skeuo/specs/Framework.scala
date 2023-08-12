package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.{avroToJson_byCataTransAlg, jsonToAvro_byAnaTransCoalg}
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import io.circe.Decoder.Result
import io.circe.{Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._

import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.schemaData.jsonData.circeData.Data._
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.ImplicitArgs






object Framework extends AnyFunSpec with Matchers {
	
	
	object Vars {
		
		
		implicit val impArgs: ImplicitArgs = new ImplicitArgs(rawAvroStr, rawJsonStr, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
		
		import impArgs._
		
		val dcommon = CommonDecoderCheckSpecs()
		val d1 = DecoderCheck1_Canonical_AvroStringToJsonString()
		val d2 = DecoderCheck2_JsonSkeuoToAvroString()
		val d3a = DecoderCheck3a_JsonStringBegin_JsonDecoderVsJsonTrans()
		val d3b = DecoderCheck3b_JsonStringBegin_AvroDecoderVsAvroTrans()
		val d3c = DecoderCheck3c_AvroStringBegin_AvroDecoderVsAvroTrans()
		val d3d = DecoderCheck3d_AvroStringBegin_JsonDecoderVsJsonTrans()
	}
	
	
	// NOTE: must pass in the args explicitly for this function because the data  is different
	def testStructure(
		scenarioType: String,
		rawAvroStr: String, rawJsonStr: String,
		jsonCirceCheck: JsonCirce,
		avroS: AvroSchema_S[_], tpeS: String, avroC: AvroSchema_S[_], tpeC: String,
		avroFixS: Fix[AvroSchema_S],
		jsonFixS: Fix[JsonSchema_S]
	) = {
		
		
		
		Vars()
		
		describe(s"\n\nA skeuo avro schema - $scenarioType") {
			
			describe("\nValue-checking - the different kinds of schema definitions ... ") {
				
				they("should have the same value") {
					
					assume(List(
						//array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
						avroS, avroC, avroFixS
					).distinct.length == 1
					)
					
				}
			}
			
			describe("\nType-checking - the different kinds of schema definitions") {
				
				they("should all have the same Avro Schema type") {
					
					forEvery(List(avroS, avroC, avroFixS)) {
						ska ⇒ ska shouldBe a[AvroSchema_S[_]]
					}
					//.map(skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]]) )
					
					// TODO
					/*info(s"func type avros = ${UtilMain.getFuncTypeSubs(avroS)}")
					UtilMain.getFuncTypeSubs(avroS) shouldEqual tpeS
					UtilMain.getFuncTypeSubs(avroC) shouldEqual tpeC*/
					UtilMain.getFuncTypeSubs(avroFixS) shouldEqual "Fix[AvroSchema_S]"
				}
			}
		}
		
		
		describe("\n\nThe conversion function") {
			
			describe("\nShould convert between Skeuomorph schemas") {
				
				it("should have avro-schema to json-schema type") {
					
					avroToJson_byCataTransAlg shouldBe a[AvroSchema_S[_] => JsonSchema_S[_]]
					
					UtilMain.getFuncTypeSubs(avroToJson_byCataTransAlg) shouldEqual "Fix[AvroSchema_S] => Fix[JsonSchema_S]"
				}
				
				it("should have json-schema to avro-schema type") {
					
					jsonToAvro_byAnaTransCoalg shouldBe a[JsonSchema_S[_] => AvroSchema_S[_]]
					
					UtilMain.getFuncTypeSubs(jsonToAvro_byAnaTransCoalg) shouldEqual "Fix[JsonSchema_S] => Fix[AvroSchema_S]"
				}
			}
		}
		
		describe("\n\nConverting avro-schema to json-schema, and vice versa - using Trans function") {
			val jsonSkeuoResult: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
			val avroSkeuoResult: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
			
			describe("\nType checking") {
				
				it("the json-skeuo result should have type JsonSchema (from skeuomorph)") {
					
					jsonSkeuoResult shouldBe a[JsonSchema_S[_]] // Fix is invisible
					UtilMain.getFuncTypeSubs(jsonSkeuoResult) shouldEqual "Fix[JsonSchema_S]"
				}
				
				it("the avro-skeuo result should have type AvroSchema (from skeuomorph)") {
					
					avroSkeuoResult shouldBe a[AvroSchema_S[_]]
					UtilMain.getFuncTypeSubs(avroSkeuoResult) shouldEqual "Fix[AvroSchema_S]"
				}
			}
			
			describe("\nValue checking") {
				it("the json-skeuo result should equal the json-skeuo input") {
					
					jsonSkeuoResult should equal(jsonFixS)
				}
				
				it("the avro-skeuo result should equal the avro-skeuo input") {
					avroSkeuoResult should equal(avroFixS)
				}
			}
		}
		
		describe("\n\nConverting avro-schema to json-schema - using circe decoder (as check against Trans function)") {
			
			
			describe("\nCommon decoder specs") {
				
				
				it("avro-string and json-string should yield same avro-skeuo") {
					info("\n\nSOMETHING HERE TOO")
					
					dcommon.equalityOfInitialAvroSkeuosFromAvroOrJsonInputString()
				}
				
				it("avro-string and json-string should yield same json-skeuo") {
					dcommon.equalityOfInitialJsonSkeuosFromAvroOrJsonInputString()
				}
				
				it("json-circe should be the same when starting from either avro-string or json-string") {
					dcommon.equalityOfCirceFromAvroOrJsonInputString()
				}
				it("json-circe should be the same when starting from either avro-skeuo or json-skeuo") {
					dcommon.equalityOfCirceBySkeuoAvroAndCirceBySkeuoJsonFromAvroOrJsonInputString()
				}
				
				it("the ending avro-skeuo should be the same regardless of the kind of source-string:  " +
				   "\navro-string --> circe --> (initial) avro-skeuo --> circe --> avro-skeuo" +
				   "\navro-string --> circe --> (initial) json-skeuo --> circe --> avro-skeuo" +
				   "\njson-string --> circe --> (initial) avro-skeuo --> circe --> avro-skeuo" +
				   "\njson-string --> circe --> (initial) json-skeuo --> circe --> avro-skeuo" +
				   "\nThese last avro-skeuos should be the same even though they start from either avro/json-strings and go through either avro/json-skeuos"
				) {
					
					dcommon.equalityOfLastAvroSkeuosFromAvroOrJsonInputString()
				}
				
				it("the ending json-skeuo should be the same regardless of the kind of source-string:  " +
				   "\navro-string --> circe --> (initial) avro-skeuo --> circe --> json-skeuo" +
				   "\navro-string --> circe --> (initial) json-skeuo --> circe --> json-skeuo" +
				   "\njson-string --> circe --> (initial) avro-skeuo --> circe --> json-skeuo" +
				   "\njson-string --> circe --> (initial) json-skeuo --> circe --> json-skeuo" +
				   "\nThese last json-skeuos should be the same even though they start from either avro/json-strings and go through either avro/json-skeuos"
				) {
					
					dcommon.equalityOfLastJsonSkeuosFromAvroOrJsonInputString()
				}
				
				it("the skeuo-adt from decoding via circe should equal the skeuo-adt from the Trans function") {
					
					dcommon.equalityOfSkeuosFromInputAndCirceDecodingAndTrans()
				}
				
			}
			
			
			describe("\nSpec 1 - canonical conversion of avro-string to json-string") {
				
				
				it("avro-skeuo (from apache) should equal avro-skeuo (from input)") {
					d1.Checking.parsingAvroStrToApacheToSkeuoYieldsCorrectAvroSkeuo()
				}
				
				it("Trans function applied to either apache-avro or skeuo-avro should yield same json-skeuo") {
					d1.Checking.transOfApacheAvroOrSkeuoAvroShouldYieldSameJsonSkeuo()
				}
				
				it("json-circe (from circe decoder of the json-skeuo generated by Trans function) should match the correct json-circe") {
					d1.Checking.transLeadsToCorrectCirce()
				}
			}
			
			
			describe("\nSpec 2: going backwards from json-skeuo to avro-skeuo and comparing the resulting avro-str with given avro-str") {
				
				
				it("given avro-string should match the apache-avro-string generated it") {
					
					d2.Checking.equalityOfInputAvroSkeuoAndOutputAvroString()
				}
				
				it("avro-skeuo generated from Trans function should match avro-skeuo from decoding json-skeuo -> circe -> avro-skeuo") {
					d2.Checking.equalityOfAvroSkeuoFromDecoderAndTrans()
				}
				
				it("json-circe from the chain (avro-str -> apache-avro -> avro-skeuo) should match the json-circe from the given json-skeuo") {
					d2.Checking.equalityOfCircesFromAvroOrJsonInputSkeuo()
				}
				
			}
			
			describe("\nSpec 3a: json-string --> json-skeuo, and comparing the decoder json-skeuo with Trans json-skeuo") {
				
				
				it("json-skeuo from circe-decoder should match json-skeuo from Trans function") {
					
					d3a.Checking.equalityOfJsonSkeuoFromDecoderAndTrans()
				}
			}
			
			describe("\nSpec 3b: json-string --> avro-skeuo, and comparing the decoder avro-skeuo with Trans avro-skeuo") {
				
				it("avro-skeuo from circe-decoder should match avro-skeuo from Trans function") {
					
					d3b.Checking.equalityOfAvroSkeuoFromDecoderAndTrans()
				}
			}
			
			describe("\nSpec 3c: avro-string --> avro-skeuo, and comparing the decoder avro-skeuo with Trans avro-skeuo") {
				
				
				it("avro-skeuo from circe-decoder should match avro-skeuo from Trans function") {
					
					d3c.Checking.equalityOfAvroSkeuoFromDecoderAndTrans()
				}
			}
			
			
			describe("\nSpec 3d: avro-string --> json-skeuo, and comparing the decoder json-skeuo with Trans json-skeuo") {
				
				
				it("json-skeuo from circe-decoder should match json-skeuo from Trans function") {
					
					d3d.Checking.equalityOfJsonSkeuoFromDecoderAndTrans()
				}
			}
			
			
		}
		
	}
}
