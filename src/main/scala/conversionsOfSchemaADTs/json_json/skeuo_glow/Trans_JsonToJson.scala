package conversionsOfSchemaADTs.json_json.skeuo_glow


import com.github.andyglow.json.Value
import higherkindness.droste.Trans
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.openapi.{JsonSchemaF, JsonSchemaF => JsonSchema_S}
import json.Schema.`object`
//import JsonSchema_S._

import json.{Schema ⇒ JsonSchema_G}
//import JsonSchema_G._

//import json.Schema.`object`.{Field => FieldJson}
//import higherkindness.skeuomorph.openapi.JsonSchemaF.{Property => PropertyJson}


import utilMain.utilJson.utilGlowSkeuo.FieldToPropertyConversions._

/**
 *
 */
object Trans_JsonToJson {

	type FieldJson[T] = JsonSchema_G.`object`.Field[T]
	type PropertyJson[T] = JsonSchema_S.Property[T]

	def trans_GlowToSkeuo_InnerSkeuo: Trans[JsonSchema_G, JsonSchema_S, Fix[JsonSchema_S]] = Trans {

		case JsonSchema_G.`integer` => JsonSchema_S.IntegerF()
		case JsonSchema_G.`string` => JsonSchema_S.StringF()

		case JsonSchema_G.`boolean` => JsonSchema_S.BooleanF()


		/*case JsonSchema_G.`number`[Float] ⇒ JsonSchema_S.FloatF()
		case JsonSchema_G.`number`[Long] ⇒ JsonSchema_S.LongF()

		case JsonSchema_G.`number`[Byte] ⇒ JsonSchema_S.ByteF()
		case JsonSchema_G.`number`[Int] ⇒ JsonSchema_S.IntegerF()*/
		//case JsonSchema_G.`number`[Integer] ⇒ JsonSchema_S.IntegerF()



		case JsonSchema_G.array(componentType: Fix[JsonSchema_S], unique) => JsonSchema_S.array(componentType)

		case JsonSchema_G.dictionary(valueType: Fix[JsonSchema_S]) => JsonSchema_S.ObjectMapF(additionalProperties = JsonSchema_S.AdditionalProperties(tpe = valueType))



		case JsonSchema_G.`enum`(tpe: JsonSchema_G[Fix[JsonSchema_S]], values: Set[Value]) => JsonSchema_S.EnumF(cases = values.toList.map(_.toString), name = None)



		case JsonSchema_G.`object`(fields: Set[FieldJson[Fix[JsonSchema_S]]]) => {


			val objF: JsonSchema_S[Fix[JsonSchema_S]] = fields.isEmpty match {

				case true => JsonSchema_S.ObjectF(List(), List() )

				case false => {
					val psRs: List[(PropertyJson[Fix[JsonSchema_S]], RequiredBool)] = fields.toList.map(f => field2Property(f))

					val rs: List[String] = psRs.filter(_._2 == true).unzip._1.map(_.name)

					val ps: List[PropertyJson[Fix[JsonSchema_S]]] = psRs.unzip._1

					JsonSchema_S.ObjectF(properties = ps, required = rs)
				}
			}

			objF
		}



		case JsonSchema_G.`oneof`(subTypes: Set[JsonSchema_G[_]], discriminationField: Option[String]) => {

			//val tt: A = typeTag[A].tpe.typeSymbol.toString.asInstanceOf[A]

			//subTypes.toList.map((g: JsonSchema_G[Fix[JsonSchema_S]]) => )

			//JsonSchema_S.SumF()

			???
			// TODO how to extract the inner items, outside of the JsonSchema_G[_]?
		}

		case JsonSchema_G.`allof`(subTypes: Set[JsonSchema_G[_]]) => ???

		case JsonSchema_G.`not`(tpe: JsonSchema_G[Fix[JsonSchema_S]]) => ???

		case JsonSchema_G.`def`(sig: String, tpe: JsonSchema_G[_]) => ???

		case JsonSchema_G.`const`(value: Value) => ???

		case JsonSchema_G.`ref`(sig: String) => JsonSchema_S.ReferenceF(ref = sig)

		// Logical types
		case JsonSchema_G.`string`/*[Fix[JsonSchema_S]]*/(format: Option[JsonSchema_G.string.Format]) => {

			//type FormatG = JsonSchema_G.`string`.Format

			format.isDefined match {

				case false => JsonSchema_S.StringF()

				case true => format.get match {
					case JsonSchema_G.`string`.Format.`date` => JsonSchema_S.date()
					case JsonSchema_G.`string`.Format.`time` => ??? // HELP
					case JsonSchema_G.`string`.Format.`date-time` => JsonSchema_S.dateTime()

					case JsonSchema_G.`string`.Format.`idn-hostname` => ???
					case JsonSchema_G.`string`.Format.`hostname` => ???
					case JsonSchema_G.`string`.Format.`uri` => ???
					case JsonSchema_G.`string`.Format.`duration` => ???

					case JsonSchema_G.`string`.Format.`email` => ???
					case JsonSchema_G.`string`.Format.`ipv4` => ???
					case JsonSchema_G.`string`.Format.`ipv6` => ???
					case JsonSchema_G.`string`.Format.`uuid` => ???
				}
			}
		}
	}
}
