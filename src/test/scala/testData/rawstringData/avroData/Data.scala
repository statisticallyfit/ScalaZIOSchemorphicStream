package testData.rawstringData.avroData

/**
 *
 */
object Data {

	
	val nullAvro_R: String = "null"
	
	val intAvro_R: String = "integer"
	
	val strAvro_R: String = "string"
	
	val booleanAvro_R: String = "boolean"
	
	val longAvro_R: String = "long"
	
	val floatAvro_R: String = "float"
	
	val doubleAvro_R: String = "double"
	
	val bytesAvro_R: String = "bytes"
	
	// -------------------------
	
	val array1IntAvro_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": "int"
		  |}
		  |""".stripMargin.trim
		  
	
	val array1StrAvro_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": "string"
		  |}
		  |""".stripMargin.trim
		  
	
	val array3IntAvro_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": {
		  |    "type": "array",
		  |    "items": {
		  |      "type": "array",
		  |      "items": "int"
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim
	
	// -------------------------
	val map1IntAvro_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "items": "int"
		  |}
		  |""".stripMargin.trim
	
	
	val map1StrAvro_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "items": "string"
		  |}
		  |""".stripMargin.trim
	
	
	val map3IntAvro_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "items": {
		  |    "type": "map",
		  |    "items": {
		  |      "type": "map",
		  |      "items": "int"
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim
		  
	
	// -------------------------
	
	
	val recordStrAvro_R: String =
		"""
		  |{
		  |  "type": "record",
		  |  "name": "StringRecord",
		  |  "namespace": "StringNamespace",
		  |  "fields": [ {
		  |    "name": "stringField1",
		  |    "type": "string",
		  |    "order": "ignore"
		  |  } ],
		  |  "aliases": [ "a1", "a2" ]
		  |}
		  |""".stripMargin
		/*"""
		  |{
		  |  "type": "record",
		  |  "name": "StringRecord",
		  |  "namespace": "StringNamespace",
		  |  "fields": [
		  |    {
		  |      "name": "stringField1",
		  |      "type": "string",
		  |      "order": "ignore"
		  |    }
		  |  ],
		  |  "aliases": [
		  |    "a1",
		  |    "a2"
		  |  ]
		  |}
		  |""".stripMargin.trim*/
		  
	
	val recordExPositionAvro_R: String = """"""
	val recordExLocationAvro_R: String = """"""
	
	
}
