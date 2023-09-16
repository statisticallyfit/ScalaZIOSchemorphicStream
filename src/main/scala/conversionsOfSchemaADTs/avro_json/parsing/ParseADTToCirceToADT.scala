package conversionsOfSchemaADTs.avro_json.parsing

import cats.data.NonEmptyList
import higherkindness.droste.data.Fix
import higherkindness.droste.{Algebra, scheme}
import higherkindness.skeuomorph.avro.AvroF.{Field => FieldAvro, _}
import higherkindness.skeuomorph.avro.{AvroF => AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF => JsonSchema_S}


// NOTE: commenting out the JsonDecoders file so I can import the jsonSchemaDecoders from my Skeuo_Skeuo file (to easily update) instead of having to publish skeuomorph each time I make a change.
//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_fromSkeuoProject._
//import higherkindness.skeuomorph.openapi.JsonDecoders._


import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._

import io.circe.Decoder.Result
import io.circe.{Decoder, Json => JsonCirce}


/**
 *
 */
object ParseADTToCirceToADT {


	object LibFuncs {

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


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{/*skeuoProject_AJ,*/ skeuoProject_AA}


		// TODO IMPLICITS (1) = work to manipulate the implicits here (says drosebasisforfix using project[_,_] todo figure out change the Projcet??
		val libToJsonAltered: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(toCirceJsonString_fromAvroSkeuo).apply(_)//scheme.cata(toCirceAvroString_fromAvroSkeuo).apply(_)


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{skeuoProject_JJ}

		val libRenderAltered: Fix[JsonSchema_S] ⇒ JsonCirce = scheme.cata(toCirceJsonString_fromJsonSkeuo).apply(_)

	}
	import LibFuncs._



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

			// Logical types
			case DateF() => jsonType("string", format("date"))
			case DateTimeF() => jsonType("string", format("date-time"))

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


			case ArrayF(values: JsonCirce) =>
				jsonType(
					"array",
					"items" -> values
				)
			case EnumF(cases: List[String]) => JsonCirce.obj(
				"type" -> JsonCirce.fromString("string"),
				"enum" -> JsonCirce.arr(cases.map(JsonCirce.fromString): _*)
				//JsonCirce.fromValues(cases.map(JsonCirce.fromString))
			)
				//jsonType("string", "enum" -> JsonCirce.fromValues(cases.map(JsonCirce.fromString)))
			case SumF(cases) =>
				JsonCirce.obj("oneOf" -> JsonCirce.arr(cases: _*))
			case ReferenceF(value) =>
				JsonCirce.obj(
					s"$$ref" -> JsonCirce.fromString(value)
				)

			case PasswordF() => jsonType("string", format("password"))

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


		// TODO find example of named type (avro -> json) in data files ?

		case TNamedType(namespace: String, name: String) ⇒ {
			JsonCirce.obj(
				"title" -> JsonCirce.fromString(name), // name for avro-string, title for json string
				"namespace" -> JsonCirce.fromString(namespace),
			)
		}

