package testData.rawstringData.jsonData


import utilMain.UtilMain.implicits._

/**
 *
 */
object Data {

	// TODO which definition to use for null?

	// (1) From json-skeuo:
	// ObjectF(properties = List(), required = List())

	// Wrong - yields Left decoding error ' not well formed type' when converting funCirceToJson/AvroSkeuo
	/*val nullJson_simple_R: String =
	"""
	  |{
	  |  "type": "null"
	  |}
	  |""".stripMargin.trim()*/

	val nullJson_R: String =
		"""
		  |{
		  |  "type": "object",
		  |  "properties": {},
		  |  "required": []
		  |}
		  |""".stripMargin.trim

	 // (2) From json-skeuo:
	// ObjectF(List(Property(name = "null", tpe = StringF())), List())
	val nullJson_complicated_R: String = """
	  |{
	  |  "type": "object",
	  |  "properties": {
	  |    "null": {
	  |      "type": "string"
	  |    }
	  |  },
	  |  "required": []
	  |}
	  |""".stripMargin



	val intJson_R: String =
		"""
		  |{
		  |  "type": "integer",
		  |  "format": "int32"
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


	val longJson_R: String =
		"""
		  |{
		  |  "type": "integer",
		  |  "format": "int64"
		  |}
		  |""".stripMargin.trim

	val floatJson_R: String =
		"""
		  |{
		  |  "type": "number",
		  |  "format": "float"
		  |}
		  |""".stripMargin.trim

	val doubleJson_R: String =
		"""
		  |{
		  |  "type": "number",
		  |  "format": "double"
		  |}
		  |""".stripMargin.trim


	val bytesJson_R: String =
		"""
		  |{
		  |  "type": "string",
		  |  "format": "byte"
		  |}
		  |""".stripMargin.trim()


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
		  |        "type": "integer",
		  |        "format": "int32"
		  |      }
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim


	val map1IntJson_R: String =
		"""
		  |{
		  |  "title": "TITLE_NAME",
		  |  "type": "object",
		  |  "additionalProperties": {
		  |     "type": "integer",
		  |     "format": "int32"
		  |  }
		  |}
		  |""".stripMargin.manicure

	val map1StrJson_R: String =
		"""
		  |{
		  |  "type": "object",
		  |  "additionalProperties": {
		  |    "type": "string"
		  |  }
		  |}
		  |""".stripMargin.trim()
		/*"""
		  |{
		  |  "type": "map",
		  |  "values": {
		  |    "type": "string"
		  |  }
		  |}
		  |""".stripMargin.trim*/


	val map3IntJson_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "values": {
		  |    "type": "map",
		  |    "values": {
		  |      "type": "map",
		  |      "values": {
		  |        "type": "integer",
		  |        "format": "int32"
		  |      }
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim



	// TODO: find a way to write the json-skeuo equivalents so that when converted to json-circe the results contain the title too.

	// NOTE (from autoschema generation not from original json file)
	// TODO warning - this string does not contain the 'type' field while the skeuo-json equivalent DOES (to match the avro-skeuo (and the avro-skeuo has the 'type' field because the avro-str has it))
	val recordExPositionJson_AR: String =
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
		  |""".stripMargin.trim()
//	val map1StrJson_R: String =
//		"""
//		  |{
//		  |  "type": "object",
//		  |  "additionalProperties": {
//		  |    "type": "string"
//		  |  }
//		  |}
//		  |""".stripMargin.trim()

	// NOTE: from tati original data file
	val recordExPositionJson_R: String =
		"""
		  |{
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
		  |""".stripMargin.trim()
		  /*
		  //TODO how to include the topo name - 'properties'?
		{
			"title": "Position",
			"type": "object",
			"required": [],
			"properties": {
				"coordinates": {
					"type": "array",
					"items": {
						"type": "number",
						"format": "number"
					}
				}
			}
		}
		   */

	// NOTE (from autoschema generation not from original json file)
	val recordExLocationJson_R: String =
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

	// ---------------------------------------------

	val enumJson_R: String =
		"""
		  |{
		  |  "title": "TheColorsEnum",
		  |  "type": "object",
		  |  "required": [
		  |    "value"
		  |  ],
		  |  "properties": {
		  |    "value": {
		  |      "type": "number",
		  |      "format": "number"
		  |    }
		  |  }
		  |}
		  |""".stripMargin
}
