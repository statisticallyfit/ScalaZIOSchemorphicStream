package testData.schemaData.avroData.skeuoData

import cats.data.NonEmptyList
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro, _}
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import io.circe.{Json ⇒ JsonCirce}


/**
 *
 */
object Data {

	// TODO - to put strin g/ int / null in place of T? or to leave as Nothing?
	val nullAvro_S: AvroSchema_S[Null] = TNull()
	val nullAvro_Circe_S: AvroSchema_S[JsonCirce] = TNull()
	val nullAvro_Fix_S: Fix[AvroSchema_S] = Fix(TNull())

	val strAvro_S: AvroSchema_S[String] = TString()
	val strAvro_Circe_S: AvroSchema_S[JsonCirce] = TString()
	val strAvro_Fix_S: Fix[AvroSchema_S] = Fix(TString())

	val intAvro_S: AvroSchema_S[Int] = TInt()
	val intAvro_Circe_S: AvroSchema_S[JsonCirce] = TInt()
	val intAvro_Fix_S: Fix[AvroSchema_S] = Fix(TInt())

	val booleanAvro_S: AvroSchema_S[Boolean] = TBoolean()
	val booleanAvro_Circe_S: AvroSchema_S[JsonCirce] = TBoolean()
	val booleanAvro_Fix_S: Fix[AvroSchema_S] = Fix(TBoolean())

	val longAvro_S: AvroSchema_S[Long] = TLong()
	val longAvro_Circe_S: AvroSchema_S[JsonCirce] = TLong()
	val longAvro_Fix_S: Fix[AvroSchema_S] = Fix(TLong())

	val floatAvro_S: AvroSchema_S[Float] = TFloat()
	val floatAvro_Circe_S: AvroSchema_S[JsonCirce] = TFloat()
	val floatAvro_Fix_S: Fix[AvroSchema_S] = Fix(TFloat())


	val doubleAvro_S: AvroSchema_S[Double] = TDouble()
	val doubleAvro_Circe_S: AvroSchema_S[JsonCirce] = TDouble()
	val doubleAvro_Fix_S: Fix[AvroSchema_S] = Fix(TDouble())

	val bytesAvro_S: AvroSchema_S[Byte] = TBytes()
	val bytesAvro_Circe_S: AvroSchema_S[JsonCirce] = TBytes()
	val bytesAvro_Fix_S: Fix[AvroSchema_S] = Fix(TBytes())

	val array1IntAvro_S: AvroSchema_S[AvroSchema_S[Int]] = TArray(TInt())
	val array1IntAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TArray(TInt())
	val array1IntAvro_Fix_S: Fix[AvroSchema_S] = Fix(TArray(Fix(TInt())))

	val array1StrAvro_S: AvroSchema_S[AvroSchema_S[String]] = TArray(TString())
	val array1StrAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TArray(TString())
	val array1StrAvro_Fix_S: Fix[AvroSchema_S] = Fix(TArray(Fix(TString())))


