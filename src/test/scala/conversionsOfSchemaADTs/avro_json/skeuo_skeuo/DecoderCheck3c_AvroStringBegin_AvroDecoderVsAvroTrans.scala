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
case class DecoderCheck3c_AvroStringBegin_AvroDecoderVsAvroTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	val step: Stepping = new Stepping(Some(rawAvroStr), Some(rawJsonStr))
	val sa: AvroStringDecodeInfo = step.avroInfoOpt.get
	val sj: JsonStringDecodeInfo = step.jsonInfoOpt.get
	val sai: SkeuoDecodeInfo = sa.skInfo
	val sji: SkeuoDecodeInfo = sj.skInfo
	
	
	val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val skeuoAvro_fromTransOfGivenJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	def showResults(): String = {
		
		// RULE: if starting from avro string in the test, then use avrostep, else use jsonstep.
		
		var infoVar: String = s"\n-----------------------------------------------------------"
		
		infoVar += (s"\nCHECKER 3c: " +
			
		     s"\n\nraw-avro-str (input) -> (skeuo-avro) -> json-circe -> skeuo-avro (decoder output) vs. skeuo-avro (trans output)" +
		     s"\n|\t Reason: find out how avro-str translates to avro-adt  + get common denominator (skeuo-avro), from avro-side." +
		     s"\n|\t (from avro-side) " +
		     s"\n|\t (starting from: avro-str)" +
		     s"\n" +
		     s"\n--- raw-avro-str (given): \n$rawAvroStr" +
		     s"\n--> (apache-avro): \n${sa.parsedApacheAvroStr}" +
		     s"\n--> (skeuo-avro):                     \t${sa.skeuoAvro_fromApache}" +
		     s"\n--> json-circe (from avro-str): \n${sa.interimCirce_fromAvroSKeuo.manicure}" +
		     s"\n    json-circe (from json-str): \n${sj.interimCirce_fromJsonStr.manicure}" +
		     s"\n--> skeuo-avro (avro-decoder output): \t${sai.skeuoAvro_fromRaw}" +
		     s"\n    skeuo-avro (json-decoder output): \t${sji.skeuoAvro_fromRaw}" +
		     s"\n    skeuo-avro (trans output):        \t${skeuoAvro_fromTransOfGivenJsonSkeuo}")
		
		infoVar
	}
	
	
	override def checking(): Unit = {
		import Checking._
		
		// Spirit of the test
		equalityOfAvroSkeuoFromDecoderAndTrans()
		
		
	}
	
	object Checking {
		
		// Spirit of the test
		
		def equalityOfAvroSkeuoFromDecoderAndTrans(): Assertion = {
			
			forEvery(List(
				
				DecodingStr.decodeAvroStringToCirceToAvroSkeuo(rawAvroStr, Some(rawJsonStr)),
				
				sai.skeuoAvro_fromDecodeAvroSkeuo,
				sai.skeuoAvro_fromRaw,
				
				DecodingStr.decodeJsonStringToCirceToAvroSkeuo(rawJsonStr, Some(rawAvroStr)),
				
				sji.skeuoAvro_fromDecodeAvroSkeuo,
				sji.skeuoAvro_fromRaw,
				
				Right(skeuoAvro_fromTransOfGivenJsonSkeuo)
			)
			) {
				ska ⇒ ska.right.get should equal(avroFixS)
			}
		}
		
		
	}
}