		// TODO must make map become an Object

		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[JsonCirce]]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("object"),
				"properties" -> {

					val properties: List[Property[JsonCirce]] = fields.map(field2Property(_))

					JsonCirce.obj(properties.map(prop => prop.name -> prop.tpe): _*)
				},
				"required" -> JsonCirce.fromValues(List())

					//JsonCirce.arr(fields.map(field2Obj): _*) // TODO update this function (field2Obj) to preserve all the args from Field (like order, doc, etc)

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


	// NOTE: AvroCirce is just avro-string language containined within a JsonCirce string
	type AvroCirce = JsonCirce

	// NOTE: converting the avro-circe-str to json-circe-str so that the avro-str primitives can be seen as json-str primitives
	def avroCirceToJsonCirce: AvroCirce => JsonCirce = (avroCirce: AvroCirce) => {


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{/*skeuoProject_AA,*/ skeuoProject_AA}

		val avroSkeuoToJsonCirce: Fix[AvroSchema_S] => JsonCirce = scheme.cata(toCirceJsonString_fromAvroSkeuo).apply(_)


		// TODO find wayto pattern match on json
//		avroCirce match {
//
//			case JsonCirce.JString("null") => avroSkeuoToJsonCirce(Fix(TNull()))
//			case JsonCirce.JString("int") => avroSkeuoToJsonCirce(Fix(TInt()))
//
//			/*case JsonCirce.JObject(
//			"type" -> JsonCirce.JString("array"),
//			"items" -> inner
//			)*/
//		}
		avroCirce
	}

	def toCirceAvroString_fromAvroSkeuo: Algebra[AvroSchema_S, AvroCirce] = Algebra {
		//import io.circe.JsonObject
		//JsonCirce.JObject(io.circe.JsonObject.)
				//JsonCirce.fromJsonObject(JsonObject)
		case TNull() ⇒ JsonCirce.fromString("null")
		case TInt() =>  JsonCirce.fromString("int")
		/*case TInt() => JsonCirce.fromJsonObject(
			io.circe.JsonObject.apply(("", JsonCirce.fromString("int")))
		)*/
		case TString() => JsonCirce.fromString("string")
		case TBoolean() => JsonCirce.fromString("boolean")
		case TLong() => JsonCirce.fromString("long")
		case TFloat() => JsonCirce.fromString("float")
		case TDouble() => JsonCirce.fromString("double")
		case TBytes() => JsonCirce.fromString("bytes")

		case TArray(inner: JsonCirce) ⇒ JsonCirce.obj(
			"type" -> JsonCirce.fromString("array"),
			"items" -> inner
		)

		case TMap(values: JsonCirce) => JsonCirce.obj(
			"type" -> JsonCirce.fromString("map"),
			"values" -> values
		)

		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[JsonCirce]]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				"type" -> JsonCirce.fromString("record"),
				"name" -> JsonCirce.fromString(name),
				"fields" -> JsonCirce.arr(fields.map(field2Obj): _*)
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
		/*// Source = https://hyp.is/gcf1CCv3Ee6M4RMGFdKckA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TDate() => jsonType("string", format("date"))

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


	}


	object CirceAvroToSkeuoAvro {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{ skeuoEmbed_AA, skeuoProject_AA}

		//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_fromSkeuoProject._

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_AvroSchemaDecoderImplicit_usingJsonCirceString._

		//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_AvroSchemaDecoderImplicits_usingAvroCirceString._



		val decoderAA: JsonCirce ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_) //compose avroCirceToJsonCirce


		val f1: AvroCirce => Result[Fix[AvroSchema_S]] = (av: AvroCirce)  => decoderAA(avroCirceToJsonCirce(av))

		val f2: AvroCirce => Result[Fix[AvroSchema_S]] = avroCirceToJsonCirce andThen decoderAA

		val funcCirceAvroToSkeuoAvro: AvroCirce => Result[Fix[AvroSchema_S]] = decoderAA compose avroCirceToJsonCirce


		// TODO IMPLICITS (2)
		// basis[AvroF, Fix[AvroF]]
		// 	algebra: AvroF[Fix[AvroF]] => Fix[AvroF]
		// 	coalgebra: Fix[AvroF] => AvroF[Fix[AvroF]]
		// NEED: avro -> avro
		// 	skeuoEmbed_AA
		// NEED: json -> avro
		// 	skeuoEmbed_JA

	}
	// json decoder: Embed[JsonSchema_S, *]
	// implicit: Embed[JsonSchema_S, Fix[AvroSchema_S]]


	object CirceAvroToSkeuoJson {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{skeuoEmbed_AJ, skeuoProject_AJ}

		//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_usingJsonCirceString._

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_AvroSchemaDecoderImplicit_usingJsonCirceString._

		//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_AvroSchemaDecoderImplicits_usingAvroCirceString._


		val decoderAJ: JsonCirce ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)


		val funcCirceAvroToSkeuoJson: AvroCirce => Result[Fix[JsonSchema_S]] = decoderAJ compose avroCirceToJsonCirce


		// TODO IMPLICITS (2)
		// basis[AvroF, Fix[AvroF]]
		// 	algebra: AvroF[Fix[AvroF]] => Fix[AvroF]
		// 	coalgebra: Fix[AvroF] => AvroF[Fix[AvroF]]
		// NEED: avro -> avro
		// 	skeuoEmbed_AA
		// NEED: json -> avro
		// 	skeuoEmbed_JA
	}





	// -----------------
	object CirceJsonToSkeuoJson {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{skeuoEmbed_JJ}

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_usingJsonCirceString._//, skeuoProject_AJ}

		val decoderJJ: JsonCirce ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)


		val funcCirceJsonToSkeuoJson: AvroCirce => Result[Fix[JsonSchema_S]] = decoderJJ //compose avroCirceToJsonCirce

		// basis[JsonF, Fix[JsonF]]
		// 	algebra: JsonF[Fix[JsonF]] => Fix[JsonF]
		// 	coalgebra: Fix[JsonF] => JsonF[Fix[JsonF]]
		// NEED: avro -> json
		// 	skeuoEmbed_AJ
		// NEED: json -> json
		// 	skeuoEmbed_JJ
	}

	object CirceJsonToSkeuoAvro {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.{skeuoEmbed_JA}

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TEMP_JsonSchemaDecoderImplicit_usingJsonCirceString._ //, skeuoProject_AJ}

		val decoderJA: JsonCirce ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)


		val funcCirceJsonToSkeuoAvro: AvroCirce => Result[Fix[AvroSchema_S]] = decoderJA //compose avroCirceToJsonCirce

		// basis[JsonF, Fix[JsonF]]
		// 	algebra: JsonF[Fix[JsonF]] => Fix[JsonF]
		// 	coalgebra: Fix[JsonF] => JsonF[Fix[JsonF]]
		// NEED: avro -> json
		// 	skeuoEmbed_AJ
		// NEED: json -> json
		// 	skeuoEmbed_JJ
	}

	// avro decoder: Embed[AvroSchema_S, *]
	// implicit: Embed[AvroSchema_S, Fix[JsonSchema_F]] == skeuoEmbed_AJ

	import CirceAvroToSkeuoAvro._
	import CirceAvroToSkeuoJson._
	import CirceJsonToSkeuoJson._
	import CirceJsonToSkeuoAvro._



	object DecodingSkeuo {
		//jsonSchemaDecoder
		def decodeAvroSkeuoToCirceToJsonSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceAvroToSkeuoJson compose libToJsonAltered
		// libtojsonaltered: should be: avroskeuo -> circe-avro
		// funccircetojsonskeuo: should be: circe-avro -> json-skeuo

		def decodeJsonSkeuoToCirceToJsonSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceJsonToSkeuoJson compose libRenderAltered

		def decodeAvroSkeuoToCirceToAvroSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceAvroToSkeuoAvro compose libToJsonAltered

		def decodeJsonSkeuoToCirceToAvroSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceJsonToSkeuoAvro compose libRenderAltered

	}
}
