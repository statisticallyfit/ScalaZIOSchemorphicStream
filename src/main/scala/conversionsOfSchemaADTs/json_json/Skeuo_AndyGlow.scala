package conversionsOfSchemaADTs.json_json

import conversionsOfSchemaStrings.json_json.RawJsonSchemaStr_To_AndyGlowJsonSchemaADT.parseType

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}

import json.{Schema ⇒ JsonSchema_G}

import scala.reflect.runtime.universe


import utilTest. // TODO need the test support -- must move the testsupport to main (utilMain)


/**
 *
 */
object Skeuo_AndyGlow extends App {
	
	
	def skeuoToAndyGlowJsonSchema[T](skeuoJsonSchema: JsonSchema_S[T]): JsonSchema_G[T] = ???
	
	
	import scala.reflect.runtime.universe._
	
	def andyGlowToSkeuoJsonSchema[T](andyJsonSchema: JsonSchema_G[T]): JsonSchema_S[T] = andyJsonSchema match {
		
		// HELP andy glow has more string sub-types available while Skeuo does not = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L129-L137
		case _: JsonSchema_G.`string`[tpe] ⇒ JsonSchema_S.StringF()
		
		case _:JsonSchema_G.`integer` ⇒ JsonSchema_S.IntegerF()
		
		//case _:JsonSchema_G.`number`[Float] ⇒ JsonSchema_S.FloatF() // WORKS
		
		case _:JsonSchema_G.`number`[tpe] ⇒ typeOf[tpe] match {
			case Byte ⇒ JsonSchema_S.ByteF()
			case Float ⇒ JsonSchema_S.FloatF()
			case Int ⇒ JsonSchema_S.IntegerF()
			//case Short ⇒ JsonSchema_S.S
			case Long ⇒ JsonSchema_S.LongF()
			case Double ⇒ JsonSchema_S.DoubleF()
			//case Char ⇒ JsonSchema_S.
			//case BigInt ⇒
			//case BigDecimal ⇒ // TODO missing a lot of types on skeuo's end!
		}
		
		case _:JsonSchema_G.`array`[tpe, c] ⇒ {
			val res: universe.Type = typeOf[tpe]
			val innerSchema: JsonSchema_G[_] = andyJsonSchema.asInstanceOf[JsonSchema_G.array[tpe,c]].componentType
			
			println(s"Inside array converter: glow -> skeuo")
			println(s"typeOf[tpe], where tpe = inner(arraytype) = $res")
			println(s"innerSchema (from asinstanceof .. componenttype) = $innerSchema")
			
			// TODO not sure if this is right
			JsonSchema_S.array(andyGlowToSkeuoJsonSchema(innerSchema))
		}
		
		
		
		
	}
	
	
	println("TESTING BRIEF GLOW -> SKEUO (json schema)")
	
	println("\n\nINT CONVERSION")
	
	
	println("\n\nSTRING CONVERSION")
	val jsonSchemaStr: String =
		"""
		  |{
		  |  "type": "string"
		  |}
		  |""".stripMargin
	val resultStr: JsonSchema_G[_] = parseType(jsonSchemaStr).value
	println(s"type = ${typeOf[resultStr.type]}")
	println(s"result string = $resultStr")
	
	println("\n\nARRAY CONVERSION")
	val resultArr: JsonSchema_G[_] = parseType {
		"""{
		  | "type": "array",
		  | "items": {
		  |   "type": "string"
		  | }
		  |}
					""".stripMargin
	}.value
	
	//resultArr shouldBe `array`(`string`)
	println(s"type = ${typeOf[resultArr.type]}")
	println(s"array result = $resultArr")
	
	
	/*def algebra_AndyGlowToSkeuo: Algebra[JsonSchema_G, JsonSchema_S] = // meaning: Andy[Skeuo[T]] => SKeuo[T]
		Algebra {
			
			
			
			// HELP andy glow has more string sub-types available while Skeuo does not = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L129-L137
			case JsonSchema_G.`string` ⇒ JsonSchema_S.StringF()
			
			
			case JsonSchema_G.`integer` ⇒ JsonSchema_S.IntegerF()
			
			// HELP error with the below code it says: Pattern type is incompatible with expected type, found: Schema.number.type, required: Schema[JsonSchemaF]
			//case JsonSchema_G.`number`[_] ⇒ JsonSchema_S.FloatF()
			//case JsonSchema_G.number[Float] ⇒ JsonSchema_S.FloatF()
			//case JsonSchema_G.number[JsonSchema_S.Fixed] ⇒ JsonSchema_S.FloatF()
			case JsonSchema_G.`number`[_:JsonSchema_S[_]] ⇒ JsonSchema_S.FloatF()
			//case json.Json.schema[JsonSchema_S[_]] ⇒ JsonSchema_S.FloatF()
			
			//case json.Json.schema[Float] ⇒ JsonSchema_S.FloatF()
			
			
			case JsonSchema_G[Float] ⇒
			
			val t: JsonSchema_G[Float] = json.Json.schema[Float]
			val s: JsonSchema_G.number[Float] = JsonSchema_G.`number`[Float]
			
			val x: Schema.string.type = JsonSchema_G.`string`
			val x2: JsonSchema_G[String] = JsonSchema_G.`string`
			
			
			JsonSchema_G.`number`[JsonSchema_S.FloatF[_]]
			
			
			case JsonSchema_G.`object`[JsonSchema_S.ObjectF] ⇒ JsonSchema_S.ObjectF()
		}*/

}
