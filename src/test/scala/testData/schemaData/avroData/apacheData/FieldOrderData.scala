package testData.schemaData.avroData.apacheData


import org.apache.avro.Schema.Field.{Order ⇒ Order_A}
import org.apache.avro.Schema.{Field ⇒ FieldAvro_A}
import testData.schemaData.avroData.apacheData.Data._


/**
 *
 */
object FieldOrderData {
	val strFieldAvro_1_oa: FieldAvro_A = new FieldAvro_A("fieldStrAscending1", strAvro_A, "doc-field-1", null, Order_A.ASCENDING)
	val strFieldAvro_2_oa: FieldAvro_A = new FieldAvro_A("fieldStrAscending2", strAvro_A, null, null, Order_A.ASCENDING)
	val strFieldAvro_3_oa: FieldAvro_A = new FieldAvro_A("fieldStrAscending3", strAvro_A, null, null, Order_A.ASCENDING)
	val strFieldAvro_1_od: FieldAvro_A = new FieldAvro_A("fieldStrDescending1", strAvro_A, null, null, Order_A.DESCENDING)
	val strFieldAvro_2_od: FieldAvro_A = new FieldAvro_A("fieldStrDescending2", strAvro_A, null, null, Order_A.DESCENDING)
	val strFieldAvro_3_od: FieldAvro_A = new FieldAvro_A("fieldStrDescending3", strAvro_A, null, null, Order_A.DESCENDING)
	val strFieldAvro_1_oi: FieldAvro_A = new FieldAvro_A("fieldStrIgnore1", strAvro_A, "doc-field-1", null, Order_A.IGNORE)
	val strFieldAvro_2_oi: FieldAvro_A = new FieldAvro_A("fieldStrIgnore2", strAvro_A, null, null, Order_A.IGNORE)
	val strFieldAvro_3_oi: FieldAvro_A = new FieldAvro_A("fieldStrIgnore3", strAvro_A, "doc-field-3", null, Order_A.IGNORE)
	
	val intFieldAvro_1_oa: FieldAvro_A = new FieldAvro_A("fieldIntAscending1", intAvro_A, "doc-field-1", null, Order_A.ASCENDING)
	val intFieldAvro_2_oa: FieldAvro_A = new FieldAvro_A("fieldIntAscending2", intAvro_A, null, null, Order_A.ASCENDING)
	val intFieldAvro_3_oa: FieldAvro_A = new FieldAvro_A("fieldIntAscending3", intAvro_A, null, null, Order_A.ASCENDING)
	val intFieldAvro_1_od: FieldAvro_A = new FieldAvro_A("fieldIntDescending1", intAvro_A, null, null, Order_A.DESCENDING)
	val intFieldAvro_2_od: FieldAvro_A = new FieldAvro_A("fieldIntDescending2", intAvro_A, null, null, Order_A.DESCENDING)
	val intFieldAvro_3_od: FieldAvro_A = new FieldAvro_A("fieldIntDescending3", intAvro_A, null, null, Order_A.DESCENDING)
	val intFieldAvro_1_oi: FieldAvro_A = new FieldAvro_A("fieldIntIgnore1", intAvro_A, "doc-field-1", null, Order_A.IGNORE)
	val intFieldAvro_2_oi: FieldAvro_A = new FieldAvro_A("fieldIntIgnore2", intAvro_A, null, null, Order_A.IGNORE)
	val intFieldAvro_3_oi: FieldAvro_A = new FieldAvro_A("fieldIntIgnore3", intAvro_A, "doc-field-3", null, Order_A.IGNORE)
}
