//package conversionsOfSchemaADTs.avro_json.skeuo_skeuo
//
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
///**
// * Algebra type is:
// * Avro[Json[T]] => Json[T]
// *
// * @return
// */
//object Algebra_AvroToJson {
//
//	//def algebra2[T: TypeTag]: Algebra[SchemaAvro_Skeuo[_], SchemaJson_Skeuo[T]] = ???
//
//	def algebra_AvroToJson_TYPED[T: TypeTag]: Algebra[SchemaAvro_Skeuo, SchemaJson_Skeuo[T]] = Algebra {
//
//		case TNull() ⇒ StringF[T]() // TODO ??? no null in json-skeuo
//		case TInt() ⇒ IntegerF[T]()
//		case TBoolean() ⇒ BooleanF[T]()
//		case TString() ⇒ StringF[T]()
//		case TFloat() ⇒ FloatF[T]()
//		case TLong() ⇒ LongF[T]()
//		case TDouble() ⇒ DoubleF[T]()
//		case TBytes() ⇒ ByteF[T]()
//
//		case TRecord(name, namespace, aliases, doc, fields) ⇒ ObjectF[T]()
//		case TNamedType(namespace, name) ⇒ ObjectF[T]()
//
//		case TMap(innerSchema: SchemaJson_Skeuo[T]) ⇒ ObjectF[T]()
//		case TArray(innerSchema: SchemaJson_Skeuo[T]) ⇒ ArrayF[T](innerSchema)
//	}
//
//
//	def algebra_AvroToJson: Algebra[SchemaAvro_Skeuo, SchemaJson_Skeuo[_]] = Algebra {
//
//		case TNull() ⇒ SchemaJson_Skeuo
//
//		case TBoolean() ⇒ BooleanF()
//		case TString() ⇒ StringF()
//
//		case TFloat() ⇒ FloatF()
//
//	}
//}
