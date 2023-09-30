package conversionsOfSchemaADTs.avro_avro.skeuo_apache

import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import org.apache.avro.{LogicalType ⇒ LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache, Schema ⇒ AvroSchema_A}
import utilMain.utilAvro.utilSkeuoApache.FieldAndOrderConversions.{FieldS, field2Field_SA}
import utilMain.utilAvro.utilSkeuoApache.ValidateLogicalTypes.isValidated

import scala.jdk.CollectionConverters._


/**
 *
 */
object Skeuo_Apache {


	/**
	 * Coalgebra type is:
	 * Apache => Skeuo[Apache]
	 *
	 * @return
	 */
	def coalgebra_ApacheToSkeuo: Coalgebra[AvroSchema_S, AvroSchema_A] = AvroSchema_S.fromAvro


	/*import scala.reflect.runtime.universe._


	def galgebra_SkeuoToApache[T: TypeTag]: GAlgebra[AvroSchema_S, T, AvroSchema_A] = GAlgebra {

		case AvroSchema_S.TNull() ⇒ AvroSchema_A.create(AvroSchema_A.Type.NULL)
	}*/


	def nonalgebra_SkeuoToApache /*[T: TypeTag]*/ (schemaSkeuo: AvroSchema_S[AvroSchema_A]): AvroSchema_A = {

		schemaSkeuo match {
			case AvroSchema_S.TNull() => AvroSchema_A.create(AvroSchema_A.Type.NULL)

			case AvroSchema_S.TBoolean() => AvroSchema_A.create(AvroSchema_A.Type.BOOLEAN)

			case AvroSchema_S.TString() => AvroSchema_A.create(AvroSchema_A.Type.STRING)

			case AvroSchema_S.TInt() => AvroSchema_A.create(AvroSchema_A.Type.INT)

			case AvroSchema_S.TLong() => AvroSchema_A.create(AvroSchema_A.Type.LONG)

			case AvroSchema_S.TFloat() => AvroSchema_A.create(AvroSchema_A.Type.FLOAT)

			case AvroSchema_S.TDouble() => AvroSchema_A.create(AvroSchema_A.Type.DOUBLE)

			case AvroSchema_S.TBytes() => AvroSchema_A.create(AvroSchema_A.Type.BYTES)


			/**
			 * Apache array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
			 *
			 * HELP: figure out why inner part is INT and not array because it is supposed to be array since it is the schema inside.
			 *
			 * HELP: see here `toJson` seems like inner part is INT = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L249
			 */
			case AvroSchema_S.TArray(innerItemSchema: AvroSchema_A) ⇒ {

				/*println("Inside avroFToApache ARRAY converter: ")
				println(s"apacheSchema = $innerItemSchema")
				println(s"apacheSchema.getType = ${innerItemSchema.getType}")*/

				AvroSchema_A.createArray(innerItemSchema)
			}
			/*apacheSchema.getType match {
			case n @ AvroSchema_A.Type.NULL ⇒ AvroSchema_A.createArray(AvroSchema_A.create(n))
			case b @ AvroSchema_A.Type.BOOLEAN ⇒ AvroSchema_A.createArray(AvroSchema_A.create(b))
			case i @ AvroSchema_A.Type.INT ⇒ {
				println(s"apacheSchema = $apacheSchema")
				AvroSchema_A.createArray(AvroSchema_A.create(i))
			}
			case l @ AvroSchema_A.Type.LONG ⇒ AvroSchema_A.createArray(AvroSchema_A.create(l))
			case f @ AvroSchema_A.Type.FLOAT ⇒ AvroSchema_A.createArray(AvroSchema_A.create(f))
			case d @ AvroSchema_A.Type.DOUBLE ⇒ AvroSchema_A.createArray(AvroSchema_A.create(d))
			case by @ AvroSchema_A.Type.BYTES ⇒ AvroSchema_A.createArray(AvroSchema_A.create(by))
			case s @ AvroSchema_A.Type.STRING ⇒ AvroSchema_A.createArray(AvroSchema_A.create(s))
			case AvroSchema_A.Type.MAP ⇒ AvroSchema_A.createArray(AvroSchema_A.createMap(apacheSchema.getValueType))
			case AvroSchema_A.Type.ARRAY ⇒ AvroSchema_A.createArray(AvroSchema_A.createArray(apacheSchema.getElementType))
			case AvroSchema_A.Type.RECORD ⇒ {
				val a: AvroSchema_A = apacheSchema
				/*val res: AvroSchema_A = AvroSchema_A.createArray(AvroSchema_A.createRecord(a.getName, a.getDoc, a.getNamespace, a.isError, a.getFields))*/
				val res2 = AvroSchema_A.createArray(a)
				res2
			} //createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields)
			case AvroSchema_A.Type.ENUM ⇒ {
				val a = apacheSchema
				//val ea: AvroSchema_A = apacheSchema.asInstanceOf[AvroSchema_A.Type.ENUM]
				AvroSchema_A.createArray(AvroSchema_A.createEnum(a.getName, a.getDoc, a.getNamespace, a.getEnumSymbols))
			}
				//createEnum(String name, String doc, String namespace, List<String> values)
			}*/

