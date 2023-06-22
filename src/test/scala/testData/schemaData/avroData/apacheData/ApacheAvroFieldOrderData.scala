package testData.schemaData.avroData.apacheData


import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.Schema.{Field ⇒ Field_A}
import org.apache.avro.Schema.Field.{Order ⇒ Order_A}
import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._


/**
 *
 */
object ApacheAvroFieldOrderData {
	val strField_1_oa: Field_A = new Field_A("field-str-ascending-1", strSchema_A, "doc-field-1", null, Order_A.ASCENDING)
	val strField_2_oa: Field_A = new Field_A("field-str-ascending-2", strSchema_A, null, null, Order_A.ASCENDING)
	val strField_3_oa: Field_A = new Field_A("field-str-ascending-3", strSchema_A, null, null, Order_A.ASCENDING)
	val strField_1_od: Field_A = new Field_A("field-str-descending-1", strSchema_A, null, null, Order_A.DESCENDING)
	val strField_2_od: Field_A = new Field_A("field-str-descending-2", strSchema_A, null, null, Order_A.DESCENDING)
	val strField_3_od: Field_A = new Field_A("field-str-descending-3", strSchema_A, null, null, Order_A.DESCENDING)
	val strField_1_oi: Field_A = new Field_A("field-str-ignore-1", strSchema_A, "doc-field-1", null, Order_A.IGNORE)
	val strField_2_oi: Field_A = new Field_A("field-str-ignore-2", strSchema_A, null, null, Order_A.IGNORE)
	val strField_3_oi: Field_A = new Field_A("field-str-ignore-3", strSchema_A, "doc-field-3", null, Order_A.IGNORE)
	
	val intField_1_oa: Field_A = new Field_A("field-int-ascending-1", intSchema_A, "doc-field-1", null, Order_A.ASCENDING)
	val intField_2_oa: Field_A = new Field_A("field-int-ascending-2", intSchema_A, null, null, Order_A.ASCENDING)
	val intField_3_oa: Field_A = new Field_A("field-int-ascending-3", intSchema_A, null, null, Order_A.ASCENDING)
	val intField_1_od: Field_A = new Field_A("field-int-descending-1", intSchema_A, null, null, Order_A.DESCENDING)
	val intField_2_od: Field_A = new Field_A("field-int-descending-2", intSchema_A, null, null, Order_A.DESCENDING)
	val intField_3_od: Field_A = new Field_A("field-int-descending-3", intSchema_A, null, null, Order_A.DESCENDING)
	val intField_1_oi: Field_A = new Field_A("field-int-ignore-1", intSchema_A, "doc-field-1", null, Order_A.IGNORE)
	val intField_2_oi: Field_A = new Field_A("field-int-ignore-2", intSchema_A, null, null, Order_A.IGNORE)
	val intField_3_oi: Field_A = new Field_A("field-int-ignore-3", intSchema_A, "doc-field-3", null, Order_A.IGNORE)
}
