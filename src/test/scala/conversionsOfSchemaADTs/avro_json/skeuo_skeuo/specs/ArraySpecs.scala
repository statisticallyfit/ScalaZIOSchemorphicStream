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
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._
import utilMain.UtilMain
import utilMain.UtilMain.implicits._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo._

/**
 * Source funspec structures = https://www.scalatest.org/at_a_glance/FunSpec
 */
class ArraySpecs extends AnyFunSpec with Matchers {
	
	def testStructure(scenarioType: String,
				   rawAvroStr: String, rawJsonStr: String,
				   jsonCirceCheck: JsonCirce,
				   avroS: AvroSchema_S[_], tpeS: String, avroC: AvroSchema_S[_], tpeC: String,
				   avroFixS: Fix[AvroSchema_S],
				   jsonFixS: Fix[JsonSchema_S]/*,
				   redocly_jsonSchemaFromData: String*/
				  ) = {
		
		
		describe(s"A Skeuomorph avro schema - $scenarioType") {
			
			describe("value-checking - the different kinds of schema definitions ... ") {
				
				they("should have the same value") {
					
					assume(List(
						//array1IntAvro_S, array1IntAvro_Circe_S, array1IntAvro_Fix_S
						avroS, avroC, avroFixS
					).distinct.length == 1)
					
				}
			}
			
			describe("type-checking - the different kinds of schema definitions") {
				
				they("should all have the same Avro Schema type") {
					
					forEvery(List(avroS, avroC, avroFixS)) {
						skeuoSchema ⇒ skeuoSchema shouldBe a[AvroSchema_S[_]]
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
		
		
		describe("the conversion function") {
			
			describe("should convert between Skeuomorph schemas") {
				
				it("should have avro-schema to json-schema type") {
					
					avroToJson_byCataTransAlg shouldBe a[AvroSchema_S[_] => JsonSchema_S[_]]
					
					UtilMain.getFuncTypeSubs(avroToJson_byCataTransAlg) shouldEqual "Fix[AvroSchema_S] => Fix[JsonSchema_S]"
				}
				
				it("should have json-schema to avro-schema type"){
					
					jsonToAvro_byAnaTransCoalg shouldBe a [JsonSchema_S[_] => AvroSchema_S[_] ]
					
					UtilMain.getFuncTypeSubs(jsonToAvro_byAnaTransCoalg) shouldEqual "Fix[JsonSchema_S] => Fix[AvroSchema_S]"
				}
			}
		}
		
		describe("converting avro-schema to json-schema") {
			val jsonSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(array1IntAvro_Fix_S)
			
			
			describe("type checking") {
				it("the result should have type Skeuomorph-json-schema") {
					
					jsonSkeuo shouldBe a[JsonSchema_S[_]] // Fix is invisible
					UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "Fix[JsonSchema_S]"
				}
			}
			
			describe("avro to json conversion, using circe decoder as backwards-check") {
				
				import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.ImplicitArgs
				
				implicit val impArgs = new ImplicitArgs(rawAvroStr, rawJsonStr, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
				
				// TODO find out how to call just the class name without args?
				
				val d0 = DecoderCheck0_CanonicalWay_AvroStringToJsonString(impArgs)
				d0.printOuts()
				
				d0.Checks.checkInputAvroSkeuoEqualsAvroSkeuoFromApacheString
				d0.Checks.checkInputJsonSkeuoEqualsOutputJsonSkeuosFromApacheStringAndFromTrans
				d0.Checks.checkInputJsonCirceEqualsOutputJsonCirceFromTrans
				//d0.checking()
				
				
				
				val d1a = DecoderCheck1a_AvroSkeuoToJsonSkeuo(impArgs)
				d1a.printOuts()
				d1a.Checks.checkInputJsonSkeuoEqualsOutputJsonSkeuosFromDecoderAndFromTrans
				//d1a.checking()
				
				
				val d1b = DecoderCheck1b_JsonSkeuoToAvroSkeuo(impArgs)
				d1b.printOuts()
				d1b.Checks.checkInputAvroSkeuoEqualsAvroSkeuoFromDecoderAndFromTrans
				//d1b.checking()
				
				
				
				val d2 = DecoderCheck2_JsonSkeuoToAvroString(impArgs)
				d2.printOuts()
				d2.Checks.checkInputAvroStringEqualsApacheAvroStringFromAvroSkeuoTransOutput
				d2.Checks.checkInputAvroSkeuoEqualsAvroSkeuoFromJsonSkeuoTransOutput
				//d2.checking()
				
				
				val d3a = DecoderCheck3a_AvroSkeuoToJsonCirce(impArgs)
				d3a.printOuts()
				d3a.Checks.checkInputAvroSkeuoShouldMatchAvroSkeuoStr
				d3a.Checks.checkInputJsonSkeuoShouldMatchJsonSkeuoFromAvroSkeuoTransAndStrAndDecoder
				d3a.Checks.checkJsonCirceFromAvroSkeuoEqualsJsonCirceFromJsonSkeuo
				//d3a.checking()
				
				val d3b = DecoderCheck3b_JsonSkeuoToJsonCirce(impArgs)
				d3b.printOuts()
				d3b.Checks.checkInputAvroSkeuoShouldMatchAvroSkeuoStr
				d3b.Checks.checkInputJsonSkeuoShouldMatchJsonSkeuoFromAvroSkeuoTransAndStrAndDecoder
				d3b.Checks.checkJsonCirceFromAvroSkeuoEqualsJsonCirceFromJsonSkeuo
				
				
				
				val d4a = DecoderCheck4a_JsonStringBegin_JsonDecoderVersusJsonTrans(impArgs)
				d4a.printOuts()
				d4a.Checks.checkInputJsonStringEqualsJsonCirceStrings
				d4a.Checks.checkJsonFromDecoderMatchesJsonFromTransOfAvroSkeuo
				
				
				val d4b = DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans(impArgs)
				d4b.printOuts()
				//d4b.checking()
				d4b.Checks.checkInputJsonStringEqualsJsonCirceStrings()
				d4b.Checks.checkAvroFromDecoderMatchesAvroFromTransOfJsonSkeuo()
				
				
				val d4c = DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans(impArgs)
				d4c.Checks.checkInputJsonCirceMatchesJsonCirceFromDecoder
				d4c.Checks.checkInputAvroSkeuoMatchesAvroSkeuoFromStrAndDecoderAndTransOfJsonSkeuo
				
				
				val d4d = DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans(impArgs)
				d4d.Checks.checkEqualityOfAllAvroSources()
				d4d.Checks.checkEqualityOfAllJsonCirceSources()
				d4d.Checks.checkEqualityOfJsonSkeuoFromAllAvroSources()
				
				
				info(s"\n-----------------------------------------------------------")
				
				val apacheAvro_fromGivenStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
				val apacheAvroStr_fromGivenStr: String = apacheAvro_fromGivenStr.toString(true).manicure
				val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromGivenStr)
				
				val jsonCirce_fromRawJsonStr: JsonCirce = unsafeParse(rawJsonStr)
				val jsonCirce_fromDecoderOfAvro: JsonCirce = libToJsonAltered(avroFixS)
				
				val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
				
				
				val skeuoAvro_fromDecoder: Result[Fix[AvroSchema_S]] = decodeJsonStringToCirceToAvroSkeuo(rawJsonStr)
				
				val skeuoAvro_fromTransOfJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
				
				
				info(s"\nCHECKER 6b: " +
					s"\nskeuo-json (trans output) -> json-circe -> skeuo-avro (decoder output) vs. skeuo avro (trans input) vs. skeuo-avro (trans output)" +
					s"\n|\t Reason: compare how skeuo-json (trans output) renders to skeuo-avro to check correctness of my Trans converter " +
					s"\n|\t (from avro-side) " +
					s"\n|\t (starting from: skeuo-json output)" +
					s"\n--- skeuo-json (trans output): $skeuoJson_trans_fromAvroADT" +
					s"\n--> json-circe: \n${libRender(skeuoJson_trans_fromAvroADT).manicure}" +
					s"\n--> skeuo-avro (decoder output): ${decodeJsonSkeuoToCirceToAvroSkeuo(skeuoJson_trans_fromAvroADT)}" +
					s"\n\t VERSUS. skeuo-avro (trans input): ${avroFixS}" +
					s"\n\t VERSUS. skeuo-avro (trans output): ${jsonToAvro_byAnaTransCoalg(jsonFixS)}"
					)
				
				
				
				
				
				info(s"\n-----------------------------------------------------------")
				info("\nDECODING SANITY CHECK:")
				info(s"\nCHECK 1" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-avro: $avroFixS" +
					s"\njson-circe: \n${libToJsonAltered(avroFixS).manicure}" +
					s"\nskeuo-json: ${decodeAvroSkeuoToCirceToJsonSkeuo(avroFixS)}")
				
				
				info(s"\nCHECK 2" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-avro: $avroFixS" +
					s"\njson-circe: \n${libToJsonAltered(avroFixS).manicure}" +
					s"\nskeuo-avro: ${decodeAvroSkeuoToCirceToAvroSkeuo(avroFixS)}")
				
				info(s"\nCHECK 3" +
					s"\nskeuo-json (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-json: $jsonFixS" +
					s"\njson-circe: \n${libRender(jsonFixS).manicure}" +
					s"\nskeuo-avro: ${decodeJsonSkeuoToCirceToAvroSkeuo(jsonFixS)}")
				
				info(s"\nCHECK 4" +
					s"\nskeuo-json (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-json: $jsonFixS" +
					s"\njson-circe: \n${libRender(jsonFixS).manicure}" +
					s"\nskeuo-json: ${decodeJsonSkeuoToCirceToJsonSkeuo(jsonFixS)}")
				
				
				
				
			}
		}
	}
	
	/*Map(array1IntAvro_S → "AvroSchema_S[AvroSchema_S[Int]]",
		array1IntAvro_Circe_S → "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		array1IntAvro_Fix_S → "AvroSchema_S[AvroSchema_S[JsonCirce]]")*/
	
	
	/*,
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
				|""".stripMargin*/
	
	
	
	info(s"PRINT OUT array string from skeuo schema avro to see avro str")
	info(s"${skeuoToApacheAvroSchema(nullAvro_Fix_S).toString(true).removeSpaceBeforeColon}")
	info(s"${skeuoToApacheAvroSchema(intAvro_Fix_S).toString(true).removeSpaceBeforeColon}")
	info(s"${skeuoToApacheAvroSchema(strAvro_Fix_S).toString(true).removeSpaceBeforeColon}")
	info(s"${skeuoToApacheAvroSchema(booleanAvro_Fix_S).toString(true).removeSpaceBeforeColon}")
	info(s"${skeuoToApacheAvroSchema(array1IntAvro_Fix_S).toString(true).removeSpaceBeforeColon}")
	
	testStructure(scenarioType = "array of int",
		rawAvroStr = array1IntAvro_R,
		rawJsonStr = array1IntJson_R,
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFixS = array1IntAvro_Fix_S,
		jsonFixS = array1IntJson_Fix_S
	)
	// ----
	
	
	/*"Given: A Skeuomorph avro schema" when {
		
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
