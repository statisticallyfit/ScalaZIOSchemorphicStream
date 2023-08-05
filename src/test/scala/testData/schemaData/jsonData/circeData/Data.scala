package testData.schemaData.jsonData.circeData

import io.circe.{Json ⇒ JsonCirce}

import testData.rawstringData.jsonData.Data._

import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._

/**
 *
 */
object Data {
	
	
	val nullJson_C: JsonCirce = unsafeParse(nullJson_R)
	
	val intJson_C: JsonCirce = unsafeParse(intJson_R)
	
	val strJson_C: JsonCirce = unsafeParse(strJson_R)
	
	
	val array1IntJson_C: JsonCirce = unsafeParse(array1IntJson_R)
	val array1StrJson_C: JsonCirce = unsafeParse(array1StrJson_R)
	val array3IntJson_C: JsonCirce = unsafeParse(array3IntJson_R)
	
}
