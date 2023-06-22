package testData.schemaData.avroData.apacheData



import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.Schema.{Field ⇒ Field_A}
import org.apache.avro.Schema.Field.{Order ⇒ Order_A}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}

import scala.jdk.CollectionConverters._

/**
 *
 */
object ApacheAvroSchemaData  {
	
	// Primitive schemas
	val strSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.STRING)
	
	val intSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	
	val nullSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.NULL)
	
	val booleanSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BOOLEAN)
	
	val longSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.LONG)
	
	val floatSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.FLOAT)
	
	val doubleSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.DOUBLE)
	
	val bytesSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.BYTES)
	
	
	
	// Array schemas
	val arrayStrSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(strSchema_A)
	val arrayIntSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(intSchema_A)
	val arrayNullSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(nullSchema_A)
	val arrayBooleanSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(booleanSchema_A)
	val arrayLongSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(longSchema_A)
	val arrayFloatSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(floatSchema_A)
	val arrayDoubleSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(doubleSchema_A)
	val arrayBytesSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createArray(bytesSchema_A)
	
	//Map schema
	val mapStrSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(strSchema_A)
	val mapIntSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(intSchema_A)
	val mapNullSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(nullSchema_A)
	val mapBooleanSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(booleanSchema_A)
	val mapLongSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(longSchema_A)
	val mapFloatSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(floatSchema_A)
	val mapDoubleSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(doubleSchema_A)
	val mapBytesSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createMap(bytesSchema_A)
	
	// Named type schema
	val namedTypeSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("Named Type Thing", "doc for named type", "namespace for named type", /*isError =*/ false)
	// TODO add aliases??
	
	
	// Record schema
	// TODO add aliases?
	import testData.schemaData.avroData.apacheData.ApacheAvroFieldOrderData._
	
	val recordSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createRecord("Record Thing", "doc for record", "namespace for record", /*isError =*/ false, List(strField_1_oa, strField_2_oa, strField_3_oa, strField_1_od, strField_2_od, strField_3_od, strField_1_oi, strField_2_oi, strField_3_oi, intField_1_oa, intField_2_oa, intField_3_oa, intField_1_od, intField_2_od, intField_3_od, intField_1_oi, intField_2_oi, intField_3_oi).asJava)
	
	// Union schema (union of schemas)
	val unionSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createUnion(List(intSchema_A, strSchema_A, floatSchema_A, arrayIntSchema_A).asJava)
	
	// Enum schema
	val enumSchema_A: SchemaAvro_Apache = SchemaAvro_Apache.createEnum("Color", "the colors of the rainbow with additional ones added in", "Color namespace string here", List("red", "orange", "yellow", "green", "pink", "magenta", "periwinkle", "tan", "jade", "blue", "indigo", "violet").asJava)

	// Fixed schema
	
	// LOGICAL TYPES
	
	// Date logical type schema
	
	// Time millis logical type schema
	
	// Timestamp millis logical type schema
	
	// Decimal logical type schema
}
