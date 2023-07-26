package conversionsOfSchemaADTs.avro_json.parsing

import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._


/**
 *
 */
object ParseStringToCirceToADT {
	
	def strToCirceToSkeuoJson(rawJsonStr: String): Option[Fix[JsonSchema_S]] = {
		val parsed: JsonCirce = unsafeParse(rawJsonStr)
		val decoded: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(parsed)
		
		decoded.getOrElse(None) match {
			case None ⇒ None
			case v: Fix[JsonSchema_S] ⇒ Some(v)
		}
	}
	
	def strToCirceToSkeuoAvro(rawJsonStr: String): Result[Fix[AvroSchema_S]] = {
		val parsed: JsonCirce = unsafeParse(rawJsonStr)
		
		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
		
		
		val decoded: Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(parsed)
		
		decoded
		/*decoded.getOrElse(None) match {
			case None ⇒ None
			case v: Fix[AvroSchema_S] ⇒ Some(v)
		}*/
	}
	
}
