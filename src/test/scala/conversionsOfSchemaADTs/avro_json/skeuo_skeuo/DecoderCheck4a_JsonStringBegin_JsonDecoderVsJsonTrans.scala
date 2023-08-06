package conversionsOfSchemaADTs.avro_json.skeuo_skeuo



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
import io.circe.Decoder.Result
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import org.scalatest.Assertion
import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import utilMain.UtilMain.implicits._

/**
 *
 */
class DecoderCheck4a_JsonStringBegin_JsonDecoderVsJsonTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	
	/*val skeuoJson_trans_fromADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val skeuoJson_trans_fromStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromStr)*/
	/*val skeuoJson_fromStr: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get*/
	
	val obj: Info.JsonOutputInfo = Info(rawAvroStr, rawJsonStr).stepAnyStringToJsonSkeuo()
	
	val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	
	def printOuts(): Unit = {
		
		info(s"\n-----------------------------------------------------------")
		
		info(s"\nCHECKER 4a: " +
			s"\nraw-json-str (expectation input) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output) " +
			s"\n|\t Reason: find out how json-str translates to json-adt + get common denominator (skeuo-json), from json-side " +
			s"\n|\t (from json side) " +
			s"\n|\t (starting from: json-str)" +
			s"\n--- raw-json-str (given): ${rawJsonStr}" +
			s"\n--> json-circe: \n${jsonCirceFromStr.manicure}" +
			s"\n--> skeuo-json (decoder output): ${skeuoJson_fromDecoder}" +
			s"\n\t VERSUS. skeuo-json (trans output): ${skeuoJson_fromTransOfGivenAvroSkeuo}")
	}
	
	
	def checking(): Unit = {
		import Checks._
		// Check all jsons are equal (inputs and outputs)
		checkInputJsonStringEqualsJsonCirceStrings
		
		// Check all trans-output jsons are equal
		checkJsonFromDecoderMatchesJsonFromTransOfAvroSkeuo
		
	}
	
	object Checks {
		def checkInputJsonStringEqualsJsonCirceStrings: Assertion = {
			forEvery(List(
				jsonCirceCheck.manicure,
				jsonCirceFromStr.manicure
			)) {
				js ⇒ js shouldEqual rawJsonStr
			}
		}
		
		def checkJsonFromDecoderMatchesJsonFromTransOfAvroSkeuo: Assertion = {
			
			forEvery(List(
				skeuoJson_fromDecoder.right.get,
				skeuoJson_fromTransOfGivenAvroSkeuo
			)) {
				sj ⇒ sj shouldEqual jsonFixS
			}
		}
	}
}



/*
object DecoderCheck4a_JsonStringBegin_JsonDecoderVsJsonTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4a_JsonStringBegin_JsonDecoderVsJsonTrans
}*/
