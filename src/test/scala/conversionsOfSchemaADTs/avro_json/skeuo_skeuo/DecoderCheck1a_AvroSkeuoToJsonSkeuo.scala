package conversionsOfSchemaADTs.avro_json.skeuo_skeuo



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.avroToJson_byCataTransAlg

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
case class DecoderCheck1a_AvroSkeuoToJsonSkeuo(implicit imp: ImplicitArgs )
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	val skeuoJson_fromDecoder: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	val skeuoJson_fromTrans_byADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	def printOuts(): Unit = {
		
		info(s"\n-----------------------------------------------------------")
		
		
		info(s"\nCHECKER 1a: " +
			s"\nskeuo-avro --> skeuo-json | Reason: Trans converter, the algebra way" +
			s"\n--- skeuo-avro (given): $avroFixS" +
			s"\n--> skeuo-json (from adt-trans): ${skeuoJson_fromTrans_byADT}" +
			s"\n\t VERSUS. skeuo-json (from str-given): ${skeuoJson_fromDecoder}" +
			s"\n\t VERSUS. skeuo-json (given): $jsonFixS ")
		
	}
	
	//skeuoJson_trans_fromADT shouldEqual skeuoJson_trans_fromStr
	//skeuoJson_trans_fromADT shouldEqual jsonFixS
	// NOTE: already tested above so no use including it again
	
	def checking(): Unit = {
		
		forEvery(List(
			skeuoJson_fromDecoder,
			skeuoJson_fromTrans_byADT
		)) {
			js ⇒ js should equal(jsonFixS)
		}
	}
	
}
object DecoderCheck1a_AvroSkeuoToJsonSkeuo {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck1a_AvroSkeuoToJsonSkeuo
}