			/**
			 * Apache Map Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
			 */
			case AvroSchema_S.TMap(innerItemSchema: AvroSchema_A) => AvroSchema_A.createMap(innerItemSchema)


			/**
			 * Apache's Named Record == skeuomorph's TNamedType
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211
			 */
			case AvroSchema_S.TNamedType(namespace: String, name: String) ⇒ {
				AvroSchema_A.createRecord(name, null /*"NO DOC"*/ , namespace, /*isError =*/ false)
			}

			/**
			 * Apache's Named Record with Fields == skeuomorph's TRecord
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L222-L225
			 */
			case AvroSchema_S.TRecord(skeuoName: String,
			skeuoNamespace: Option[String],
			aliases: List[String],
			skeuoDoc: Option[String],
			skeuoFields: List[FieldS[AvroSchema_A]]) ⇒ {

				// Skeuo Order = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L61-L64
				// Apache Order = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L530

				// Skeuo Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L75-L90
				// Apache Field = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L507-L697

				// field2Field: Skeuo converts ApacheField to SkeuoField = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L51

				// order2Order: Skeuo converts ApacheOrder to SkeuoOrder = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L42


				// Create the record
				// HELP: apache has `isError` while skeuo does not and skeuo has `aliases` while apache does not.... how to fix?
				//  TODO say by default that this record is NOT an error type? = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L364

				val recordSchema: AvroSchema_A = AvroSchema_A.createRecord(skeuoName, skeuoDoc.getOrElse(null /*"NO DOC"*/), skeuoNamespace.getOrElse(null /*"NO NAMESPACE"*/), /*isError =*/ false, skeuoFields.map(f ⇒ field2Field_SA(f)).asJava)

				aliases.foreach(a ⇒ recordSchema.addAlias(a))

				recordSchema
			}


			/**
			 * Apache Union Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254
			 */
			case AvroSchema_S.TUnion(schemaList: NonEmptyList[AvroSchema_A], name: Option[String]) ⇒ {
				AvroSchema_A.createUnion(schemaList.toList.asJava)
			}

			/**
			 * Apache Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
			 *
			 * HELP: Skeuo has 'aliases' while apache does not -- meaning? - check meaning of aliases vs. symbols - which contains the enum subcases?
			 */
			case AvroSchema_S.TEnum(name, namespaceOpt, aliases, docOpt, symbols) => {
				val enumSchema = AvroSchema_A.createEnum(name,
					docOpt.orNull,
					namespaceOpt.getOrElse(null /*"NO NAMESPACE"*/),
					symbols.asJava
				)

				aliases.foreach(a ⇒ enumSchema.addAlias(a))

				enumSchema
			}

			/**
			 * apache fixed = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L258
			 * HELP: apache has `doc` while skeuo does not
			 * TODO CHECK IF MY IMPLEMENTATION IS CORREC: skeuo has `aliases` while apache does not --- construct Fixed first then addAliases = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L233
			 * NOTE: skeup has `namespaceOption` while apache has `space`` -- they are the same (namespace wrapped in option)
			 */
			case AvroSchema_S.TFixed(name, namespaceOpt, aliases, size) => {
				val fixedSchema: AvroSchema_A = AvroSchema_A.createFixed(name, null /*"NO DOC"*/ , namespaceOpt.orNull, size)

				aliases.foreach(a => fixedSchema.addAlias(a))

				fixedSchema
			}



