package testData.schemaData.genericSchemaData.zioData

import testData.ScalaCaseClassData._


import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ SchemaGeneric_Zio, StandardType ⇒ StandardType_Zio} // TODO move the zio <-> apache tests in the other file




/**
 *
 */
object Data {
	
	
	val tangeloGeneric_Zio: SchemaGeneric_Zio[Tangelo] = DeriveSchema.gen[Tangelo]

}
