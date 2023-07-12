package conversionsOfSchemaADTs.avro_json




import cats.data.NonEmptyList


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.openapi.JsonSchemaF
//import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._


import scala.jdk.CollectionConverters._

import scala.reflect.runtime.universe._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
/**
 *
 */
object Skeuo_Skeuo {


	/**
	 * Coalgebra type is:
	 * Json => Avro[Json]
	 *
	 * @return
	 */
	def coalgebra_JsonToAvro: Coalgebra[SchemaAvro_Skeuo, SchemaJson_Skeuo] = ???

	/**
	 * Coalgebra[F[_], A]
	 * A => F[A]
	 * Avro => Json[Avro]
	 * @return
	 */
	def coalgebra_AvroToJson_TYPED[T: TypeTag]: Coalgebra[SchemaJson_Skeuo, SchemaAvro_Skeuo] = Coalgebra {

		case TNull() ⇒ StringF() // TODO is this right?
		case TInt() ⇒ IntegerF()
		case TString() ⇒ StringF()
		case TBoolean() ⇒ BooleanF()
		case TFloat() ⇒ FloatF()
		case TLong() ⇒ LongF()
		case TDouble() ⇒ DoubleF()
		case TBytes() ⇒ ByteF()

		case TArray(innerSchema: T) ⇒ ArrayF(TArray(innerSchema))

		/**
		 * TODO
		 * 1) what is 'required'? - somehow related to the key / values of the Map?
		 * 2) what is 'name' for Property? is it the name of the avro schema (TMap)?
		 */
		case tmap @ TMap(innerSchema) => {

			type A = SchemaAvro_Skeuo[T]

			// HELP: type mismatch: Found JsonSchemaF[AvroF[_]], needed instead JsonSchemaF[AvroF]
			/*val ob: SchemaJson_Skeuo[SchemaAvro_Skeuo[_]] = ObjectF[SchemaAvro_Skeuo[_]](
					properties = List[Property[SchemaAvro_Skeuo[_]]](Property[SchemaAvro_Skeuo[_]](name = "map", tpe = tmap)),
					required = List[String]("")
				)*/

			// HELP: type mismatch: Found JsonSchemaF[AvroF[T]], needed instead JsonSchemaF[AvroF[T]]
			/*val ob: SchemaJson_Skeuo[SchemaAvro_Skeuo[T]] = ObjectF[SchemaAvro_Skeuo[T]](
					properties = List[Property[SchemaAvro_Skeuo[T]]](Property[SchemaAvro_Skeuo[T]](name = "map", tpe = tmap)),
					required = List[String]("")
				)*/

			// HELP: found ObjectF[AvroF[_]], required JsonSchemaF[AvroF]
			/*ObjectF[SchemaAvro_Skeuo[_]](
				properties = List[Property[SchemaAvro_Skeuo[_]]](Property[SchemaAvro_Skeuo[_]](name = "map", tpe = tmap)),
				required = List[String]("")
			)*/

		}



		/**
		 * TODO
		 * 	1) how to tell what goes in the 'required' list - the parameters of the TNamedType or their strings? ("namespace", "name" ...)?
		 * 	2) what is the 'name' in Property?
		 * 	3) what is the 'tpe' in Property?
		 */

		case record @ TNamedType(namespace: String, name: String) ⇒ {

			type A = SchemaAvro_Skeuo[T]

			ObjectF(
				properties = List[Property[SchemaJson_Skeuo[T]]](Property(name = "named type", tpe = record)),
				required = List(namespace, name)
			)

		}


	}

	/**
	 * Algebra type is:
	 * Avro[Json] => Json
	 *
	 * @return
	 */
	def algebra_AvroToJson_TYPED[T: TypeTag]: Algebra[SchemaAvro_Skeuo, SchemaJson_Skeuo] = Algebra {

		case TNull() ⇒ StringF() // TODO ??? no null in json-skeuo
		case TInt() ⇒ IntegerF()
		case TBoolean() ⇒ BooleanF()
		case TString() ⇒ StringF()
		case TFloat() ⇒ FloatF()
		case TLong() ⇒ LongF()
		case TDouble() ⇒ DoubleF()
		case TBytes() ⇒ ByteF()

		case TRecord(name, namespace, aliases, doc, fields) ⇒ ObjectF()
		case TNamedType(namespace, name) ⇒ ObjectF()

		case TMap(innerSchema: SchemaJson_Skeuo[T]) ⇒ ObjectF()
		case TArray(innerSchema: SchemaJson_Skeuo[T]) ⇒ ArrayF(innerSchema)
	}

	def algebra_AvroToJson: Algebra[SchemaAvro_Skeuo, SchemaJson_Skeuo] = Algebra {

		case TNull() ⇒ SchemaJson_Skeuo

		case TBoolean() ⇒ BooleanF()
		case TString() ⇒ StringF()

		case TFloat() ⇒ FloatF()

	}

	/**
	 * Json[Avro] => Avro
	 * @return
	 */
	def algebra_JsonToAvro_TYPEDVERSION[T: TypeTag]: Algebra[SchemaJson_Skeuo, SchemaAvro_Skeuo] = Algebra {

		case ObjectF(properties: List[Property[SchemaAvro_Skeuo[T]]], required: List[String]) ⇒ {
			properties.head.tpe // TODO is the head type same as type of all other elements in the property list?
		}

		case IntegerF() ⇒ TInt()
		case StringF() ⇒ TString()
		case BooleanF() ⇒ TBoolean()
		case FloatF() ⇒ TFloat()
		case DoubleF() ⇒ TDouble()
		case LongF() ⇒ TLong()
		case BinaryF() ⇒ ???
		case ByteF() ⇒ TBytes()

		case DateF() ⇒ TDate()

		// TODO verify this is correct =
		case DateTimeF() ⇒ TUnion(NonEmptyList(TLong(), List(TTimestampMillis())))

		case PasswordF() ⇒

		case ArrayF(innerItemSchema: SchemaAvro_Skeuo[T]) ⇒ TArray()

		case EnumF() ⇒
		case SumF() ⇒
		case ReferenceF() ⇒
	}


