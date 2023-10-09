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
object projectImplicits {


	import JsonSchema_S._
	import AvroSchema_S._


	/**
	 * A => F[A]
	 * Fix[AvroF] => AvroF[Fix[AvroF]]
	 *
	 * @return
	 */
	implicit def skeuoProject_AA: Project[AvroSchema_S, Fix[AvroSchema_S]] = new Project[AvroSchema_S, Fix[AvroSchema_S]] {

		def coalgebra: Coalgebra[AvroSchema_S, Fix[AvroSchema_S]] = Coalgebra {
			case Fix(TNull()) => TNull()
			case Fix(TInt()) => TInt()
			case entire@Fix(TString()) => TString()
			case Fix(TBoolean()) => TBoolean()
			case Fix(TFloat()) => TFloat()
			case Fix(TLong()) => TLong()
			case Fix(TDouble()) => TDouble()
			case Fix(TBytes()) => TBytes()

			// NOTE: return 'ta' only, don't wrap again in another layer or else stackoverflow error
			case entire@Fix(TArray(inner: Fix[AvroSchema_S])) => TArray(entire) //TArray(inner)

			case Fix(TMap(values: Fix[AvroSchema_S])) => TMap(values)


			case Fix(trecord@TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[Property[Fix[AvroSchema_S]]])) => {

				trecord
			}

			case entire@Fix(tenum@TEnum(_, _, _, _, _)) => tenum
		}
	}
	/*implicit def basis_AA: Basis[AvroSchema_S, Fix[AvroSchema_S]] = new Basis[AvroSchema_S, Fix[AvroSchema_S]] {
		def algebra: Algebra[AvroSchema_S, Fix[AvroSchema_S]] = Algebra {

			case TInt() => Fix(TInt())

			case TArray(inner: Fix[AvroSchema_S]) => inner
		}

		def coalgebra: Coalgebra[AvroSchema_S, Fix[AvroSchema_S]] = Coalgebra {
			case Fix(TInt()) => TInt()
			case Fix(ta @ TArray(inner: Fix[AvroSchema_S])) => ta
		}
	}*/


	/**
	 * A => F[A]
	 * Fix[JsonF] => JsonF[Fix[JsonF]]
	 *
	 * @return
	 */
	implicit def skeuoProject_JJ: Project[JsonSchema_S, Fix[JsonSchema_S]] = new Project[JsonSchema_S, Fix[JsonSchema_S]] {

		def coalgebra: Coalgebra[JsonSchema_S, Fix[JsonSchema_S]] = Coalgebra {

			case Fix(IntegerF()) => IntegerF()
			case Fix(StringF()) => StringF()
			case Fix(BooleanF()) => BooleanF()
			case Fix(LongF()) => LongF()
			case Fix(FloatF()) => FloatF()
			case Fix(DoubleF()) => DoubleF()
			case Fix(ByteF()) => ByteF()
			case Fix(BinaryF()) => BinaryF()

			// NOTE: return 'ar' only, don't wrap again in another layer or else stackoverflow error
			case entire@Fix(ar@ArrayF(inner: Fix[JsonSchema_S])) => ArrayF(entire)

			case Fix(ob@ObjectMapF(addProps: AdditionalProperties[Fix[JsonSchema_S]])) => ob

			case Fix(ob@ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[Fix[JsonSchema_S]])) => ob

			case Fix(ob@ObjectNamedF(name, properties, required)) => ob

			case Fix(ob@ObjectF(properties, required)) => ob


			case Fix(EnumF(cases: List[String], name: Option[String])) => EnumF(cases, name)
		}
	}


	/**
	 * Coalgebra[F[_], A]
	 * A => F[A]
	 * Fix[AvroSchema_S] ===> JsonSchema_S[ Fix[AvroSchema_S] ]
	 *
	 * @return
	 */
	implicit def skeuoProject_JA: Project[JsonSchema_S, Fix[AvroSchema_S]] = new Project[JsonSchema_S, Fix[AvroSchema_S]] {

		def coalgebra: Coalgebra[JsonSchema_S, Fix[AvroSchema_S]] = Coalgebra {

			case Fix(TNull()) => ObjectF(List(), List())
			case Fix(TInt()) => IntegerF()
			case Fix(TString()) => StringF()
			case Fix(TBoolean()) => BooleanF()
			case Fix(TDouble()) => DoubleF()
			case Fix(TFloat()) => FloatF()
			case Fix(TLong()) => LongF()
			case Fix(TBytes()) => ByteF()
			// ?? --> BinaryF?


			// HERE overflow 1
			case entire@Fix(TArray(inner: Fix[AvroSchema_S])) => ArrayF(entire)
			//ArrayF(inner) //ArrayF(inner) // TODO need: ArrayF(inner) or ArrayF(Fix(Tarray(inner))?

			case Fix(TMap(inner: Fix[AvroSchema_S])) => ObjectMapF(additionalProperties = AdditionalProperties[Fix[AvroSchema_S]](tpe = inner))

			case Fix(TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fix[AvroSchema_S]]])) => {

				ObjectNamedF(name = name, properties = fields.map(f => field2Property(f)), required = List())
			}
			case Fix(TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String])) => EnumF(cases = symbols)
		}
	}


	/**
	 * Trans[F[_], G[_], A]
	 *
	 * algebra, Embed is: Embed[G[_], A]
	 * coalgebra, Project is: Project[F[_], A]
	 *
	 * A => F[A]
	 * Fix[JsonF] => AvroF[Fix[JsonF]]
	 *
	 * @return
	 */

	/**
	 * Coalgebra means:
	 * Fix[JsonSchema_S] => AvroSchema_S[ Fix[JsonSchema_S] ]
	 *
	 * @return
	 */
	implicit def skeuoProject_AJ: Project[AvroSchema_S, Fix[JsonSchema_S]] = new Project[AvroSchema_S, Fix[JsonSchema_S]] {

		def coalgebra: Coalgebra[AvroSchema_S, Fix[JsonSchema_S]] = Coalgebra {

			case Fix(IntegerF()) ⇒ TInt()
			case Fix(StringF()) ⇒ TString()
			case Fix(BooleanF()) ⇒ TBoolean()
			case Fix(LongF()) ⇒ TLong()
			case Fix(DoubleF()) ⇒ TDouble()
			case Fix(FloatF()) ⇒ TFloat()
			case Fix(ByteF()) ⇒ TBytes()

			case Fix(ArrayF(inner: Fix[JsonSchema_S])) ⇒ TArray(inner)

			case Fix(ObjectMapF(addProps: AdditionalProperties[Fix[JsonSchema_S]])) => TMap(addProps.tpe)

			case Fix(ObjectNamedMapF(name: String, addProps: AdditionalProperties[Fix[JsonSchema_S]])) => TMap(addProps.tpe)

			// Map
			case Fix(ObjectMapF(additionalProperties: AdditionalProperties[Fix[JsonSchema_S]])) ⇒ {

				println(s"ObjectNamedMapF: INSIDE PROJECT'S COALGEBRA: " +
					s"\naddedproperties = $additionalProperties"
				)

				TMap(additionalProperties.tpe)
			}

			// Object Name
			case Fix(ObjectNamedF(name: String, props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒ {

				println(s"ObjectNamedF: INSIDE PROJECT'S COALGEBRA: " +
					s"\nname = $name" +
					s"\nproperties = $props" +
					s"\nrequired = $reqs"
				)

				val result: AvroSchema_S[Fix[JsonSchema_S]] = if (props.isEmpty && reqs.isEmpty) {
					TNull()
				} else {
					TRecord(name = name, namespace = None, aliases = List(), doc = None,
						fields = props.map(p ⇒ property2Field(p))
					)
				}
				result
			}

			// Record
			case Fix(ObjectF(props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒ {


				println(s"ObjectF: INSIDE PROJECT'S COALGEBRA: " +
					s"\nproperties = $props" +
					s"\nrequired = $reqs")


				val result: AvroSchema_S[Fix[JsonSchema_S]] = if (props.isEmpty && reqs.isEmpty) {
					TNull()
				}
				// METHOD 2: the 'properties' / 'values' way
				/*else if (props.length == 1 && props.head.name == "values") {
					TMap(props.head.tpe)
				}*/
				else {

					TRecord(name = /*null*/ "TODO_OBJ_NAME", namespace = None, aliases = List(), doc = None,
						fields = props.map(p ⇒ property2Field(p))
					)
				}
				result
			}

			// TODO m ake a jsonschema enum to be named?
			case Fix(EnumF(cases: List[String], name: Option[String])) => TEnum(name = name.getOrElse("NO_NAME"), namespace = None, aliases = List(), doc = None, symbols = cases)
		}
	}

}