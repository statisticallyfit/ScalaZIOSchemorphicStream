package testData.schemaData.avroData.apacheData



import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.Schema.{Field ⇒ Field_Apache}
import org.apache.avro.Schema.Field.{Order ⇒ Order_Apache}
import org.apache.avro.{LogicalType => LogicalType_Apache, LogicalTypes ⇒ LogicalTypes_Apache}

import scala.jdk.CollectionConverters._

/**
 *
 */
object AvroSchemaData_Apache  {
	
	// Primitive schemas
	val strSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.STRING)
	
	val intSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	
	val nullSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
	
	val booleanSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BOOLEAN)
	
	val longSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
	
	val floatSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.FLOAT)
	
	val doubleSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.DOUBLE)
	
	val bytesSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BYTES)
	
	
	
	// Array schemas
	val arrayNullSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(nullSchema_Apache)
	val arrayBooleanSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(booleanSchema_Apache)
	val arrayStrSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(strSchema_Apache)
	val arrayIntSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(intSchema_Apache)
	val arrayLongSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(longSchema_Apache)
	val arrayFloatSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(floatSchema_Apache)
	val arrayDoubleSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(doubleSchema_Apache)
	val arrayBytesSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(bytesSchema_Apache)
	
	val array3IntSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(SchemaAvro_Apache.createArray(SchemaAvro_Apache.createArray(intSchema_Apache)))
	val array2IntSchema_Apache: SchemaAvro_Apache = array3IntSchema_Apache.getElementType
	
	
	
	//Map schema
	val mapNullSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(nullSchema_Apache)
	val mapBooleanSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(booleanSchema_Apache)
	val mapStrSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(strSchema_Apache)
	val mapIntSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(intSchema_Apache)
	val mapLongSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(longSchema_Apache)
	val mapFloatSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(floatSchema_Apache)
	val mapDoubleSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(doubleSchema_Apache)
	val mapBytesSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createMap(bytesSchema_Apache)
	
	// Named type schema
	val namedTypeSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("NamedType", "doc for named type", "namespace for named type", /*isError =*/ false)
	// TODO add aliases??
	
	
	// Record schema
	// TODO add aliases?
	import testData.schemaData.avroData.apacheData.ApacheAvroFieldOrderData._
	
	val recordSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("Record", "doc for record", "namespace for record", /*isError =*/ false, List(strField_1_oa, strField_2_oa, strField_3_oa, strField_1_od, strField_2_od, strField_3_od, strField_1_oi, strField_2_oi, strField_3_oi, intField_1_oa, intField_2_oa, intField_3_oa, intField_1_od, intField_2_od, intField_3_od, intField_1_oi, intField_2_oi, intField_3_oi).asJava)
	
	// Union schema (union of schemas)
	val unionSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createUnion(List(intSchema_Apache, strSchema_Apache, floatSchema_Apache, arrayIntSchema_Apache).asJava)
	
	// Enum schema
	val enumSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createEnum("Color", "the colors of the rainbow with additional ones added in", "Color namespace string here", List("red", "orange", "yellow", "green", "pink", "magenta", "periwinkle", "tan", "jade", "blue", "indigo", "violet").asJava)

	// Fixed schema
	val fixedSchema_Apache: SchemaAvro_Apache = SchemaAvro_Apache.createFixed("Fixed", "doc for fixed", "namespace for fixed", 10) // TODO different sizes ? (does it matter?)
	
	
	
	
	// LOGICAL TYPES
	
	
	// Date logical type schema
	val intSchema_forDate: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	val dateLogicalType: LogicalType_Apache = LogicalTypes_Apache.date()
	
	val dateSchema_Apache: SchemaAvro_Apache = dateLogicalType.addToSchema(intSchema_forDate)
	
	
	
	// Time millis logical type schema
	val intSchema_forMillis: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	val millisLogicalType: LogicalType_Apache = LogicalTypes_Apache.timeMillis()
	
	val millisSchema_Apache: SchemaAvro_Apache = millisLogicalType.addToSchema(intSchema_forMillis)
	
	
	
	// Timestamp millis logical type schema
	val longSchema_forTimestamp: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
	
	val timestampMillisLogicalType: LogicalType_Apache = LogicalTypes_Apache.timestampMillis()
	val timestampMillisSchema: SchemaAvro_Apache = timestampMillisLogicalType.addToSchema(longSchema_forTimestamp)
	
	
	
	// Decimal logical type schema
	val fixedSchema_forDecimal: SchemaAvro_Apache = SchemaAvro_Apache.createFixed("FixedSchemaForDecimalLogicalType", null /*"doc_decimal_fixed"*/ , null, /*"decimal_namespace"*/ 20)
	
	
	import scala.util.Random
	import java.lang.Math
	
	// Formula for max precision of decimal logical type (source = https://github.com/apache/avro/blob/d02241e2adeb923e5b89b57f48c19c40ad4fbb07/lang/java/avro/src/main/java/org/apache/avro/LogicalTypes.java#L318-L329)
	final val MAX_PRECISION: Long = Math.floor(Math.log10(2.0) * (8 * fixedSchema_forDecimal.getFixedSize - 1).toDouble).round
	
	// NOTE: validate() function from LogicalTypes.java says we must have 0 <= scale <= precision <= maxPrecision
	// HELP what if this breaks because Long won't fit in Int? Need int to make the random number generate in range.
	val randPrecision: Int = 1 + Random.nextInt(MAX_PRECISION.toInt) // + 1 so it is not zero ever.
	val randScale: Int = 1 + Random.nextInt(randPrecision) // Random.between(1, randPrecision) (only for scala 2.13)
	
	val decimalLogicalType: LogicalType_Apache = LogicalTypes_Apache.decimal(randPrecision, randScale)
	
	val decimalSchema: SchemaAvro_Apache = decimalLogicalType.addToSchema(fixedSchema_forDecimal)
}
