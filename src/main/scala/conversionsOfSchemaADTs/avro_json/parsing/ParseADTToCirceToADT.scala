package conversionsOfSchemaADTs.avro_json.parsing

import cats.data.NonEmptyList

import higherkindness.droste._
import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonDecoders._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._
import AvroSchema_S.{Field ⇒ FieldAvro, _}

import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}


/**
 *
 */
object ParseADTToCirceToADT {
	
	/**
	 * Definition from skeuomorph library - just make it possible to pass the parameter to it by apply catamorphism over it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L238
	 */
	
	val libToJson: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(AvroSchema_S.toJson).apply(_)
	
	
	val libToJsonAltered: Fix[AvroSchema_S] ⇒ JsonCirce = scheme.cata(myToJson).apply(_)
	
	
	// Copied from skeuomorph (is private) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L88
	def jsonType(value: String, attr: (String, JsonCirce)*): JsonCirce =
		JsonCirce.obj((("type" -> JsonCirce.fromString(value)) :: attr.toList): _*)
	
	
	def format(value: String): (String, JsonCirce) =
		"format" -> JsonCirce.fromString(value)
	
	
	def myToJson: Algebra[AvroSchema_S, JsonCirce] = Algebra {
		// TODO check if correct
		case TNull() ⇒ jsonType("null", attr = List():_*)
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
		
	}
	
	/**
	 * Definition from skeuomorph library - just applying anamorphism to make parameters passable to it
	 * Source = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/openapi/JsonSchema.scala#L94
	 */
	val libRender: Fix[JsonSchema_S] ⇒ JsonCirce = scheme.cata(JsonSchema_S.render).apply(_)
	
	
	import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
	
	
	val funcCirceToJsonSkeuo: JsonCirce ⇒ Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(_)
	
	val funcCirceToAvroSkeuo: JsonCirce ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(_)
	
	
	def decodeAvroSkeuoToCirceToJsonSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libToJsonAltered
	
	def decodeJsonSkeuoToCirceToJsonSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo compose libRender
	
	def decodeAvroSkeuoToCirceToAvroSkeuo: Fix[AvroSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo compose libToJsonAltered
	
	def decodeJsonSkeuoToCirceToAvroSkeuo: Fix[JsonSchema_S] ⇒ Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo compose libRender
	
	//////// ------------------------------------------------------------
	/*val avroS_to_JsonCirce: JsonCirce = libToJson(array1IntAvro_Fix_S)
	val jsonS_to_JsonCirce: JsonCirce = libRender(array1IntJson_Fix_S)
	
	val avroS_to_JsonCirce_to_JsonS: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(avroS_to_JsonCirce)
	
	
	val jsonS_to_JsonCirce_to_JsonS: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(jsonS_to_JsonCirce)
	val avroS_to_JsonCirce_To_AvroS: Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]].decodeJson(avroS_to_JsonCirce)*/
}
