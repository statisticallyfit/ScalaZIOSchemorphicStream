package testData.schemaData.avroData.skeuoData

import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._


import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import SchemaAvro_Skeuo.{Field ⇒ FieldAvro}

import io.circe.{Json ⇒ JsonCirce}

/**
 *
 */
object Data {

	// TODO - to put strin g/ int / null in place of T? or to leave as Nothing?
	val nullAvro_Skeuo: SchemaAvro_Skeuo[Null] = TNull()
	val nullAvro_SkeuoC: SchemaAvro_Skeuo[JsonCirce] = TNull()
	val nullAvro_SkeuoFix: Fix[SchemaAvro_Skeuo] = Fix(TNull())
	
	val strAvro_Skeuo: SchemaAvro_Skeuo[String] = TString()
	val strAvro_SkeuoC: SchemaAvro_Skeuo[JsonCirce] = TString()
	val strAvro_SkeuoFix: Fix[SchemaAvro_Skeuo] = Fix(TString())
	
	val intAvro_Skeuo: SchemaAvro_Skeuo[Int] = TInt()
	val intAvro_SkeuoC: SchemaAvro_Skeuo[JsonCirce] = TInt()
	val intAvro_SkeuoFix: Fix[SchemaAvro_Skeuo] = Fix(TInt())
	
	
	/*val arrayIntAvro_Skeuo: SchemaAvro_Skeuo[SchemaAvro_Skeuo[Int]] = TArray(TInt())
	val arrayIntAvro_SkeuoC: SchemaAvro_Skeuo[JsonCirce] = TArray(TInt())
	val arrayIntAvro_SkeuoFix: Fix[SchemaAvro_Skeuo] = Fix(TArray(Fix(TInt())))
	
	val arrayStrAvro_Skeuo: SchemaAvro_Skeuo[SchemaAvro_Skeuo[String]] = TArray(TString())
	val arrayStrAvro_SkeuoC: SchemaAvro_Skeuo[JsonCirce] = TArray(TString())
	val arrayStrAvro_SkeuoFix: Fix[SchemaAvro_Skeuo] = Fix(TArray(Fix(TString())))
	
	
	val arrayIntAvro3_Skeuo: SchemaAvro_Skeuo[SchemaAvro_Skeuo[SchemaAvro_Skeuo[SchemaAvro_Skeuo[Int]]]] = TArray(TArray(TArray(TInt())))
	val arrayIntAvro3_SkeuoC: SchemaAvro_Skeuo[SchemaAvro_Skeuo[SchemaAvro_Skeuo[SchemaAvro_Skeuo[JsonCirce]]]] = TArray(TArray(TArray(TInt())))
	val arrayIntAvro3_SkeuoFix: Fix[SchemaAvro_Skeuo]  = Fix(TArray(Fix(TArray(Fix(TArray(Fix(TInt())))))))
	
	*/
	
	// NOTE; trying to copy avro schema "RawCityMesh - devs - datasource
	// TODO what is the inner type parameter?
	/*val recordAvro_Skeuo/*: SchemaAvro_Skeuo[SchemaAvro_Skeuo[Nothing]]*/ = TRecord("Location", namespace = Some("location namespace"), aliases = List("a1", "a2"), doc = None, fields = List(
		FieldAvro/*[SchemaAvro_Skeuo[Nothing]]*/(name = "id", aliases = List("i1", "i2"), doc = None, order = None, tpe = TString()),
		
		FieldAvro/*[SchemaAvro_Skeuo[Nothing]]*/(name = "position", aliases = List(),
			doc = None, order = None, tpe = TRecord(
				name = "Position", namespace = Some("position namespace"), aliases = List(), doc = None, fields = List(
					FieldAvro(name = "coordinates", aliases = List(), doc = None, order = None, tpe = TArray(TFloat())),
					FieldAvro(name = "type", aliases = List(), doc = None, order = None, tpe = TString())
				)
			))
	))*/
	
	val recordStringAvro_Skeuo: SchemaAvro_Skeuo[SchemaAvro_Skeuo[Nothing]] = TRecord(
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
	
	val recordStringAvro_SkeuoFix: Fix[SchemaAvro_Skeuo]  = Fix(TRecord(
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
