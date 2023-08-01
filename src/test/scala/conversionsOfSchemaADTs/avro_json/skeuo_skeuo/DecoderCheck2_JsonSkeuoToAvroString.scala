package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans._
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
case class DecoderCheck2_JsonSkeuoToAvroString(implicit imp: ImplicitArgs )
	extends AnyFunSpec with DecoderChecks with Matchers {
	
	import imp._
	
	
	info(s"\n-----------------------------------------------------------")
	
	val skeuoAvro_fromTransOfADT: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(jsonFixS)
	// convert: json-circe -> json-skeuo -> avro-skeuo
	/*val skeuoAvro_trans_fromStr: Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg(
		funcCirceToJsonSkeuo(jsonCirceCheck).right.get
	)*/
	
	val apacheAvro_fromAvroSkeuoOfTransOfADT: AvroSchema_A = skeuoToApacheAvroSchema(skeuoAvro_fromTransOfADT)
	
	val apacheAvroStr_fromSkeuoOfTransOfADT: String = apacheAvro_fromAvroSkeuoOfTransOfADT.toString(true).removeSpaceBeforeColon
	
	
	def printOuts(): Unit = {
		info(s"\nCHECKER 2: " +
			s"\nskeuo-json --> skeuo-avro --> avro-str (trans output) vs. avro-str (input) | Reason: get common denominator (avro-str), from avro-side")
		info(s"\n--- skeuo-json (given): ${jsonFixS}")
		info(s"\n--> skeuo-avro (trans output): ${skeuoAvro_fromTransOfADT}")
		/*info(s"\n\t VERSUS: skeuo-avro (decoder output): ${skeuoAvro_trans_fromStr}")*/
		info(s"\n--> apache-avro-str (trans output): \n$apacheAvroStr_fromSkeuoOfTransOfADT")
		info(s"\n\t VERSUS: apache-avro-str (given): \n$rawAvroStr")
	}
	
	def checking(): Unit = {
		import Checks._
		checkInputAvroSkeuoEqualsAvroSkeuoFromJsonSkeuoTransOutput
		checkInputAvroStringEqualsApacheAvroStringFromAvroSkeuoTransOutput
	}
	
	object Checks {
		
		def checkInputAvroSkeuoEqualsAvroSkeuoFromJsonSkeuoTransOutput: Assertion = {
			skeuoAvro_fromTransOfADT shouldEqual avroFixS
		}
		
		def checkInputAvroStringEqualsApacheAvroStringFromAvroSkeuoTransOutput: Assertion = {
			
			apacheAvroStr_fromSkeuoOfTransOfADT shouldEqual rawAvroStr
		}
		
		
	}
}


object DecoderCheck2_JsonSkeuoToAvroString {
	def apply(implicit imp: ImplicitArgs) = new DecoderCheck2_JsonSkeuoToAvroString
}
