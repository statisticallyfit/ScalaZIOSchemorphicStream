package conversionsOfSchemaADTs.avro_json.skeuo_skeuo



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.avroToJson_byCataTransAlg
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import org.scalatest.{Assertion, Assertions}
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
	
	//Equivalent to skeuoAvro_fromStr (of test 1a) because no other way here to start from string except via the json-circe-str
	val skeuoJson_fromDecoder: Fix[JsonSchema_S] = funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	
	val skeuoJson_fromTransOfAvroSkeuo: Fix[JsonSchema_S] = avroToJson_byCataTransAlg(avroFixS)
	
	def printOuts(): Unit = {
		
		info(s"\n-----------------------------------------------------------")
		
		
		info(s"\nCHECKER 1a: " +
			s"\nskeuo-avro --> skeuo-json | Reason: Trans converter, the algebra way" +
			s"\n--- skeuo-avro (given): $avroFixS" +
			s"\n--> skeuo-json (from adt-trans): ${skeuoJson_fromTransOfAvroSkeuo}" +
			s"\n\t VERSUS. skeuo-json (from str-given): ${skeuoJson_fromDecoder}" +
			s"\n\t VERSUS. skeuo-json (given): $jsonFixS ")
		
	}
	
	
	def checking(): Unit = {
		 import Checks._
		checkInputJsonSkeuoEqualsOutputJsonSkeuosFromDecoderAndFromTrans
	}
	
	object Checks {
		
		def checkInputJsonSkeuoEqualsOutputJsonSkeuosFromDecoderAndFromTrans
		: Assertion = {
			
			forEvery(List(
				skeuoJson_fromDecoder,
				skeuoJson_fromTransOfAvroSkeuo
			)) {
				js ⇒ js should equal(jsonFixS)
			}
		}
	}
	
}
object DecoderCheck1a_AvroSkeuoToJsonSkeuo {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck1a_AvroSkeuoToJsonSkeuo
}
