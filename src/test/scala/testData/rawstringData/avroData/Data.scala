package testData.rawstringData.avroData

/**
 *
 */
object Data {



	val nullAvro_R: String = "\"null\""

	val intAvro_R: String = "int"

	val strAvro_R: String = """string"""

	val booleanAvro_R: String = "\"boolean\""

	val longAvro_R: String = "\"long\""

	val floatAvro_R: String = "\"float\""

	val doubleAvro_R: String = "\"double\""

	val bytesAvro_R: String = "\"bytes\""

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
		  |  "values": "int"
		  |}
		  |""".stripMargin.trim


	val map1StrAvro_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "values": "string"
		  |}
		  |""".stripMargin.trim


	val map3IntAvro_R: String =
		"""
		  |{
		  |  "type": "map",
		  |  "values": {
		  |    "type": "map",
		  |    "values": {
		  |      "type": "map",
		  |      "values": "int"
		  |    }
		  |  }
		  |}
		  |""".stripMargin.trim


	// -------------------------

	// default order = Ascending, when not passing it in the constructor of Field
	val recordStrAvro_R: String =
		"""
		  |{
		  |  "type": "record",
		  |  "name": "RecordName",
		  |  "namespace": "Namespace",
		  |  "fields": [ {
		  |    "name": "FieldName1",
		  |    "type": "string"
		  |  } ],
		  |  "aliases": [ "alias1", "alias2" ]
		  |}
		  |""".stripMargin.trim()


	// NOTE: given, from the .avsc file (converted from .avdl)
	val recordExPositionAvro_avsc_R: String =
		"""
		  |{
		  |  "type": "record",
		  |  "name": "Position",
		  |  "fields": [
		  |    {
		  |      "name": "coordinates",
		  |      "type": {
		  |        "type": "array",
		  |        "items": "float"
		  |      }
		  |    },
		  |    {
		  |      "name": "type",
		  |      "type": "string"
		  |    }
		  |  ]
		  |}
		  |""".stripMargin.trim()

	// NOTE: from converting: avro-skeuo -> avro-str
	val recordExPositionAvro_conv_R: String =
		"""
		  |{
		  |  "type": "record",
		  |  "name": "Position",
		  |  "fields": [ {
		  |    "name": "coordinates",
		  |    "type": {
		  |      "type": "array",
		  |      "items": "float"
		  |    }
		  |  }, {
		  |    "name": "type",
		  |    "type": "string"
		  |  } ]
		  |}
		  |""".stripMargin

	val recordExLocationAvro_R: String = """"""




	// ---------------------------------------------

	// TODO convert to json string
	val enumAvro_R: String =
		"""{
		  |  "type": "enum",
		  |  "name": "Colors",
		  |  "symbols": [ "Red", "Orange", "Pink", "Yellow", "Green", "Blue", "Indigo", "Violet" ]
		  |}
		  |""".stripMargin

	// SOURCE of example = from velo.tf_devs.managed.avdl
	val enumExGeometryAvro_R: String =
		"""
		  |{
		  |  "type": "enum",
		  |  "name": "GeometryType",
		  |  "symbols": [
		  |    "Point",
		  |    "MultiPolygon"
		  |  ]
		  |}
		  |""".stripMargin


	// -------------------------------------------------------------------


	val unionAvro_R: String =
		"""
		  |{
		  |  "name": "symbol",
		  |  "type": [
		  |    "null",
		  |    "string"
		  |  ]
		  |}
		  |""".stripMargin


	// -------------------------------------------------------------------

	val namedTypeAvro_R: String =
		"""
		  |{
		  |  "name": "NamedTypeExample",
		  |  "namespace": "NamedTypeNamespace"
		  |}
		  |""".stripMargin

	// -------------------------------------------------------------------

	val fixedAvro_R: String =
		"""
		  |{
		  |  "type": "fixed",
		  |  "name": "FixedTypeName",
		  |  "size": 25
		  |}
		  |""".stripMargin

}
