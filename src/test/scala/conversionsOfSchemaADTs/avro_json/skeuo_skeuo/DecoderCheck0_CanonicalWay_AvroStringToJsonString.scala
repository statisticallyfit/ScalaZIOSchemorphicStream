package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.avroToJson_byCataTransAlg
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import org.scalatest.Assertion
import org.scalatest.Inspectors._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import utilMain.UtilMain.implicits._


/**
 *
 */
// Using implicitargs class to remove the need for passing all the arguments explicitly
class DecoderCheck0_CanonicalWay_AvroStringToJsonString(implicit imp: ImplicitArgs )
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	/*rawAvroStr: String,
											rawJsonStr: String,
											jsonCirceCheck: JsonCirce, avroS: AvroSchema_S[_], tpeS: String,
											avroC: AvroSchema_S[_], tpeC: String,
											avroFixS: Fix[AvroSchema_S], jsonFixS: Fix[JsonSchema_S]*/
	
	import imp._
	
	//val apacheAvro: AvroSchema_A = skeuoToApacheAvroSchema(avroFixS)
	//val apacheAvroStr: String = apacheAvro.toString(true).removeSpaceBeforeColon
	val apacheAvro_fromStr: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO Option for parsing failure?
	
	val skeuoAvro_fromApacheStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(apacheAvro_fromStr)
	
	val skeuoJson_fromTransOfAvroStr: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(skeuoAvro_fromApacheStr)
	
	val skeuoJson_fromTransOfAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	val circeJson_fromTransOfJsonSkeuoOfAvroStr: JsonCirce = libRender(skeuoJson_fromTransOfAvroStr)
	//val circeJson_fromJsonSkeuo: JsonCirce = libRender(skeuoJson_trans_fromApache)
	
	
	def printOuts(): Unit = {
		info(s"\n-----------------------------------------------------------")
		
		info(s"CANONICAL way: apache-avro-str ---> skeuo-avro --> skeuo-json --> json-circe-str")
		info(s"--- apache-avro-str (given): \n${rawAvroStr}")
		info(s"--> skeuoAvro: $skeuoAvro_fromApacheStr")
		info(s"--> skeuoJson (from str-trans): $skeuoJson_fromTransOfAvroStr")
		info(s"VERSUS: skeuoJson (from adt-trans): $skeuoJson_fromTransOfAvroSkeuo")
		info(s"--> json-circe (from trans): \n${circeJson_fromTransOfJsonSkeuoOfAvroStr.manicure}")
	}
	
	def checking(): Unit = {
		
		import Checks._
		
		checkInputAvroSkeuoEqualsAvroSkeuoFromApacheString
		checkInputJsonSkeuoEqualsOutputJsonSkeuosFromApacheStringAndFromTrans
		checkInputJsonCirceEqualsOutputJsonCirceFromTrans
		
	}
	
	object Checks {
		def checkInputAvroSkeuoEqualsAvroSkeuoFromApacheString: Assertion = {
			skeuoAvro_fromApacheStr shouldEqual avroFixS
		}
		
		def checkInputJsonSkeuoEqualsOutputJsonSkeuosFromApacheStringAndFromTrans: Assertion = {
			forEvery(List(
				skeuoJson_fromTransOfAvroStr,
				skeuoJson_fromTransOfAvroSkeuo
			)) {
				js ⇒ js should equal(jsonFixS)
			}
		}
		
		def checkInputJsonCirceEqualsOutputJsonCirceFromTrans: Assertion = {
			circeJson_fromTransOfJsonSkeuoOfAvroStr should equal(jsonCirceCheck)
		}
	}
}

/*
implicit object DecoderCheck0_CanonicalWay_AvroStringToJsonString {
	implicit def apply(implicit imp: ImplicitArgs) = new DecoderCheck0_CanonicalWay_AvroStringToJsonString
}*/
