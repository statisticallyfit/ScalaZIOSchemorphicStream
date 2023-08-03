package conversionsOfSchemaADTs.avro_json.parsing


import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import higherkindness.skeuomorph.openapi.JsonDecoders._

import org.apache.avro.{Schema ⇒ AvroSchema_A}
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache.apacheToSkeuoAvroSchema

import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import utilMain.UtilMain.implicits._

/**
 *
 */
object ParseStringToCirceToADT {
	
	object StepByStep {
		
		def stepAvroStringToJsonSkeuo(rawAvroStr: String): (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]], Result[Fix[JsonSchema_S]]) = {
			
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val jsonCirce_fromAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro)
			
			val skeuoJson_fromDecodingAvroSkeuo: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoToCirceToJsonSkeuo(skeuoAvro)
			
			val skeuoJson_fromDecodingCirce: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromAvroSkeuo)
			
			(parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro,
				jsonCirce_fromAvroSkeuo,
				skeuoJson_fromDecodingAvroSkeuo,
				skeuoJson_fromDecodingCirce
			)
		}
		
		def stepAvroStringToAvroSkeuo(rawAvroStr: String): (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[AvroSchema_S]], Result[Fix[AvroSchema_S]]) = {
			
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro_fromGiven: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val jsonCirce_fromAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromGiven)
			
			
			import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
			
			val skeuoAvro_fromDecodingAvroSkeuo: Result[Fix[AvroSchema_S]] = decodeAvroSkeuoToCirceToAvroSkeuo(skeuoAvro_fromGiven)
			
			val skeuoAvro_fromDecodingCirce: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromAvroSkeuo)
			
			(parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro_fromGiven,
				jsonCirce_fromAvroSkeuo,
				skeuoAvro_fromDecodingAvroSkeuo,
				skeuoAvro_fromDecodingCirce
			)
			
		}
		
		def stepJsonStringToJsonSkeuo(rawJsonStr: String): (JsonCirce, Result[Fix[JsonSchema_S]]) = {
			
			val parsedJsonCirce: JsonCirce = unsafeParse(rawJsonStr)
			
			val skeuoJson_decoded: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(parsedJsonCirce)
			
			(parsedJsonCirce,
				skeuoJson_decoded)
		}
		
		def stepJsonStringToAvroSkeuo(rawJsonStr: String): (JsonCirce, Result[Fix[AvroSchema_S]]) = {
			
			val parsedJsonCirce: JsonCirce = unsafeParse(rawJsonStr)
			
			import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
			
			
			val skeuoAvro_decoded: Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(parsedJsonCirce)
			
			(parsedJsonCirce,
				skeuoAvro_decoded)
		}
	}
	
	
	def decodeAvroStringToCirceToJsonSkeuo(rawAvroStr: String): Result[Fix[JsonSchema_S]] = {
		
		val result: (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]], Result[Fix[JsonSchema_S]]) = StepByStep.stepAvroStringToJsonSkeuo(rawAvroStr)
		
		
		result._6
	}
	
	def decodeAvroStringToCirceToAvroSkeuo(rawAvroStr: String): Result[Fix[AvroSchema_S]] = {
		
		val result: (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[AvroSchema_S]], Result[Fix[AvroSchema_S]]) = StepByStep.stepAvroStringToAvroSkeuo(rawAvroStr)
		
		result._6
	}
	
	def decodeJsonStringToCirceToJsonSkeuo(rawJsonStr: String): Result[Fix[JsonSchema_S]] = {
		
		
		val result: (JsonCirce, Result[Fix[JsonSchema_S]]) = StepByStep.stepJsonStringToJsonSkeuo(rawJsonStr)
		
		result._2
	}
	
	def decodeJsonStringToCirceToAvroSkeuo(rawJsonStr: String): Result[Fix[AvroSchema_S]] = {
		
		val result: (JsonCirce, Result[Fix[AvroSchema_S]]) = StepByStep.stepJsonStringToAvroSkeuo(rawJsonStr)
		
		result._2
	}
	
}
