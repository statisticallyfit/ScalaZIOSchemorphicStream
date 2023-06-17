package conversionsOfSchemaADTs.avro_avro

import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}




import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}


import zio.schema._
import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType} // TODO move the zio <-> apache tests in the other file
import zio.schema.codec.AvroCodec

import scala.jdk.CollectionConverters._


import utilMain.utilSkeuoApache.FieldAndOrderConversions._
import utilMain.utilSkeuoApache.ValidateUtil._

/**
 *
 */
object Skeuo_Apache {
	
	
	/**
	 * Coalgebra type is:
	 * 	Apache => Skeuo[Apache]
	 *
	 * @return
	 */
	def coalgebra_ApacheSkeuo: Coalgebra[SchemaSkeuoAvro, SchemaApacheAvro] = SchemaSkeuoAvro.fromAvro
	
	
	/**
	 * Algebra type is:
	 * 	Skeuo[Apache] => Apache
	 *
	 * @return
	 */
	def algebra_SkeuoApache: Algebra[SchemaSkeuoAvro, SchemaApacheAvro] =
		Algebra { // Algebra[Skeuo, Apache] ----- MEANING ---->  Skeuo[Apache] => Apache
			
			case SchemaSkeuoAvro.TNull() => SchemaApacheAvro.create(SchemaApacheAvro.Type.NULL)
			
			case SchemaSkeuoAvro.TBoolean() => SchemaApacheAvro.create(SchemaApacheAvro.Type.BOOLEAN)
			
			case SchemaSkeuoAvro.TInt() => SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
			
			case SchemaSkeuoAvro.TLong() => SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG)
			
			case SchemaSkeuoAvro.TFloat() => SchemaApacheAvro.create(SchemaApacheAvro.Type.FLOAT)
			
			case SchemaSkeuoAvro.TDouble() => SchemaApacheAvro.create(SchemaApacheAvro.Type.DOUBLE)
			
			case SchemaSkeuoAvro.TBytes() => SchemaApacheAvro.create(SchemaApacheAvro.Type.BYTES)
			
