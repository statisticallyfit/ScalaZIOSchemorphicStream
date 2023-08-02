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


import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import org.scalatest.Assertion

import utilMain.UtilMain.implicits._


/**
 *
 */
case class DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	val apacheAvro_fromGivenStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val apacheAvroStr_fromGivenStr: String = apacheAvro_fromGivenStr.toString(true).manicure
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromGivenStr)
	val skeuoAvro_fromDecoder: Result[Fix[AvroSchema_S]] = decodeJsonStringToCirceToAvroSkeuo(rawJsonStr)
	
	val jsonCirce_fromJsonStr: JsonCirce = unsafeParse(rawJsonStr)
	val jsonCirce_fromDecoderAvroStr: JsonCirce = libToJsonAltered(skeuoAvro_fromStr)
	
	
	val skeuoAvro_fromTransOfGivenJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		
		info(s"\nCHECKER 4c: " +
			s"\nraw-avro-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans output) " +
			s"\n|\t Reason: find out how avro-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side." +
			s"\n|\t (from avro side) " +
			s"\n|\t (starting from: avro-str)")
		
		info(s"\n--- raw-avro-str (given): $rawAvroStr" +
			s"\n--> (apache-avro -> skeuo-avro (via apacheToSkeuo func)) -> json-circe (via libToJsonAltered): ${jsonCirce_fromDecoderAvroStr.manicure}" +
			s"\n--> skeuo-avro (decoder output): ${skeuoAvro_fromDecoder}" +
			s"\n\t VERSUS. skeuo-avro (trans output): ${skeuoAvro_fromTransOfGivenJsonSkeuo}")
	}
	
	def checking(): Unit = {
		import Checks._
		
		checkInputJsonCirceMatchesJsonCirceFromDecoder
		checkInputAvroSkeuoMatchesAvroSkeuoFromStrAndDecoderAndTransOfJsonSkeuo
	}
	
	object Checks {
		def checkInputJsonCirceMatchesJsonCirceFromDecoder: Assertion = {
			
			forEvery(List(
				jsonCirce_fromJsonStr,
				jsonCirce_fromDecoderAvroStr
			)) {
				jc ⇒ jc should equal (jsonCirceCheck)
			}
		}
		
		def checkInputAvroSkeuoMatchesAvroSkeuoFromStrAndDecoderAndTransOfJsonSkeuo: Assertion = {
			forEvery(List(
				skeuoAvro_fromStr,
				skeuoAvro_fromDecoder,
				skeuoAvro_fromTransOfGivenJsonSkeuo
			)) {
				sa ⇒ sa should equal(avroFixS)
			}
		}
	}
}


object DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans
}