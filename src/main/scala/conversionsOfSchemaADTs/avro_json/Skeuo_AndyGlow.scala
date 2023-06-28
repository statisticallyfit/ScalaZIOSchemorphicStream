package conversionsOfSchemaADTs.avro_json



import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.JsonSchemaF.Fixed
import json.{Schema, Schema ⇒ JsonSchema_G}
/**
 *
 */
object Skeuo_AndyGlow {
	
	
	def skeuoToAndyGlowJsonSchema[T](skeuoJsonSchema: JsonSchema_S[T]): JsonSchema_G[T] = ???
	
	def andyGlowToSkeuoJsonSchema[T](andyJsonSchema: JsonSchema_G[T]): JsonSchema_S[T] = andyJsonSchema match {
	
		case JsonSchema_G.`integer` ⇒ JsonSchema_S.IntegerF()
		case JsonSchema_G.`number`[Float] ⇒ JsonSchema_S.FloatF()
	}
	
	
	def algebra_AndyGlowToSkeuo: Algebra[JsonSchema_G, JsonSchema_S] = // meaning: Andy[Skeuo[T]] => SKeuo[T]
		Algebra {
			
			case JsonSchema_G.`integer` ⇒ JsonSchema_S.IntegerF()
			case JsonSchema_G.`string` ⇒ JsonSchema_S.StringF()
			case JsonSchema_G.`object`[JsonSchema_S.ObjectF] ⇒ JsonSchema_S.ObjectF()
			case JsonSchema_G.`number`[JsonSchema_S.FloatF] ⇒ JsonSchema_S.FloatF() // TODO this way `number`[Float] or Json.schema[Float]?
			
			val t: JsonSchema_G[Float] = json.Json.schema[Float]
			val s: JsonSchema_G.number[Float] = JsonSchema_G.`number`[Float]
		}

}
