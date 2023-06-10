package testUtil.utilZio


import org.apache.avro.{Schema ⇒ SchemaApacheAvro}

import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType}


import zio.Chunk
import zio.schema.codec.AvroPropMarker


/**
 * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L946-L958
 */
object ImplicitSchemaExtensionClasses {


  implicit private[utilZio] class SchemaExtensions(schema: ZioSchema[_]) {

    def addAllAnnotations(annotations: Chunk[Any]): ZioSchema[_] =
      annotations.foldLeft(schema)((schema, annotation) => schema.annotate(annotation))
  }

  implicit private[utilZio] class SchemaAvroExtensions(schemaAvro: SchemaApacheAvro) {

    def addMarkerProp(propMarker: AvroPropMarker): SchemaApacheAvro = {
      schemaAvro.addProp(propMarker.propName, propMarker.value)
      schemaAvro
    }
  }

}
