//package conversionsOfSchemaADTs.avro_json.skeuo_skeuo
//
//
//
//import cats.data.NonEmptyList
//import higherkindness.droste._
//import higherkindness.droste.data.Fix
////import higherkindness.droste.syntax.all._
//
//
//import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
//import SchemaAvro_Skeuo._
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import SchemaJson_Skeuo._
//
//import scala.reflect.runtime.universe._
//
//import SchemaAvro_Skeuo.{Field ⇒ FieldAvro}
//import SchemaJson_Skeuo.{Property ⇒ PropertyJson}
//
//import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._
//
///**
// * Coalgebra[F[_], A]
// *
// * A => F[A]
// *
// * Avro => Json[Avro]
// *
// * @return
// */
//object Coalgebra_AvroToJson {
//
//	def coalgebra_AvroToJson_TYPED[T: TypeTag]: Coalgebra[SchemaJson_Skeuo, SchemaAvro_Skeuo[T]] = Coalgebra {
//
//		case TNull() ⇒ StringF() // TODO is this right?
//		case TInt() ⇒ IntegerF()
//		case TString() ⇒ StringF()
//		case TBoolean() ⇒ BooleanF()
//		case TFloat() ⇒ FloatF()
//		case TLong() ⇒ LongF()
//		case TDouble() ⇒ DoubleF()
//		case TBytes() ⇒ ByteF()
//
//		case TArray(innerSchema: T) ⇒ ArrayF(TArray(innerSchema))
//
//		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[T]]) ⇒ {
//
//			//val res: List[Property[T]] = fields.map(field2Property(_))
//			//fields.map((f: FieldAvro[T]) ⇒ f.tpe) // TODO how to lift the f.tpe into another JSonSchemaF[T]????
//
//			val res: ObjectF[SchemaJson_Skeuo[T]] = ObjectF(
//				properties = fields.map(field2Property(_)),
//				required = fields.map(f ⇒ f.name)
//			)
//			res
//		}
//		/**
//		 * TODO
//		 * 1) what is 'required'? - somehow related to the key / values of the Map?
//		 * 2) what is 'name' for Property? is it the name of the avro schema (TMap)?
//		 */
////		case tmap@TMap(innerSchema) => {
////
////			type A = SchemaAvro_Skeuo[T]
////
////			// HELP: type mismatch: Found JsonSchemaF[AvroF[_]], needed instead JsonSchemaF[AvroF]
////			/*val ob: SchemaJson_Skeuo[SchemaAvro_Skeuo[_]] = ObjectF[SchemaAvro_Skeuo[_]](
////					properties = List[Property[SchemaAvro_Skeuo[_]]](Property[SchemaAvro_Skeuo[_]](name = "map", tpe = tmap)),
////					required = List[String]("")
////				)*/
////
////			// HELP: type mismatch: Found JsonSchemaF[AvroF[T]], needed instead JsonSchemaF[AvroF[T]]
////			/*val ob: SchemaJson_Skeuo[SchemaAvro_Skeuo[T]] = ObjectF[SchemaAvro_Skeuo[T]](
////					properties = List[Property[SchemaAvro_Skeuo[T]]](Property[SchemaAvro_Skeuo[T]](name = "map", tpe = tmap)),
////					required = List[String]("")
////				)*/
////
////			// HELP: found ObjectF[AvroF[_]], required JsonSchemaF[AvroF]
////			/*ObjectF[SchemaAvro_Skeuo[_]](
////				properties = List[Property[SchemaAvro_Skeuo[_]]](Property[SchemaAvro_Skeuo[_]](name = "map", tpe = tmap)),
////				required = List[String]("")
////			)*/
////
////		}
//
//
//		/**
//		 * TODO
//		 * 1) how to tell what goes in the 'required' list - the parameters of the TNamedType or their strings? ("namespace", "name" ...)?
//		 * 2) what is the 'name' in Property?
//		 * 3) what is the 'tpe' in Property?
//		 */
//
//		/*case record@TNamedType(namespace: String, name: String) ⇒ {
//
//			type A = SchemaAvro_Skeuo[T]
//
//			ObjectF(
//				properties = List[Property[SchemaJson_Skeuo[T]]](Property(name = "named type", tpe = record)),
//				required = List(namespace, name)
//			)
//
//		}*/
//
//
//	}
//
//
//	def coalgebra_AvroToJson: Coalgebra[SchemaJson_Skeuo, SchemaAvro_Skeuo[_]] = Coalgebra {
//		???
//	}
//}
