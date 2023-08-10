package testData.rawstringData.avroData

/**
 *
 */
object Data {

	
	val nullAvro_R: String = """null""" // TODO
	
	val intAvro_R: String = """integer"""
	
	val strAvro_R: String = """string"""
	
	val booleanAvro_R: String = "boolean"
	
	
	val array1IntAvro_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": "int"
		  |}
		  |""".stripMargin
}
