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
	val nullJson_Skeuo: SchemaJson_Skeuo[Null] = ObjectF(properties = List(), required = List())
	val nullJson_Skeuo_C: SchemaJson_Skeuo[JsonCirce] = ObjectF(properties = List(), required = List())
	val nullJson_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(ObjectF(properties = List(), required = List()))
	
	
	val strJson_Skeuo: SchemaJson_Skeuo[String] = StringF()
	val strJson_Skeuo_C: SchemaJson_Skeuo[JsonCirce] = StringF()
	val strJson_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(StringF())
	
	val intJson_Skeuo: SchemaJson_Skeuo[Null] = IntegerF()
	val intJson_Skeuo_C: SchemaJson_Skeuo[JsonCirce] = IntegerF()
	val intJson_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(IntegerF())
	
	
	val arrayIntJson_Skeuo: SchemaJson_Skeuo[SchemaJson_Skeuo[Int]] = ArrayF(IntegerF())
	val arrayIntJson_Skeuo_C: SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]] = ArrayF(IntegerF())
	val arrayIntJson_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(IntegerF())))
	
	val arrayStrJson_Skeuo: SchemaJson_Skeuo[SchemaJson_Skeuo[String]] = ArrayF(StringF())
	val arrayStrJson_Skeuo_C: SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]] = ArrayF(StringF())
	val arrayStrJson_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(StringF())))
	
	
	val arrayIntAvro3_Skeuo: SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[Int]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Skeuo_C: SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[SchemaJson_Skeuo[JsonCirce]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Skeuo_F: Fix[SchemaJson_Skeuo] = Fix(ArrayF(Fix(ArrayF(Fix(ArrayF(Fix(IntegerF())))))))
}
