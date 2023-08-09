package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import org.apache.avro.{Schema ⇒ AvroSchema_A}

import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}


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

trait CommonDecoderChecks extends DecoderChecks {
	
	
	def equalityOfInitialAvroSkeuosFromAvroOrJsonInputString(): Assertion
	
	def equalityOfInitialJsonSkeuosFromAvroOrJsonInputString(): Assertion
	
	def equalityOfCirceFromAvroOrJsonInputString(): Assertion
	def equalityOfCirceBySkeuoAvroAndCirceBySkeuoJsonFromAvroOrJsonInputString(): Assertion
	
	def equalityOfLastAvroSkeuosFromAvroOrJsonInputString(): Assertion
	
	def equalityOfLastJsonSkeuosFromAvroOrJsonInputString(): Assertion
	
	def equalityOfSkeuosFromInputAndCirceDecodingAndTrans(): Assertion
	
	def checking(): Unit = {
		equalityOfInitialAvroSkeuosFromAvroOrJsonInputString()
		equalityOfInitialJsonSkeuosFromAvroOrJsonInputString()
		
		equalityOfCirceFromAvroOrJsonInputString()
		equalityOfCirceBySkeuoAvroAndCirceBySkeuoJsonFromAvroOrJsonInputString()
		
		equalityOfLastAvroSkeuosFromAvroOrJsonInputString()
		equalityOfLastJsonSkeuosFromAvroOrJsonInputString()
		
		equalityOfSkeuosFromInputAndCirceDecodingAndTrans()
	}
}


