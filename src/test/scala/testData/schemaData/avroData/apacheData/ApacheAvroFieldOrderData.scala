package testData.schemaData.avroData.apacheData


import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.Schema.{Field ⇒ Field_A}
import org.apache.avro.Schema.Field.{Order ⇒ Order_A}
import testData.schemaData.avroData.apacheData.ApacheAvroSchemaData._


/**
 *
 */
object ApacheAvroFieldOrderData {
	val strField_1_oa: Field_A = new Field_A("fieldStrAscending1", strSchema_Apache, "doc-field-1", null, Order_A.ASCENDING)
	val strField_2_oa: Field_A = new Field_A("fieldStrAscending2", strSchema_Apache, null, null, Order_A.ASCENDING)
	val strField_3_oa: Field_A = new Field_A("fieldStrAscending3", strSchema_Apache, null, null, Order_A.ASCENDING)
	val strField_1_od: Field_A = new Field_A("fieldStrDescending1", strSchema_Apache, null, null, Order_A.DESCENDING)
	val strField_2_od: Field_A = new Field_A("fieldStrDescending2", strSchema_Apache, null, null, Order_A.DESCENDING)
	val strField_3_od: Field_A = new Field_A("fieldStrDescending3", strSchema_Apache, null, null, Order_A.DESCENDING)
	val strField_1_oi: Field_A = new Field_A("fieldStrIgnore1", strSchema_Apache, "doc-field-1", null, Order_A.IGNORE)
	val strField_2_oi: Field_A = new Field_A("fieldStrIgnore2", strSchema_Apache, null, null, Order_A.IGNORE)
	val strField_3_oi: Field_A = new Field_A("fieldStrIgnore3", strSchema_Apache, "doc-field-3", null, Order_A.IGNORE)
	
	val intField_1_oa: Field_A = new Field_A("fieldIntAscending1", intSchema_Apache, "doc-field-1", null, Order_A.ASCENDING)
	val intField_2_oa: Field_A = new Field_A("fieldIntAscending2", intSchema_Apache, null, null, Order_A.ASCENDING)
	val intField_3_oa: Field_A = new Field_A("fieldIntAscending3", intSchema_Apache, null, null, Order_A.ASCENDING)
	val intField_1_od: Field_A = new Field_A("fieldIntDescending1", intSchema_Apache, null, null, Order_A.DESCENDING)
	val intField_2_od: Field_A = new Field_A("fieldIntDescending2", intSchema_Apache, null, null, Order_A.DESCENDING)
	val intField_3_od: Field_A = new Field_A("fieldIntDescending3", intSchema_Apache, null, null, Order_A.DESCENDING)
	val intField_1_oi: Field_A = new Field_A("fieldIntIgnore1", intSchema_Apache, "doc-field-1", null, Order_A.IGNORE)
	val intField_2_oi: Field_A = new Field_A("fieldIntIgnore2", intSchema_Apache, null, null, Order_A.IGNORE)
	val intField_3_oi: Field_A = new Field_A("fieldIntIgnore3", intSchema_Apache, "doc-field-3", null, Order_A.IGNORE)
}
