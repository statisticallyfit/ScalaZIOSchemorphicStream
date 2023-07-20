package conversionsOfSchemaADTs.avro_avro.skeuo_apache

import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import org.apache.avro.{LogicalType ⇒ LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache, Schema ⇒ SchemaAvro_Apache}
import utilMain.utilAvro.utilSkeuoApache.FieldAndOrderConversions.{FieldS, field2Field_SA}
import utilMain.utilAvro.utilSkeuoApache.ValidateLogicalTypes.isValidated

import scala.jdk.CollectionConverters._


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
	def coalgebra_ApacheToSkeuo: Coalgebra[SchemaAvro_Skeuo, SchemaAvro_Apache] = SchemaAvro_Skeuo.fromAvro
	
	
	/*import scala.reflect.runtime.universe._
	
	
	def galgebra_SkeuoToApache[T: TypeTag]: GAlgebra[SchemaAvro_Skeuo, T, SchemaAvro_Apache] = GAlgebra {
		
		case SchemaAvro_Skeuo.TNull() ⇒ SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
	}*/
	
	
	
	def nonalgebra_SkeuoToApache/*[T: TypeTag]*/(schemaSkeuo: SchemaAvro_Skeuo[SchemaAvro_Apache]): SchemaAvro_Apache = {
		
		schemaSkeuo match {
			case SchemaAvro_Skeuo.TNull() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
			
			case SchemaAvro_Skeuo.TBoolean() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BOOLEAN)
			
			case SchemaAvro_Skeuo.TString() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.STRING)
			
			case SchemaAvro_Skeuo.TInt() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
			
			case SchemaAvro_Skeuo.TLong() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
			
			case SchemaAvro_Skeuo.TFloat() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.FLOAT)
			
			case SchemaAvro_Skeuo.TDouble() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.DOUBLE)
			
			case SchemaAvro_Skeuo.TBytes() => SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BYTES)
			
			
			/**
			 * Apache array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
			 *
			 * HELP: figure out why inner part is INT and not array because it is supposed to be array since it is the schema inside.
			 *
			 * HELP: see here `toJson` seems like inner part is INT = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L249
			 */
			case SchemaAvro_Skeuo.TArray(innerItemSchema: SchemaAvro_Apache) ⇒ {
				
				println("Inside avroFToApache ARRAY converter: ")
				println(s"apacheSchema = $innerItemSchema")
				println(s"apacheSchema.getType = ${innerItemSchema.getType}")
				
				SchemaAvro_Apache.createArray(innerItemSchema)
			}
			/*apacheSchema.getType match {
			case n @ SchemaAvro_Apache.Type.NULL ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(n))
			case b @ SchemaAvro_Apache.Type.BOOLEAN ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(b))
			case i @ SchemaAvro_Apache.Type.INT ⇒ {
				println(s"apacheSchema = $apacheSchema")
				SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(i))
			}
			case l @ SchemaAvro_Apache.Type.LONG ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(l))
			case f @ SchemaAvro_Apache.Type.FLOAT ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(f))
			case d @ SchemaAvro_Apache.Type.DOUBLE ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(d))
			case by @ SchemaAvro_Apache.Type.BYTES ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(by))
			case s @ SchemaAvro_Apache.Type.STRING ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.create(s))
			case SchemaAvro_Apache.Type.MAP ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.createMap(apacheSchema.getValueType))
			case SchemaAvro_Apache.Type.ARRAY ⇒ SchemaAvro_Apache.createArray(SchemaAvro_Apache.createArray(apacheSchema.getElementType))
			case SchemaAvro_Apache.Type.RECORD ⇒ {
				val a: SchemaAvro_Apache = apacheSchema
				/*val res: SchemaAvro_Apache = SchemaAvro_Apache.createArray(SchemaAvro_Apache.createRecord(a.getName, a.getDoc, a.getNamespace, a.isError, a.getFields))*/
				val res2 = SchemaAvro_Apache.createArray(a)
				res2
			} //createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields)
			case SchemaAvro_Apache.Type.ENUM ⇒ {
				val a = apacheSchema
				//val ea: SchemaAvro_Apache = apacheSchema.asInstanceOf[SchemaAvro_Apache.Type.ENUM]
				SchemaAvro_Apache.createArray(SchemaAvro_Apache.createEnum(a.getName, a.getDoc, a.getNamespace, a.getEnumSymbols))
			}
				//createEnum(String name, String doc, String namespace, List<String> values)
			}*/
			
			/**
			 * Apache Map Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
			 */
			case SchemaAvro_Skeuo.TMap(innerItemSchema: SchemaAvro_Apache) => SchemaAvro_Apache.createMap(innerItemSchema)
			
			
			/**
			 * Apache's Named Record == skeuomorph's TNamedType
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211
			 */
			case SchemaAvro_Skeuo.TNamedType(namespace: String, name: String) ⇒ {
				SchemaAvro_Apache.createRecord(name, null /*"NO DOC"*/ , namespace, /*isError =*/ false)
			}
			
			/**
			 * Apache's Named Record with Fields == skeuomorph's TRecord
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L222-L225
			 */
			case SchemaAvro_Skeuo.TRecord(skeuoName: String,
			skeuoNamespace: Option[String],
			aliases: List[String],
			skeuoDoc: Option[String],
			skeuoFields: List[FieldS[SchemaAvro_Apache]]) ⇒ {
				
				// Skeuo Order = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L61-L64
				// Apache Order = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L530
				
				// Skeuo Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L75-L90
				// Apache Field = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L507-L697
				
				// field2Field: Skeuo converts ApacheField to SkeuoField = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L51
				
				// order2Order: Skeuo converts ApacheOrder to SkeuoOrder = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L42
				
				
				// Create the record
				// HELP: apache has `isError` while skeuo does not and skeuo has `aliases` while apache does not.... how to fix?
				//  TODO say by default that this record is NOT an error type? = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L364
				
				val recordSchema: SchemaAvro_Apache = SchemaAvro_Apache.createRecord(skeuoName, skeuoDoc.getOrElse(null /*"NO DOC"*/), skeuoNamespace.getOrElse(null /*"NO NAMESPACE"*/), /*isError =*/ false, skeuoFields.map(f ⇒ field2Field_SA(f)).asJava)
				
				aliases.foreach(a ⇒ recordSchema.addAlias(a))
				
				recordSchema
			}
			
			
			/**
			 * Apache Union Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254
			 */
			case SchemaAvro_Skeuo.TUnion(schemaList: NonEmptyList[SchemaAvro_Apache]) ⇒ {
				SchemaAvro_Apache.createUnion(schemaList.toList.asJava)
			}
			
			/**
			 * Apache Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
			 *
			 * HELP: Skeuo has 'aliases' while apache does not -- meaning? - check meaning of aliases vs. symbols - which contains the enum subcases?
			 */
			case SchemaAvro_Skeuo.TEnum(name, namespaceOpt, aliases, docOpt, symbols) => {
				val enumSchema = SchemaAvro_Apache.createEnum(name,
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
			case SchemaAvro_Skeuo.TFixed(name, namespaceOpt, aliases, size) => {
				val fixedSchema: SchemaAvro_Apache = SchemaAvro_Apache.createFixed(name, null /*"NO DOC"*/ , namespaceOpt.orNull, size)
				
				aliases.foreach(a => fixedSchema.addAlias(a))
				
				fixedSchema
			}
			
			
			
			// Logical Types: Date, Time, Decimal etc
			
			case SchemaAvro_Skeuo.TDate() => {
				val intSchema: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
				val dateLogicalType: LogicalTypeApache = LogicalTypesApache.date()
				
				assert(/*dateLogicalType.validate(intSchema)*/ isValidated(dateLogicalType, intSchema),
					s"${dateLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val dateSchema: SchemaAvro_Apache = dateLogicalType.addToSchema(intSchema)
				
				assert(dateSchema == intSchema) // the int schema was altered (warning: state change!)
				
				dateSchema
			}
			case SchemaAvro_Skeuo.TTimeMillis() ⇒ {
				
				val intSchema: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
				val millisLogicalType: LogicalTypeApache = LogicalTypesApache.timeMillis()
				
				assert(isValidated(millisLogicalType, intSchema), s"${millisLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val millisSchema: SchemaAvro_Apache = millisLogicalType.addToSchema(intSchema)
				
				millisSchema
			}
			
			case SchemaAvro_Skeuo.TTimestampMillis() => {
				
				val longSchema: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
				val timestampMillisLogicalType: LogicalTypeApache = LogicalTypesApache.timestampMillis()
				
				assert(isValidated(timestampMillisLogicalType, longSchema), s"${timestampMillisLogicalType.getName} Logical Type uses ${longSchema.getType} schema only") // check if compatible
				
				val timestampMillisSchema: SchemaAvro_Apache = timestampMillisLogicalType.addToSchema(longSchema)
				
				timestampMillisSchema
			}
			
			case SchemaAvro_Skeuo.TDecimal(precision: Int, scale: Int) ⇒ {
				
				val fixedSchema: SchemaAvro_Apache = SchemaAvro_Apache.createFixed("Fixed (schema) for Decimal (logical type)", null /*"doc_decimal_fixed"*/ , null, /*"decimal_namespace"*/ 0)
				// TODO is this OK?
				
				val decimalLogicalType: LogicalTypeApache = LogicalTypesApache.decimal(precision, scale)
				assert(isValidated(decimalLogicalType, fixedSchema), s"${decimalLogicalType.getName} Logical Type uses ${fixedSchema.getType} schema only") // check if compatible
				
				val decimalSchema: SchemaAvro_Apache = decimalLogicalType.addToSchema(fixedSchema)
				
				decimalSchema
			}
			
			
			case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
		}
	}
	
	
	/**
	 * Algebra type is:
	 * 	Skeuo[Apache] => Apache
	 *
	 * @return
	 */
	def algebra_SkeuoToApache: Algebra[SchemaAvro_Skeuo, SchemaAvro_Apache] =
		Algebra { // Algebra[Skeuo, Apache] ----- MEANING ---->  Skeuo[Apache] => Apache
			fa: SchemaAvro_Skeuo[SchemaAvro_Apache] ⇒ nonalgebra_SkeuoToApache(fa)
			
			
		}
	
	
	
	// TODO change names: SchemaAvro_Apache etc
	
	def skeuoToApacheAvroSchema: Fix[SchemaAvro_Skeuo] ⇒ SchemaAvro_Apache = scheme.cata(algebra_SkeuoToApache).apply(_)
	
	
	// TODO meaning of putting [Fix] as scheme parameter?
	// TODO is there a way to wrap with fix or unwrap fix out of it using some kind of identity from droste? (instead of ana/cata)
	/*import higherkindness.droste.data._
	
	
	val r: Fix[SchemaAvro_Skeuo] ⇒ SchemaAvro_Apache = scheme[Fix].cata(algebra_SkeuoToApache).apply(_)*/
	
	
	def apacheToSkeuoAvroSchema: SchemaAvro_Apache ⇒ Fix[SchemaAvro_Skeuo] = scheme.ana(coalgebra_ApacheToSkeuo).apply(_)
	
	
	
	
	
	def roundTrip_ApacheAvroToApacheAvro: SchemaAvro_Apache ⇒ SchemaAvro_Apache = skeuoToApacheAvroSchema compose apacheToSkeuoAvroSchema
	
	def roundTrip_SkeuoAvroToSkeuoAvro: Fix[SchemaAvro_Skeuo] ⇒ Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema compose skeuoToApacheAvroSchema
	
}
