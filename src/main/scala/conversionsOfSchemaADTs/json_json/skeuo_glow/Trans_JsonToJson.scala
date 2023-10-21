package conversionsOfSchemaADTs.json_json.skeuo_glow




import higherkindness.droste.Trans
import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
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

	def trans[A]: Trans[JsonSchema_G, JsonSchema_S, Fix[JsonSchema_S]] = Trans {

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



		case JsonSchema_G.`object`(fields: Set[FieldJson[Fix[JsonSchema_S]]]) => {



			JsonSchema_S.ObjectF()
		}

		// Logical types
		case JsonSchema_G.`string`/*[Fix[JsonSchema_S]]*/(format: Option[JsonSchema_G.string.Format]) => {

			//type FormatG = JsonSchema_G.`string`.Format

			format.isDefined match {

				case false => JsonSchema_S.StringF()

				case true => format.get match {
					case JsonSchema_G.`string`.Format.`date` => JsonSchema_S.date()
					case JsonSchema_G.`string`.Format.`time` => ??? // HELP
					case JsonSchema_G.`string`.Format.`date-time` => JsonSchema_S.dateTime()
				}
			}
		}
	}
}
