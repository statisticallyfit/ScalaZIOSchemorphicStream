package conversionsOfSchemaADTs.json_json


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import json.{Schema ⇒ SchemaJson_Glow}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

// TODO need the test support -- must move the testsupport to main (utilMain)


/**
 *
 */
object Skeuo_AndyGlow {
	
	
	def skeuoToAndyGlowJsonSchema[T](skeuoJsonSchema: SchemaJson_Skeuo[T]): SchemaJson_Glow[T] = ???
	
	
	def andyGlowToSkeuoJsonSchema[T](andyJsonSchema: SchemaJson_Glow[T])(implicit tag: TypeTag[T]): SchemaJson_Skeuo[T] = andyJsonSchema match {
		
		// HELP andy glow has more string sub-types available while Skeuo does not = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L129-L137
		case _: SchemaJson_Glow.`string`[tpe] ⇒ SchemaJson_Skeuo.StringF()
		
		case _: SchemaJson_Glow.`integer` ⇒ SchemaJson_Skeuo.IntegerF()
		
		// NOTE: number takes type parameters that are in Numeric (Long, Float, Double, BigInt, BigDecimal, Byte, Short)
		// NOTE or use Andy Glow Predef code link = https://github.com/andyglow/scala-jsonschema/blob/d52b2bb7d38785bc4e4545285eee7eca1e8978ce/core/src/main/scala/json/schema/Predef.scala#L37
		
		case _: SchemaJson_Glow.`number`[Float] ⇒ SchemaJson_Skeuo.FloatF()
		case _: SchemaJson_Glow.`number`[Long] ⇒ SchemaJson_Skeuo.LongF()
		
		case _: SchemaJson_Glow.`number`[Byte] ⇒ SchemaJson_Skeuo.ByteF()
		case _: SchemaJson_Glow.`number`[Int] ⇒ SchemaJson_Skeuo.IntegerF()
		case _: SchemaJson_Glow.`number`[Integer] ⇒ SchemaJson_Skeuo.IntegerF()
		
		/*case _: SchemaJson_Glow.`number`[tpe] ⇒ typeOf[tpe] match {
			case Byte ⇒ SchemaJson_Skeuo.ByteF()
			case Float ⇒ SchemaJson_Skeuo.FloatF()
			case Int ⇒ SchemaJson_Skeuo.IntegerF()
			//case Short ⇒ SchemaJson_Skeuo.S
			case Long ⇒ SchemaJson_Skeuo.LongF()
			case Double ⇒ SchemaJson_Skeuo.DoubleF()
			//case Char ⇒ SchemaJson_Skeuo.
			//case BigInt ⇒
			//case BigDecimal ⇒ // TODO missing a lot of types on skeuo's end!
		}*/
		
		
		case SchemaJson_Glow.array(componentSchema: SchemaJson_Glow[T], unique) ⇒ {
			
			println(s"INSIDE array converter: glow -> skeuo")
			println(s"inner type (tag.tpe.toString) = ${tag.tpe.toString}")
			//val a: universe.TypeTag[T] = tag
			//val b: universe.TypeTag[T] = implicitly[TypeTag[T]]
			val c: universe.TypeTag[SchemaJson_Glow[T]] = implicitly[TypeTag[SchemaJson_Glow[T]]]
			println(s"entire schema type = ${c.tpe.toString}")
			
			
			SchemaJson_Skeuo.array(andyGlowToSkeuoJsonSchema(componentSchema))
		}
		
		//case SchemaJson_Glow.map
		
		
	}
	
	
	/*def algebra_AndyGlowToSkeuo: Algebra[SchemaJson_Glow, SchemaJson_Skeuo] = // meaning: Andy[Skeuo[T]] => SKeuo[T]
		Algebra {
			
			
			
			// HELP andy glow has more string sub-types available while Skeuo does not = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L129-L137
			case SchemaJson_Glow.`string` ⇒ SchemaJson_Skeuo.StringF()
			
			
			case SchemaJson_Glow.`integer` ⇒ SchemaJson_Skeuo.IntegerF()
			
			// HELP error with the below code it says: Pattern type is incompatible with expected type, found: Schema.number.type, required: Schema[JsonSchemaF]
			//case SchemaJson_Glow.`number`[_] ⇒ SchemaJson_Skeuo.FloatF()
			//case SchemaJson_Glow.number[Float] ⇒ SchemaJson_Skeuo.FloatF()
			//case SchemaJson_Glow.number[SchemaJson_Skeuo.Fixed] ⇒ SchemaJson_Skeuo.FloatF()
			case SchemaJson_Glow.`number`[_:SchemaJson_Skeuo[_]] ⇒ SchemaJson_Skeuo.FloatF()
			//case json.Json.schema[SchemaJson_Skeuo[_]] ⇒ SchemaJson_Skeuo.FloatF()
			
			//case json.Json.schema[Float] ⇒ SchemaJson_Skeuo.FloatF()
			
			
			case SchemaJson_Glow[Float] ⇒
			
			val t: SchemaJson_Glow[Float] = json.Json.schema[Float]
			val s: SchemaJson_Glow.number[Float] = SchemaJson_Glow.`number`[Float]
			
			val x: Schema.string.type = SchemaJson_Glow.`string`
			val x2: SchemaJson_Glow[String] = SchemaJson_Glow.`string`
			
			
			SchemaJson_Glow.`number`[SchemaJson_Skeuo.FloatF[_]]
			
			
			case SchemaJson_Glow.`object`[SchemaJson_Skeuo.ObjectF] ⇒ SchemaJson_Skeuo.ObjectF()
		}*/
	
}
