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
class DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	/*val result: (
		AvroSchema_A,
			String,
			Fix[AvroSchema_S],
			Result[Fix[AvroSchema_S]],
			JsonCirce, JsonCirce,
			Result[Fix[AvroSchema_S]],
			Result[Fix[AvroSchema_S]]
		) = StepByStep.stepAvroStringToAvroSkeuo(rawAvroStr, rawJsonStr)
	
	val (parsedApacheAvro, parsedApacheAvroStr,
		skeuoAvro_fromDecodingAvroStr, skeuoAvro_fromDecodingJsonStr,
		jsonCirce_fromAvroSkeuo, jsonCirce_fromJsonStr,
		skeuoAvro_fromDecodingAvroSkeuo, skeuoAvro_fromDecodingCirce) = result*/
	
	
	val obj: Stepping.AvroOutputInfo = Stepping(rawAvroStr, rawJsonStr).stepAnyStringToAvroSkeuo()
	
	val skeuoAvro_fromTransOfGivenJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		
		info(s"\nCHECKER 4c: " +
			s"\nraw-avro-str (expectation input) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans output) " +
			s"\n|\t Reason: find out how avro-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side." +
			s"\n|\t (from avro side) " +
			s"\n|\t (starting from: avro-str)")
		
		info(s"\n--- raw-avro-str (given): $rawAvroStr" +
			s"\n--> apache-avro: \n${obj.parsedApacheAvroStr}" +
			s"\n--> skeuo-avro (apache -> skeuo output): ${obj.skeuoAvro_fromDecodingAvroStr}" +
			s"\n    skeuo-avro (from json str): ${obj.skeuoAvro_fromDecodingJsonStr}" +
			s"\n--> json-circe (from skeuo-avro): ${obj.jsonCirce_fromInputAvroSkeuo.manicure}" +
			s"\n    json-circe (from json-str): ${obj.jsonCirce_fromInputJsonStr.manicure}" +
			s"\n--> skeuo-avro (avro-decoder output): ${obj.skeuoAvro_fromDecodingAvroSkeuo}" +
			s"\n    skeuo-avro (circe-decoder output): ${obj.skeuoAvro_fromDecodingCirce}" +
			s"\n    skeuo-avro (trans output): ${skeuoAvro_fromTransOfGivenJsonSkeuo}")
	}
	
	def checking(): Unit = {
		import Checks._
		
		checkMatchingJsonCirce()
		checkEqualityOfAllAvroSources()
	}
	
	object Checks {
		def checkMatchingJsonCirce(): Assertion = {
			
			forEvery(List(
				obj.jsonCirce_fromInputJsonStr.manicure,
				obj.jsonCirce_fromInputAvroSkeuo.manicure,
				rawJsonStr
			)) {
				jc ⇒ jc should equal (jsonCirceCheck.manicure)
			}
		}
		
		def checkEqualityOfAllAvroSources(): Assertion = {
			forEvery(List(
				obj.skeuoAvro_fromDecodingAvroStr,
				obj.skeuoAvro_fromDecodingJsonStr,
				obj.skeuoAvro_fromDecodingAvroSkeuo,
				obj.skeuoAvro_fromDecodingCirce,
				
				skeuoAvro_fromTransOfGivenJsonSkeuo
			)) {
				sa ⇒ sa should equal(avroFixS)
			}
		}
	}
}


/*
object DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4c_AvroStringBegin_AvroDecoderVsAvroTrans
}*/
