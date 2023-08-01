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
case class DecoderCheck1b_JsonSkeuoToAvroSkeuo(implicit imp: ImplicitArgs )
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	
	
	val apacheAvro_fromStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromStr)
	
	// convert: json-circe -> json-skeuo -> avro-skeuo
	val skeuoAvro_fromTrans_byStr: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(
		funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	)
	val skeuoAvro_fromTrans_byADT: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		
		info(s"\nCHECKER 1b: " +
			s"\nskeuo-json -> skeuo-avro | Reason: Trans converter, the coalgebra way" +
			s"\n--- skeuo-json (given): $jsonFixS" +
			s"\n--> skeuo-avro (adt-trans): $skeuoAvro_fromTrans_byADT" +
			s"\n\t VERSUS. skeuo-avro (decoder output): $skeuoAvro_fromTrans_byStr" +
			s"\n\t VERSUS. skeuo-avro (str-given): $skeuoAvro_fromStr" +
			s"\n\t VERSUS. skeuo-avro (given): $avroFixS")
	}
	
	//skeuoAvro_trans_fromADT shouldEqual skeuoAvro_trans_fromStr
	
	def checking(): Unit = {
		forEvery(List(
			skeuoAvro_fromTrans_byADT,
			skeuoAvro_fromTrans_byStr
		)) {
			as ⇒ as should equal(avroFixS)
		}
	}
}


object DecoderCheck1b_JsonSkeuoToAvroSkeuo {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck1b_JsonSkeuoToAvroSkeuo
}