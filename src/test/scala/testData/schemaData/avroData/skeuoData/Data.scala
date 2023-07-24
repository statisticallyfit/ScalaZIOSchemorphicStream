package testData.schemaData.avroData.skeuoData

import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._


import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._
import AvroSchema_S.{Field ⇒ FieldAvro}

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
	
	
	val arrayIntAvro_S: AvroSchema_S[AvroSchema_S[Int]] = TArray(TInt())
	val arrayIntAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TArray(TInt())
	val arrayIntAvro_Fix_S: Fix[AvroSchema_S] = Fix(TArray(Fix(TInt())))
	
	val arrayStrAvro_S: AvroSchema_S[AvroSchema_S[String]] = TArray(TString())
	val arrayStrAvro_Circe_S: AvroSchema_S[AvroSchema_S[JsonCirce]] = TArray(TString())
	val arrayStrAvro_Fix_S: Fix[AvroSchema_S] = Fix(TArray(Fix(TString())))
	
	
	val arrayIntAvro3_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[Int]]]] = TArray(TArray(TArray(TInt())))
	val arrayIntAvro3_Circe_S: AvroSchema_S[AvroSchema_S[AvroSchema_S[AvroSchema_S[JsonCirce]]]] = TArray(TArray(TArray(TInt())))
	val arrayIntAvro3_Fix_S: Fix[AvroSchema_S]  = Fix(TArray(Fix(TArray(Fix(TArray(Fix(TInt())))))))
	
	
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
	
	val recordStringAvro_S: AvroSchema_S[AvroSchema_S[Nothing]] = TRecord(
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
	
	val recordStringAvro_SFix: Fix[AvroSchema_S]  = Fix(TRecord(
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
}
