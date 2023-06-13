package utilMain.utilSkeuoApache


import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}
import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldSkeuo, Order ⇒ OrderSkeuo}

import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.Schema.{Field ⇒ FieldApache}
import org.apache.avro.Schema.Field.{Order ⇒ OrderApache}

import scala.jdk.CollectionConverters._
import java.util


/**
 *
 */
object FieldAndOrderConversions {


  type OrderS = OrderSkeuo
  type OrderA = OrderApache
  type FieldA = FieldApache
  type FieldS[T] = FieldSkeuo[T]


  def order2Order_AS: OrderA ⇒ OrderS = SchemaSkeuoAvro.order2Order

  def field2Field_AS: FieldA ⇒ FieldS[SchemaApacheAvro] = SchemaSkeuoAvro.field2Field

  def order2Order_SA: OrderS ⇒ OrderA = (orderS: OrderS) ⇒ orderS match {
    case OrderSkeuo.Ascending ⇒ OrderApache.ASCENDING
    case OrderSkeuo.Descending ⇒ OrderApache.DESCENDING
    case OrderSkeuo.Ignore ⇒ OrderApache.IGNORE
  }

  def field2Field_SA: FieldS[SchemaApacheAvro] ⇒ FieldA =
    (fieldS: FieldS[SchemaApacheAvro]) ⇒ {
      //val t: util.List[String] = fieldS.aliases.asJava

      val fieldApache_name: String = fieldS.name
      val fieldApache_schema: SchemaApacheAvro = fieldS.tpe
      val fieldApache_doc: String = fieldS.doc.getOrElse("NO DOC")
      val fieldApache_defaultValue: util.List[String] = fieldS.aliases.asJava // HELP: apache has no 'aliases' while skeuo does and skeuo has no 'default value' while apache does -- are they the same?
      val fieldApache_order: OrderA = order2Order_SA(fieldS.order.getOrElse(OrderSkeuo.Ignore))

      new FieldApache(fieldApache_name, fieldApache_schema, fieldApache_doc, fieldApache_defaultValue, fieldApache_order)
    }

}
