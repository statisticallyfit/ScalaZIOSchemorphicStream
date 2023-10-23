package conversionsOfSchemaADTs.json_json.skeuo_glow.implicitsEmbedProject

import higherkindness.droste.{Algebra, Embed}
import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.openapi.{JsonSchemaF => JsonSchema_S}
import JsonSchema_S._

/**
 *
 */
object embedImplicits {


	implicit def skeuoEmbed_SS: Embed[JsonSchema_S, Fix[JsonSchema_S]] = new Embed[JsonSchema_S, Fix[JsonSchema_S]] {

		def algebra: Algebra[JsonSchema_S, Fix[JsonSchema_S]] = Algebra {

			case IntegerF() ⇒ Fix(IntegerF())
			// String
			case StringF() ⇒ Fix(StringF())
			// Boolean
			case BooleanF() ⇒ Fix(BooleanF())
			// Long
			case LongF() ⇒ Fix(LongF())
			// Float
			case FloatF() ⇒ Fix(FloatF())
			// Double
			case DoubleF() ⇒ Fix(DoubleF())
			// Byte
			case ByteF() ⇒ Fix(ByteF())
			// Array
			case ar@ArrayF(inner: Fix[JsonSchema_S]) ⇒ Fix(ar) // TODO just inner or wrap with TArray?


			// Object with name
			case ob@ObjectNamedF(name: String, props: List[Property[Fix[JsonSchema_S]]], reqs: List[String]) ⇒ {

				println(s"ObjectNamedF: INSIDE EMBED'S ALGEBRA: " +
					s"\nname = $name" +
					s"\nproperties = $props" +
					s"\nrequired = $reqs"
				)

				Fix(ob)

			}

			// Map

			// METHOD 1: using the 'additionalProperties' way of declaring a map
			case ob@ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[Fix[JsonSchema_S]]) ⇒ {

				println(s"ObjectNamedMapF: INSIDE EMBED'S ALGEBRA: " +
					s"\nname = $name" +
					s"\nadditionalProperties = $additionalProperties"
				)

				// TODO what about name now?
				Fix(ob)
			}

			case ob@ObjectMapF(additionalProperties: AdditionalProperties[Fix[JsonSchema_S]]) ⇒ {

				println(s"ObjectMapF: INSIDE EMBED'S ALGEBRA: " +
					//s"\nname = $name" +
					s"\nadditionalProperties = $additionalProperties"
				)

				// TODO what about name now?
				Fix(ob)
			}

			// Record
			case ob@ObjectF(props: List[Property[Fix[AvroSchema_S]]],
			reqs: List[String]) ⇒ {

				println(s"ObjectF: INSIDE EMBED'S ALGEBRA: " +
					s"\nproperties = $props" +
					s"\nrequired = $reqs"
				)


				Fix(ob)
			}

			case EnumF(cases: List[String], name: Option[String]) => Fix(EnumF(cases, name))
		}

	}
}
