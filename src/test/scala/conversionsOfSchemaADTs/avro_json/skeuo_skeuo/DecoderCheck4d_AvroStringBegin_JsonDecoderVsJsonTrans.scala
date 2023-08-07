package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import io.circe.Decoder.Result
import io.circe.{Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
import org.scalatest.Assertion
import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import utilMain.UtilMain.implicits._


/**
 *
 */
case class DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	/*val apacheAvro_fromGivenStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val apacheAvroStr_fromGivenStr: String = apacheAvro_fromGivenStr.toString(true).manicure
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromGivenStr)
	
	val jsonCirce_fromRawJsonStr: JsonCirce = unsafeParse(rawJsonStr)
	val jsonCirce_fromDecoderOfAvro: JsonCirce = libToJsonAltered(avroFixS)
	
	
	//val skeuoJson_fromDecoderOfAvro: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromDecoderOfAvro)
	val skeuoJson_fromDecoderOfCirce: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	val skeuoJson_fromDecoderOfAvro: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoToCirceToJsonSkeuo(avroFixS)*/
	
	val step: Stepping = Stepping(rawAvroStr, rawJsonStr)
	val sa: AvroStringDecodeInfo = step.avroStep
	val sj: JsonStringDecodeInfo = step.jsonStep
	val sai: SkeuoDecodeInfo = step.avroStep.skInfo
	val sji: SkeuoDecodeInfo = step.jsonStep.skInfo
	
	
	val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	def printOuts(): Unit = {
		
		// RULE: if starting from avro string in the test, then use avrostep, else use jsonstep.
		
		info(s"\n-----------------------------------------------------------")
		info(s"\nCHECKER 4d: " +
		     s"\nraw-avro-str (expectation input) -> (skeuo-avro) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
		     s"\n|\t Reason: find out how avro-str translates to json-adt  + get common denominator (skeuo-json), from json-side." +
		     s"\n|\t (from json-side) " +
		     s"\n|\t (starting from: avro-str)" +
		     s"\n--- raw-avro-str (given): \n$rawAvroStr" +
		     s"\n--> (apache-avro): \n${sa.parsedApacheAvroStr}" +
		     s"\n--> (skeuo-avro): ${sa.skeuoAvro_fromApache}" +
		     s"\n--> json-circe (from avro-str): ${sa.interimCirce_fromAvro.manicure}" +
		     /*s"\n    json-circe (from json-str): ${sj.interimCirce_fromJson.manicure}" +*/
		     s"\n--> skeuo-json (avro-decoder output): ${sai.skeuoJson_fromRaw}" +
		     /*s"\n    skeuo-json (json-decoder output): ${sji.skeuoJson_fromRaw}" +*/
		     s"\n    skeuo-json (trans output): ${skeuoJson_fromTransOfGivenAvroSkeuo}")
	}
	
	
	def checking(): Unit = {
		import Checking._
		
		equalityOfStartingAvroSkeuosFromInputAvroString()
		equalityOfStartingAvroSkeuosFromInputJsonString()
		equalityOfCirceBySkeouAvroAndCirceBySkeouJsonFromStartingAvroString()
		equalityOfEndingJsonSkeuosFromInputAvroString()
		equalityOfEndingJsonSkeuosFromInputJsonString()
	}
	
	object Checking {
		
		def equalityOfStartingAvroSkeuosFromInputAvroString(): Assertion = {
			
			sa.parsedApacheAvroStr shouldEqual rawAvroStr
			
			forEvery(List(
				sai.skeuoAvro_fromRaw,
				sai.skeuoAvro_fromDecodingAvro,
				sai.skeuoAvro_fromDecodingJson,
			)) {
				sa ⇒ sa.right.get shouldEqual avroFixS
			}
		}
		
		def equalityOfStartingAvroSkeuosFromInputJsonString(): Assertion = {
			
			sa.parsedApacheAvroStr shouldEqual rawAvroStr
			
			forEvery(List(
				sji.skeuoAvro_fromRaw,
				sji.skeuoAvro_fromDecodingAvro,
				sji.skeuoAvro_fromDecodingJson
			)
			) {
				sa ⇒ sa.right.get shouldEqual avroFixS
			}
		}
		
		def equalityOfCirceBySkeouAvroAndCirceBySkeouJsonFromStartingAvroString(): Assertion = {
			forEvery(List(
				jsonCirceCheck,
				sai.jsonCirce_fromRawAvro,
				sai.jsonCirce_fromRawJson,
				sji.jsonCirce_fromRawAvro,
				sji.jsonCirce_fromRawJson
			)) {
				jc ⇒ jc.manicure shouldEqual rawJsonStr
			}
		}
		
		def equalityOfEndingJsonSkeuosFromInputAvroString(): Assertion = {
			forEvery(List(
				Right(skeuoJson_fromTransOfGivenAvroSkeuo),
				sai.skeuoJson_fromRaw,
				sai.skeuoJson_fromDecodingAvro,
				sai.skeuoJson_fromDecodingJson,
				sji.skeuoJson_fromRaw,
				sji.skeuoJson_fromDecodingAvro,
				sji.skeuoJson_fromDecodingJson,
			)) {
				sj ⇒ sj.right.get should equal(jsonFixS)
			}
		}
		
		def equalityOfEndingJsonSkeuosFromInputJsonString(): Assertion = {
			forEvery(List(
				Right(skeuoJson_fromTransOfGivenAvroSkeuo),
				sji.skeuoJson_fromRaw,
				sji.skeuoJson_fromDecodingAvro,
				sji.skeuoJson_fromDecodingJson,
			)
			) {
				sj ⇒ sj.right.get should equal(jsonFixS)
			}
		}
	}
	
	
}


/*
object DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans
}*/
