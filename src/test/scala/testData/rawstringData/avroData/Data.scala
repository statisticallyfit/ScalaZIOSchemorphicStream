package testData.rawstringData.avroData

/**
 *
 */
object Data {

	
	val nullAvro_R: String = """null""" // TODO
	
	val intAvro_R: String = """integer"""
	
	val strAvro_R: String = """string"""
	
	val booleanAvro_R: String = """boolean"""
	
	
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
		  |""".stripMargin
		  
	
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
		  
	
	val recordStrAvro_R: String =
		"""
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
		  |""".stripMargin.trim
		  
	
	val recordEXPositionAvro_R: String = """"""
	val recordEXLocationAvro_R: String = """"""
	
	
}
