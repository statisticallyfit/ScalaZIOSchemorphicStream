package conversionsOfSchemaADTs.avro_json.parsing

import cats.data.NonEmptyList

import higherkindness.droste.{scheme, Algebra, Coalgebra}
import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._
import AvroSchema_S.{Field ⇒ FieldAvro, _}


// NOTE: commenting out the JsonDecoders file so I can import the jsonSchemaDecoders from my Skeuo_Skeuo file (to easily update) instead of having to publish skeuomorph each time I make a change.
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_fromSkeuoProject._
//import higherkindness.skeuomorph.openapi.JsonDecoders._



import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}


/**
 *
 */
object ParseADTToCirceToADT {

	/**
	 * Definition from skeuomorph library - just applying anamorphism to make parameters passable to it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
	 */
	val libRender: Fix[JsonSchema_S] ⇒ JsonCirce = scheme.cata(JsonSchema_S.render).apply(_)

	/**
	 * Definition from skeuomorph library - just make it possible to pass the parameter to it by apply catamorphism over it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238
	 */

	val libToJson: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(AvroSchema_S.toJson).apply(_)


	//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA

	val libToJsonAltered: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(toCirceJsonString_fromAvroSkeuo).apply(_)

	val libRenderAltered: Fix[JsonSchema_S] ⇒ JsonCirce = scheme.cata(toCirceJsonString_fromJsonSkeuo).apply(_)




	// Copied from skeuomorph (is private) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L88
	// TODO add title here like in autoschema (so can include 'name' from avro-kind) = https://github.com/sauldhernandez/autoschema/blob/8e6f394acb3f4b55dbfe8916ffe33abf17aaef2e/src/main/scala/org/coursera/autoschema/AutoSchema.scala#L104
	def jsonType(value: String, attr: (String, JsonCirce)*): JsonCirce =
		JsonCirce.obj((("type" -> JsonCirce.fromString(value)) :: attr.toList): _*)


	def format(value: String): (String, JsonCirce) =
		"format" -> JsonCirce.fromString(value)






	// Alterign this function to include my own ObjectF-alike types: https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
	def toCirceJsonString_fromJsonSkeuo: Algebra[JsonSchema_S, JsonCirce] =
		Algebra {
			case IntegerF() => jsonType("integer", format("int32"))
			case LongF() => jsonType("integer", format("int64"))
			case FloatF() => jsonType("number", format("float"))
			case DoubleF() => jsonType("number", format("double"))
			case StringF() => jsonType("string")
			case ByteF() => jsonType("string", format("byte"))
			case BinaryF() => jsonType("string", format("binary"))
			case BooleanF() => jsonType("boolean")
			case DateF() => jsonType("string", format("date"))
			case DateTimeF() => jsonType("string", format("date-time"))
			case PasswordF() => jsonType("string", format("password"))

			case ObjectF(properties: List[Property[JsonCirce]], required: List[String]) =>
				JsonCirce.obj(
					"type" -> JsonCirce.fromString("object"),
					"properties" -> JsonCirce.obj(properties.map(prop => prop.name -> prop.tpe): _*),
					"required" -> JsonCirce.fromValues(required.map(JsonCirce.fromString))
				)

			case ObjectNamedF(name: String, properties: List[Property[JsonCirce]], required: List[String]) ⇒ JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("object"),
				"properties" -> JsonCirce.obj(properties.map(prop => prop.name -> prop.tpe): _*),
				"required" -> JsonCirce.fromValues(required.map(JsonCirce.fromString))
			  )

