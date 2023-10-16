package utilMain.utilAvroJson.utilSkeuoSkeuo


import cats.data.NonEmptyList
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

		case TEnum(name: String,	namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) => s"enum"

		case TNamedType(namespace: String, name: String) => s"namedType"

		case TRecord(name: String, namespace: Option[String], aliases: List[String],	doc: Option[String], fields: List[FieldAvro[String]]) => s"record(${fields.map(f => (f.name, f.tpe)).mkString(", ")})"

		case TUnion(options: NonEmptyList[String], name: Option[String]) => s"union(${options.toList.mkString(", ")})"

		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) => "fixed"

		case TDate() => "date"
		case TTimestampMillis() => "timestampMillis"
		case TTimeMillis() => "timeMillis"
		case TDecimal(precision: Int, scale: Int) => "decimal"
	}




	def algebra_skeuoJsonToString: Algebra[JsonSchema_S, String] = Algebra {

		case IntegerF() => "integer"
		case StringF() => "string"
		case LongF() => "long"
		case FloatF() => "float"
		case DoubleF() => "double"
		case ByteF() => "byte"
		case BinaryF() => "binary"
		case BooleanF() => "boolean"

		case ArrayF(inner: String) => s"array($inner)"

		case ObjectF(properties: List[Property[String]], required: List[String]) => s"object(${properties.map(p => (p.name, p.tpe)).mkString(", ")})"

		case ObjectNamedF(name: String, properties: List[Property[String]], required: List[String]) => s"objectNamed($name, ${properties.map(p => (p.name, p.tpe)).mkString(", ")})"

		case ObjectMapF(additionalProperties: AdditionalProperties[String]) => s"objectMap(${additionalProperties.tpe})"

		case ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[String]) => s"objectNamedMap($name, ${additionalProperties.tpe})"

		case DateF() => "date"
		case DateTimeF() => "dateTime"

		case PasswordF() => "password"


		case SumF(cases: List[String]) => s"sum(${cases.mkString(",")})"

		case ReferenceF(ref: String) => s"reference"
	}


	val skeuoAvroToString: Fix[AvroSchema_S] => String = scheme.cata(algebra_skeuoAvroToString).apply(_)


	val skeuoJsonToString: Fix[JsonSchema_S] => String = scheme.cata(algebra_skeuoJsonToString).apply(_)


}
