package testData.schemaData.jsonData.skeuoData

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import io.circe.Json
/**
 *
 */
object Data {
	
	// TODO find out if this is correct
	val nullJson_Skeuo: SchemaJson_Skeuo[Null] = ObjectF(properties = List(), required = List())
	val nullJson_Skeuo_C: SchemaJson_Skeuo[Json] = ObjectF(properties = List(), required = List())
	
	
	val intJson_Skeuo: SchemaJson_Skeuo[Int] = IntegerF()
	val intJson_Skeuo_C: SchemaJson_Skeuo[Json] = IntegerF()
	
	val arrJson_Skeuo: SchemaJson_Skeuo[Nothing] = ArrayF(IntegerF())
	val arrJson_Skeuo_C: SchemaJson_Skeuo[Json] = ArrayF(IntegerF())
}
