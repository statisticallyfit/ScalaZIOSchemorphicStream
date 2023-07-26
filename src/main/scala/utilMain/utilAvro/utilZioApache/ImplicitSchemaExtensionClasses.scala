package utilMain.utilAvro.utilZioApache

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import zio.Chunk
import zio.schema.codec.AvroPropMarker
import zio.schema.{Schema ⇒ SchemaZIO}


/**
 * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L946-L958
 */
object ImplicitSchemaExtensionClasses {
	
	
	implicit private[utilZioApache] class SchemaExtensions(schema: SchemaZIO[_]) {
		
		def addAllAnnotations(annotations: Chunk[Any]): SchemaZIO[_] =
			annotations.foldLeft(schema)((schema, annotation) => schema.annotate(annotation))
	}
	
	implicit private[utilZioApache] class SchemaAvroExtensions(schemaAvro: SchemaAvro_Apache) {
		
		def addMarkerProp(propMarker: AvroPropMarker): SchemaAvro_Apache = {
			schemaAvro.addProp(propMarker.propName, propMarker.value)
			schemaAvro
		}
	}
	
}
