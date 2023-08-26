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
	val nullJson_Fix_S_complicated: Fix[JsonSchema_S] = Fix(ObjectF(List(
		Property(name = "null", tpe = Fix(StringF()))
	), List()))

	val nullJson_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(properties = List(), required = List()))


	val strJson_S: JsonSchema_S[String] = StringF()
	val strJson_Circe_S: JsonSchema_S[JsonCirce] = StringF()
	val strJson_Fix_S: Fix[JsonSchema_S] = Fix(StringF())

	val intJson_S: JsonSchema_S[Null] = IntegerF()
	val intJson_Circe_S: JsonSchema_S[JsonCirce] = IntegerF()
	val intJson_Fix_S: Fix[JsonSchema_S] = Fix(IntegerF())


	val booleanJson_S: JsonSchema_S[Boolean] = BooleanF()
	val booleanJson_Circe_S: JsonSchema_S[JsonCirce] = BooleanF()
	val booleanJson_Fix_S: Fix[JsonSchema_S] = Fix(BooleanF())


	val longJson_S: JsonSchema_S[Long] = LongF()
	val longJson_Circe_S: JsonSchema_S[JsonCirce] = LongF()
	val longJson_Fix_S: Fix[JsonSchema_S] = Fix(LongF())

	val floatJson_S: JsonSchema_S[Float] = FloatF()
	val floatJson_Circe_S: JsonSchema_S[JsonCirce] = FloatF()
	val floatJson_Fix_S: Fix[JsonSchema_S] = Fix(FloatF())


	val doubleJson_S: JsonSchema_S[Double] = DoubleF()
	val doubleJson_Circe_S: JsonSchema_S[JsonCirce] = DoubleF()
	val doubleJson_Fix_S: Fix[JsonSchema_S] = Fix(DoubleF())

	val bytesJson_S: JsonSchema_S[Byte] = ByteF()
	val bytesJson_Circe_S: JsonSchema_S[JsonCirce] = ByteF()
	val bytesJson_Fix_S: Fix[JsonSchema_S] = Fix(ByteF())

	val array1IntJson_S: JsonSchema_S[JsonSchema_S[Int]] = ArrayF(IntegerF())
	val array1IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(IntegerF())
	val array1IntJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(IntegerF())))

	val array1StrJson_S: JsonSchema_S[JsonSchema_S[String]] = ArrayF(StringF())
	val array1StrJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = ArrayF(StringF())
	val array1StrJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(StringF())))


	val array3IntJson_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Int]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val array3IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = ArrayF(ArrayF(ArrayF(IntegerF())))
	val array3IntJson_Fix_S: Fix[JsonSchema_S] = Fix(ArrayF(Fix(ArrayF(Fix(ArrayF(Fix(IntegerF())))))))



	// TODO - map is an ObjectF must think about this
	/*val map1IntJson_mapname_S: JsonSchema_S[JsonSchema_S[Int]] = ObjectF(
		properties = List(
			Property(name = "map", tpe = IntegerF())
		),
		required = List()
	)
	val map1IntJson_objectname_S: JsonSchema_S[JsonSchema_S[Int]] = ObjectF(
		properties = List(
			Property(name = "object", tpe = IntegerF())
		),
		required = List()
	)*/

	//val map1IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = MapF(IntegerF())

	/*val map1IntJson_mapname_Fix_S: Fix[JsonSchema_S] = Fix(ObjectF(
		properties = List(
			Property(name = "map", tpe = Fix(IntegerF()))
		),
		required = List()
	))*/
	val map1IntJson_objname_Fix_S: Fix[JsonSchema_S] = Fix(ObjectNameF(
		name = "MapNameObj",
		properties = List(Property(name = "map", tpe = Fix(IntegerF()))),
		required = List()
	))
	/*Fix(ObjectF(
		properties = List(
			Property(name = "object", tpe = Fix(IntegerF()))
		),
		required = List()
	)
	)*/
	val map1IntJson_objmap_Fix_S: Fix[JsonSchema_S] = Fix(ObjectMapF(name = "MapNameAddProps",
		additionalProperties = AdditionalProperties(tpe = Fix(IntegerF()))
	))


	/*val map1StrJson_S: JsonSchema_S[JsonSchema_S[String]] = MapF(StringF())
	val map1StrJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonCirce]] = MapF(StringF())*/
	//val map1StrJson_Fix_S: Fix[JsonSchema_S] = ??? //Fix(MapF(Fix(StringF())))


	/*val map3IntJson_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Int]]]] = MapF(MapF(MapF(IntegerF())))
	val map3IntJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = MapF(MapF(MapF(IntegerF())))*/
	//val map3IntJson_Fix_S: Fix[JsonSchema_S] = ??? // Fix(MapF(Fix(MapF(Fix(MapF(Fix(IntegerF())))))))


	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */

	//val recordStrJson_Fix_S: Fix[JsonSchema_S] = ???

	// TODO test Nothing vs. Float (because of innermost type at 'tpe'
	val recordExPositionJson_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[Float]]] = ObjectF(
		properties = List(
			Property(name = "coordinates", tpe = ArrayF(FloatF()))
		),
		required = List()
	)
	//ObjectF(List(Property(coordinates,ArrayF(FloatF()))),List())

	val recordExPositionJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]] = ObjectF(
		properties = List(
			Property(name = "coordinates", tpe = ArrayF(FloatF()))
		),
		required = List()
	)
	val recordExPositionJson_Fix_S: Fix[JsonSchema_S] = Fix(ObjectNameF(
		name = "Position",
		properties = List(
			Property(name = "coordinates", tpe = Fix(ArrayF(Fix(FloatF()))))
		),
		required = List()
	))



	// TODO test diff between Float and Nothing in innermost type.
	val recordExLocationJson_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[Float]]]] = ObjectF(
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

	val recordExLocationJson_Circe_S: JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonSchema_S[JsonCirce]]]] = ObjectF(
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

	/*val testAddProps: Fix[JsonSchema_S] = Fix(ObjectNameF(
		name = "upper_name_here",
		properties = List(
			Property(name = "additionalProperties", tpe = Fix(StringF()))
		),
		required = List()
	))*/

	val testAddProps2: Fix[JsonSchema_S] = Fix(ObjectMapF(
		name = "upper_name_here",
		additionalProperties = AdditionalProperties(tpe = recordExPositionJson_Fix_S)
	))

	val recordExLocationJson_Fix_S: Fix[JsonSchema_S] = Fix(ObjectNameF(
		name = "Locations",
		properties = List(
			Property(name = "id", tpe = Fix(StringF())),
			Property(name = "name", tpe = Fix(StringF())),
			Property(name = "position", tpe = Fix(ObjectNameF(
				name = "Position",
				properties = List(
					Property(name = "coordinates", tpe = Fix(ArrayF(Fix(FloatF()))))
				),
				required = List())
			)),
			Property(name = "sensorName", tpe = Fix(StringF())),
			Property(name = "symbol", tpe = Fix(ObjectNameF(
				name = "Symbol",
				properties = List(),
				required = List()
			))),
		),
		required = List("position", "sensorName", "name", "id")
	))
}