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
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.DecoderCheck_CanonicalWay_TransAvroStrToJsonStr
import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._
import utilMain.UtilMain
import utilMain.UtilMain.implicits._


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
			
			describe("Canonical check: apache-avro-str (input) vs. json-circe-str (output)") {
				
				import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.ImplicitArgs
				
				implicit val i = new ImplicitArgs(rawAvroStr, rawJsonStr, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
				
				// TODO find out how to call just the class name without args?
				
				DecoderCheck_CanonicalWay_TransAvroStrToJsonStr(i).printOutsForUnderstanding
				DecoderCheck_CanonicalWay_TransAvroStrToJsonStr(i).checking
				
				/*val o1 = DecoderCheck_CanonicalWay_TransAvroStrToJsonStr(rawAvroStr, rawJsonStr, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
				
				o1.printOutsForUnderstanding
				o1.checking*/
				
				
				
				info(s"\n-----------------------------------------------------------")
				info(s"\n\nMY DECODING WAY: ")
				
				
				
				val skeuoJson_fromStr: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
				val skeuoJson_trans_fromADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
				
				info(s"\nCHECKER 1a: " +
					s"\nskeuo-avro --> skeuo-json | Reason: Trans converter, the algebra way" +
					s"\n--- skeuo-avro (given): $avroFixS" +
					s"\n--> skeuo-json (from adt): ${skeuoJson_trans_fromADT}" +
					s"\n\t VERSUS. skeuo-json (from str): ${skeuoJson_fromStr}" +
					s"\n\t VERSUS. skeuo-json (given): $jsonFixS ")
				
				//skeuoJson_trans_fromADT shouldEqual skeuoJson_trans_fromStr
				//skeuoJson_trans_fromADT shouldEqual jsonFixS
				// NOTE: already tested above so no use including it again
				/*forEvery(List(
					skeuoJson_trans_fromStr,
					skeuoJson_trans_fromADT
				)) {
					js ⇒ js should equal(jsonFixS)
				}*/
				
				
				
				info(s"\n-----------------------------------------------------------")
				
				// convert: json-circe -> json-skeuo -> avro-skeuo
				val skeuoAvro_trans_fromStr: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(
					funcCirceToJsonSkeuo(jsonCirceCheck).right.get
				)
				val skeuoAvro_trans_fromADT: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
				info(s"\nCHECKER 1b: " +
					s"\nskeuo-json -> skeuo-avro | Reason: Trans converter, the coalgebra way" +
					s"\n--- skeuo-json (given): $jsonFixS" +
					s"\n--> skeuo-avro (from adt): $skeuoAvro_trans_fromADT" +
					s"\n\t VERSUS. skeuo-avro (from str): $skeuoAvro_fromStr" +
					s"\n\t VERSUS. skeuo-avro (given): $avroFixS")
				
				//skeuoAvro_trans_fromADT shouldEqual skeuoAvro_trans_fromStr
				forEvery(List(
					skeuoAvro_trans_fromADT,
					skeuoAvro_trans_fromStr
				)) {
					as ⇒ as should equal(avroFixS)
				}
				
				
				
				info(s"\n-----------------------------------------------------------")
				
				val apacheAvro_trans: AvroSchema_A = skeuoToApacheAvroSchema(skeuoAvro_trans_fromADT)
				val apacheAvroStr_trans: String = apacheAvro_trans.toString(true).removeSpaceBeforeColon
				
				info(s"\nCHECKER 2: " +
					s"\nskeuo-json --> skeuo-avro --> avro-str (trans output) vs. avro-str (input) | Reason: get common denominator (avro-str), from avro-side")
				info(s"\n--- skeuo-json (given): ${jsonFixS}")
				info(s"\n--> skeuo-avro (trans output): ${skeuoAvro_trans_fromADT}")
				info(s"\n--> apache-avro-str (trans output): \n$apacheAvroStr_trans")
				info(s"\n\t VERSUS: apache-avro-str (given): \n$rawAvroStr")
				
				skeuoAvro_trans_fromADT shouldEqual skeuoAvro_trans_fromStr
				apacheAvroStr_trans shouldEqual rawAvroStr
				
				
				info(s"\n-----------------------------------------------------------")
				
				val skeuoJson_trans_fromAvroADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
				val skeuoJson_trans_fromStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromStr)
				val circeJson_fromAvro: JsonCirce = libToJsonAltered(avroFixS)
				val circeJson_fromJson: JsonCirce = libRender(skeuoJson_trans_fromStr)
				
				info(s"\nCHECKER 3a: " +
					s"\nskeuo-avro --> json-circe | Reason: get common denominator (circe), from json side" +
					s"\n--- skeuo-avro (given): $avroFixS" +
					s"\n--> json-circe: \n${circeJson_fromAvro.manicure}")
				
				info(s"\nCHECKER 3b: " +
					s"\nskeuo-json --> json-circe | Reason: get common denominator (circe), from json side" +
					s"\n--- skeuo-json (trans output): ${skeuoJson_trans_fromStr}" +
					s"\n--> json-circe (decoder output): \n${circeJson_fromJson.manicure}")
				
				circeJson_fromAvro shouldEqual circeJson_fromJson
				
				// TODO check all equal
				skeuoJson_trans_fromADT
				skeuoJson_trans_fromStr
				skeuoJson_fromStr
				skeuoJson_trans_fromAvroADT
				
				
				info(s"\n-----------------------------------------------------------")
				info(s"\nCHECKER 5a: " +
					s"\nraw-json-str (expectation input) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output) " +
					s"\n|\t Reason: find out how json-str translates to json-adt to check correctness of my Trans converter " +
					s"\n|\t (from json side) " +
					s"\n|\t (starting from: json-str)" +
					s"\n--- raw-json-str (given): ${rawJsonStr}" +
					s"\n--> json-circe: \n${unsafeParse(rawJsonStr).manicure}" +
					s"\n--> skeuo-json (decoder output): ${strToCirceToSkeuoJson(rawJsonStr)}" +
					s"\n\t VERSUS. skeuo-json (trans output): ${skeuoJson_trans_fromStr}")
				
				
				info(s"\n-----------------------------------------------------------")
				info(s"\nCHECKER 5b: " +
					s"\nraw-json-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans input) vs. skeuo-avro (output)" +
					s"\n|\t Reason: find out how json-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side" +
					s"\n|\t (from avro side) " +
					s"\n|\t (starting from: json-str)" +
					s"\n--- raw-json-str (given): ${rawJsonStr}" +
					s"\n--> json-circe: \n${unsafeParse(rawJsonStr).manicure}" +
					s"\n--> skeuo-avro (decoder output): ${strToCirceToSkeuoAvro(rawJsonStr)}" +
					s"\n\t VERSUS. skeuo-avro (trans output): ${jsonToAvro_byAnaTransCoalg(jsonFixS)}")
				
				info(s"\n-----------------------------------------------------------")
				
				info(s"\nCHECKER 5c: " +
					s"\nraw-avro-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans output) " +
					s"\n|\t Reason: find out how avro-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side." +
					s"\n|\t (from avro side) " +
					s"\n|\t (starting from: avro-str)")
				val jsonCirce_2: JsonCirce = libToJsonAltered(skeuoAvro_fromStr)
				info(s"\n--- raw-avro-str (given): $rawAvroStr" +
					s"\n--> (apache-avro -> skeuo-avro (via apacheToSkeuo func)) -> json-circe (via libToJsonAltered): ${jsonCirce_2.manicure}" +
					s"\n--> skeuo-avro (decoder output): ${funcCirceToAvroSkeuo(jsonCirce_2)}" +
					s"\n\t VERSUS. skeuo-avro (trans output): ${jsonToAvro_byAnaTransCoalg(jsonFixS)}")
				
				
				info(s"\n-----------------------------------------------------------")
				info(s"\nCHECKER 5d: " +
					s"\nraw-avro-str (expectation input) -> (skeuo-avro) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
					s"\n|\t Reason: compare skeuo-json (trans output) vs. skeuo-json (decoder output) to check correctness of my Trans converter " +
					s"\n|\t (from json-side) " +
					s"\n|\t (starting from: avro-str)" +
					s"\n--- raw-avro-str (given): \n$rawAvroStr" +
					s"\n--> (apache-avro -> skeuo-avro (via apacheToSkeuo func)) -> json-circe (via libToJsonAltered): \n${jsonCirce_2.manicure}" +
					s"\n--> skeuo-json (decoder output): ${funcCirceToJsonSkeuo(jsonCirce_2)}" +
					s"\n\t VERSUS. skeuo-json (trans output): ${avroToJson_byCataTransAlg(avroFixS)}")
				
				info(s"\n-----------------------------------------------------------")
				info(s"\nCHECKER 6a: " +
					s"\nskeuo-avro -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
					s"\n|\t Reason: compare skeuo-json (trans output) vs. skeuo-json (decoder output) to check correctness of my Trans converter " +
					s"\n|\t (from json-side) " +
					s"\n|\t (starting from: skeuo-avro-adt (easier than string-start))" +
					s"\n--- skeuo-avro (given): $avroFixS" +
					s"\n--> json-circe: \n${libToJsonAltered(avroFixS).manicure}" +
					s"\n--> skeuo-json (decoder output): ${checker_AvroSkeuo_toJsonCirce_toJsonSkeuo(avroFixS)}" +
					s"\n\t VERSUS. skeuo-json (trans output): ${avroToJson_byCataTransAlg(avroFixS)}")
				
				
				info(s"\n-----------------------------------------------------------")
				
				val skeuoJson_trans_fromAvroADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
				info(s"\nCHECKER 6b: " +
					s"\nskeuo-json (trans output) -> json-circe -> skeuo-avro (decoder output) vs. skeuo avro (trans input) vs. skeuo-avro (trans output)" +
					s"\n|\t Reason: compare how skeuo-json (trans output) renders to skeuo-avro to check correctness of my Trans converter " +
					s"\n|\t (from avro-side) " +
					s"\n|\t (starting from: skeuo-json output)" +
					s"\n--- skeuo-json (trans output): $skeuoJson_trans_fromAvroADT" +
					s"\n--> json-circe: \n${libRender(skeuoJson_trans_fromAvroADT).manicure}" +
					s"\n--> skeuo-avro (decoder output): ${checker_JsonSkeuo_toJsonCirce_toAvroSkeuo(skeuoJson_trans_fromAvroADT)}" +
					s"\n\t VERSUS. skeuo-avro (trans input): ${avroFixS}" +
					s"\n\t VERSUS. skeuo-avro (trans output): ${jsonToAvro_byAnaTransCoalg(jsonFixS)}"
					)
				
				
				
				
				
				info(s"\n-----------------------------------------------------------")
				info("\nDECODING SANITY CHECK:")
				info(s"\nCHECK 1" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-avro: $avroFixS" +
					s"\njson-circe: \n${libToJsonAltered(avroFixS).manicure}" +
					s"\nskeuo-json: ${checker_AvroSkeuo_toJsonCirce_toJsonSkeuo(avroFixS)}")
				
				
				info(s"\nCHECK 2" +
					s"\nskeuo-avro (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-avro: $avroFixS" +
					s"\njson-circe: \n${libToJsonAltered(avroFixS).manicure}" +
					s"\nskeuo-avro: ${checker_AvroSkeuo_toJsonCirce_toAvroSkeuo(avroFixS)}")
				
				info(s"\nCHECK 3" +
					s"\nskeuo-json (fix) --> json-circe --> skeuo-avro (fix)" +
					s"\nskeuo-json: $jsonFixS" +
					s"\njson-circe: \n${libRender(jsonFixS).manicure}" +
					s"\nskeuo-avro: ${checker_JsonSkeuo_toJsonCirce_toAvroSkeuo(jsonFixS)}")
				
				info(s"\nCHECK 4" +
					s"\nskeuo-json (fix) --> json-circe --> skeuo-json (fix)" +
					s"\nskeuo-json: $jsonFixS" +
					s"\njson-circe: \n${libRender(jsonFixS).manicure}" +
					s"\nskeuo-json: ${checker_JsonSkeuo_toJsonCirce_toJsonSkeuo(jsonFixS)}")
				
				
				
				
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
