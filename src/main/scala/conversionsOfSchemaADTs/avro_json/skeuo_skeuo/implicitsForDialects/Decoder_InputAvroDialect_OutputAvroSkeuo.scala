package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForDialects




// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)
import io.circe._
import io.circe.Decoder
import io.circe.Decoder.{Result, decodeInt, resultInstance}
import io.circe.{Json => JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import cats.syntax.all._
import conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.SkeuomorphExample.res
import higherkindness.skeuomorph.openapi.JsonSchemaF
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
object Decoder_InputAvroDialect_OutputAvroSkeuo {


	import AvroSchema_S._


	implicit def identifyAvroDecoderWithPriorityBasicDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {


		// TODO must use combination of forproduct1 ("type") and the above, where abvoe is just for simple types (primitives) and the "type" extraction for when in composed types (like array)



		// Decoder.decodeString.flatMap {
			//case "\"null\"" | """int""" | "\"string\"" | "\"boolean\"" | "\"long\"" | "\"float\"" | "\"double\"" | "\"bytes\"" => basicAvroSchemaDecoder[A] //primitiveAvroSchemaDecoder[A]

		Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).flatMap {

			//case "null" | "int" | "string" | "boolean" | "long" | "float" | "double" | "bytes" => basicAvroSchemaDecoder[A]

			case ("null", _) | ("int", _) | ("string", _) | ("boolean", _) | ("long", _) | ("float", _) | ("double", _) | ("bytes", _) => primitiveAvroSchemaDecoder[A]

			//case (x, _) => s"$x is not well formed type".asLeft
			case _ => avroSchemaDecoder[A]
		}

		//Decoder.forProduct1[String, String]("")(Tuple1[String](_)._1).flatMap {
		//Decoder.decodeString.flatMap {
		/*Decoder.decodeTuple1[String].flatMap {
			//case "\"null\"" | """int""" | "\"string\"" | "\"boolean\"" | "\"long\"" | "\"float\"" | "\"double\"" | "\"bytes\"" => basicAvroSchemaDecoder[A] //primitiveAvroSchemaDecoder[A]
			case ("null", )| ("int", ) | ("string", ) | ("boolean", ) | ("long", ) | ("float", ) | ("double", ) | ("bytes", ) => basicAvroSchemaDecoder[A]

			// TODO must use combination of forproduct1 ("type") and the above, where abvoe is just for simple types (primitives) and the "type" extraction for when in composed types (like array)

			//case (x, _) => s"$x is not well formed type".asLeft
			case _ => avroSchemaDecoder[A]
		}*/
	}

	private def primitiveAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.decodeString.flatMap {

			case "null" | "int" | "string" | "boolean" | "long" | "float" | "double" | "bytes" => basicAvroSchemaDecoder[A]

			case _ => avroSchemaDecoder[A]
		}
	}

	private def basicAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		import AvroSchema_S._

		//Decoder.forProduct1[String, String]("items")(Tuple1[String](_)._1).emap {
		Decoder.decodeString.emap {
			case "null" => `null`[A]().embed.asRight
			case "int" => int[A]().embed.asRight
			case "string" => string[A]().embed.asRight
			case "boolean" => boolean[A]().embed.asRight
			case "long" => long[A]().embed.asRight
			case "float" => float[A]().embed.asRight
			case "double" => double[A]().embed.asRight
			case "bytes" => bytes[A]().embed.asRight

			case x => s"$x is not well formed type".asLeft
		}

	}


	private def avroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		//fieldAvroSchemaDecoder orElse
		primitiveAvroSchemaDecoder orElse
		logicalTypeAvroSchemaDecoder orElse
		arrayAvroSchemaDecoder orElse
		mapAvroSchemaDecoder orElse
		//enumAvroSchemaDecoder orElse
		recordAvroSchemaDecoder /*orElse
		enumAvroSchemaDecoder*/
		/*orElse
			recordAvroSchemaDecoder orElse
			namedTypeAvroSchemaDecoder*/

		// NOTE; namedtype AFTER record because namedtype is a subset of record and if put it first, the records will incorrectly be put as namedtype


	}


	private def logicalTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {


		Decoder.forProduct4[(String, Option[String], Option[Int], Option[Int]), String, Option[String], Option[Int], Option[Int]]("type", "logicalType", "precision", "scale")(Tuple4.apply).emap {

			case ("int", Some("date"), None, None) => date[A]().embed.asRight
			case ("long", Some("timestamp-millis"), None, None) => timestampMillis[A]().embed.asRight
			case ("int", Some("time-millis"), None, None) => timeMillis[A]().embed.asRight
			case ("bytes", Some("decimal"), Some(precision), Some(scale)) => decimal[A](precision, scale).embed.asRight

			case (x, _, _, _) => s"$x is not well formed logical-type".asLeft
		}
	}

	private def arrayAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.instance { c: HCursor =>
			for {
				items: A <- c.downField("items").as[A]
				_ <- validateType(c, "array")
			} yield AvroSchema_S.array[A](items).embed
		}
	}



	private def enumAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.instance { c: HCursor =>



			// NOTE: the avro enum extraction
			def makeAvroCirceEnum(c: HCursor): Result[A] = {

				val tenum: (String, Option[String], List[String], Option[String], List[String]) => AvroSchema_S[A] = (name, namespace, aliases, doc, symbols) => AvroSchema_S.`enum`[A](name, namespace, aliases, doc, symbols)

				val resultArgs: Result[(String, Option[String], List[String], Option[String], List[String])] =
					for {
						// validation
						// TODO - why error when including namspace, aliases, properties for validation below?
						// TODO - maybe use the avro-string circe instead of json-string circe (from avro-skeuo) so no more mish-mash between json/avro string parameters.

						_ <- validateType(c, "enum")
						_ <- propertyExists(c, "type")
						//_ <- propertyExists(c, "name")
						//_ <- propertyExists(c, "symbols")

						//_ <- propertyExists(c, "namespace")
						//_ <- propertyExists(c, "aliases")
						//_ <- propertyExists(c, "doc")

						name: String <- c.downField("name").as[Option[String]].map(_.getOrElse(""))

						namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

						aliases: List[String] <- c.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))

						doc: Option[String] <- c.downField("doc").as[Option[Option[String]]].map(_.getOrElse(None))

						symbols: List[String] <- c.downField("symbols").as[Option[List[String]]].map(_.getOrElse(List.empty))

					} yield (name, namespace, aliases, doc, symbols)


				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (name, namespace, aliases, doc, symbols) => tenum(name, namespace, aliases, doc, symbols) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeAvroCirceEnum (avro -> avro):")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed
			}

			makeAvroCirceEnum(c)
		}
	}

	// NOW
	private def mapAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.instance { c: HCursor =>

			// Added by @statisticallyfit:

			/**
			 * Extract the string { "type": "something" } or simple primitive "something" that comes after 'values'
			 *
			 * @return
			 */
			def makeAvroCirceMap(c: HCursor): Result[A] = {

				val tmap: A => AvroSchema_S[A] = (inner) => AvroSchema_S.map[A](inner)


				val resultArgs: Result[A] = for {
					// validation
					_ <- validateType(c, "map")
					_ <- propertyExists(c, "type")
					//_ <- propertyExists(c, "values")

					inner: A <- {

						val resTpe: Result[A] = c.downField("values").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A]) /*.as[Option[String]].map(_.getOrElse(""))*/

						val resMap: Result[TMap[A]] = resTpe.map(tpe => AvroSchema_S.TMap(tpe))

						println(s"\n\nINSIDE makeAvroCirceMap (avro -> avro):")
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


	private def recordAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[A] = {

		Decoder.instance { c: HCursor =>


			def makeAvroCirceRecord(c: HCursor): Result[A] = {

				val trecord: (String, Option[String], List[String], Option[String], List[FieldAvro[A]]) => AvroSchema_S[A] = (name, namespace, aliases, doc, fields) => AvroSchema_S.record[A](name, namespace, aliases, doc, fields)


				import cats.Traverse

				val resultArgs: Result[(String, Option[String], List[String], Option[String], List[FieldAvro[A]])] =
					for {
						// validation
						// TODO - why error when including namspace, aliases, properties for validation below?
						// TODO - maybe use the avro-string circe instead of json-string circe (from avro-skeuo) so no more mish-mash between json/avro string parameters.
						_ <- validateType(c, "record")
						_ <- propertyExists(c, "type")
						//_ <- propertyExists(c, "name")
						//_ <- propertyExists(c, "fields")
						//_ <- propertyExists(c, "namespace")
						//_ <- propertyExists(c, "aliases")
						//_ <- propertyExists(c, "doc")

						name: String <- c.downField("name").as[Option[String]].map(_.getOrElse(""))

						namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

						aliases: List[String] <- c.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))

						doc: Option[String] <- c.downField("doc").as[Option[Option[String]]].map(_.getOrElse(None))


						theFieldsJson <- c.downField("fields").as[List[JsonCirce]]

						listFieldsAvro: List[FieldAvro[A]] <- Traverse[List].traverse(theFieldsJson)(
							(fj: JsonCirce) => {
								val inner: Result[FieldAvro[A]] = for {
									fname: String <- fj.hcursor.downArray.downField("name").as[Option[String]].map(_.getOrElse(""))
									faliases <- fj.hcursor.downArray.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))
									ftype <- fj.hcursor.downArray.downField("type").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A])
								} yield FieldAvro[A](fname, faliases, None, None, ftype)

								inner
							}
						)
					} yield (name, namespace, aliases, doc, listFieldsAvro)


				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (name, namespace, aliases, doc, fields) => trecord(name, namespace, aliases, doc, fields) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeAvroCirceRecord (avro -> avro):")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed

			}

			makeAvroCirceRecord(c)
		}
	}

	private implicit def fieldAvroSchemaDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[FieldAvro[A]] = {

		Decoder.instance { c: HCursor =>
			val res: Result[FieldAvro[A]] = for {
				name: String <- c.downField("name").as[Option[String]].map(_.getOrElse(""))

				aliases: List[String] <- c.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))

				doc: Option[String] <- c.downField("doc").as[Option[Option[String]]].map(_.getOrElse(None))

				//TODO make decoder for Order
				// order: Option[Order] <- c.downField("order").as[Option[Option[Order]]].map(_.getOrElse(None))

				theType: A <- c.downField("type").as[A](/*Decoder.decodeOption(*/identifyAvroDecoderWithPriorityBasicDecoder[A]) //)

			} yield FieldAvro[A](name, aliases, doc, None, theType)


			res
		}
	}



	private def namedTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {


		Decoder.instance { c: HCursor =>


			def makeAvroCirceNamedType(c: HCursor): Result[A] = {

				val tnamedtype: (String, Option[String]) => AvroSchema_S[A] = (name, namespace) => AvroSchema_S.namedType[A](namespace.getOrElse(""), name)

				val resultArgs: Result[(String, Option[String])] =
					for {
						// TODO: verify property exists for each type-function because otherwise it assigns the wrong class to the situation.

						//_ <- propertyExists(c, "name") // name for avro-string, title for json-string
						//_ <- propertyExists(c, "namespace")

						name: String <- c.downField("name").as[Option[String]].map(_.getOrElse(""))

						namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

					} yield (name, namespace)


				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (name, namespace) => tnamedtype(name, namespace) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeAvroCirceNamedType (avro -> avro):")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed

			}

			//assert (isNamedType(c))

			makeAvroCirceNamedType(c)

		}
	}
}
