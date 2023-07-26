package conversionsOfSchemaADTs.avro_json.parsing

import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.AvroF._
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}


/**
 *
 */
object ParseADTToCirceToADT {
	
	/**
	 * Definition from skeuomorph library - just make it possible to pass the parameter to it by apply catamorphism over it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238
	 */
	
	val libToJson: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(AvroSchema_S.toJson).apply(_)
	
	/**
	 * Definition from skeuomorph library - just applying anamorphism to make parameters passable to it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
	 */
	val libRender: Fix[JsonSchema_S] ⇒ JsonCirce = scheme.cata(JsonSchema_S.render).apply(_)
	
	
	import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
	
	
	val funcCirceToJsonSkeuo: JsonCirce ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)
	
	val funcCirceToAvroSkeuo: JsonCirce ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)
	
	
	def checker_AvroSkeuo_toJsonCirce_toJsonSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libToJson
	
	def checker_JsonSkeuo_toJsonCirce_toJsonSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libRender
	
	def checker_AvroSkeuo_toJsonCirce_toAvroSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo compose libToJson
	
	//////// ------------------------------------------------------------
	/*val avroS_to_JsonCirce: JsonCirce = libToJson(array1IntAvro_Fix_S)
	val jsonS_to_JsonCirce: JsonCirce = libRender(array1IntJson_Fix_S)
	
	val avroS_to_JsonCirce_to_JsonS: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(avroS_to_JsonCirce)
	
	
	val jsonS_to_JsonCirce_to_JsonS: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(jsonS_to_JsonCirce)
	val avroS_to_JsonCirce_To_AvroS: Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(avroS_to_JsonCirce)*/
}
