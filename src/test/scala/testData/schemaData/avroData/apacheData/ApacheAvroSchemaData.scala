package testData.schemaData.avroData.apacheData



import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}

import scala.jdk.CollectionConverters._

/**
 *
 */
object ApacheAvroSchemaData {
	val strApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)
	
	val intApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
	val arrayApache: SchemaApacheAvro = SchemaApacheAvro.createArray(intApache)
	
	val enumApache: SchemaApacheAvro = SchemaApacheAvro.createEnum("Color", "doc", "namespace", List("red", "yellow", "blue").asJava)
	
}
