package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.skeuomorph.avro.AvroF._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}


/**
 * Algebra[SchemaAvro, SchemaJsonFixed]
 * Avro[JsonFixed] => JsonFixed
 *
 * @return
 */

object Algebra_AvroToJson_Fixed {

	/**
	 * Modeling after this function SkeuoAvro -> JsonCirce (toJson): https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238-L303
	 *
	 * @return
	 */
	def algebra_AvroToJsonFixed: Algebra[SchemaAvro_Skeuo, SchemaJson_Skeuo.Fixed] = Algebra {

		/*case TNull() ⇒ Fixed.`object`()ObjectF[T](
			properties = List[Property[T]](/*Property(name = "", tpe = null.asInstanceOf[T])*/),
			required = List[String]()
		)*/
		case TNull() ⇒ ???

		case TInt() ⇒ Fixed.integer()
		case TString() ⇒ Fixed.string()
		case TBoolean() ⇒ Fixed.boolean()
		case TDouble() ⇒ Fixed.double()
		case TFloat() => Fixed.float()
		case TLong() ⇒ Fixed.long()
		case TBytes() ⇒ Fixed.byte()

		case TArray(innerSchema: SchemaJson_Skeuo.Fixed) ⇒ Fixed.array(innerSchema)


		// TODO how to create map?

		case TMap(innerSchema: SchemaJson_Skeuo.Fixed) ⇒ Fixed.`object`(
			properties = List(("values", innerSchema)), // List[(String, Fixed)]
			required = List("map") // List[String]
		)

		case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) ⇒ ??? // TODO HELP https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L273

		// TODO how to convert NonEmptyList to one value only? Cannot!
		//case TUnion(options: NonEmptyList[SchemaJson_Skeuo.Fixed]) ⇒ ??? ///Fixed.array(options.toList:_*)


	}
}
