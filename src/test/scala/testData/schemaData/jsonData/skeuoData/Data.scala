package testData.schemaData.jsonData.skeuoData

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import io.circe.Json
/**
 *
 */
object Data {
	
	// TODO find out if this is correct
	val nullJson_Skeuo: ObjectF[Null] = ObjectF(properties = List(), required = List())
	
	val nullJson_SkeuoC: SchemaJson_Skeuo[Json] = ObjectF(properties = List(), required = List())
	
	
	val intJson_Skeuo: IntegerF[Null] = IntegerF()
}
