package conversionsOfSchemaADTs.avro_json.skeuo_skeuo



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._

import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import io.circe.{Json ⇒ JsonCirce}

import org.apache.avro.{Schema ⇒ AvroSchema_A}

import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

import utilMain.UtilMain.implicits._


/**
 *
 */
case class DecoderCheck3b_JsonSkeuoToJsonCirce(implicit imp: ImplicitArgs)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	info(s"\n-----------------------------------------------------------")
	
	val apacheAvro_fromStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromStr)
	
	val skeuoJson_fromTrans_byADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val skeuoJson_fromTrans_byStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromStr)
	val skeuoJson_fromDecoder: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	
	val circeJson_fromAvro: JsonCirce = libToJsonAltered(avroFixS)
	val circeJson_fromJson: JsonCirce = libRender(skeuoJson_fromTrans_byStr)
	
	
	def printOuts(): Unit = {
		info(s"\nCHECKER 3b: " +
			s"\nskeuo-json --> json-circe | Reason: get common denominator (circe), from json side" +
			s"\n--- skeuo-json (trans output): ${skeuoJson_fromTrans_byStr}" +
			s"\n--> json-circe (decoder output): \n${circeJson_fromJson.manicure}")
	}
	
	
	def checking(): Unit = {
		skeuoAvro_fromStr shouldEqual avroFixS
		
		forEvery(List(
			skeuoJson_fromTrans_byADT shouldEqual skeuoJson_fromTrans_byStr,
			skeuoJson_fromTrans_byStr,
			skeuoJson_fromDecoder
		)) {
			sj ⇒ sj shouldEqual jsonFixS
		}
		
		circeJson_fromAvro shouldEqual circeJson_fromJson
	}
}


object DecoderCheck3b_JsonSkeuoToJsonCirce {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck3b_JsonSkeuoToJsonCirce
}
