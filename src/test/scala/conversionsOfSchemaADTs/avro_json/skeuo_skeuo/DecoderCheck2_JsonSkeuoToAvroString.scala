package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.Decoder.Result
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
case class DecoderCheck2_JsonSkeuoToAvroString(implicit imp: ImplicitArgs )
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	
	val skeuoAvro_fromTransOfGivenJsonSkeuo: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	
	
	val apacheAvro_fromAvroSkeuo: AvroSchema_A = skeuoToApacheAvroSchema(skeuoAvro_fromTransOfGivenJsonSkeuo)
	
	val apacheAvroStr: String = apacheAvro_fromAvroSkeuo.toString(true).manicure
	
	// -----
	val jsonCirce_fromJsonSkeuo: JsonCirce = libRender(jsonFixS)
	val jsonCirce_fromAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromTransOfGivenJsonSkeuo)
	
	val skeuoAvro_fromDecoder: Result[Fix[AvroSchema_S]] = DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(jsonFixS)
	
	def showResults(): String = {
		
		var infoVar: String = s"\n-----------------------------------------------------------"
		
		infoVar +=
		(s"\nCHECKER 2: " +
		 s"\nskeuo-json --> skeuo-avro --> avro-str (trans output) vs. avro-str (input) " +
		 s"|\n\t Reason: get common denominator (avro-str), from avro-side" +
			
		 s"\n\n--- skeuo-json (given): ${jsonFixS}" +
		 s"\n--> skeuo-avro (trans output): ${skeuoAvro_fromTransOfGivenJsonSkeuo}" +
		 s"\n    skeuo-avro (decoder output): ${skeuoAvro_fromDecoder}" +
		 s"\n--> apache-avro-str (trans output): \n${apacheAvroStr}" +
		 s"\n    raw-avro-str (input): \n$rawAvroStr" +
		 s"\n--> json-circe (from avro-skeuo): \n${jsonCirce_fromAvroSkeuo}" +
		 s"\n--> json-circe (from json-skeuo): \n${jsonCirce_fromJsonSkeuo}")
		
		
		infoVar
	}
	
	def checking(): Unit = {
		import Checking._
		
		equalityOfInputAvroSkeuoAndOutputAvroString()
		
		equalityOfCircesFromAvroOrJsonInputSkeuo()
		
		equalityOfAvroSkeuoFromDecoderAndTrans()
	}
	
	object Checking {
		
		def equalityOfInputAvroSkeuoAndOutputAvroString(): Assertion = {
			
			apacheAvroStr shouldEqual rawAvroStr
		}
		
		def equalityOfCircesFromAvroOrJsonInputSkeuo(): Assertion = {
			forEvery(List(
				jsonCirce_fromAvroSkeuo,
				jsonCirce_fromJsonSkeuo
			)){
				jc ⇒ jc shouldEqual jsonCirceCheck
			}
		}
		
		def equalityOfAvroSkeuoFromDecoderAndTrans(): Assertion = {
			
			forEvery(List(
				skeuoAvro_fromTransOfGivenJsonSkeuo,
				skeuoAvro_fromDecoder.right.get
			)) {
				ska ⇒ ska shouldEqual avroFixS
			}
		}
	}
}

/*

object DecoderCheck2_JsonSkeuoToAvroString {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck2_JsonSkeuoToAvroString
}
*/
