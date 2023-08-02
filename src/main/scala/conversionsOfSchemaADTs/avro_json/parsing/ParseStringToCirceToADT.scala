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
	
	object InterimResults {
		def interimDecodeAvroToJson(rawAvroStr: String): (List[String], (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]])) = {
			
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val jsonCirce_fromAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro)
			
			val skeuoJson: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromAvroSkeuo)
			
			//val skeuoJson: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoCirceToJsonSkeuo(skeuoAvro)
			
			(List("parsedApacheAvro",
				"parsedAvroApacheStr",
				"skeuoAvro",
				"jsonCirce_fromAvroSkeuo",
				"skeuoJson"),
				
				(parsedApacheAvro,
					parsedApacheAvroStr,
					skeuoAvro,
					jsonCirce_fromAvroSkeuo,
					skeuoJson
				)
			)
		}
		
		object Helper {
			def listPairToMap(names: List[String], objs: (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]])): Map[String, Any] = {
				/*
				val names: List[String] = result._1
				
				val objs: (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]]) = result._2*/
				
				val nameToObjMap: Map[String, Any] = names.zip(objs.productIterator.toList).toMap
				
				nameToObjMap
			}
		}
	}
	
	
	def decodeAvroStringToCirceToJsonSkeuo(rawAvroStr: String): Result[Fix[JsonSchema_S]] = {
		
		
		val result: (List[String], (AvroSchema_A, String, Fix[AvroSchema_S], JsonCirce, Result[Fix[JsonSchema_S]])) = InterimResults.interimDecodeAvroToJson(rawAvroStr)
		
		
		InterimResults.Helper.listPairToMap(result._1, result._2).get("skeuoJson")
	}
	
	def decodeJsonStringToCirceToJsonSkeuo(rawJsonStr: String): Option[Fix[JsonSchema_S]] = {
		val parsedJsonCirce: JsonCirce = unsafeParse(rawJsonStr)
		
		val decoded: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(parsedJsonCirce)
		
		decoded
	}
	
	def decodeJsonStringToCirceToAvroSkeuo(rawJsonStr: String): Result[Fix[AvroSchema_S]] = {
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
