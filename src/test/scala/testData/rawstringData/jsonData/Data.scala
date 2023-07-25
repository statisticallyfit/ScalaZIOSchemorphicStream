package testData.rawstringData.jsonData

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
		  |""".stripMargin
	
	val intJson_R: String =
		"""
		  |{
		  |  "type": "integer"
		  |}
		  |""".stripMargin
		  
	
	val strJson_R: String =
		"""
		  |{
		  |  "type": "string"
		  |}
		  |""".stripMargin
		  

	val array1StrJson_R: String =
		"""
		  |{
		  |  "type": "array",
		  |  "items": {
		  |    "type": "string"
		  |  }
		  |}
		  |""".stripMargin
		  
	
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
		  |""".stripMargin
	
	
	
	// TODO MAP
	
	
	
	// NOTE (from autoschema generation not from original json file)
	val recordExampleJson_Position_R: String =
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
		  |""".stripMargin
		  
	// NOTE (from autoschema generation not from original json file)
	val recordExampleJson_Location_R: String =
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
		  |""".stripMargin
	
	
	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */
}