			case ObjectNamedMapF(name: String, addProps: AdditionalProperties[JsonCirce]) ⇒ JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("object"),
				"additionalProperties" -> addProps.tpe
			)

			case ObjectMapF(addProps: AdditionalProperties[JsonCirce]) ⇒ JsonCirce.obj(
				//"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("object"),
				"additionalProperties" -> addProps.tpe
			)


			case ArrayF(values) =>
				jsonType(
					"array",
					"items" -> values
				)
			case EnumF(cases) =>
				jsonType("string", "enum" -> JsonCirce.fromValues(cases.map(JsonCirce.fromString)))
			case SumF(cases) =>
				JsonCirce.obj("oneOf" -> JsonCirce.arr(cases: _*))
			case ReferenceF(value) =>
				JsonCirce.obj(
					s"$$ref" -> JsonCirce.fromString(value)
				)

		}



	// TODO how to alter libToJsonAltered so that null works? (funcCirceToAvroSkeuo not worknig)

	def toCirceJsonString_fromAvroSkeuo: Algebra[AvroSchema_S, JsonCirce] = Algebra {

		// ObjectF(List(Property(name = "null", tpe = StringF())), List())

		case TNull() ⇒ jsonType(
			"object",
			"properties" -> JsonCirce.obj(List(): _*),
			"required" -> JsonCirce.fromValues(List())
		)
		case TInt() => jsonType("integer", format("int32"))
		case TString() => jsonType("string")
		case TBoolean() => jsonType("boolean")
		case TLong() => jsonType("integer", format("int64"))
		case TFloat() => jsonType("number", format("float"))
		case TDouble() => jsonType("number", format("double"))

		case TBytes() => jsonType("string", format("byte"))

		/*case BinaryF() => jsonType("string", format("binary"))*/


		case TArray(inner: JsonCirce) ⇒ jsonType(
			"array",
			"items" -> inner
		)
		// TODO compare between above way of declaring array and the below way (jsonType vs. Json.obj)


		// METHOD 2: the 'additionalProperties' way
		case TMap(innerValues: JsonCirce) => JsonCirce.obj(
			"type" -> JsonCirce.fromString("object"),
			"additionalProperties" -> innerValues
		)

		// METHOD 1: the 'properties' and 'values' way
			/*JsonCirce.obj(
			"type" -> JsonCirce.fromString("object"),
			"properties" -> JsonCirce.obj("values" -> innerValues /*JsonCirce.fromString("string")*/)
		)*/
			/*JsonCirce.obj(
				"type" -> JsonCirce.fromString("object"),
				"additionalProperties" -> JsonCirce.obj("type" -> innerValues /*JsonCirce.fromString("string")*/)
			)*/

		// TODO find example of named type (avro -> json) in data files ?

		case TNamedType(namespace: String, name: String) ⇒ {
			JsonCirce.obj(
				"name" -> JsonCirce.fromString(name),
				"namespace" -> JsonCirce.fromString(namespace),
			)
		}

		// TODO must make map become an Object

		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[JsonCirce]]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("record"),
				"fields" -> JsonCirce.arr(fields.map(field2Obj): _*) // TODO update this function (field2Obj) to preserve all the args from Field (like order, doc, etc)

				// TODO must rename fields to be properties
			)

			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonCirce = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}
		case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" → JsonCirce.fromString("enum"),
				"symbols" → JsonCirce.arr(symbols.map(JsonCirce.fromString): _*)
			)
			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonCirce = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}

		case TUnion(options: NonEmptyList[JsonCirce]) => JsonCirce.arr(options.toList: _*)

		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) => {

			val base: JsonCirce = JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("fixed"),
				"size" -> JsonCirce.fromInt(size)
			)

			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			withAliases
		}
		// Source = https://hyp.is/gcf1CCv3Ee6M4RMGFdKckA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		/*case TDate() => jsonType("string", format("date"))

		// Source = https://hyp.is/b2IUFiv3Ee6z6geHBkITQA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TTimeMillis() ⇒ jsonType("string", format("time"))

		// Source = https://hyp.is/PUYlCB_5Ee6kOOPNKtp-zQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
		case TTimestampMillis() => jsonType("string", format("date-time"))*/

		// Replaced above with the original from toJson (skeuomorph) since this part doesn't seem to be value-level ...
		case TDate() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("int"),
				"logicalType" -> JsonCirce.fromString("date")
			)
		case TTimestampMillis() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("long"),
				"logicalType" -> JsonCirce.fromString("timestamp-millis")
			)
		case TTimeMillis() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("int"),
				"logicalType" -> JsonCirce.fromString("time-millis")
			)
		case TDecimal(precision, scale) =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("bytes"),
				"logicalType" -> JsonCirce.fromString("decimal"),
				"precision" -> JsonCirce.fromInt(precision),
				"scale" -> JsonCirce.fromInt(scale)
			)

		/*case PasswordF() => jsonType("string", format("password"))*/

	}


	// TODO MAJOR - this thing is resulting in avro-string-like not strictly json-circe ---- see they say 'record' instead of 'object'. Need to clean this up to be purely avro-string and compare it to the original avro-string, and need to clean up above one and make it purely JSON-circe to compare it to original json-string
	/*def toCirceAvroString: Algebra[AvroSchema_S, JsonCirce] = Algebra {

		// ObjectF(List(Property(name = "null", tpe = StringF())), List())
		case TNull() ⇒ jsonType(
			"object",
			"properties" -> JsonCirce.obj(List(): _*),
			"required" -> JsonCirce.fromValues(List())
		)
		// TODO change the above to be using 'record' - the avro equivalent of json object.
		// NOTE: will not be able to convert from this form to skeuo using Decoder. Will just have to compare THIS end avro-string version with the original avro-str.
		/*JsonCirce.obj(
			"type" -> JsonCirce.fromString("record"),
			//"name" -> JsonCirce.fromString("null"),
			"fields" -> JsonCirce.arr(List(): _*)
		)*/
		// NO, just string not a null
		//jsonType("string", "name" -> JsonCirce.fromString("null"))
		// BAD
		/*JsonCirce.obj(
			"type" -> JsonCirce.fromString("record"),
			"name" -> JsonCirce.fromString("null")
		)*/

		/*JsonCirce.obj(
			"type" -> JsonCirce.fromString("record"),
			"name" -> JsonCirce.fromString("null"),
			"fields" -> JsonCirce.arr(List():_*)
			)*/
		//jsonType("null", attr = List():_*)

		case TInt() => jsonType("integer", format("int32"))
		case TString() => jsonType("string")
		case TBoolean() => jsonType("boolean")
		case TLong() => jsonType("integer", format("int64"))
		case TFloat() => jsonType("number", format("float"))
		case TDouble() => jsonType("number", format("double"))

		case TBytes() => jsonType("string", format("byte"))

		/*case BinaryF() => jsonType("string", format("binary"))*/


		case TArray(inner: JsonCirce) ⇒ jsonType(
			"array",
			"items" -> inner
		)
		// TODO compare between above way of declaring array and the below way (jsonType vs. Json.obj)

		case TMap(values: JsonCirce) =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("map"),
				"values" -> values
			)


		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[JsonCirce]]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"type" -> JsonCirce.fromString("record"),
				"name" -> JsonCirce.fromString(name),
				"fields" -> JsonCirce.arr(fields.map(field2Obj): _*) // TODO update this function (field2Obj) to preserve all the args from Field (like order, doc, etc)
			)

			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonCirce = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}
		case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"type" → JsonCirce.fromString("enum"),
				"name" → JsonCirce.fromString(name),
				"symbols" → JsonCirce.arr(symbols.map(JsonCirce.fromString): _*)
			)
			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonCirce = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}

		case TUnion(options: NonEmptyList[JsonCirce]) => JsonCirce.arr(options.toList: _*)

		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) => {

			val base: JsonCirce = JsonCirce.obj(
				"type" -> JsonCirce.fromString("fixed"),
				"name" -> JsonCirce.fromString(name),
				"size" -> JsonCirce.fromInt(size)
			)

			val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			withAliases
		}
		// Source = https://hyp.is/gcf1CCv3Ee6M4RMGFdKckA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		/*case TDate() => jsonType("string", format("date"))

		// Source = https://hyp.is/b2IUFiv3Ee6z6geHBkITQA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TTimeMillis() ⇒ jsonType("string", format("time"))

		// Source = https://hyp.is/PUYlCB_5Ee6kOOPNKtp-zQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
		case TTimestampMillis() => jsonType("string", format("date-time"))*/

		// Replaced above with the original from toJson (skeuomorph) since this part doesn't seem to be value-level ...
		case TDate() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("int"),
				"logicalType" -> JsonCirce.fromString("date")
			)
		case TTimestampMillis() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("long"),
				"logicalType" -> JsonCirce.fromString("timestamp-millis")
			)
		case TTimeMillis() =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("int"),
				"logicalType" -> JsonCirce.fromString("time-millis")
			)
		case TDecimal(precision, scale) =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("bytes"),
				"logicalType" -> JsonCirce.fromString("decimal"),
				"precision" -> JsonCirce.fromInt(precision),
				"scale" -> JsonCirce.fromInt(scale)
			)

		/*case PasswordF() => jsonType("string", format("password"))*/

	}*/



	import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{skeuoEmbed_JA /*, skeuoProject_AJ*/}


	// TODO figure out if  must create decoder for AvroSchema (like identifyDecoderWithPrioerity but with AvroSchema instead)
	val funcCirceToAvroSkeuo: JsonCirce ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)

	import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_fromSkeuoProject._

	val funcCirceToJsonSkeuo: JsonCirce ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)




	object DecodingSkeuo {
		//jsonSchemaDecoder
		def decodeAvroSkeuoToCirceToJsonSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libToJsonAltered

		def decodeJsonSkeuoToCirceToJsonSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libRenderAltered

		def decodeAvroSkeuoToCirceToAvroSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo compose libToJsonAltered

		def decodeJsonSkeuoToCirceToAvroSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo compose libRenderAltered

	}
}