			case SchemaSkeuoAvro.TString() => SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)
			
			
			/**
			 * Apache Map Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
			 */
			case SchemaSkeuoAvro.TMap(sch: SchemaApacheAvro) => SchemaApacheAvro.createMap(sch)
			
			
			/**
			 * Apache array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
			 *
			 * HELP: figure out why inner part is INT and not array because it is supposed to be array since it is the schema inside.
			 *
			 * HELP: see here `toJson` seems like inner part is INT = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L249
			 */
			case SchemaSkeuoAvro.TArray(apacheSchema: SchemaApacheAvro) ⇒ {
				
				println("Inside avroFToApache ARRAY converter: ")
				println(s"apacheSchema = $apacheSchema")
				println(s"apacheSchema.getType = ${apacheSchema.getType}")
				
				SchemaApacheAvro.createArray(apacheSchema)
			}
			/*apacheSchema.getType match {
			case n @ SchemaApacheAvro.Type.NULL ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(n))
			case b @ SchemaApacheAvro.Type.BOOLEAN ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(b))
			case i @ SchemaApacheAvro.Type.INT ⇒ {
				println(s"apacheSchema = $apacheSchema")
				SchemaApacheAvro.createArray(SchemaApacheAvro.create(i))
			}
			case l @ SchemaApacheAvro.Type.LONG ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(l))
			case f @ SchemaApacheAvro.Type.FLOAT ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(f))
			case d @ SchemaApacheAvro.Type.DOUBLE ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(d))
			case by @ SchemaApacheAvro.Type.BYTES ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(by))
			case s @ SchemaApacheAvro.Type.STRING ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(s))
			case SchemaApacheAvro.Type.MAP ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createMap(apacheSchema.getValueType))
			case SchemaApacheAvro.Type.ARRAY ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createArray(apacheSchema.getElementType))
			case SchemaApacheAvro.Type.RECORD ⇒ {
				val a: SchemaApacheAvro = apacheSchema
				/*val res: SchemaApacheAvro = SchemaApacheAvro.createArray(SchemaApacheAvro.createRecord(a.getName, a.getDoc, a.getNamespace, a.isError, a.getFields))*/
				val res2 = SchemaApacheAvro.createArray(a)
				res2
			} //createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields)
			case SchemaApacheAvro.Type.ENUM ⇒ {
				val a = apacheSchema
				//val ea: SchemaApacheAvro = apacheSchema.asInstanceOf[SchemaApacheAvro.Type.ENUM]
				SchemaApacheAvro.createArray(SchemaApacheAvro.createEnum(a.getName, a.getDoc, a.getNamespace, a.getEnumSymbols))
			}
				//createEnum(String name, String doc, String namespace, List<String> values)
			}*/
			
			
			/**
			 * Apache's Named Record == skeuomorph's TNamedType
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211
			 */
			case SchemaSkeuoAvro.TNamedType(namespace: String, name: String) ⇒ {
				SchemaApacheAvro.createRecord(name, null/*"NO DOC"*/, namespace, /*isError =*/ false)
			}
			
			/**
			 * Apache's Named Record with Fields == skeuomorph's TRecord
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L222-L225
			 */
			case SchemaSkeuoAvro.TRecord(skeuoName: String,
			skeuoNamespace: Option[String],
			aliases: List[String],
			skeuoDoc: Option[String],
			skeuoFields: List[FieldS[SchemaApacheAvro]]) ⇒ {
				
				// Skeuo Order = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L61-L64
				// Apache Order = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L530
				
				// Skeuo Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L75-L90
				// Apache Field = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L507-L697
				
				// field2Field: Skeuo converts ApacheField to SkeuoField = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L51
				
				// order2Order: Skeuo converts ApacheOrder to SkeuoOrder = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L42
				
				
				// Create the record
				// HELP: apache has `isError` while skeuo does not and skeuo has `aliases` while apache does not.... how to fix?
				//  TODO say by default that this record is NOT an error type? = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L364
				
				val recordSchema: SchemaApacheAvro = SchemaApacheAvro.createRecord(skeuoName, skeuoDoc.getOrElse(null/*"NO DOC"*/), skeuoNamespace.getOrElse(null/*"NO NAMESPACE"*/), /*isError =*/ false, skeuoFields.map(f ⇒ field2Field_SA(f)).asJava)
				
				aliases.foreach(a ⇒ recordSchema.addAlias(a))
				
				recordSchema
			}
			
			
			/**
			 * Apache Union Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254
			 */
			case SchemaSkeuoAvro.TUnion(schemaList: NonEmptyList[SchemaApacheAvro]) ⇒ {
				SchemaApacheAvro.createUnion(schemaList.toList.asJava)
			}
			
			/**
			 * Apache Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
			 *
			 * HELP: Skeuo has 'aliases' while apache does not -- meaning? - check meaning of aliases vs. symbols - which contains the enum subcases?
			 */
			case SchemaSkeuoAvro.TEnum(name, namespaceOpt, aliases, docOpt, symbols) => {
				val enumSchema = SchemaApacheAvro.createEnum(name,
					docOpt.orNull,
					namespaceOpt.getOrElse(null/*"NO NAMESPACE"*/),
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
			case SchemaSkeuoAvro.TFixed(name, namespaceOpt, aliases, size) => {
				val fixedSchema: SchemaApacheAvro = SchemaApacheAvro.createFixed(name, null/*"NO DOC"*/, namespaceOpt.getOrElse(null/*"NO SPACE (from namespace)"*/), size)
				
				aliases.foreach(a => fixedSchema.addAlias(a))
				
				fixedSchema
			}
			
			
			
			// Logical Types: Date, Time, Decimal etc
			
			case SchemaSkeuoAvro.TDate() => {
				val intSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
				val dateLogicalType: LogicalTypeApache = LogicalTypesApache.date()
				assert(/*dateLogicalType.validate(intSchema)*/ isValidated(dateLogicalType, intSchema),
					s"${dateLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val dateSchema: SchemaApacheAvro = dateLogicalType.addToSchema(intSchema)
				
				dateSchema
			}
			case SchemaSkeuoAvro.TTimeMillis() ⇒ {
				
				val intSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
				val millisLogicalType: LogicalTypeApache = LogicalTypesApache.timeMillis()
				assert(isValidated(millisLogicalType, intSchema), s"${millisLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val millisSchema: SchemaApacheAvro = millisLogicalType.addToSchema(intSchema)
				
				millisSchema
			}
			
			case SchemaSkeuoAvro.TTimestampMillis() => {
				
				val longSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG)
				val timestampMillisLogicalType: LogicalTypeApache = LogicalTypesApache.timestampMillis()
				assert(isValidated(timestampMillisLogicalType, longSchema), s"${timestampMillisLogicalType.getName} Logical Type uses ${longSchema.getType} schema only") // check if compatible
				
				val timestampMillisSchema: SchemaApacheAvro = timestampMillisLogicalType.addToSchema(longSchema)
				
				timestampMillisSchema
			}
			
			case SchemaSkeuoAvro.TDecimal(precision: Int, scale: Int) ⇒ {
				
				val fixedSchema: SchemaApacheAvro = SchemaApacheAvro.createFixed("decimal_fixed", "doc_decimal_fixed", "decimal_namespace", 5)
				// TODO is this OK?
				
				val decimalLogicalType: LogicalTypeApache = LogicalTypesApache.decimal(precision, scale)
				assert(isValidated(decimalLogicalType, fixedSchema), s"${decimalLogicalType.getName} Logical Type uses ${fixedSchema.getType} schema only") // check if compatible
				
				val decimalSchema: SchemaApacheAvro = decimalLogicalType.addToSchema(fixedSchema)
				
				decimalSchema
			}
			
			
			case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
		}
	
	
	// TODO change names: SchemaAvro_Apache etc
	
	def skeuoToApacheAvroSchema: Fix[SchemaSkeuoAvro] ⇒ SchemaApacheAvro = scheme.cata(algebra_SkeuoApache).apply(_)
	
	
	
	
	def apacheToSkeuoAvroSchema: SchemaApacheAvro ⇒ Fix[SchemaSkeuoAvro] = scheme.ana(coalgebra_ApacheSkeuo).apply(_)
	
	
}
