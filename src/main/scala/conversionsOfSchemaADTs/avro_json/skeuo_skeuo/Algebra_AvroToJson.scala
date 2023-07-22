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
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import SchemaJson_Skeuo._
//
//import SchemaAvro_Skeuo.{Field ⇒ FieldAvro}
//import SchemaJson_Skeuo.{Property ⇒ PropertyJson}
//
//import scala.reflect.runtime.universe._
//
//
//import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._
//
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
//		// NOTE: in the Avro file here the 'symbol' has type 'null' and in json the 'symbol' has type 'object' with  required = [], and properties = {}
//
//		case TNull() ⇒ ObjectF[T](
//			properties = List[Property[T]](/*Property(name = "", tpe = null.asInstanceOf[T])*/),
//			required = List[String]()
//		)
//		case TInt() ⇒ IntegerF[T]()
//
//		case TBoolean() ⇒ BooleanF[T]()
//
//		case TString() ⇒ StringF[T]()
//
//		case TFloat() ⇒ FloatF[T]()
//
//		case TLong() ⇒ LongF[T]()
//
//		case TDouble() ⇒ DoubleF[T]()
//
//		case TBytes() ⇒ ByteF[T]()
//
//
//		// TODO - where to put 'name', 'namespace' ... from this TRecord itself?
//		// HELP: giving up on using simple Algebra or Coalgebra when converting from F[A] => G[A] --- thinking I need the Trans {} type from droste --- https://github.com/higherkindness/skeuomorph/blob/cc739d3dcdc07ead250461b6ecc6fa4daf2ba988/src/main/scala/higherkindness/skeuomorph/mu/Transform.scala#L59
//
//		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[SchemaJson_Skeuo[T]]]) ⇒ {
//
//			//val res: List[Property[T]] = fields.map(field2Property(_))
//
//			val properties: List[PropertyJson[SchemaJson_Skeuo[T]]] = fields.map(f ⇒ PropertyJson[SchemaJson_Skeuo[T]](
//				name = f.name,
//				tpe = f.tpe // HELP cannot access the T type -- still SkeuoJson
//			))
//
//			// HELP ... so this needs to be ObjectF[T] not this ...
//			val objF: ObjectF[SchemaJson_Skeuo[T]] = ObjectF(
//				properties = properties,
//				required = fields.map(f ⇒ f.name)
//			)
//
//			objF
//		}
//
//
//
//		case TArray(innerSchema: SchemaJson_Skeuo[T]) ⇒ ArrayF(innerSchema)
//
//		/*case TNamedType(namespace, name) ⇒ ObjectF[T]()
//
//		case TMap(innerSchema: SchemaJson_Skeuo[T]) ⇒ ObjectF[T]()*/
//
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
