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
case class DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	val jsonCirceFromStr: JsonCirce = unsafeParse(rawJsonStr)
	val skeuoAvro_fromDecoder: Result[Fix[AvroSchema_S]] = decodeJsonStringToCirceToAvroSkeuo(rawJsonStr)
	val skeuoAvro_fromTransOfJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	def printOuts(): Unit = {
		
		info(s"\n-----------------------------------------------------------")
		info(s"\nCHECKER 4b: " +
			s"\nraw-json-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans input) vs. skeuo-avro (output)" +
			s"\n|\t Reason: find out how json-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side" +
			s"\n|\t (from avro side) " +
			s"\n|\t (starting from: json-str)" +
			s"\n--- raw-json-str (given): ${rawJsonStr}" +
			s"\n--> json-circe: \n${jsonCirceFromStr.manicure}" +
			s"\n--> skeuo-avro (decoder output): ${skeuoAvro_fromDecoder}" +
			s"\n\t VERSUS. skeuo-avro (trans output): ${skeuoAvro_fromTransOfJsonSkeuo}")
	}
	
	def checking(): Unit = {
		
		import Checks._
		
		checkInputJsonStringEqualsJsonCirceStrings()
		checkAvroFromDecoderMatchesAvroFromTransOfJsonSkeuo()
	}
	
	object Checks {
		// Check all jsons are equal (inputs and outputs)
		def checkInputJsonStringEqualsJsonCirceStrings(): Assertion = {
			
			forEvery(List(
				jsonCirceCheck.manicure,
				jsonCirceFromStr.manicure
			)) {
				js ⇒ js shouldEqual rawJsonStr
			}
			
		}
		
		def checkAvroFromDecoderMatchesAvroFromTransOfJsonSkeuo(): Assertion = {
			skeuoAvro_fromDecoder.right.get shouldEqual skeuoAvro_fromTransOfJsonSkeuo
		}
	}
	
	
}


object DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4b_JsonStringBegin_AvroDecoderVsAvroTrans
}