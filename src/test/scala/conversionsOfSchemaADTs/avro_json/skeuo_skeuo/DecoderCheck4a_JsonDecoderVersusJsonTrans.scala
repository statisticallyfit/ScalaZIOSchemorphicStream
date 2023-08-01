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


import org.apache.avro.{Schema ⇒ AvroSchema_A}

import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

import utilMain.UtilMain.implicits._

/**
 *
 */
case class DecoderCheck4a_JsonDecoderVersusJsonTrans(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	
	
	
	
	/*val skeuoJson_trans_fromADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val skeuoJson_trans_fromStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromStr)*/
	/*val skeuoJson_fromStr: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get*/
	
	val jsonCirceFromStr: JsonCirce = unsafeParse(rawJsonStr)
	val skeuoJson_fromDecoder: Option[Fix[JsonSchema_S]] = strToCirceToSkeuoJson(rawJsonStr)
	val skeuoJson_fromTrans_byADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	
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
			s"\n\t VERSUS. skeuo-json (trans output): ${skeuoJson_fromTrans_byADT}")
	}
	
	
	def checking(): Unit = {
		// Check all jsons are equal (inputs and outputs)
		forEvery(List(
			jsonCirceCheck.manicure,
			jsonCirceFromStr.manicure
		)) {
			js ⇒ js shouldEqual rawJsonStr
		}
		
		// Check all trans-output jsons are equal
		skeuoJson_fromDecoder.get shouldEqual skeuoJson_fromTrans_byADT
	}
}



object DecoderCheck4a_JsonDecoderVersusJsonTrans {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck4a_JsonDecoderVersusJsonTrans
}