case class CommonDecoderCheckSpecs(implicit imp: ImplicitArgs)
	extends AnyFunSpec with CommonDecoderChecks with Matchers {
	
	import imp._
	
	
	val step: Stepping = Stepping(rawAvroStr, rawJsonStr)
	val sa: AvroStringDecodeInfo = step.avroStep
	val sj: JsonStringDecodeInfo = step.jsonStep
	val sai: SkeuoDecodeInfo = step.avroStep.skInfo
	val sji: SkeuoDecodeInfo = step.jsonStep.skInfo
	
	
	val skeuoJson_fromTransOfGivenAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val skeuoAvro_fromTransOfGivenJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		info(s"COMMON DECODER CHECKS:")
		info(s"\nraw avro str: \n$rawAvroStr" +
		     s"\n--> apacheAvro (from parse): \n${sa.parsedApacheAvroStr}" +
		     s"\n--> skeuoAvro (from apache): ${sa.skeuoAvro_fromApache}" +
		     s"\n--> json circe (interim-avro): \n${sa.interimCirce.manicure}" +
		     
		     s"\n------> skeuoAvro (from AVRO STR): ${sai.skeuoAvro_fromRaw}" +
		     s"\n--> json circe (from avro-skeuo): \n${sai.jsonCirce_fromAvroSkeuo.manicure}" +
		     s"\n--> skeuoAvro (from decode-avro-skeuo): ${sai.skeuoAvro_fromDecodeAvroSkeuo}" +
		     s"\n    skeuoJson (from decode-avro-skeuo): ${sai.skeuoJson_fromDecodeAvroSkeuo}" +
		     
		     s"\n------> skeuoJson (from AVRO STR): ${sai.skeuoJson_fromRaw}" +
		     s"\n--> json circe (from json-skeuo): \n${sai.jsonCirce_fromJsonSkeuo.manicure}" +
		     s"\n--> skeuoAvro (from decode-json-skeuo): ${sai.skeuoAvro_fromDecodeJsonSkeuo}" +
		     s"\n    skeuoJson (from decode-json-skeuo): ${sai.skeuoJson_fromDecodeJsonSkeuo}" +
		     
		     
		     s"\n------> skeuoAvro (from JSON STR): ${sji.skeuoAvro_fromRaw}" +
		     s"\n--> json circe (from avro-skeuo): \n${sji.jsonCirce_fromAvroSkeuo.manicure}" +
		     s"\n--> skeuoAvro (from decode-avro-skeuo): ${sji.skeuoAvro_fromDecodeAvroSkeuo}" +
		     s"\n    skeuoJson (from decode-avro-skeuo): ${sji.skeuoJson_fromDecodeAvroSkeuo}" +
		     
		     s"\n------> skeuoJson (from JSON STR): ${sji.skeuoJson_fromRaw}" +
		     s"\n--> json circe (from json-skeuo): \n${sji.jsonCirce_fromJsonSkeuo.manicure}" +
		     s"\n--> skeuoAvro (from decode-json-skeuo): ${sji.skeuoAvro_fromDecodeJsonSkeuo}" +
		     s"\n    skeuoJson (from decode-json-skeuo): ${sji.skeuoJson_fromDecodeJsonSkeuo}"
		)
	}
	
	def equalityOfInitialAvroSkeuosFromAvroOrJsonInputString(): Assertion = {
		sa.parsedApacheAvroStr shouldEqual rawAvroStr
		
		forEvery(List(
			Right(sa.skeuoAvro_fromApache),
			sai.skeuoAvro_fromRaw,
			sji.skeuoAvro_fromRaw
		)
		) {
			ska ⇒ ska.right.get shouldEqual avroFixS
		}
	}
	
	def equalityOfInitialJsonSkeuosFromAvroOrJsonInputString(): Assertion = {
		forEvery(List(
			sai.skeuoJson_fromRaw,
			sji.skeuoJson_fromRaw
		)
		) {
			skj ⇒ skj.right.get shouldEqual jsonFixS
		}
	}
	
	def equalityOfCirceFromAvroOrJsonInputString(): Assertion = {
		
		forEvery(List(
			sa.interimCirce,
			sj.interimCirce,
		
		)
		) {
			jc ⇒ jc should equal(jsonCirceCheck)
		}
	}
	
	def equalityOfCirceBySkeuoAvroAndCirceBySkeuoJsonFromAvroOrJsonInputString(): Assertion = {
		forEvery(List(
			jsonCirceCheck,
			sai.jsonCirce_fromAvroSkeuo,
			sai.jsonCirce_fromJsonSkeuo,
			sji.jsonCirce_fromAvroSkeuo,
			sji.jsonCirce_fromJsonSkeuo
		)
		) {
			jc ⇒ jc.manicure shouldEqual rawJsonStr
		}
	}
	
	
	def equalityOfLastAvroSkeuosFromAvroOrJsonInputString(): Assertion = {
		forEvery(List(
			Right(skeuoAvro_fromTransOfGivenJsonSkeuo),
			sai.skeuoAvro_fromDecodeAvroSkeuo,
			sai.skeuoAvro_fromDecodeJsonSkeuo,
			sji.skeuoAvro_fromDecodeAvroSkeuo,
			sji.skeuoAvro_fromDecodeJsonSkeuo,
		)
		) {
			ska ⇒ ska.right.get should equal(avroFixS)
		}
	}
	
	def equalityOfLastJsonSkeuosFromAvroOrJsonInputString(): Assertion = {
		forEvery(List(
			Right(skeuoJson_fromTransOfGivenAvroSkeuo),
			//sji.skeuoJson_fromRaw,
			sai.skeuoJson_fromDecodeAvroSkeuo,
			sai.skeuoJson_fromDecodeJsonSkeuo,
			sji.skeuoJson_fromDecodeAvroSkeuo,
			sji.skeuoJson_fromDecodeJsonSkeuo,
		)
		) {
			skj ⇒ skj.right.get should equal(jsonFixS)
		}
	}
	
	
	def equalityOfSkeuosFromInputAndCirceDecodingAndTrans(): Assertion = {
		// Checking the conversions of skeuo through circe
		SkeuoRoundTrip.avroToAvroSkeuoByCirceDecoderShouldMatchAvroSkeuoByTrans()
		SkeuoRoundTrip.avroToJsonSkeuoByCirceDecoderShouldMatchJsonSkeuoByTrans()
		SkeuoRoundTrip.jsonToAvroSkeuoByCirceDecoderShouldMatchAvroSkeuoByTrans()
		SkeuoRoundTrip.jsonToJsonSkeuoByCirceDecodingShouldMatchJsonSkeuoByTrans()
	}
	
	
	object SkeuoRoundTrip {
		
		def avroToAvroSkeuoByCirceDecoderShouldMatchAvroSkeuoByTrans(): Assertion = {
			forEvery(List(
				decodeAvroSkeuoToCirceToAvroSkeuo(avroFixS),
				Right(skeuoAvro_fromTransOfGivenJsonSkeuo)
			)
			) {
				ska ⇒ ska should equal(Right(avroFixS))
			}
		}
		
		def avroToJsonSkeuoByCirceDecoderShouldMatchJsonSkeuoByTrans(): Assertion = {
			forEvery(List(
				decodeAvroSkeuoToCirceToJsonSkeuo(avroFixS),
				Right(skeuoJson_fromTransOfGivenAvroSkeuo)
			)
			) {
				skj ⇒ skj should equal(Right(jsonFixS))
			}
		}
		
		def jsonToAvroSkeuoByCirceDecoderShouldMatchAvroSkeuoByTrans(): Assertion = {
			forEvery(List(
				decodeJsonSkeuoToCirceToAvroSkeuo(jsonFixS),
				Right(skeuoAvro_fromTransOfGivenJsonSkeuo)
			)
			) {
				ska ⇒ ska should equal(Right(avroFixS))
			}
		}
		
		def jsonToJsonSkeuoByCirceDecodingShouldMatchJsonSkeuoByTrans(): Assertion = {
			
			forEvery(List(
				decodeJsonSkeuoToCirceToJsonSkeuo(jsonFixS),
				Right(skeuoJson_fromTransOfGivenAvroSkeuo)
			)
			) {
				skj ⇒ skj should equal(Right(jsonFixS))
			}
		}
	}
	
}
