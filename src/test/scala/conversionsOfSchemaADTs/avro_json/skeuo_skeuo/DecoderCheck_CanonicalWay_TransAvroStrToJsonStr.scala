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
case class DecoderCheck_CanonicalWay_TransAvroStrToJsonStr(implicit imp: ImplicitArgs/*rawAvroStr: String,
										    rawJsonStr: String,
										    jsonCirceCheck: JsonCirce, avroS: AvroSchema_S[_], tpeS: String,
										    avroC: AvroSchema_S[_], tpeC: String,
										    avroFixS: Fix[AvroSchema_S], jsonFixS: Fix[JsonSchema_S]*/)
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	//val apacheAvro: AvroSchema_A = skeuoToApacheAvroSchema(avroFixS)
	//val apacheAvroStr: String = apacheAvro.toString(true).removeSpaceBeforeColon
	val apacheAvro_fromStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	val skeuoAvro_fromStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromStr)
	val skeuoJson_trans_fromStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromStr)
	val skeuoJson_trans_fromADT: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	val circeJson_trans_fromStr: JsonCirce = libRender(skeuoJson_trans_fromStr)
	//val circeJson_fromJsonSkeuo: JsonCirce = libRender(skeuoJson_trans_fromApache)
	
	
	def printOutsForUnderstanding: Unit = {
		info(s"CANONICAL way: apache-avro-str ---> skeuo-avro --> skeuo-json --> json-circe-str")
		info(s"--- apache-avro-str (given): \n${rawAvroStr}")
		info(s"--> skeuoAvro: $skeuoAvro_fromStr")
		info(s"--> skeuoJson: $skeuoJson_trans_fromStr")
		info(s"--> json-circe: \n${circeJson_trans_fromStr.manicure}")
	}
	
	def checking: Unit = {
		
		skeuoAvro_fromStr shouldEqual avroFixS
		//skeuoJson_trans_fromStr should equal(jsonFixS)
		//skeuoJson_trans_fromStr should equal(skeuoJson_trans_fromADT)
		
		forEvery(List(
			skeuoJson_trans_fromStr,
			skeuoJson_trans_fromADT
		)) {
			js ⇒ js should equal(jsonFixS)
		}
		
		circeJson_trans_fromStr should equal(jsonCirceCheck)
	}
}


object DecoderCheck_CanonicalWay_TransAvroStrToJsonStr {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck_CanonicalWay_TransAvroStrToJsonStr(i)
}
