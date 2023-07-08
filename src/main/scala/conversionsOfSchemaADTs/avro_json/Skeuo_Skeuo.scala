package conversionsOfSchemaADTs.avro_json




import cats.data.NonEmptyList

import higherkindness.droste._
import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._


import scala.jdk.CollectionConverters._

import scala.reflect.runtime.universe._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

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
	 * Avro => Avro[Json]
	 * @return
	 */
	def coalgebra_AvroToJson: Coalgebra[SchemaJson_Skeuo, SchemaAvro_Skeuo] = ???
	
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
			.
		case TBoolean() ⇒ BooleanF()
		case TString() ⇒ StringF()
		
	}
	
	/**
	 * Json[Avro] => Avro
	 * @return
	 */
	/*def algebra_JsonToAvro_TYPEDVERSION[T: TypeTag]: Algebra[SchemaJson_Skeuo, SchemaAvro_Skeuo] = Algebra {
		
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
		
		// TODO how to convert date time to avro?
		case DateTimeF() ⇒ TUnion(cats.data.NonEmptyList(TLong(), List(TTimestampMillis())))
		
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
		
	}*/
	
	
	
	def skeuoToApacheAvroSchema: Fix[SchemaAvro_Skeuo] ⇒ SchemaAvro_Apache = scheme.cata(algebra_JsonToAvro).apply(_)
	
	
	def apacheToSkeuoAvroSchema: SchemaAvro_Apache ⇒ Fix[SchemaAvro_Skeuo] = scheme.ana(coalgebra_AvroToJson).apply(_)
	
	
	def roundTrip_ApacheAvroToApacheAvro: SchemaAvro_Apache ⇒ SchemaAvro_Apache = skeuoToApacheAvroSchema compose apacheToSkeuoAvroSchema
	
	def roundTrip_SkeuoAvroToSkeuoAvro: Fix[SchemaAvro_Skeuo] ⇒ Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema compose skeuoToApacheAvroSchema
	
}
