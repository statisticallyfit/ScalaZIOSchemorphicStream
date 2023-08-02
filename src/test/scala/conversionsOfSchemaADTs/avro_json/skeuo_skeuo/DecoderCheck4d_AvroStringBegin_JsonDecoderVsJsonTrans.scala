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
	
	
	val apacheAvro_fromGivenStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val apacheAvroStr_fromGivenStr: String = apacheAvro_fromGivenStr.toString(true).manicure
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromGivenStr)
	
	val jsonCirce_fromRawJsonStr: JsonCirce = unsafeParse(rawJsonStr)
	val jsonCirce_fromDecoderOfAvro: JsonCirce = libToJsonAltered(avroFixS)
	
	val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	//val skeuoJson_fromDecoderOfAvro: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromDecoderOfAvro)
	val skeuoJson_fromDecoderOfCirce: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	val skeuoJson_fromDecoderOfAvro: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoCirceToJsonSkeuo(avroFixS)
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		info(s"\nCHECKER 4d: " +
			s"\nraw-avro-str (expectation input) -> (skeuo-avro) -> json-circe -> skeuo-json (decoder output) vs. skeuo-json (trans output)" +
			s"\n|\t Reason: find out how avro-str translates to json-adt  + get common denominator (skeuo-json), from json-side." +
			s"\n|\t (from json-side) " +
			s"\n|\t (starting from: avro-str)" +
			s"\n--- raw-avro-str (given): \n$rawAvroStr" +
			s"\n--> (apache-avro -> skeuo-avro): ${skeuoAvro_fromStr} " +
			s"\n--> json-circe (decoder output): \n${jsonCirce_fromDecoderOfAvro.manicure}" +
			s"\n--> skeuo-json (circe-decoder output): ${skeuoJson_fromDecoderOfCirce}" +
			s"\n--> skeuo-json (avro-decoder output): ${skeuoJson_fromDecoderOfAvro}" +
			s"\n\t VERSUS. skeuo-json (trans output): ${skeuoJson_fromTransOfGivenAvroSkeuo}")
	}
	
	
	def checking(): Unit = {
		import Checks._
		
		checkEqualityOfAllAvroSources()
		checkEqualityOfAllJsonCirceSources()
		checkEqualityOfJsonSkeuoFromAllAvroSources()
	}
	
	object Checks {
		
		def checkEqualityOfAllAvroSources(): Assertion = {
			apacheAvroStr_fromGivenStr shouldEqual rawAvroStr
			
			skeuoAvro_fromStr shouldEqual avroFixS
		}
		
		def checkEqualityOfAllJsonCirceSources(): Assertion = {
			forEvery(List(
				jsonCirce_fromRawJsonStr,
				jsonCirce_fromDecoderOfAvro
			)) {
				jc ⇒ jc should equal(jsonCirceCheck)
			}
		}
		
		def checkEqualityOfJsonSkeuoFromAllAvroSources(): Assertion = {
			forEvery(List(
				skeuoJson_fromTransOfGivenAvroSkeuo,
				skeuoJson_fromDecoderOfAvro,
				skeuoJson_fromDecoderOfCirce
			)) {
				sj ⇒ sj should equal(jsonFixS)
			}
		}
	}
	
	
}


object DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4d_AvroStringBegin_JsonDecoderVsJsonTrans
}