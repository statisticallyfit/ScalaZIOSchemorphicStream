package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForDialects




// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)
import io.circe._
import io.circe.Decoder
import io.circe.Decoder.{Result, resultInstance}
import io.circe.{Json => JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import cats.syntax.all._
//import cats.implicits._
//import cats.syntax._



import higherkindness.droste._
import higherkindness.droste.data._
//import higherkindness.droste.syntax.all._
import higherkindness.droste.syntax.embed._
import higherkindness.skeuomorph.openapi.schema._

import scala.language.postfixOps
import scala.language.higherKinds
//import scala.language.implicitConversions



import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}


import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.utilForDecoders.UtilForDecoder._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/

/**
 *
 */
object Decoder_InputJsonDialect_OutputAvroSkeuo {


	import AvroSchema_S._


	implicit def identifyAvroDecoderWithPriorityBasicDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).flatMap {

			case ("integer", _) | ("number", _) | ("string", _) | ("boolean", _) => basicAvroSchemaDecoder[A]

			//case (x, _) => s"$x is not well formed type".asLeft
			case _ => avroSchemaDecoder[A]
		}
	}

	private def basicAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {


		Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).emap {

			case ("integer", Some("int32")) => int[A]().embed.asRight
			case ("integer", Some("int64")) => long[A]().embed.asRight
			case ("number", Some("float")) => float[A]().embed.asRight
			case ("number", Some("double")) => double[A]().embed.asRight

			case ("string", Some("byte")) => bytes[A]().embed.asRight
			//case ("string", Some("binary")) => binary[A]().embed.asRight
			case ("boolean", _) => boolean[A]().embed.asRight

			/*// Logical types:
			// Source = https://hyp.is/QGAf_knVEe6TsC_bpOcmJw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			case ("string", Some("date")) => date[A]().embed.asRight
			// Source = https://hyp.is/UEBSwknVEe6MM1sQBZZdfQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			case ("string", Some("date-time")) => timestampMillis[A]().embed.asRight
			// Source = https://hyp.is/M62zKknVEe6WIGvlurzrGQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
			case ("string", Some("time")) => timeMillis[A]().embed.asRight
			//case ("string", Some("password")) => password[A]().embed.asRight
			// TODO no decimal from json*/


			case ("string", _) => string[A]().embed.asRight
			case (x, _) => s"$x is not well formed type".asLeft
		}
	}

	private def avroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

		logicalTypeAvroSchemaDecoder orElse
			arrayAvroSchemaDecoder orElse
			mapAvroSchemaDecoder orElse
			recordAvroSchemaDecoder orElse
			namedTypeAvroSchemaDecoder orElse
			enumAvroSchemaDecoder
		// NOTE; namedtype AFTER record because namedtype is a subset of record and if put it first, the records will incorrectly be put as namedtype


	} /*orElse
			mapAvroSchemaDecoder orElse
			recordAvroSchemaDecoder orElse
			namedTypeAvroSchemaDecoder orElse
			unionAvroSchemaDecoder orElse
			fixedAvroSchemaDecoder orElse
			enumAvroSchemaDecoder*/


	private def logicalTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {


		def decoderForDateAndTimestampAndMillis: Decoder[A] = {
			Decoder.forProduct2[(String, String), String, String]("type", "format")(Tuple2.apply).emap {

				case ("string", "date") => date[A]().embed.asRight
				case ("string", "date-time") => timestampMillis[A]().embed.asRight
				case ("string", "time") => timeMillis[A]().embed.asRight

				case (x, _) => s"$x is not well formed logical-type (date ,timestamp, or millis)".asLeft
			}
		}

		def decoderForDecimal: Decoder[A] = {
			Decoder.forProduct4[(String, Option[String], Option[Int], Option[Int]), String, Option[String], Option[Int], Option[Int]]("type", "logicalType", "precision", "scale")(Tuple4.apply).emap {

				case ("bytes", Some("decimal"), Some(precision), Some(scale)) => decimal[A](precision, scale).embed.asRight

				case (x, _, _, _) => s"$x is not well formed (decimal) logical-type".asLeft
			}
		}


		decoderForDateAndTimestampAndMillis orElse
			decoderForDecimal
	}


	private def arrayAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] =
		Decoder.instance { c: HCursor =>
			for {
				items: A <- c.downField("items").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A])
				_ <- validateType(c, "array")
			} yield AvroSchema_S.array(items).embed
		}

	private def mapAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.instance { c: HCursor =>

			// Added by @statisticallyfit:

			/**
			 * Extract the string { "type": "something" } that comes after 'additionalProperties'
			 *
			 * @return
			 */
			// Added by @statisticallyfit:
			// Separating by different kinds of object classes so can properly interpret a MAP from avro into json
			def makeAvroCirceMap(c: HCursor): Result[A] = {

				val tmap: A => AvroSchema_S[A] = (inner) => AvroSchema_S.map[A](inner)


				val resultArgs: Result[A] = for {
					// validation
					_ <- validateType(c, "object")
					_ <- propertyExists(c, "type")
					//_ <- propertyExists(c, "additionalProperties")

					inner: A <- {

						val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A])

						val resMap: Result[TMap[A]] = resTpe.map(tpe => AvroSchema_S.TMap(tpe))

						println(s"\n\nINSIDE makeAvroCirceMap:")
						println(s"resTpe = $resTpe")
						println(s"resMap = $resMap")

						resTpe
					}

				} yield inner


				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (inner) => tmap(inner) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeAvroCirceMap:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed
			}


			//assert(isObjectMap)

			makeAvroCirceMap(c)
		}
	}

	private def namedTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {


		Decoder.instance { c: HCursor =>


			def makeAvroCirceNamedType(c: HCursor): Result[A] = {

				val tnamedtype: (String, Option[String]) => AvroSchema_S[A] = (title, namespace) => AvroSchema_S.namedType[A](namespace.getOrElse(""), title)

				val resultArgs: Result[(String, Option[String])] =
					for {
						// TODO: verify property exists for each type-function because otherwise it assigns the wrong class to the situation.

						_ <- propertyExists(c, "title") // name for avro-string, title for json-string
						_ <- propertyExists(c, "namespace")

						title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

						namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

					} yield (title, namespace)


				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (title, namespace) => tnamedtype(title, namespace) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeAvroCirceNamedType:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed

			}

			//assert (isNamedType(c))

			makeAvroCirceNamedType(c)

		}
	}


	private def recordAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = Decoder.instance { c: HCursor =>


		def makeAvroCirceRecord(c: HCursor): Result[A] = {

			val trecord: (String, Option[String], List[String], Option[String], List[FieldAvro[A]]) => AvroSchema_S[A] = (title, namespace, aliases, doc, fields) => AvroSchema_S.record[A](title, namespace, aliases, doc, fields)

			val resultArgs: Result[(String, Option[String], List[String], Option[String], List[FieldAvro[A]])] =
				for {
					// validation
					// TODO - why error when including namspace, aliases, properties for validation below?
					// TODO - maybe use the avro-string circe instead of json-string circe (from avro-skeuo) so no more mish-mash between json/avro string parameters.
					_ <- validateType(c, "object")
					_ <- propertyExists(c, "title")
					_ <- propertyExists(c, "type")
					_ <- propertyExists(c, "properties")
					//_ <- propertyExists(c, "namespace")
					//_ <- propertyExists(c, "aliases")
					//_ <- propertyExists(c, "doc")

					title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

					namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

					aliases: List[String] <- c.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))

					doc: Option[String] <- c.downField("doc").as[Option[Option[String]]].map(_.getOrElse(None))

					// NOTE: no point extracting the 'required' because that goes as arg just in the json-skeuo and avro-skeuo has no arg 'required'
					//required: List[String] <- c.downField("required").as[Option[List[String]]].map(_.getOrElse(List.empty))

					fields: List[FieldAvro[A]] <- {

						val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
							Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyAvroDecoderWithPriorityBasicDecoder[A]))
						)

						val resMap: Result[Map[String, A]] = resOptMap.map(_.getOrElse(Map.empty))


						// Step 1: first, since getting from json-circe-string, we are interpreting result into a json-skeuo.
						val resListProps: Result[List[JsonSchema_S.Property[A]]] = resMap
							.map((mapStrA: Map[String, A]) => mapStrA.toList.map((JsonSchema_S.Property.apply[A] _).tupled /*{
							val funcTup: ((String, A)) => JsonSchema_S.Property[A] = (JsonSchema_S.Property.apply[A] _).tupled
							funcTup(tup)
						}*/
							))

						// Step 2: convert from the json-skeuo into avro-skeuo.
						val resListFields: Result[List[FieldAvro[A]]] = resListProps.map((lstProps: List[JsonSchema_S.Property[A]]) => lstProps.map((p: JsonSchema_S.Property[A]) => property2Field(p)))


						println(s"\n\nINSIDE makeAvroCirceObjectNamed:")
						println(s"resOptMap  = $resOptMap")
						println(s"resMap  = $resMap")
						println(s"resListFields = $resListFields")

						resListFields
					}
				} yield (title, namespace, aliases, doc, fields)


			val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (title, namespace, aliases, doc, fields) => trecord(title, namespace, aliases, doc, fields) }
			val result_embed: Result[A] = result_noEmbed.map(_.embed)

			println(s"\n\nINSIDE makeAvroCirceRecord:")
			println(s"result (not embed) = $result_noEmbed")
			println(s"result (embed) = $result_embed")

			result_embed

		}

		makeAvroCirceRecord(c)
	}

	private def unionAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???

	private def fixedAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???


	private def enumAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {
		Decoder.instance(c =>
			for {
				_ <- validateType(c, "string") // type -> string
				//_ <- propertyExists(c, "title")
				_ <- propertyExists(c, "type")
				_ <- propertyExists(c, "enum")

				cases: List[String] <- c.downField("enum").as[Option[List[String]]].map(_.getOrElse(List.empty[String]))

			} yield AvroSchema_S.`enum`[A](name = "", namespace = None, aliases = List(), doc = None, symbols = cases).embed
		)
	}
}
