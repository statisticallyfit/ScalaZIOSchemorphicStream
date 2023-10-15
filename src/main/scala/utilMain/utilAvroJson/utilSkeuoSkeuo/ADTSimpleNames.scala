package utilMain.utilAvroJson.utilSkeuoSkeuo


import higherkindness.droste.data.Fix
import higherkindness.droste.{Algebra, scheme}
import higherkindness.skeuomorph.avro.AvroF.{Field => FieldAvro, _}
import higherkindness.skeuomorph.avro.{AvroF => AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF => JsonSchema_S}



/**
 *
 */
object ADTSimpleNames {


	def algebra_skeuoAvroToString: Algebra[AvroSchema_S, String] = Algebra {

		case TNull() => "null"
		case TString() => "string"
		case TInt() => "int"
		case TFloat() => "float"
		case TBytes() => "bytes"
		case TDouble() => "double"
		case TLong() => "long"
		case TBoolean() => "boolean"

		case TArray(inner: String) => s"array($inner)"
		case TMap(values: String) => s"map($values)"

	}

	val skeuoAvroToString: Fix[AvroSchema_S] => String = scheme.cata(algebra_skeuoAvroToString).apply(_)
}