	def algebra_JsonToAvro: Algebra[SchemaJson_Skeuo, SchemaAvro_Skeuo] = Algebra {

		case ObjectF(properties: List[Property[SchemaAvro_Skeuo[_]]], required: List[String]) ⇒ {
			properties.head.tpe // TODO is the head type same as type of all other elements in the property list?
		}

		case IntegerF() ⇒ TInt()
		case StringF() ⇒ TString()
		case BooleanF() ⇒ TBoolean()
		case FloatF() ⇒ TFloat()
		case LongF() ⇒ TLong()

		case ArrayF(innerItemSchema: SchemaAvro_Skeuo[_]) ⇒ TArray()

	}


	
	/// TODO: try using JsonSchemaF.Fixed instead of just JsonSchemaF[T]
	// ---- JFixed => AvroF[JFixed]
	// ---- AvroF[JFixed] => JFixed
	
	/**
	 * Coalgebra[SchemaAvro, SchemaJsonFixed]
	 * JsonFixed => Avro[JsonFixed]
	 *
	 * @return
	 */
	def coalgebra_JsonFixedToAvro: Coalgebra[SchemaAvro_Skeuo, SchemaJson_Skeuo.Fixed] =
		Coalgebra {
			import SchemaJson_Skeuo.Fixed
			
			// TODO which json maps to TNull() avro?
			//case _ ⇒ TNull()
			
			
			case Fixed.integer() ⇒ TInt()
			case Fixed.long() ⇒ TLong()
			case Fixed.float() ⇒ TFloat()
			case Fixed.double() ⇒ TDouble()
			case Fixed.string() ⇒ TString()
			case Fixed.byte() ⇒ TBytes() // TODO is json bytes meaning avro bytes UNION with avro decimal?? https://hyp.is/pHQnBh_6Ee6j8yvgHD5x9g/avro.apache.org/docs/1.11.1/specification/
			case Fixed.binary() ⇒ ??? // TODO
			case Fixed.boolean() ⇒ TBoolean()
			
			/**
			 * TODO verify: json date to avro date
			 * 	1) https://hyp.is/wJznXh_3Ee6nebPz2wEyyw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			 * 	2) if the TDate() (skeuo) contains the logical type info (int, date) like in apache-avro schema or if you have to add TInt() next to Date() in this List as well???? (implications: then TDate() is not same as apache-avro schema even though it gets mapped that way in MY conversion function)
			 */
			
			case Fixed.date() ⇒ TUnion(NonEmptyList(TNull(), List(TString(), TDate())))
			
			
			/**
			 * TODO where is the Time in json schema?
			 * https://hyp.is/nK88eB_5Ee6b-mub_jo24A/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			 */
			
			/**
			 * TODO verify: Json date time -> avro: Union (long+ timestampmillis, null, string)
			 * 	1) https://hyp.is/PUYlCB_5Ee6kOOPNKtp-zQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			 * 	2) check if must include Long with Timestampmillis in AvroF or if the Long is already included in the Timestampmillis (like it is in apache avro)
			 * 	3) if must include the TLong with TTimestampmillis then how to do it - with the list or with another UNion?
			 */
			
			case Fixed.dateTime() ⇒ TUnion(NonEmptyList(TNull(), List(TString(), /*TLong(),*/ TTimestampMillis())))
			
			
			case Fixed.password() ⇒ ??? // TODO what is password to avro?
			
			case Fixed.array(_) ⇒
			case fixed1: SchemaJson_Skeuo.Fixed  ⇒ Fixed.array(value = fixed1) ⇒ TArray(fixed1)
			
			/*def `object`(properties: List[(String, JsonSchemaF.Fixed)], required: List[String]): JsonSchemaF.Fixed =
				Fix(JsonSchemaF.`object`(properties.map((JsonSchemaF.Property.apply[JsonSchemaF.Fixed] _).tupled), required))
			
			def array(value: JsonSchemaF.Fixed): JsonSchemaF.Fixed = Fix(JsonSchemaF.array(value))
			
			def `enum`(value: List[String]): JsonSchemaF.Fixed = Fix(JsonSchemaF.`enum`(value))
			
			def sum[A](value: List[JsonSchemaF.Fixed]): JsonSchemaF.Fixed = Fix(JsonSchemaF.sum(value))
			
			def reference(value: String): JsonSchemaF.Fixed = Fix(JsonSchemaF.reference(value))*/
		}
	}
	
	
	/// -----------------------------
	

	def skeuoToApacheAvroSchema: Fix[SchemaAvro_Skeuo] ⇒ SchemaAvro_Apache = scheme.cata(algebra_JsonToAvro).apply(_)


	def apacheToSkeuoAvroSchema: SchemaAvro_Apache ⇒ Fix[SchemaAvro_Skeuo] = scheme.ana(coalgebra_AvroToJson).apply(_)


	def roundTrip_ApacheAvroToApacheAvro: SchemaAvro_Apache ⇒ SchemaAvro_Apache = skeuoToApacheAvroSchema compose apacheToSkeuoAvroSchema

	def roundTrip_SkeuoAvroToSkeuoAvro: Fix[SchemaAvro_Skeuo] ⇒ Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema compose skeuoToApacheAvroSchema

}
