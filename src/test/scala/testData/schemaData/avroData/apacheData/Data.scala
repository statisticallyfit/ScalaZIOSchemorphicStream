package testData.schemaData.avroData.apacheData



import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.Schema.{Field ⇒ FieldAvro_Apache}
import org.apache.avro.Schema.Field.{Order ⇒ Order_Apache}
import org.apache.avro.{LogicalType => LogicalType_Apache, LogicalTypes ⇒ LogicalTypes_Apache}

import scala.jdk.CollectionConverters._

/**
 *
 */
object Data  {
	
	// Primitive schemas
	val strAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.STRING)
	
	val intAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	
	val nullAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
	
	val booleanAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BOOLEAN)
	
	val longAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
	
	val floatAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.FLOAT)
	
	val doubleAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.DOUBLE)
	
	val bytesAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BYTES)
	
	
	
	// Array schemas
	val arrayNullAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(nullAvro_Apache)
	val arrayBooleanAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(booleanAvro_Apache)
	val arrayStrAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(strAvro_Apache)
	val arrayIntAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(intAvro_Apache)
	val arrayLongAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(longAvro_Apache)
	val arrayFloatAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(floatAvro_Apache)
	val arrayDoubleAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(doubleAvro_Apache)
	val arrayBytesAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(bytesAvro_Apache)
	
	val array3IntAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(SchemaAvro_Apache.createArray(SchemaAvro_Apache.createArray(intAvro_Apache)))
	val array2IntAvro_Apache: SchemaAvro_Apache = array3IntAvro_Apache.getElementType
	
	
	
	//Map schema
	val mapNullAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(nullAvro_Apache)
	val mapBooleanAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(booleanAvro_Apache)
	val mapStrAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(strAvro_Apache)
	val mapIntAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(intAvro_Apache)
	val mapLongAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(longAvro_Apache)
	val mapFloatAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(floatAvro_Apache)
	val mapDoubleAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(doubleAvro_Apache)
	val mapBytesAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(bytesAvro_Apache)
	
	// Named type schema
	val namedTypeAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("NamedType", "doc for named type", "namespace for named type", /*isError =*/ false)
	// TODO add aliases??
	
	
	// Record schema
	// TODO add aliases?
	import testData.schemaData.avroData.apacheData.FieldOrderData._
	
	val recordAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("Record", "doc for record", "namespace for record", /*isError =*/ false, List(strFieldAvro_1_oa, strFieldAvro_2_oa, strFieldAvro_3_oa, strFieldAvro_1_od, strFieldAvro_2_od, strFieldAvro_3_od, strFieldAvro_1_oi, strFieldAvro_2_oi, strFieldAvro_3_oi, intFieldAvro_1_oa, intFieldAvro_2_oa, intFieldAvro_3_oa, intFieldAvro_1_od, intFieldAvro_2_od, intFieldAvro_3_od, intFieldAvro_1_oi, intFieldAvro_2_oi, intFieldAvro_3_oi).asJava)
	
	// Union schema (union of schemas)
	val unionAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createUnion(List(intAvro_Apache, strAvro_Apache, floatAvro_Apache, arrayIntAvro_Apache).asJava)
	
	// Enum schema
	val enumAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createEnum("Color", "the colors of the rainbow with additional ones added in", "Color namespace string here", List("red", "orange", "yellow", "green", "pink", "magenta", "periwinkle", "tan", "jade", "blue", "indigo", "violet").asJava)

	// Fixed schema
	val fixedAvro_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createFixed("Fixed", "doc for fixed", "namespace for fixed", 10) // TODO different sizes ? (does it matter?)
	
	
	
	
	// LOGICAL TYPES
	
	
	// Date logical type schema
	val intAvro_forDate: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	val dateLogicalType: LogicalType_Apache = LogicalTypes_Apache.date()
	
	val dateAvro_Apache: SchemaAvro_Apache = dateLogicalType.addToSchema(intAvro_forDate)
	
	
	
	// Time millis logical type schema
	val intAvro_forMillis: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	val millisLogicalType: LogicalType_Apache = LogicalTypes_Apache.timeMillis()
	
	val millisAvro_Apache: SchemaAvro_Apache = millisLogicalType.addToSchema(intAvro_forMillis)
	
	
	
	// Timestamp millis logical type schema
	val longAvro_forTimestamp: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
	
	val timestampMillisLogicalType: LogicalType_Apache = LogicalTypes_Apache.timestampMillis()
	val timestampMillisSchema: SchemaAvro_Apache = timestampMillisLogicalType.addToSchema(longAvro_forTimestamp)
	
	
	
	// Decimal logical type schema
	val fixedAvro_forDecimal: SchemaAvro_Apache = SchemaAvro_Apache.createFixed("FixedSchemaForDecimalLogicalType", null /*"doc_decimal_fixed"*/ , null, /*"decimal_namespace"*/ 20)
	
	
	import scala.util.Random
	import java.lang.Math
	
	// Formula for max precision of decimal logical type (source = https://github.com/apache/avro/blob/d02241e2adeb923e5b89b57f48c19c40ad4fbb07/lang/java/avro/src/main/java/org/apache/avro/LogicalTypes.java#L318-L329)
	final val MAX_PRECISION: Long = Math.floor(Math.log10(2.0) * (8 * fixedAvro_forDecimal.getFixedSize - 1).toDouble).round
	
	// NOTE: validate() function from LogicalTypes.java says we must have 0 <= scale <= precision <= maxPrecision
	// HELP what if this breaks because Long won't fit in Int? Need int to make the random number generate in range.
	val randPrecision: Int = 1 + Random.nextInt(MAX_PRECISION.toInt) // + 1 so it is not zero ever.
	val randScale: Int = 1 + Random.nextInt(randPrecision) // Random.between(1, randPrecision) (only for scala 2.13)
	
	val decimalLogicalType: LogicalType_Apache = LogicalTypes_Apache.decimal(randPrecision, randScale)
	
	val decimalSchema: SchemaAvro_Apache = decimalLogicalType.addToSchema(fixedAvro_forDecimal)
}
