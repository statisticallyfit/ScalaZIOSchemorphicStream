//package conversionsOfSchemaADTs.avro_json.skeuo_skeuo
//
//
//import cats.data.NonEmptyList
//import higherkindness.droste._
//import higherkindness.droste.data.Fix
////import higherkindness.droste.syntax.all._
//
//import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
//import SchemaAvro_Skeuo._
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import SchemaJson_Skeuo._
//
//import scala.reflect.runtime.universe._
//
//
//
//object Algebra_JsonToAvro {
//
//
//	/**
//	 * Json[Avro] => Avro
//	 *
//	 * @return
//	 */
//	def algebra_JsonToAvro_TYPED[T: TypeTag]: Algebra[SchemaJson_Skeuo, SchemaAvro_Skeuo[T]] = Algebra {
//
//		case ObjectF(properties: List[Property[SchemaAvro_Skeuo[T]]], required: List[String]) ⇒ {
//			properties.head.tpe // TODO is the head type same as type of all other elements in the property list?
//		}
//
//		case IntegerF() ⇒ TInt()
//		case StringF() ⇒ TString()
//		case BooleanF() ⇒ TBoolean()
//		case FloatF() ⇒ TFloat()
//		case DoubleF() ⇒ TDouble()
//		case LongF() ⇒ TLong()
//		case BinaryF() ⇒ ???
//		case ByteF() ⇒ TBytes()
//
//		case DateF() ⇒ TDate()
//
//		// TODO verify this is correct =
//		case DateTimeF() ⇒ TUnion(NonEmptyList(TLong(), List(TTimestampMillis())))
//
//		case PasswordF() ⇒
//
//		case ArrayF(innerItemSchema: SchemaAvro_Skeuo[T]) ⇒ TArray()
//
//		case EnumF(cases: List[String]) ⇒ TEnum(
//			name = "enum",
//			namespace = None,
//			null, // TODO what are the aliases in the json schema?
//			doc = None,
//			symbols = cases
//		)
//
//		case SumF() ⇒
//		case ReferenceF() ⇒
//	}
//
//
//	def algebra_JsonToAvro: Algebra[SchemaJson_Skeuo, SchemaAvro_Skeuo[_]] = Algebra {
//
//		case ObjectF(properties: List[Property[SchemaAvro_Skeuo[_]]], required: List[String]) ⇒ {
//			properties.head.tpe // TODO is the head type same as type of all other elements in the property list?
//		}
//
//		case IntegerF() ⇒ TInt()
//		case StringF() ⇒ TString()
//		case BooleanF() ⇒ TBoolean()
//		case FloatF() ⇒ TFloat()
//		case LongF() ⇒ TLong()
//
//		case ArrayF(innerItemSchema: SchemaAvro_Skeuo[_]) ⇒ TArray()
//
//	}
//}
