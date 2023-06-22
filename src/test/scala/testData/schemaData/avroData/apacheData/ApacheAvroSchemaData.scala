package testData.schemaData.avroData.apacheData



import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}

import scala.jdk.CollectionConverters._

/**
 *
 */
object ApacheAvroSchemaData  {
	val strApache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.STRING)
	
	val intApache: SchemaAvro_Apache = SchemaAvro_Apache.create(SchemaAvro_Apache.Type.INT)
	val arrayIntApache: SchemaAvro_Apache = SchemaAvro_Apache.createArray(intApache)
	
	val enumApache: SchemaAvro_Apache = SchemaAvro_Apache.createEnum("Color", "doc", "namespace", List("red", "yellow", "blue").asJava)


	/*println(arrayIntApache)
	println(arrayIntApache.toString)*/
	/*println(arrayIntApache.getElementType.toString)
	
	println(arrayIntApache.getElementType.getType)*/
	//println(s"${arrayIntApache.getElementType}")
	//println(s"something: ${arrayIntApache.getElementType.toString}")
	//println(s"'\'{\"tpe\":\"array\"}")
	// TODO why this error?: ')' expected but string literal found.
	//println(s"{\"mango\":\"array\",\"items\":${arrayIntApache.getElementType}}")
}
