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
	
	
	
	//val record // TODO record position, location
	
	// NOTE; trying to copy avro schema "RawCityMesh - devs - datasource
	// TODO what is the inner type parameter?
	/*val recordAvro_S/*: AvroSchema_S[AvroSchema_S[Nothing]]*/ = TRecord("Location", namespace = Some("location namespace"), aliases = List("a1", "a2"), doc = None, fields = List(
		FieldAvro/*[AvroSchema_S[Nothing]]*/(name = "id", aliases = List("i1", "i2"), doc = None, order = None, tpe = TString()),
		
		FieldAvro/*[AvroSchema_S[Nothing]]*/(name = "position", aliases = List(),
			doc = None, order = None, tpe = TRecord(
				name = "Position", namespace = Some("position namespace"), aliases = List(), doc = None, fields = List(
					FieldAvro(name = "coordinates", aliases = List(), doc = None, order = None, tpe = TArray(TFloat())),
					FieldAvro(name = "type", aliases = List(), doc = None, order = None, tpe = TString())
				)
			))
	))*/
	
	// TODO alter these to be more understandable (canonical record datas, not examples)
	
	val recordStrAvro_S: AvroSchema_S[AvroSchema_S[Nothing]] = TRecord(
		name = "StringRecord",
		namespace = Some("StringNamespace"),
		aliases = List("a1", "a2"),
		doc = None,
		fields = List(
			FieldAvro(
				name = "stringField1",
				aliases = List(),
				doc = None,
				order = None,
				tpe = TString()
			)
		)
	)
	
	val recordStrAvro_Fix_S: Fix[AvroSchema_S] = Fix(TRecord(
		name = "StringRecord",
		namespace = Some("StringNamespace"),
		aliases = List("a1", "a2"),
		doc = None,
		fields = List(
			FieldAvro(
				name = "stringField1",
				aliases = List(),
				doc = None,
				order = None,
				tpe = Fix(TString())
			)
		)
	))
	//TODO
	//val recordExPositionAvro_Fix_S: Fix[AvroSchema_S] = ???
	//val recordExLocationAvro_Fix_S: Fix[AvroSchema_S] = ???
	/**
	 * TODO
	 * null, int, string
	 * boolean, long, float, double, bytes,
	 * array
	 * map, record, union, enum, union, fixed, date, timestamp-millis, time-mills, decimeal
	 */
	
	
	val unionAvro_S: AvroSchema_S[AvroSchema_S[Nothing]] = TUnion(NonEmptyList(TInt(), List(TString(), TInt(), TBoolean())))
	val unionAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TUnion(NonEmptyList(TInt(), List(TString(), TInt(), TBoolean())))
	//val unionAvro_Fix_S: Fix[AvroSchema_S] = ???
}
