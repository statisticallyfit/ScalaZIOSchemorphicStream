package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import io.circe.Decoder.Result
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import org.scalatest.Assertion
import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import utilMain.UtilMain.implicits._


/**
 *
 */
class DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	/*val jsonCirceFromStr: JsonCirce = unsafeParse(rawJsonStr)
	val skeuoAvro_fromDecoder: Result[Fix[AvroSchema_S]] = decodeJsonStringToCirceToAvroSkeuo(rawJsonStr)
	*/
	
	val obj = Stepping(rawAvroStr, rawJsonStr).stepAnyStringToAvroSkeuo()
	
	
	val skeuoAvro_fromTransOfJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	def printOuts(): Unit = {
		
		info(s"\n-----------------------------------------------------------")
		
		info(s"\nCHECKER 4b: " +
			s"\nraw-json-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans input) vs. skeuo-avro (output)" +
			s"\n|\t Reason: find out how json-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side" +
			s"\n|\t (from avro side) " +
			s"\n|\t (starting from: json-str)")
		
		info(s"--- raw-json-str (given): $rawJsonStr" +
			s"\n--> apache-avro: \n${obj.parsedApacheAvroStr}" +
			s"\n--> skeuo-avro (apache -> skeuo output): ${obj.skeuoAvro_fromDecodingAvroStr}" +
			s"\n    skeuo-avro (from json str): ${obj.skeuoAvro_fromDecodingJsonStr}" +
			s"\n--> json-circe (from skeuo-avro): ${obj.jsonCirce_fromInputAvroSkeuo.manicure}" +
			s"\n    json-circe (from json-str): ${obj.jsonCirce_fromInputJsonStr.manicure}" +
			s"\n--> skeuo-avro (avro-decoder output): ${obj.skeuoAvro_fromDecodingAvroSkeuo}" +
			s"\n    skeuo-avro (circe-decoder output): ${obj.skeuoAvro_fromDecodingCirce}" +
			s"\n    skeuo-avro (trans output): ${skeuoAvro_fromTransOfJsonSkeuo}")
	}
	
	def checking(): Unit = {
		
		import Checks._
		
		checkInputJsonStringEqualsJsonCirceStrings()
		checkEqualityOfAllAvroSources()
	}
	
	object Checks {
		// Check all jsons are equal (inputs and outputs)
		def checkInputJsonStringEqualsJsonCirceStrings(): Assertion = {
			
			forEvery(List(
				jsonCirceCheck.manicure,
				obj.jsonCirce_fromInputJsonStr.manicure,
				obj.jsonCirce_fromInputAvroSkeuo.manicure
			)) {
				js ⇒ js shouldEqual rawJsonStr
			}
			
		}
		
		def checkEqualityOfAllAvroSources(): Assertion = {
			
			forEvery(List(
				obj.skeuoAvro_fromDecodingAvroStr,
				obj.skeuoAvro_fromDecodingJsonStr,
				obj.skeuoAvro_fromDecodingCirce,
				obj.skeuoAvro_fromDecodingAvroSkeuo,
				skeuoAvro_fromTransOfJsonSkeuo
			)) {
				sa ⇒ sa should equal (avroFixS)
			}
			
		}
	}
	
	
}


/*
object DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans
}*/
