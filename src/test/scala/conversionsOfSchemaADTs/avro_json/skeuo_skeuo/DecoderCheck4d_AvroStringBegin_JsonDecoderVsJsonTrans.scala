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
class DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans(implicit imp: ImplicitArgs)
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
	val sj = step.jsonStep
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
		     s"\n--> (interim-apache-avro): \n${sa.parsedApacheAvroStr}" +
		     s"\n--> (interim-skeuo-avro): ${sa.skeuoAvro_fromApache}" +
		     s"\n--> json-circe (from skeuo-avro): ${sai.jsonCirce_fromRawAvro.manicure}" +
		     s"\n    json-circe (from json-str): ${sai.jsonCirce_fromRawJson.manicure}" +
		     s"\n--> skeuo-json (avro-decoder output): ${sai.skeuoJson_fromDecodingAvro}" +
		     s"\n    skeuo-json (circe-decoder output): ${sai.skeuoJson_fromRaw}" +
		     s"\n    skeuo-json (trans output): ${skeuoJson_fromTransOfGivenAvroSkeuo}")
	}
	
	
	def checking(): Unit = {
		import Checking._
		
		equalityOfAllAvroSources()
		equalityOfAllCirces()
		equalityOfJsonSkeuoFromAllAvroSources()
	}
	
	object Checking {
		
		def equalityOfAllAvroSources(): Assertion = {
			sa.parsedApacheAvroStr shouldEqual rawAvroStr
			
			forEvery(List(
				sai.skeuoAvro_fromRaw,
				sji.skeuoAvro_fromRaw,
				sai.skeuoAvro_fromDecodingAvro,
				sji.skeuoAvro_fromDecodingAvro,
				sai.skeuoAvro_fromDecodingJson,
				sji.skeuoAvro_fromDecodingJson
			)) {
				sa ⇒ sa.right.get shouldEqual avroFixS
			}
		}
		
		def equalityOfAllCirces(): Assertion = {
			forEvery(List(
				jsonCirceCheck,
				sai.jsonCirce_fromRawAvro,
				sai.jsonCirce_fromRawJson,
				sji.jsonCirce_fromRawAvro,
				sji.jsonCirce_fromRawJson
			)) {
				c ⇒ c.manicure shouldEqual rawJsonStr
			}
		}
		
		def equalityOfJsonSkeuoFromAllAvroSources(): Assertion = {
			forEvery(List(
				skeuoJson_fromTransOfGivenAvroSkeuo,
				step.skeuoJson_fromDecodingAvroSkeuo.right.get,
				step.skeuoJson_fromDecodingCirce.right.get
			)) {
				sj ⇒ sj should equal(jsonFixS)
			}
		}
	}
	
	
}


/*
object DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans
}*/
