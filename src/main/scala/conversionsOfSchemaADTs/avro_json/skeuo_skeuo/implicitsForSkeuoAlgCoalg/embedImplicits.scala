package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg



// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)

import io.circe._
import io.circe.Decoder
import io.circe.Decoder.{Result, resultInstance}
import io.circe.{Json => JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import cats.syntax.all._
//import cats.implicits._
//import cats.syntax._


import higherkindness.droste._
import higherkindness.droste.data._
//import higherkindness.droste.syntax.all._
import higherkindness.droste.syntax.embed._
import higherkindness.skeuomorph.openapi.schema._

import scala.language.postfixOps
import scala.language.higherKinds
//import scala.language.implicitConversions


import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}


import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._


/**
 *
 */
object embedImplicits {


	import JsonSchema_S._
	import AvroSchema_S._


	implicit def skeuoEmbed_AA: Embed[AvroSchema_S, Fix[AvroSchema_S]] = new Embed[AvroSchema_S, Fix[AvroSchema_S]] {

		def algebra: Algebra[AvroSchema_S, Fix[AvroSchema_S]] = Algebra {

			case TNull() => Fix(TNull())
			case TInt() => Fix(TInt())
			case TString() => Fix(TString())
			case TBoolean() => Fix(TBoolean())
			case TLong() => Fix(TLong())
			case TFloat() => Fix(TFloat())
			case TDouble() => Fix(TDouble())
			case TBytes() => Fix(TBytes())

			case tarray @ TArray(inner: Fix[AvroSchema_S]) => Fix(TArray(inner))

			case tmap@TMap(values: Fix[AvroSchema_S]) => Fix(tmap)

			case trecord@TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fix[AvroSchema_S]]]) => Fix(trecord)

			case te@TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) => Fix(te)
		}
	}


	implicit def skeuoEmbed_JJ: Embed[JsonSchema_S, Fix[JsonSchema_S]] = new Embed[JsonSchema_S, Fix[JsonSchema_S]] {

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
			case ar@ArrayF(inner: Fix[JsonSchema_S]) ⇒ Fix(ArrayF(inner)) // TODO just inner or wrap with TArray?


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


	implicit def skeuoEmbed_AJ: Embed[AvroSchema_S, Fix[JsonSchema_S]] = new Embed[AvroSchema_S, Fix[JsonSchema_S]] {

		// AvroSchema_S [ Fix[JsonSchema_S]] => Fix[JsonSchema_S]
		def algebra: Algebra[AvroSchema_S, Fix[JsonSchema_S]] = Algebra {

			case TNull() => Fix(ObjectF(List(), List()))

			case TInt() => Fix(IntegerF())
			case TString() => Fix(StringF())
			case TBoolean() => Fix(BooleanF())
			case TLong() => Fix(LongF())
			case TFloat() => Fix(FloatF())
			case TDouble() => Fix(DoubleF())
			case TBytes() => Fix(ByteF())
			case TArray(inner: Fix[JsonSchema_S]) => Fix(ArrayF(inner))


			case TMap(inner: Fix[JsonSchema_S]) => {

				Fix(ObjectMapF(additionalProperties = AdditionalProperties[Fix[JsonSchema_S]](inner)))
			}

			case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fix[JsonSchema_S]]]) => {

				Fix(ObjectNamedF(name = name, properties = fields.map(f => field2Property(f)), required = List()))
			}


			case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) => {

				Fix(EnumF(cases = symbols, name = Some(name)))
			}
		}
	}


	/**
	 * Trans[F[_], G[_], A]
	 *
	 * algebra, Embed is: Embed[G[_], A]
	 * coalgebra, Project is: Project[F[_], A]
	 *
	 * @return
	 */

	implicit def skeuoEmbed_JA: Embed[JsonSchema_S, Fix[AvroSchema_S]] = new Embed[JsonSchema_S, Fix[AvroSchema_S]] {

		// JsonSchema_S [ Fix[AvroSchema_S]] => Fix[AvroSchema_S]
		def algebra: Algebra[JsonSchema_S, Fix[AvroSchema_S]] = Algebra {
			// Null
			//case ObjectF(List(), List()) ⇒ Fix(TNull())
			// Integer
			case IntegerF() ⇒ Fix(TInt())
			// String
			case StringF() ⇒ Fix(TString())
			// Boolean
			case BooleanF() ⇒ Fix(TBoolean())
			// Long
			case LongF() ⇒ Fix(TLong())
			// Float
			case FloatF() ⇒ Fix(TFloat())
			// Double
			case DoubleF() ⇒ Fix(TDouble())
			// Byte
			case ByteF() ⇒ Fix(TBytes())
			// Array
			case ar@ArrayF(inner: Fix[AvroSchema_S]) ⇒ Fix(TArray(inner)) // NOTE: th inner is just the inner type of the array so must create the avrof array with the inner type passed on into it, cannot just return inner alone.


			// Object with name
			case ObjectNamedF(name: String, props: List[Property[Fix[AvroSchema_S]]],
			reqs: List[String]) ⇒ {

				println(s"ObjectNamedF: INSIDE EMBED'S ALGEBRA: " +
					s"\nname = $name" +
					s"\nproperties = $props" +
					s"\nrequired = $reqs"
				)

				val result: Fix[AvroSchema_S] = if (props.isEmpty && reqs.isEmpty) {
					Fix(TNull())
				} else {
					Fix(
						TRecord(name = name, namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					)
				}
				result
			}
			// Record
			case ObjectF(props: List[Property[Fix[AvroSchema_S]]],
			reqs: List[String]) ⇒ {

				println(s"ObjectF: INSIDE EMBED'S ALGEBRA: " +
					s"\nproperties = $props" +
					s"\nrequired = $reqs"
				)


				val result: Fix[AvroSchema_S] = if (props.isEmpty && reqs.isEmpty) {
					Fix(TNull())
				}
				// METHOD 2: the 'properties' / 'values' way
				/*else if(props.length == 1 && props.head.name == "values"){
					Fix(TMap(props.head.tpe))
				}*/
				else {
					Fix(
						TRecord(name = /*null*/ "record", namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					)
				}
				result
			}

			// Map

			// METHOD 1: using the 'additionalProperties' way of declaring a map
			case ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[Fix[AvroSchema_S]]) ⇒ {

				println(s"ObjectNamedMapF: INSIDE EMBED'S ALGEBRA: " +
					s"\nname = $name" +
					s"\nadditionalProperties = $additionalProperties"
				)

				// TODO what about name now?
				Fix(TMap(additionalProperties.tpe))
			}

			case ObjectMapF(additionalProperties: AdditionalProperties[Fix[AvroSchema_S]]) ⇒ {

				println(s"ObjectMapF: INSIDE EMBED'S ALGEBRA: " +
					//s"\nname = $name" +
					s"\nadditionalProperties = $additionalProperties"
				)

				// TODO what about name now?
				Fix(TMap(additionalProperties.tpe))
			}



			// TODO Make named enum here in json schema?
			case EnumF(cases: List[String], name: Option[String]) => Fix(TEnum(name = name.getOrElse("NO_NAME"), namespace = None, aliases = List(), doc = None, symbols = cases))
		}
	}

}