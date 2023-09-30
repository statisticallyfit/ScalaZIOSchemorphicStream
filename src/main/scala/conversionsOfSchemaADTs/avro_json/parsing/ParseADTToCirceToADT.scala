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


	// NOTE: AvroCirce is just avro-string language containined within a JsonDialect string
	type AvroDialect = io.circe.Json
	type JsonDialect = io.circe.Json


	object LibFuncs {

		/**
		 * Definition from skeuomorph library - just applying anamorphism to make parameters passable to it
		 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
		 */
		val libRender: Fix[JsonSchema_S] ⇒ JsonDialect = scheme.cata(JsonSchema_S.render).apply(_)

		/**
		 * Definition from skeuomorph library - just make it possible to pass the parameter to it by apply catamorphism over it
		 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238
		 */

		val libToJson: Fix[AvroSchema_S] ⇒ JsonDialect = scheme.cata(AvroSchema_S.toJson).apply(_)



		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		//import embedImplicits.skeuoEmbed_AJ
		import projectImplicits.skeuoProject_AA

		// TODO IMPLICITS (1) = work to manipulate the implicits here (says drostebasisforfix using project[_,_] todo figure out change the Project??

		val libToJsonAltered: Fix[AvroSchema_S] ⇒ AvroDialect = scheme.cata(toCirceAvroDialect_fromAvroSkeuo).apply(_) //scheme.cata(toCirceJsonDialect_fromAvroSkeuo).apply(_)


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		//import embedImplicits.skeuoEmbed_AJ
		import projectImplicits.skeuoProject_JJ

		val libRenderAltered: Fix[JsonSchema_S] ⇒ JsonDialect = scheme.cata(toCirceJsonDialect_fromJsonSkeuo).apply(_)

	}
	import LibFuncs._



	// Copied from skeuomorph (is private) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L88
	// TODO add title here like in autoschema (so can include 'name' from avro-kind) = https://github.com/sauldhernandez/autoschema/blob/8e6f394acb3f4b55dbfe8916ffe33abf17aaef2e/src/main/scala/org/coursera/autoschema/AutoSchema.scala#L104
	def jsonType(value: String, attr: (String, JsonDialect)*): JsonDialect =
		JsonCirce.obj((("type" -> JsonCirce.fromString(value)) :: attr.toList): _*)


	def format(value: String): (String, JsonDialect) =
		"format" -> JsonCirce.fromString(value)






	// Alterign this function to include my own ObjectF-alike types: https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
	def toCirceJsonDialect_fromJsonSkeuo: Algebra[JsonSchema_S, JsonDialect] =
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

			case EnumF(cases: List[String], name: Option[String]) => {
				val base: JsonCirce = JsonCirce.obj(
					"type" -> JsonCirce.fromString("string"),
					"enum" -> JsonCirce.fromValues(cases.map(JsonCirce.fromString))
				)

				val withName: JsonCirce = name.fold(base)(n => base deepMerge JsonCirce.obj("name" -> JsonCirce.fromString(n)))

				val result: JsonCirce = if (name.isDefined) withName else base

				result
			}
					//"properties" -> JsonCirce.obj(properties.map(prop => prop.name -> prop.tpe): _*)
					//JsonCirce.fromValues(cases.map(JsonCirce.fromString))
				//JsonCirce.arr(cases.map(JsonCirce.fromString): _*)
				//JsonCirce.fromValues(cases.map(JsonCirce.fromString))

				//jsonType("string", "enum" -> JsonCirce.fromValues(cases.map(JsonCirce.fromString)))

			case SumF(cases: List[JsonCirce]) =>
				JsonCirce.obj("oneOf" -> JsonCirce.arr(cases: _*))
			case ReferenceF(value) =>
				JsonCirce.obj(
					s"$$ref" -> JsonCirce.fromString(value)
				)

			case PasswordF() => jsonType("string", format("password"))

		}



	// TODO how to alter libToJsonAltered so that null works? (funcCirceToAvroSkeuo not worknig)

	def toCirceJsonDialect_fromAvroSkeuo: Algebra[AvroSchema_S, JsonDialect] = Algebra {

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
			// NOTE: using 'title' instead of 'name' because that is what autoschema does (when converting from class -> jsonschema)

			JsonCirce.obj(
				"title" -> JsonCirce.fromString(name), // name for avro-string, title for json string
				"namespace" -> JsonCirce.fromString(namespace),
			)
		}

		// Avro record ---> json (named) object

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



			// NOTE: not adding namespace , doc , aliases here because the json-objectF does not contain those fields (just the avro-dialect does)

			/*val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonCirce =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonDialect = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc*/

			base  //return just base (no avro-dialect stuff like namespace, doc, aliases)

		}
		case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) ⇒ {

			val base: JsonCirce = JsonCirce.obj(
				//"title" -> JsonCirce.fromString(name),
				"type" → JsonCirce.fromString("string"),
				"enum" → JsonCirce.fromValues(symbols.map(JsonCirce.fromString)) //JsonCirce.arr(symbols.map(JsonCirce.fromString): _*)
			)
			/*val withNamespace: JsonDialect = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonDialect =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: JsonDialect = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc*/

			base  //return just base (no avro-dialect stuff like namespace, doc, aliases)
		}

		case TUnion(options: NonEmptyList[JsonCirce], name: Option[String]) => {

			val base: JsonCirce = JsonCirce.arr(options.toList: _*)

			val withName: JsonCirce = name.fold(base)((n: String) => base deepMerge JsonCirce.obj("name" -> JsonCirce.fromString(n)))

			val result: JsonCirce = if(name.isDefined) withName else base
			result
		}


		// TODO there is no json-dialect for Fixed --- does that mean cannot return anything here?
		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) => {

			val base: JsonCirce = JsonCirce.obj(
				"title" -> JsonCirce.fromString(name),
				"type" -> JsonCirce.fromString("fixed"),
				"size" -> JsonCirce.fromInt(size)
			)

			/*val withNamespace: JsonCirce = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: JsonDialect =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			withAliases*/

			base
		}
		// Source = https://hyp.is/gcf1CCv3Ee6M4RMGFdKckA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		/*case TDate() => jsonType("string", format("date"))

		// Source = https://hyp.is/b2IUFiv3Ee6z6geHBkITQA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TTimeMillis() ⇒ jsonType("string", format("time"))

		// Source = https://hyp.is/PUYlCB_5Ee6kOOPNKtp-zQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
		case TTimestampMillis() => jsonType("string", format("date-time"))*/

		// SOURCE of mapping = https://hyp.is/gcf1CCv3Ee6M4RMGFdKckA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TDate() => jsonType("string", format("date"))

		// SOURCE of mapping = https://hyp.is/UEBSwknVEe6MM1sQBZZdfQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
		case TTimestampMillis() => jsonType("string", format("date-time"))
			/*JsonCirce.obj(
				"type" -> JsonCirce.fromString("long"),
				"logicalType" -> JsonCirce.fromString("timestamp-millis")
			)*/

		// SOURCE of mapping = https://hyp.is/b2IUFiv3Ee6z6geHBkITQA/docs.airbyte.com/understanding-airbyte/json-avro-conversion
		case TTimeMillis() => jsonType("string", format("time"))
			/*JsonCirce.obj(
				"type" -> JsonCirce.fromString("int"),
				"logicalType" -> JsonCirce.fromString("time-millis")
			)*/

		// TODO no json-dialect equivalent, should I include or not? (like for namedtype, fixed etc)
		case TDecimal(precision: Int, scale: Int) =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("bytes"),
				"logicalType" -> JsonCirce.fromString("decimal"),
				"precision" -> JsonCirce.fromInt(precision),
				"scale" -> JsonCirce.fromInt(scale)
			)

		/*case PasswordF() => jsonType("string", format("password"))*/

	}


	// TODO MAJOR - this thing is resulting in avro-string-like not strictly json-circe ---- see they say 'record' instead of 'object'. Need to clean this up to be purely avro-string and compare it to the original avro-string, and need to clean up above one and make it purely JSON-circe to compare it to original json-string



	// NOTE: converting the avro-circe-str to json-circe-str so that the avro-str primitives can be seen as json-str primitives

	def avroDialectToJsonDialect: AvroDialect => JsonDialect = (avroCirce: AvroDialect) => {

		import org.apache.avro.{Schema ⇒ AvroSchema_A}
		import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		//import embedImplicits.skeuoEmbed_AA
		import projectImplicits.skeuoProject_AA


		// STEPS:
		// avro-str-dialect
		// apache avro
		// skeuo-avro
		// json dialect (to circe json dialect from skeuoavro)

		type AvroDialectStr = String

		// STEP 1: convert from avro-circe (dialect) --> apache avro
		val avroStr: AvroDialectStr = avroCirce.toString()
		val avroApache: AvroSchema_A = new AvroSchema_A.Parser().parse(avroStr)
		// STEP 2: convert apache-avro -> skeuo-avro
		val avroSkeuo: Fix[AvroSchema_S] = Skeuo_Apache.apacheToSkeuoAvroSchema(avroApache)
		// STEP 3: convert avro-skeuo --> json dialect
		val skeuoAvroToJsonDialect: Fix[AvroSchema_S] => JsonDialect = scheme.cata(toCirceJsonDialect_fromAvroSkeuo).apply(_)

		val jsonCirce: JsonDialect = skeuoAvroToJsonDialect(avroSkeuo)

		println("\n\nINSIDE DIALECT CONVERTER: " +
			s"\navroStr = $avroStr" +
			s"\navroApache = $avroApache" +
			s"\navroSkeuo = $avroSkeuo" +
			s"\njsonCirce = $jsonCirce ")

		jsonCirce
	}


	def toCirceAvroDialect_fromAvroSkeuo: Algebra[AvroSchema_S, AvroDialect] = Algebra {
		//import io.circe.JsonObject
		//JsonCirce.JObject(io.circe.JsonObject.)
		//JsonCirce.fromJsonObject(JsonObject)
		case TNull() ⇒ JsonCirce.fromString("null")
		case TInt() => JsonCirce.fromString("int")
		case TString() => JsonCirce.fromString("string")
		case TBoolean() => JsonCirce.fromString("boolean")
		case TLong() => JsonCirce.fromString("long")
		case TFloat() => JsonCirce.fromString("float")
		case TDouble() => JsonCirce.fromString("double")
		case TBytes() => JsonCirce.fromString("bytes")

		// NOTE: converting the avro skeuo (only primitives) -> json dialect
		/*case TNull() ⇒ JsonCirce.obj(
			"type" -> JsonCirce.fromString("object"),
			"properties" -> JsonCirce.obj(/*properties.map(prop => prop.name -> prop.tpe)*/List(): _*),
			"required" -> JsonCirce.fromValues(/*required*/List().map(JsonCirce.fromString))
		)
		case TInt() => jsonType("integer", format("int32"))
		case TString() => jsonType("string")
		case TBoolean() => jsonType("boolean")
		case TLong() => jsonType("integer", format("int64"))
		case TFloat() => jsonType("number", format("float"))
		case TDouble() =>jsonType("number", format("double"))
		case TBytes() => jsonType("string", format("byte"))*/

		// NOTE: rst of the types are avro dialect
		case TArray(inner: AvroDialect) ⇒ JsonCirce.obj(
			"type" -> JsonCirce.fromString("array"),
			"items" -> inner
		)

		case TMap(values: AvroDialect) => JsonCirce.obj(
			"type" -> JsonCirce.fromString("map"),
			"values" -> values
		)

		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[AvroDialect]]) ⇒ {




			val base: AvroDialect = JsonCirce.obj(
				"type" -> JsonCirce.fromString("record"),
				"name" -> JsonCirce.fromString(name),
				"fields" -> JsonCirce.arr(fields.map(field2Obj): _*)
			)

			val withNamespace: AvroDialect = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: AvroDialect =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: AvroDialect = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}


		case TEnum(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], symbols: List[String]) ⇒ {

			val base: AvroDialect = JsonCirce.obj(
				"type" → JsonCirce.fromString("enum"),
				"name" → JsonCirce.fromString(name),
				"symbols" → JsonCirce.fromValues(symbols.map(JsonCirce.fromString)) //JsonCirce.arr(symbols.map(JsonCirce.fromString): _*)
			)
			val withNamespace: AvroDialect = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: AvroDialect =
				if (aliases.isEmpty)
					withNamespace
				else
					withNamespace deepMerge JsonCirce.obj("aliases" -> JsonCirce.arr(aliases.map(JsonCirce.fromString): _*))

			val withDoc: AvroDialect = doc.fold(withAliases)(f => withAliases deepMerge JsonCirce.obj("doc" -> JsonCirce.fromString(f)))

			withDoc
		}


		case TUnion(options: NonEmptyList[AvroDialect], name: Option[String]) => JsonCirce.obj(
			"name" -> JsonCirce.fromString(name.getOrElse("")),
			"type" -> JsonCirce.arr(options.toList: _*)
		)//JsonCirce.arr(options.toList: _*)

		//case TUnion(options: NonEmptyList[AvroDialect]) => JsonCirce.arr(options.toList: _*)


		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) => {

			val base: AvroDialect = JsonCirce.obj(
				"type" -> JsonCirce.fromString("fixed"),
				"name" -> JsonCirce.fromString(name),
				"size" -> JsonCirce.fromInt(size)
			)

			val withNamespace: AvroDialect = namespace.fold(base)(n => base deepMerge JsonCirce.obj("namespace" -> JsonCirce.fromString(n)))

			val withAliases: AvroDialect =
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
		case TDecimal(precision: Int, scale: Int) =>
			JsonCirce.obj(
				"type" -> JsonCirce.fromString("bytes"),
				"logicalType" -> JsonCirce.fromString("decimal"),
				"precision" -> JsonCirce.fromInt(precision),
				"scale" -> JsonCirce.fromInt(scale)
			)


	}

	// NOTE: RULE:
	//  ---- case 1: avrodialectconverter + jsondialect_outputavroskeuo
	// ---- case 2: NO avrodialectconverter + avroadialect_outputavroskeuo

	object CirceAvroToSkeuoAvro {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		import embedImplicits.skeuoEmbed_AA
		import projectImplicits.skeuoProject_AA



		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForDialects => impl}



		//import impl.Decoder_InputJsonDialect_OutputAvroSkeuo._
		import impl.Decoder_InputAvroDialect_OutputAvroSkeuo._
		//import impl.Decoder_InputJsonDialect_OutputJsonSkeuo._



		val decoderAA: AvroDialect ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)



		val f1: AvroDialect => Result[Fix[AvroSchema_S]] = (av: AvroDialect)  => decoderAA(avroDialectToJsonDialect(av))

		val f2: AvroDialect => Result[Fix[AvroSchema_S]] = avroDialectToJsonDialect andThen decoderAA

		val funcCirceAvroToSkeuoAvro: AvroDialect => Result[Fix[AvroSchema_S]] = decoderAA //compose avroDialectToJsonDialect


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


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		import embedImplicits.skeuoEmbed_AJ
		import projectImplicits.skeuoProject_AJ


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForDialects => impl}


		//import impl.Decoder_InputJsonDialect_OutputAvroSkeuo._
		import impl.Decoder_InputAvroDialect_OutputAvroSkeuo._
		//import impl.Decoder_InputJsonDialect_OutputJsonSkeuo._


		val decoderAJ: JsonDialect ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)


		// NOTE:
		// --- interpret (before decoder) from avro-dialect
		// --- decoder: using json-dialect string

		val funcCirceAvroToSkeuoJson: AvroDialect => Result[Fix[JsonSchema_S]] = decoderAJ //compose avroDialectToJsonDialect


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


	object CirceJsonToSkeuoAvro {


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		import embedImplicits.skeuoEmbed_JA
		//import projectImplicits.skeuoProject_AA


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForDialects => impl}


		//import impl.Decoder_InputJsonDialect_OutputAvroSkeuo._
		//import impl.Decoder_InputAvroDialect_OutputAvroSkeuo._
		import impl.Decoder_InputJsonDialect_OutputJsonSkeuo._

		val decoderJA: JsonDialect ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)


		val funcCirceJsonToSkeuoAvro: JsonDialect => Result[Fix[AvroSchema_S]] = decoderJA //compose avroCirceToJsonDialect

		// basis[JsonF, Fix[JsonF]]
		// 	algebra: JsonF[Fix[JsonF]] => Fix[JsonF]
		// 	coalgebra: Fix[JsonF] => JsonF[Fix[JsonF]]
		// NEED: avro -> json
		// 	skeuoEmbed_AJ
		// NEED: json -> json
		// 	skeuoEmbed_JJ
	}


	object CirceJsonToSkeuoJson {


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		import embedImplicits.skeuoEmbed_JJ
		//import projectImplicits.skeuoProject_AA


		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForDialects => impl}


		//import impl.Decoder_InputJsonDialect_OutputAvroSkeuo._
		//import impl.Decoder_InputAvroDialect_OutputAvroSkeuo._
		import impl.Decoder_InputJsonDialect_OutputJsonSkeuo._

		val decoderJJ: JsonDialect ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)


		val funcCirceJsonToSkeuoJson: JsonDialect => Result[Fix[JsonSchema_S]] = decoderJJ //compose avroCirceToJsonDialect

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
