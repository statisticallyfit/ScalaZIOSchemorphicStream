package testData.rawstringData.jsonData


import utilMain.UtilMain.implicits._

/**
 *
 */
object Data {
	
	
	val nullJson_R: String =
		"""
		  |{
		  |  "type": "object",
		  |  "properties": {},
		  |  "required": []
		  |}
		  |""".stripMargin.trim
	
	val intJson_R: String =
		"""
		  |{
		  |  "type": "integer"
		  |}
		  |""".stripMargin.trim
	
	
	val strJson_R: String =
		"""
		  |{
		  |  "type": "string"
		  |}
		  |""".stripMargin.trim
	
	val booleanJson_R: String =
		"""
		  |{
		  |  "type": "boolean"
		  |}
		  |""".stripMargin.trim
	
	
	val array1IntJson_R: String =
		"""
		  |{
		  |  "type" : "array",
		  |  "items" : {
		  |    "type" : "integer",
		  |    "format" : "int32"
		  |  }
		  |}
		  |""".stripMargin.manicure
		/*"""
		  |{
		  |  "type": "array",
		  |  "items": {
		  |    "type": "integer"
		  |  }
		  |}
		  |""".stripMargin*/
	
	val array1StrJson_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": {
		  |    "type": "string"
		  |  }
		  |}
		  |""".stripMargin.trim
	
	
	val array3IntJson_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": {
		  |    "type": "array",
		  |    "items": {
		  |      "type": "array",
		  |      "items": {
		  |        "type": "integer"
		  |      }
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim
	
	
	
	// TODO MAP
	
	
	// NOTE (from autoschema generation not from original json file)
	val recordEXPositionJson_R: String =
		"""
		  |{
		  |  "title": "Position",
		  |  "type": "object",
		  |  "required": [],
		  |  "properties": {
		  |    "coordinates": {
		  |      "type": "array",
		  |      "items": {
		  |        "type": "number",
		  |        "format": "number"
		  |      }
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim
	
	// NOTE (from autoschema generation not from original json file)
	val recordEXLocationJson_R: String =
		"""{
		  |  "title": "Locations",
		  |  "type": "object",
		  |  "required": [
		  |    "position",
		  |    "sensorName",
		  |    "name",
		  |    "id"
		  |  ],
		  |  "properties": {
		  |    "id": {
		  |      "type": "string"
		  |    },
		  |    "name": {
		  |      "type": "string"
		  |    },
		  |    "position": {
		  |      "title": "Position",
		  |      "type": "object",
		  |      "required": [],
		  |      "properties": {
		  |        "coordinates": {
		  |          "type": "array",
		  |          "items": {
		  |            "type": "number",
		  |            "format": "number"
		  |          }
		  |        }
		  |      }
		  |    },
		  |    "sensorName": {
		  |      "type": "string"
		  |    },
		  |    "symbol": {
		  |      "title": "Any",
		  |      "type": "object",
		  |      "required": [],
		  |      "properties": {}
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim
	
	
	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */
}
