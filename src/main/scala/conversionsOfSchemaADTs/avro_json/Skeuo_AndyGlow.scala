//package conversionsOfSchemaADTs.avro_json
//
//
//import higherkindness.droste._
//import higherkindness.droste.data.Fix
////import higherkindness.droste.syntax.all._
//
//import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
//import SchemaAvro_Skeuo._
//import SchemaAvro_Skeuo.{Field ⇒ FieldS}
//
//import json.{Schema ⇒ SchemaJson_Glow}
//import SchemaJson_Glow._
//
//
//import scala.reflect.runtime.universe
//import scala.reflect.runtime.universe._
//
//
///**
// *
// */
//object Skeuo_AndyGlow {
//
//	// TODO implement with TransG here too to avoid type parameter errors (lifting / lowering)
//
//
//	// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/
//
//	// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules
//
//
//	type FieldG[A] = `object`.Field[A] // TODO why is FieldG[A] type parameter not used?
//
//	def field2Field_SkeuoGlow_TYPED[A](fieldS: FieldS[A], s: SchemaJson_Glow[_]): FieldG[A] = fieldS match {
//
//		case FieldS(name: String, aliases: List[String], doc: Option[String], order: Option[Order], tpe: A) ⇒ {
//
//			new FieldG[A](name = name, tpe = s, required = true, default = None, description = doc, rwMode = `object`.Field.RWMode.ReadWrite)
//
//			// TODO read write or other modes - which to pick?
//			// TODO no Order in json andy glow (ascending, ignore ... meaning?)
//
//			// TODO is schema parameter correct?
//		}
//	}
//
//
//
//
//
//
//	/**
//	 * Avro[Json] => Json
//	 *
//	 * @tparam T
//	 * @return
//	 */
//	def algebra_AvroToJson_TYPED[T: TypeTag]: Algebra[SchemaAvro_Skeuo, SchemaJson_Glow] = Algebra {
//
//		case TNull() ⇒ ??? // see skeuo-avro to skeuo-json conversion file
//		case TInt() ⇒ 	`integer`
//		case TString() ⇒ `string`
//		case TBoolean() ⇒ `boolean`
//
//		case TArray(innerSchema: SchemaJson_Glow[T]) ⇒ `array`(innerSchema)
//
//		case TMap(innerSchema: SchemaJson_Glow[T]) ⇒ `dictionary`(innerSchema)
//
//		case TRecord(name, namespace, aliases, doc, fields: List[Field[SchemaJson_Glow[T]]]) ⇒ {
//
//			// TODO help - how to get the schema ?
//			// TODO meaning of head schema? same schema for all list elements?
//			fields.map(f ⇒ field2Field_SkeuoGlow_TYPED(f, fields.head.tpe))
//		}
//	}
//
//	def algebra_AvroToJson: Algebra[SchemaAvro_Skeuo, SchemaJson_Glow] = Algebra {
//
//		case TNull() ⇒
//		case TInt() ⇒ `integer`
//		case TString() ⇒ `string`
//		case TBoolean() ⇒ `boolean`
//
//		case TArray(innerSchema: SchemaJson_Glow[_]) ⇒ `array`(innerSchema)
//
//		case TMap(innerSchema: SchemaJson_Glow[_]) ⇒ `dictionary`(innerSchema)
//
//		case TRecord(name, namespace, aliases, doc, fields: List[Field[SchemaJson_Glow[_]]]) ⇒ {
//
//			fields.map(f ⇒ field2Field_SkeuoGlow_TYPED(f, fields.head.tpe))
//		}
//	}
//}
