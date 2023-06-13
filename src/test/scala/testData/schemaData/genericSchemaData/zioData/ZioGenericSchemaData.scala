package testData.schemaData.genericSchemaData.zioData

import testData.ScalaCaseClassData._


import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType} // TODO move the zio <-> apache tests in the other file




/**
 *
 */
object ZioGenericSchemaData {
	
	
	val tangeloZioSchema: ZioSchema[Tangelo] = DeriveSchema.gen[Tangelo]

}
