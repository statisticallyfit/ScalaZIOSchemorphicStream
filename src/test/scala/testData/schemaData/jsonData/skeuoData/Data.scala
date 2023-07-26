package testData.schemaData.jsonData.skeuoData


import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}


/**
 *
 */
object Data {
	
	// TODO find out if this is correct
	val nullbase: JsonSchema_S[JsonSchema_S[Null]] = ObjectF(List(
		Property(name = "null", tpe = StringF())
	), List())
	// TODO or is it this one?
	//ObjectF(properties = List(), required = List())
	
	val nullJson_S: JsonSchema_S[JsonSchema_S[Null]] = nullbase
	
	val nullJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ObjectF(List(
		Property(name = "null", tpe = StringF())
	), List())
	val nullJson_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(List(
		Property(name = "null", tpe = Fix(StringF()))
	), List()))
	
	
	val strJson_S: JsonSchema_S[String] = StringF()
	val strJson_Circe_S: JsonSchema_S[JsonCirce] = StringF()
	val strJson_Fix_S: Fix[JsonSchema_S] = Fix(StringF())
	
	val intJson_S: JsonSchema_S[Null] = IntegerF()
	val intJson_Circe_S: JsonSchema_S[JsonCirce] = IntegerF()
	val intJson_Fix_S: Fix[JsonSchema_S] = Fix(IntegerF())
	
	
	val array1IntJson_S: JsonSchema_S[JsonSchema_S[Int]] = ArrayF(IntegerF())
	val array1IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(IntegerF())
	val array1IntJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(IntegerF())))
	
	val array1StrJson_S: JsonSchema_S[JsonSchema_S[String]] = ArrayF(StringF())
	val array1StrJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(StringF())
	val array1StrJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(StringF())))
	
	
	val array3IntJson_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Int]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val array3IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val array3IntJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(ArrayF(Fix(ArrayF(Fix(IntegerF())))))))
	
	
	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */
	
	
	// TODO test Nothing vs. Float (because of innermost type at 'tpe'
	val recordExampleJson_Position_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[Float]]] = ObjectF(
		properties = List(
			Property(name = "coordinates", tpe = ArrayF(FloatF()))
		),
		required = List()
	)
	val recordExampleJson_Position_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]] = ObjectF(
		properties = List(
			Property(name = "coordinates", tpe = ArrayF(FloatF()))
		),
		required = List()
	)
	val recordExampleJson_Position_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(
		properties = List(
			Property(name = "coordinates", tpe = Fix(ArrayF(Fix(FloatF()))))
		),
		required = List()
	))
	
	
	// TODO test diff between Float and Nothing in innermost type.
	val recordExampleJson_Location_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Float]]]] = ObjectF(
		properties = List(
			Property(name = "name", tpe = StringF()),
			Property(name = "symbol", tpe = ObjectF(
				properties = List(),
				required = List())
			),
			Property(name = "id", tpe = StringF()),
			Property(name = "sensorName", tpe = StringF()),
			Property(name = "position", tpe = ObjectF(
				properties = List(
					Property(name = "coordinates", tpe = ArrayF(FloatF()))
				),
				required = List())
			)),
		required = List("position", "sensorName", "name", "id")
	)
	
	val recordExampleJson_Location_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = ObjectF(
		properties = List(
			Property(name = "name", tpe = StringF()),
			Property(name = "symbol", tpe = ObjectF(
				properties = List(),
				required = List())
			),
			Property(name = "id", tpe = StringF()),
			Property(name = "sensorName", tpe = StringF()),
			Property(name = "position", tpe = ObjectF(
				properties = List(
					Property(name = "coordinates", tpe = ArrayF(FloatF()))
				),
				required = List())
			)),
		required = List("position", "sensorName", "name", "id")
	)
	
	val recordExampleJson_Location_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(
		properties = List(
			Property(name = "name", tpe = Fix(StringF())),
			Property(name = "symbol", tpe = Fix(ObjectF(
				properties = List(),
				required = List()
			))),
			Property(name = "id", tpe = Fix(StringF())),
			Property(name = "sensorName", tpe = Fix(StringF())),
			Property(name = "position", tpe = Fix(ObjectF(
				properties = List(
					Property(name = "coordinates", tpe = Fix(ArrayF(Fix(FloatF()))))
				),
				required = List())
			))),
		required = List("position", "sensorName", "name", "id")
	))
}