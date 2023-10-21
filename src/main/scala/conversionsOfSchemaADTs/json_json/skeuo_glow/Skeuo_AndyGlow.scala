package conversionsOfSchemaADTs.json_json.skeuo_glow

import higherkindness.skeuomorph.openapi.{JsonSchemaF => JsonSchema_S}
import json.{Schema => JsonSchema_G}




import higherkindness.droste._
import higherkindness.droste.data._

import scala.language.postfixOps
import scala.language.higherKinds

import scala.reflect.runtime.universe._

// TODO need the test support -- must move the testsupport to main (utilMain)


/**
 *
 */
object Skeuo_AndyGlow {



	object ByTrans {

		import Trans_JsonToJson._

	}


	object ByManual {

		// HELP not done yet

		def skeuoToAndyGlowJsonSchema[T](skeuoJsonSchema: JsonSchema_S[T]): JsonSchema_G[T] = ???


		def andyGlowToSkeuoJsonSchema[T](andyJsonSchema: JsonSchema_G[T])(implicit tag: TypeTag[T]): JsonSchema_S[T] = andyJsonSchema match {

			// HELP andy glow has more string sub-types available while Skeuo does not = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/modules/parser/src/main/scala/com/github/andyglow/jsonschema/ParseJsonSchema.scala#L129-L137
			case _: JsonSchema_G.`string`[tpe] ⇒ JsonSchema_S.StringF()

			case _: JsonSchema_G.`integer` ⇒ JsonSchema_S.IntegerF()

			// NOTE: number takes type parameters that are in Numeric (Long, Float, Double, BigInt, BigDecimal, Byte, Short)
			// NOTE or use Andy Glow Predef code link = https://github.com/andyglow/scala-jsonschema/blob/d52b2bb7d38785bc4e4545285eee7eca1e8978ce/core/src/main/scala/json/schema/Predef.scala#L37

			case _: JsonSchema_G.`number`[Float] ⇒ JsonSchema_S.FloatF()
			case _: JsonSchema_G.`number`[Long] ⇒ JsonSchema_S.LongF()

			case _: JsonSchema_G.`number`[Byte] ⇒ JsonSchema_S.ByteF()
			case _: JsonSchema_G.`number`[Int] ⇒ JsonSchema_S.IntegerF()
			case _: JsonSchema_G.`number`[Integer] ⇒ JsonSchema_S.IntegerF()

			/*case _: JsonSchema_G.`number`[tpe] ⇒ typeOf[tpe] match {
				case Byte ⇒ JsonSchema_S.ByteF()
				case Float ⇒ JsonSchema_S.FloatF()
				case Int ⇒ JsonSchema_S.IntegerF()
				//case Short ⇒ JsonSchema_S.S
				case Long ⇒ JsonSchema_S.LongF()
				case Double ⇒ JsonSchema_S.DoubleF()
				//case Char ⇒ JsonSchema_S.
				//case BigInt ⇒
				//case BigDecimal ⇒ // TODO missing a lot of types on skeuo's end!
			}*/


			case JsonSchema_G.array(componentSchema: JsonSchema_G[T], unique) ⇒ {

				println(s"INSIDE array converter: glow -> skeuo")
				println(s"inner type (tag.tpe.toString) = ${tag.tpe.toString}")
				//val a: universe.TypeTag[T] = tag
				//val b: universe.TypeTag[T] = implicitly[TypeTag[T]]
				val c: TypeTag[JsonSchema_G[T]] = implicitly[TypeTag[JsonSchema_G[T]]]
				println(s"entire schema type = ${c.tpe.toString}")


				JsonSchema_S.array(andyGlowToSkeuoJsonSchema(componentSchema))
			}

			//case JsonSchema_G.map


		}
	}


}
