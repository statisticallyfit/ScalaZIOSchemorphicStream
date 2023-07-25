package testData.schemaData.jsonData.skeuoData


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import io.circe.{Json ⇒ JsonCirce}


/**
 *
 */
object Data {
	
	// TODO find out if this is correct
	val nullJson_S: SchemaJson_Skeuo[Null] = ObjectF(properties = List(), required = List())
	val nullJson_Circe_S: SchemaJson_Skeuo[JsonCirce] = ObjectF(properties = List(), required = List())
	val nullJson_Fix_S: Fix[SchemaJson_Skeuo] = Fix(ObjectF(properties = List(), required = List()))
	
	
	val strJson_S: SchemaJson_Skeuo[String] = StringF()
	val strJson_Circe_S: SchemaJson_Skeuo[JsonCirce] = StringF()
	val strJson_Fix_S: Fix[SchemaJson_Skeuo] = Fix(StringF())
	
	val intJson_S: SchemaJson_Skeuo[Null] = IntegerF()
	val intJson_Circe_S: SchemaJson_Skeuo[JsonCirce] = IntegerF()
	val intJson_Fix_S: Fix[SchemaJson_Skeuo] = Fix(IntegerF())
	
	
	val arrayIntJson_S: SchemaJson_Skeuo[SchemaJson_Skeuo[Int]] = ArrayF(IntegerF())
	val arrayIntJson_Circe_S: SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]] = ArrayF(IntegerF())
	val arrayIntJson_Fix_S: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(IntegerF())))
	
	val arrayStrJson_S: SchemaJson_Skeuo[SchemaJson_Skeuo[String]] = ArrayF(StringF())
	val arrayStrJson_Circe_S: SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]] = ArrayF(StringF())
	val arrayStrJson_Fix_S: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(StringF())))
	
	
	val arrayIntAvro3_S: SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[Int]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Circe_S: SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Fix_S: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(ArrayF(Fix(ArrayF(Fix(IntegerF())))))))
}