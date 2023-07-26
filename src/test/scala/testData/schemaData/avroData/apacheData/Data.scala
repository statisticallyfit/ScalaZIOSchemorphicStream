package testData.schemaData.avroData.apacheData


import org.apache.avro.{LogicalType ⇒ LogicalType_A, LogicalTypes ⇒ LogicalTypes_A, Schema ⇒ AvroSchema_A}

import scala.jdk.CollectionConverters._


/**
 *
 */
object Data {
	
	// Primitive schemas
	val strAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.STRING)
	
	val intAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.INT)
	
	val nullAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.NULL)
	
	val booleanAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.BOOLEAN)
	
	val longAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.LONG)
	
	val floatAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.FLOAT)
	
	val doubleAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.DOUBLE)
	
	val bytesAvro_A: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.BYTES)
	
	
	// Array schemas
	val arrayNullAvro_A: AvroSchema_A = AvroSchema_A.createArray(nullAvro_A)
	val arrayBooleanAvro_A: AvroSchema_A = AvroSchema_A.createArray(booleanAvro_A)
	val arrayStrAvro_A: AvroSchema_A = AvroSchema_A.createArray(strAvro_A)
	val arrayIntAvro_A: AvroSchema_A = AvroSchema_A.createArray(intAvro_A)
	val arrayLongAvro_A: AvroSchema_A = AvroSchema_A.createArray(longAvro_A)
	val arrayFloatAvro_A: AvroSchema_A = AvroSchema_A.createArray(floatAvro_A)
	val arrayDoubleAvro_A: AvroSchema_A = AvroSchema_A.createArray(doubleAvro_A)
	val arrayBytesAvro_A: AvroSchema_A = AvroSchema_A.createArray(bytesAvro_A)
	
	val array3IntAvro_A: AvroSchema_A = AvroSchema_A.createArray(AvroSchema_A.createArray(AvroSchema_A.createArray(intAvro_A)))
	val array2IntAvro_A: AvroSchema_A = array3IntAvro_A.getElementType
	
	
	//Map schema
	val mapNullAvro_A: AvroSchema_A = AvroSchema_A.createMap(nullAvro_A)
	val mapBooleanAvro_A: AvroSchema_A = AvroSchema_A.createMap(booleanAvro_A)
	val mapStrAvro_A: AvroSchema_A = AvroSchema_A.createMap(strAvro_A)
	val mapIntAvro_A: AvroSchema_A = AvroSchema_A.createMap(intAvro_A)
	val mapLongAvro_A: AvroSchema_A = AvroSchema_A.createMap(longAvro_A)
	val mapFloatAvro_A: AvroSchema_A = AvroSchema_A.createMap(floatAvro_A)
	val mapDoubleAvro_A: AvroSchema_A = AvroSchema_A.createMap(doubleAvro_A)
	val mapBytesAvro_A: AvroSchema_A = AvroSchema_A.createMap(bytesAvro_A)
	
	// Named type schema
	val namedTypeAvro_A: AvroSchema_A = AvroSchema_A.createRecord("NamedType", "doc for named type", "namespace for named type", /*isError =*/ false)
	// TODO add aliases??
	
	
	// Record schema
	// TODO add aliases?
	
	import testData.schemaData.avroData.apacheData.FieldOrderData._
	
	
	val recordAvro_A: AvroSchema_A = AvroSchema_A.createRecord("Record", "doc for record", "namespace for record", /*isError =*/ false, List(strFieldAvro_1_oa, strFieldAvro_2_oa, strFieldAvro_3_oa, strFieldAvro_1_od, strFieldAvro_2_od, strFieldAvro_3_od, strFieldAvro_1_oi, strFieldAvro_2_oi, strFieldAvro_3_oi, intFieldAvro_1_oa, intFieldAvro_2_oa, intFieldAvro_3_oa, intFieldAvro_1_od, intFieldAvro_2_od, intFieldAvro_3_od, intFieldAvro_1_oi, intFieldAvro_2_oi, intFieldAvro_3_oi).asJava)
	
	// Union schema (union of schemas)
	val unionAvro_A: AvroSchema_A = AvroSchema_A.createUnion(List(intAvro_A, strAvro_A, floatAvro_A, arrayIntAvro_A).asJava)
	
	// Enum schema
	val enumAvro_A: AvroSchema_A = AvroSchema_A.createEnum("Color", "the colors of the rainbow with additional ones added in", "Color namespace string here", List("red", "orange", "yellow", "green", "pink", "magenta", "periwinkle", "tan", "jade", "blue", "indigo", "violet").asJava)
	
	// Fixed schema
	val fixedAvro_A: AvroSchema_A = AvroSchema_A.createFixed("Fixed", "doc for fixed", "namespace for fixed", 10) // TODO different sizes ? (does it matter?)
	
	
	
	
	// LOGICAL TYPES
	
	
	// Date logical type schema
	val intAvro_forDate: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.INT)
	val dateLogicalType: LogicalType_A = LogicalTypes_A.date()
	
	val dateAvro_A: AvroSchema_A = dateLogicalType.addToSchema(intAvro_forDate)
	
	
	// Time millis logical type schema
	val intAvro_forMillis: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.INT)
	val millisLogicalType: LogicalType_A = LogicalTypes_A.timeMillis()
	
	val millisAvro_A: AvroSchema_A = millisLogicalType.addToSchema(intAvro_forMillis)
	
	
	// Timestamp millis logical type schema
	val longAvro_forTimestamp: AvroSchema_A = AvroSchema_A.create(AvroSchema_A.Type.LONG)
	
	val timestampMillisLogicalType: LogicalType_A = LogicalTypes_A.timestampMillis()
	val timestampMillisSchema: AvroSchema_A = timestampMillisLogicalType.addToSchema(longAvro_forTimestamp)
	
	
	// Decimal logical type schema
	val fixedAvro_forDecimal: AvroSchema_A = AvroSchema_A.createFixed("FixedSchemaForDecimalLogicalType", null /*"doc_decimal_fixed"*/ , null, /*"decimal_namespace"*/ 20)
	
	
	import scala.util.Random
	
	
	// Formula for max precision of decimal logical type (source = https://github.com/apache/avro/blob/d02241e2adeb923e5b89b57f48c19c40ad4fbb07/lang/java/avro/src/main/java/org/apache/avro/LogicalTypes.java#L318-L329)
	final val MAX_PRECISION: Long = Math.floor(Math.log10(2.0) * (8 * fixedAvro_forDecimal.getFixedSize - 1).toDouble).round
	
	// NOTE: validate() function from LogicalTypes.java says we must have 0 <= scale <= precision <= maxPrecision
	// HELP what if this breaks because Long won't fit in Int? Need int to make the random number generate in range.
	val randPrecision: Int = 1 + Random.nextInt(MAX_PRECISION.toInt) // + 1 so it is not zero ever.
	val randScale: Int = 1 + Random.nextInt(randPrecision) // Random.between(1, randPrecision) (only for scala 2.13)
	
	val decimalLogicalType: LogicalType_A = LogicalTypes_A.decimal(randPrecision, randScale)
	
	val decimalSchema: AvroSchema_A = decimalLogicalType.addToSchema(fixedAvro_forDecimal)
}
