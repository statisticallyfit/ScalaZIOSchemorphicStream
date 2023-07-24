package testData.schemaData.jsonData.skeuoData


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._

import io.circe.{Json ⇒ JsonCirce}
/**
 *
 */
object Data {
	
	// TODO find out if this is correct
	val nullJson_S: JsonSchema_S[Null] = ObjectF(properties = List(), required = List())
	val nullJson_Circe_S: JsonSchema_S[JsonCirce] = ObjectF(properties = List(), required = List())
	val nullJson_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(properties = List(), required = List()))
	
	
	val strJson_S: JsonSchema_S[String] = StringF()
	val strJson_Circe_S: JsonSchema_S[JsonCirce] = StringF()
	val strJson_Fix_S: Fix[JsonSchema_S] = Fix(StringF())
	
	val intJson_S: JsonSchema_S[Null] = IntegerF()
	val intJson_Circe_S: JsonSchema_S[JsonCirce] = IntegerF()
	val intJson_Fix_S: Fix[JsonSchema_S] = Fix(IntegerF())
	
	
	val arrayIntJson_S: JsonSchema_S[JsonSchema_S[Int]] = ArrayF(IntegerF())
	val arrayIntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(IntegerF())
	val arrayIntJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(IntegerF())))
	
	val arrayStrJson_S: JsonSchema_S[JsonSchema_S[String]] = ArrayF(StringF())
	val arrayStrJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(StringF())
	val arrayStrJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(StringF())))
	
	
	val arrayIntAvro3_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Int]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val arrayIntAvro3_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(ArrayF(Fix(ArrayF(Fix(IntegerF())))))))
}