	val array3IntAvro_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[Int]]]] = TArray(TArray(TArray(TInt())))
	val array3IntAvro_Circe_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[JsonCirce]]]] = TArray(TArray(TArray(TInt())))
	val array3IntAvro_Fix_S: Fix[AvroSchema_S] = Fix(TArray(Fix(TArray(Fix(TArray(Fix(TInt())))))))


	val map1IntAvro_S: AvroSchema_S[AvroSchema_S[Int]] = TMap(TInt())
	val map1IntAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TMap(TInt())
	val map1IntAvro_Fix_S: Fix[AvroSchema_S] = Fix(TMap(Fix(TInt())))

	val map1StrAvro_S: AvroSchema_S[AvroSchema_S[String]] = TMap(TString())
	val map1StrAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TMap(TString())
	val map1StrAvro_Fix_S: Fix[AvroSchema_S] = Fix(TMap(Fix(TString())))


	val map3IntAvro_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[Int]]]] = TMap(TMap(TMap(TInt())))
	val map3IntAvro_Circe_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[JsonCirce]]]] = TMap(TMap(TMap(TInt())))
	val map3IntAvro_Fix_S: Fix[AvroSchema_S] = Fix(TMap(Fix(TMap(Fix(TMap(Fix(TInt())))))))






	val recordStrAvro_S: AvroSchema_S[AvroSchema_S[Nothing]] = TRecord(
		name = "RecordName",
		namespace = Some("Namespace"),
		aliases = List("alias1", "alias2"),
		doc = None,
		fields = List(
			FieldAvro(
				name = "FieldName1",
				tpe = TString(),
				aliases = List("fieldAlias1", "fieldAlias2", "fieldAlias3"),
				doc = None,
				order = Some(Order.Descending)
			)
		)
	)
	val recordStrAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TRecord(
		name = "RecordName",
		namespace = Some("Namespace"),
		aliases = List("alias1", "alias2"),
		doc = None,
		fields = List(
			FieldAvro(
				name = "FieldName1",
				tpe = TString(),
				aliases = List("fieldAlias1", "fieldAlias2", "fieldAlias3"),
				doc = None,
				order = Some(Order.Descending)
			)
		)
	)


	val recordStrAvro_Fix_S: Fix[AvroSchema_S] = Fix(TRecord(
		name = "RecordName",
		namespace = Some("Namespace"),
		aliases = List("alias1", "alias2"),
		doc = None,
		fields = List(
			FieldAvro(
				name = "FieldName1",
				tpe = Fix(TString()),
				aliases = List("fieldAlias1", "fieldAlias2", "fieldAlias3"),
				doc = None,
				order = Some(Order.Descending)
			)
		)
	))





	val recordExPositionAvro_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[Nothing]]] = TRecord(
		name = "Position",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(
				name = "coordinates",
				tpe = TArray(TFloat()),
				aliases = List(), doc = None, order = None
			),
			FieldAvro(
				name = "type",
				tpe = TString(),
				aliases = List(), doc = None, order = None
			)
		)
	)
	/*val recordExPositionAvro_Circe_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[JsonCirce]]] = TRecord(
		name = "Position",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(
				name = "coordinates",
				tpe = TArray(TFloat()),
				aliases = List(), doc = None, order = None
			),
			FieldAvro(
				name = "type",
				tpe = TString(),
				aliases = List(), doc = None, order = None
			)
		)
	)*/


	val recordExPositionAvro_Fix_S: Fix[AvroSchema_S] = Fix(TRecord(
		name = "Position",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(
				name = "coordinates",
				tpe = Fix(TArray(Fix(TFloat()))),
				aliases = List(), doc = None, order = None
			),
			FieldAvro(
				name = "type",
				tpe = Fix(TString()),
				aliases = List(), doc = None, order = None
			)
		)
	))

	val map1PosRecordAvro_Fix_S: Fix[AvroSchema_S] = Fix(TMap(values = recordExPositionAvro_Fix_S))

	val recordExLocationAvro_S: AvroSchema_S[AvroSchema_S[_]] = TRecord(
		name = "Location_record",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(name = "id", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "name", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "sensorName", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "position", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = recordExPositionAvro_S),
			FieldAvro(name = "symbol", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TUnion(options = cats.data.NonEmptyList(TNull(), List(TString()))))
		)
	)
	/*val recordExLocationAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TRecord(
		name = "Location_record",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(name = "id", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "name", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "sensorName", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TString()),
			FieldAvro(name = "position", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = recordExPositionAvro_S),
			FieldAvro(name = "symbol", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = TUnion(options = cats.data.NonEmptyList(TNull(), List(TString()))))
		)
	)*/
	val recordExLocationAvro_Fix_S: Fix[AvroSchema_S] = Fix(TRecord(
		name = "Location_record",
		namespace = None, aliases = List(), doc = None,
		fields = List(
			FieldAvro(name = "id", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = Fix(TString())),
			FieldAvro(name = "name", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = Fix(TString())),
			FieldAvro(name = "sensorName", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = Fix(TString())),
			FieldAvro(name = "position", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = recordExPositionAvro_Fix_S),
			FieldAvro(name = "symbol", aliases = List(), doc = None, order = Some(Order.Ascending), tpe = Fix(TUnion(options = cats.data.NonEmptyList(Fix(TNull()), List(Fix(TString())))))
			)
		)
	))



	//val recordExLocationAvro_Fix_S: Fix[AvroSchema_S] = ???
	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */



	// -----------------------------------------------------------------------

	val namedTypeAvro_S: AvroSchema_S[Nothing] = TNamedType(name = "namedTypeHere", namespace = "namespaceHere")
	val namedTypeAvro_Fix_S: Fix[AvroSchema_S] = Fix(TNamedType(name = "namedTypeHere", namespace = "namespaceHere"))


	// -----------------------------------------------------------------------

	val COLORS_LIST: List[String] = List("Red", "Orange", "Pink", "Yellow", "Green", "Blue", "Indigo", "Violet")

	val enumAvro_S: AvroSchema_S[Nothing] = TEnum("Colors",None,List(),None,COLORS_LIST)
	val enumAvro_Fix_S: Fix[AvroSchema_S] = Fix(TEnum("Colors",None,List(),None,COLORS_LIST))


	// -----------------------------------------------------------------------

	val unionAvro_S: AvroSchema_S[AvroSchema_S[Nothing]] = TUnion(NonEmptyList(TInt(), List(TString(), TInt(), TBoolean())))
	val unionAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TUnion(NonEmptyList(TInt(), List(TString(), TInt(), TBoolean())))
	//val unionAvro_Fix_S: Fix[AvroSchema_S] = ???
}