			// Logical Types: Date, Time, Decimal etc

			case AvroSchema_S.TDate() => {
				val intSchema: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.INT)
				val dateLogicalType: LogicalTypeApache = LogicalTypesApache.date()

				assert(/*dateLogicalType.validate(intSchema)*/ isValidated(dateLogicalType, intSchema),
					s"${dateLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible

				val dateSchema: AvroSchema_A = dateLogicalType.addToSchema(intSchema)

				assert(dateSchema == intSchema) // the int schema was altered (warning: state change!)

				dateSchema
			}
			case AvroSchema_S.TTimeMillis() ⇒ {

				val intSchema: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.INT)
				val millisLogicalType: LogicalTypeApache = LogicalTypesApache.timeMillis()

				assert(isValidated(millisLogicalType, intSchema), s"${millisLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible

				val millisSchema: AvroSchema_A = millisLogicalType.addToSchema(intSchema)

				millisSchema
			}

			case AvroSchema_S.TTimestampMillis() => {

				val longSchema: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.LONG)
				val timestampMillisLogicalType: LogicalTypeApache = LogicalTypesApache.timestampMillis()

				assert(isValidated(timestampMillisLogicalType, longSchema), s"${timestampMillisLogicalType.getName} Logical Type uses ${longSchema.getType} schema only") // check if compatible

				val timestampMillisSchema: AvroSchema_A = timestampMillisLogicalType.addToSchema(longSchema)

				timestampMillisSchema
			}

			case AvroSchema_S.TDecimal(precision: Int, scale: Int) ⇒ {

				val fixedSchema: AvroSchema_A = AvroSchema_A.createFixed("Fixed (schema) for Decimal (logical type)", null /*"doc_decimal_fixed"*/ , null, /*"decimal_namespace"*/ 0)
				// TODO is this OK?

				val decimalLogicalType: LogicalTypeApache = LogicalTypesApache.decimal(precision, scale)
				assert(isValidated(decimalLogicalType, fixedSchema), s"${decimalLogicalType.getName} Logical Type uses ${fixedSchema.getType} schema only") // check if compatible

				val decimalSchema: AvroSchema_A = decimalLogicalType.addToSchema(fixedSchema)

				decimalSchema
			}


			case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
		}
	}


	/**
	 * Algebra type is:
	 * Skeuo[Apache] => Apache
	 *
	 * @return
	 */
	def algebra_SkeuoToApache: Algebra[AvroSchema_S, AvroSchema_A] =
		Algebra { // Algebra[Skeuo, Apache] ----- MEANING ---->  Skeuo[Apache] => Apache
			fa: AvroSchema_S[AvroSchema_A] ⇒ nonalgebra_SkeuoToApache(fa)


		}



	// TODO change names: AvroSchema_A etc

	def skeuoToApacheAvroSchema: Fix[AvroSchema_S] ⇒ AvroSchema_A = scheme.cata(algebra_SkeuoToApache).apply(_)


	// TODO meaning of putting [Fix] as scheme parameter?
	// TODO is there a way to wrap with fix or unwrap fix out of it using some kind of identity from droste? (instead of ana/cata)
	/*import higherkindness.droste.data._


	val r: Fix[AvroSchema_S] ⇒ AvroSchema_A = scheme[Fix].cata(algebra_SkeuoToApache).apply(_)*/


	def apacheToSkeuoAvroSchema: AvroSchema_A ⇒ Fix[AvroSchema_S] = scheme.ana(coalgebra_ApacheToSkeuo).apply(_)


	def roundTrip_ApacheAvroToApacheAvro: AvroSchema_A ⇒ AvroSchema_A = skeuoToApacheAvroSchema compose apacheToSkeuoAvroSchema

	def roundTrip_SkeuoAvroToSkeuoAvro: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = apacheToSkeuoAvroSchema compose skeuoToApacheAvroSchema

}
