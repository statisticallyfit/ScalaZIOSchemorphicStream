package testData.schemaData.genericSchemaData.zioData

import testData.ScalaCaseClassData._
import zio.schema.{DeriveSchema, Schema ⇒ ZioGenericSchema} // TODO move the zio <-> apache tests in the other file


/**
 *
 */
object Data {
	
	
	val tangeloGeneric_Zio: ZioGenericSchema[Tangelo] = DeriveSchema.gen[Tangelo]
	
}
