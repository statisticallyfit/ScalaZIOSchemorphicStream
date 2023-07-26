package utilMain.utilAvro.utilSkeuoApache

import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldSkeuo, Order ⇒ OrderSkeuo}
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import org.apache.avro.Schema.Field.{Order ⇒ OrderApache}
import org.apache.avro.Schema.{Field ⇒ FieldApache}
import org.apache.avro.{Schema ⇒ AvroSchema_A}


/**
 *
 */
object FieldAndOrderConversions {
	
	
	type OrderS = OrderSkeuo
	type OrderA = OrderApache
	type FieldA = FieldApache
	type FieldS[T] = FieldSkeuo[T]
	
	
	def order2Order_AS: OrderA ⇒ OrderS = AvroSchema_S.order2Order
	
	def field2Field_AS: FieldA ⇒ FieldS[AvroSchema_A] = AvroSchema_S.field2Field
	
	def order2Order_SA: OrderS ⇒ OrderA = (orderS: OrderS) ⇒ orderS match {
		case OrderSkeuo.Ascending ⇒ OrderApache.ASCENDING
		case OrderSkeuo.Descending ⇒ OrderApache.DESCENDING
		case OrderSkeuo.Ignore ⇒ OrderApache.IGNORE
	}
	
	def field2Field_SA: FieldS[AvroSchema_A] ⇒ FieldA =
		(fieldS: FieldS[AvroSchema_A]) ⇒ {
			//val t: util.List[String] = fieldS.aliases.asJava
			
			val fName: String = fieldS.name
			val fSchema: AvroSchema_A = fieldS.tpe
			//val fieldApache_doc: String = fieldS.doc.getOrElse("") //("NO DOC")
			val fDoc: Null = null
			val fDefaultValue: Null = null // NOTE: put assumption that null means it does not appear when printed in the Apache Schema
			// Functions references:
			// 	validateDefault (in Schema.java of org.apache)
			// 	isValidDefault (in Schema.java of org.apache)
			// 	isTextual() from JsonNodeType.java (jackson library)
			
			//: JsonNodeType = JsonNodeType.STRING // TODO temporary to make the string test pass.
			//FieldApache.NULL_DEFAULT_VALUE
			//val fieldApache_defaultValue: util.List[String] = fieldS.aliases.asJava // HELP: apache has no 'aliases' while skeuo does and skeuo has no 'default value' while apache does -- are they the same?
			val fOrder: OrderA = order2Order_SA(fieldS.order.getOrElse(OrderSkeuo.Ignore))
			
			new FieldApache(fName, fSchema, fDoc, fDefaultValue, fOrder)
		}
	
}
