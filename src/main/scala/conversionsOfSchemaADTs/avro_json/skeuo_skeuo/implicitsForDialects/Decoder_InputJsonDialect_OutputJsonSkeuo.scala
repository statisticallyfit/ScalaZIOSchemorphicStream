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

/**
 *
 */
object Decoder_InputJsonDialect_OutputJsonSkeuo {


	import JsonSchema_S._


	implicit def identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {

		// TODO identify here type, enum combo - if -> go to enumdecoder, else -> go to basic decoder where we do THIS dividing (below). Then in the basic2 decoder have what is now in the current basic decoder.

		// TODO do the same for the enumavrodcoder and for namedtypeavrodecoder. (And get changes from the fixedunionnamedtype_branch!)


		Decoder.forProduct2[(String, Option[List[String]]), String, Option[List[String]]]("type", "enum")(Tuple2.apply).flatMap {

			case ("string", casesOpt: Option[List[String]]) => casesOpt.isDefined match {

				case true => enumJsonSchemaDecoder[A]
				case false => primitiveJsonSchemaDecoder[A]
			}

			case _ => primitiveJsonSchemaDecoder[A]
		}
	}

	private def primitiveJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {

		Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).flatMap {

			case ("integer", _) | ("number", _) | ("string", _) | ("boolean", _) => basicJsonSchemaDecoder[A]

			//case (x, _) => s"$x is not well formed type".asLeft
			case _ => jsonSchemaDecoder[A]
		}
	}


	private def basicJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {


		Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).emap {

			case ("integer", Some("int32")) => integer[A]().embed.asRight
			case ("integer", Some("int64")) => long[A]().embed.asRight
			case ("integer", _) => integer[A]().embed.asRight
			case ("number", Some("float")) => float[A]().embed.asRight
			case ("number", Some("double")) => double[A]().embed.asRight
			case ("number", _) => float[A]().embed.asRight
			case ("string", Some("byte")) => byte[A]().embed.asRight
			case ("string", Some("binary")) => binary[A]().embed.asRight
			case ("boolean", _) => boolean[A]().embed.asRight
			case ("string", Some("date")) => date[A]().embed.asRight
			case ("string", Some("date-time")) => dateTime[A]().embed.asRight
			case ("string", Some("password")) => password[A]().embed.asRight
			case ("string", _) => string[A]().embed.asRight
			case (x, _) => s"$x is not well formed type".asLeft
		}
	}


	private def jsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {
		// TODO fix in case the 'type' for objectmap is NOT a simple type - must have a checker checking for the simple types and then decide to pass to jsonschemadecoder (here)
		//identifyDecoderWithPriorityBasicDecoder orElse

		//enumJsonSchemaDecoder orElse
		logicalTypeJsonSchemaDecoder orElse
			referenceJsonSchemaDecoder orElse
			sumJsonSchemaDecoder orElse
			arrayJsonSchemaDecoder orElse
			objectJsonSchemaDecoder
	}


	private def logicalTypeJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {


		Decoder.forProduct2[(String, String), String, String]("type", "format")(Tuple2.apply).emap {

			case ("string", "date") => date[A]().embed.asRight
			case ("string", "date-time") => dateTime[A]().embed.asRight
			//case ("string", "time") => timeMillis[A]().embed.asRight

			case (x, _) => s"$x is not well formed logical-type (date ,timestamp, or millis)".asLeft
		}

		// No decimal from json-skeuo
		/*def decoderForDecimal: Decoder[A] = {
			Decoder.forProduct4[(String, Option[String], Option[Int], Option[Int]), String, Option[String], Option[Int], Option[Int]]("type", "logicalType", "precision", "scale")(Tuple4.apply).emap {

				case ("bytes", Some("decimal"), Some(precision), Some(scale)) => decimal[A](precision, scale).embed.asRight

				case (x, _, _, _) => s"$x is not well formed (decimal) logical-type".asLeft
			}
		}*/

	}


	private def objectJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =

		Decoder.instance { c: HCursor =>


			// Added by @statisticallyfit:

			/**
			 * Extract the string { "type": "something" } that comes after 'additionalProperties'
			 *
			 * @return
			 */


			// Added by @statisticallyfit:
			// Separating by different kinds of object classes so can properly interpret a MAP from avro into json
			def makeJsonCirceObjectMap(c: HCursor): Result[A] = {

				val objmap: JsonSchema_S.AdditionalProperties[A] => JsonSchema_S[A] = (addProps) => JsonSchema_S.objectMap[A](additionalProperties = addProps)


				val resultArgs: Result[JsonSchema_S.AdditionalProperties[A]] = for {

					// NOTE: got withFilter error here in for-comprehension so using this plugin = https://github.com/oleg-py/better-monadic-for

					//title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

					_ <- propertyExists(c, "additionalProperties")

					addProps: AdditionalProperties[A] <- {

						val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A])

						val resAddProps: Result[JsonSchema_S.AdditionalProperties[A]] = resTpe.map(tpe => JsonSchema_S.AdditionalProperties(tpe = tpe))

						println(s"\n\nINSIDE makeJsonCirceObjectMap:")
						println(s"resTpe = $resTpe")
						println(s"resAddProps = $resAddProps")

						resAddProps
					}

				} yield /*objnamedmap*/ (addProps) //JsonSchema_S.objectNamedMap[A](name = title, additionalProperties = addProps).embed


				val result_noEmbed: Result[JsonSchema_S[A]] = resultArgs.map { case (addProps) => objmap(addProps) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeJsonCirceObjectNamedMap:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed
			}


			// Added by @statisticallyfit:
			// Separating by different kinds of object classes so can properly interpret a MAP from avro into json
			def makeJsonCirceObjectNamedMap(c: HCursor): Result[A] = {

				val objnamedmap: (String, JsonSchema_S.AdditionalProperties[A]) => JsonSchema_S[A] = (title, addProps) => JsonSchema_S.objectNamedMap[A](name = title, additionalProperties = addProps)


				val resultArgs: Either[DecodingFailure, (String, JsonSchema_S.AdditionalProperties[A])] = for {

					_ <- propertyExists(c, "title")
					_ <- propertyExists(c, "additionalProperties")

					title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

					addProps: AdditionalProperties[A] <- {

						val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A])

						val resAddProps: Result[JsonSchema_S.AdditionalProperties[A]] = resTpe.map(tpe => JsonSchema_S.AdditionalProperties(tpe = tpe))

						println(s"\n\nINSIDE makeJsonCirceObjectNamedMap:")
						println(s"resTpe = $resTpe")
						println(s"resAddProps = $resAddProps")

						resAddProps
					}

				} yield /*objnamedmap*/ (title, addProps) //JsonSchema_S.objectNamedMap[A](name = title, additionalProperties = addProps).embed


				val result_noEmbed: Result[JsonSchema_S[A]] = resultArgs.map { case (title, addProps) => objnamedmap(title, addProps) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeJsonCirceObjectNamedMap:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed
			}


			def makeJsonCirceObjectNamed(c: HCursor): Result[A] = {

				val objnamed: (String, List[JsonSchema_S.Property[A]], List[String]) => JsonSchema_S[A] = (title, properties, required) => JsonSchema_S.objectNamed[A](name = title, properties = properties, required = required)

				val resultArgs: Result[(String, List[JsonSchema_S.Property[A]], List[String])] =
					for {
						_ <- propertyExists(c, "title")
						_ <- propertyExists(c, "properties")
						_ <- propertyExists(c, "required")

						title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))
						required: List[String] <- c.downField("required").as[Option[List[String]]].map(_.getOrElse(List.empty))
						properties: List[JsonSchema_S.Property[A]] <- {
							//.as[Option[A]]
							val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
								Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A]))
							)

							val resMap: Result[Map[String, A]] = resOptMap.map(_.getOrElse(Map.empty))

							val resListProps: Result[List[JsonSchema_S.Property[A]]] = resMap
								.map((mapStrA: Map[String, A]) => mapStrA.toList.map((JsonSchema_S.Property.apply[A] _).tupled /*{
								val funcTup: ((String, A)) => JsonSchema_S.Property[A] = (JsonSchema_S.Property.apply[A] _).tupled
								funcTup(tup)
							}*/
								)
								)


							println(s"\n\nINSIDE makeJsonCirceObjectNamed:")
							println(s"resOptMap  = $resOptMap")
							println(s"resMap  = $resMap")
							println(s"resListProps = $resListProps")

							resListProps
						}
					} yield (title, properties, required) //JsonSchema_S.objectName[A](title, properties, required).embed

				val result_noEmbed: Result[JsonSchema_S[A]] = resultArgs.map { case (title, ps, rs) => objnamed(title, ps, rs) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeJsonCirceObjectNamed:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed

			}


			def makeJsonCirceObjectSimple(c: HCursor): Result[A] = {

				val objsimple: (List[JsonSchema_S.Property[A]], List[String]) => JsonSchema_S[A] = (properties, required) => JsonSchema_S.`object`[A](properties, required)


				val resultArgs: Result[(List[JsonSchema_S.Property[A]], List[String])] =
					for {
						//itle: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))
						_ <- propertyExists(c, "properties")
						_ <- propertyExists(c, "required")

						required: List[String] <- c.downField("required").as[Option[List[String]]].map(_.getOrElse(List.empty))
						properties: List[JsonSchema_S.Property[A]] <- {
							//.as[Option[A]]
							val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
								Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A]))
							)

							val resMap: Result[Map[String, A]] = resOptMap.map(_.getOrElse(Map.empty))

							val resListProps: Result[List[JsonSchema_S.Property[A]]] = resMap
								.map((mapStrA: Map[String, A]) => mapStrA.toList.map((JsonSchema_S.Property.apply[A] _).tupled /*{
								val funcTup: ((String, A)) => JsonSchema_S.Property[A] = (JsonSchema_S.Property.apply[A] _).tupled
								funcTup(tup)
							}*/
								)
								)


							println(s"\n\nINSIDE makeJsonCirceObjectSimple:")
							println(s"resOptMap  = $resOptMap")
							println(s"resMap  = $resMap")
							println(s"resListProps = $resListProps")

							resListProps
						}
					} yield (properties, required) // JsonSchema_S.`object`[A](properties, required.getOrElse(List.empty)).embed

				val result_noEmbed: Result[JsonSchema_S[A]] = resultArgs.map { case (ps, rs) => objsimple(ps, rs) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeJsonCirceObjectSimple:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed
			}

			val titleStr: ACursor = c.downField("title")
			println(s"\n\nDOWNFIELD TITLE = ${titleStr.as[String]}")


			hasNameProperty(c) match {
				case false => hasMapProperty(c) match {
					case true => makeJsonCirceObjectMap(c) // no name, map
					case false => makeJsonCirceObjectSimple(c) // no name, no map
				}
				case true => isObjectNamedMap(c) match {
					case true => makeJsonCirceObjectNamedMap(c)
					case false => makeJsonCirceObjectNamed(c)
				}
			}

		}


	implicit val referenceDecoder: Decoder[Reference] = Decoder.forProduct1(s"$$ref")(Reference.apply)

	implicit def orReferenceDecoder[A: Decoder]: Decoder[Either[A, Reference]] =
		Decoder[Reference].map(_.asRight[A]) orElse Decoder[A].map(_.asLeft[Reference])


	def enumJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {



		Decoder.instance { (c: HCursor) =>

			val result: Result[List[String]] = for {
				_ <- validateType(c, "string")
				//_ <- propertyExists(c, "enum")

				cases: List[String] <- {

					val casesValue: Result[List[String]] = c.downField("enum").as[Option[List[String]]].map(_.getOrElse(List.empty[String]))

					println(s"\n\nINSIDE ENUM JSON DECODER:")
					println(s"c.downField(enum) = ${c.downField("enum")}")
					println(s"cases = ${casesValue}")

					casesValue
				}

			} yield cases


			val result_noEmbed: Result[JsonSchema_S[A]] = result.map(vals => JsonSchema_S.enum[A](vals))
			val result_embed: Result[A] = result_noEmbed.map(_.embed)

			result_embed
		}
	}

	// NOTE: either enum decoder works - here below or the one above.
	/*Decoder.forProduct2[(String, Option[List[String]]), String, Option[List[String]]]("type", "enum")(Tuple2.apply).emap {

		case ("string", casesOpt: Option[List[String]]) => JsonSchema_S.`enum`[A](casesOpt.getOrElse(List.empty)).embed.asRight
		case _ => s"not enum type".asLeft
	}
}*/

	/*Decoder.instance(c =>
		for {
			_ <- validateType(c, "string")
			_ <- propertyExists(c, "enum")

			values <- c.downField("enum") .as[List[String]]

		} yield JsonSchema_S.enum[A](values).embed
	)*/

	private def sumJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
		Decoder.instance { (c: HCursor) =>
			val resListA: Result[List[A]] = for {
				cases <- c.downField("oneOf").as[List[A]] //.map((cases: List[A]) => JsonSchema_S.sum[A](cases).embed)
			} yield cases

			val result_noEmbed: Result[JsonSchema_S[A]] = resListA.map((cases: List[A]) => JsonSchema_S.sum[A](cases))
			val result_embed: Result[A] = result_noEmbed.map(_.embed)

			result_embed
		}


	private def arrayJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
		Decoder.instance { c: HCursor =>
			for {
				items <- c.downField("items").as[A](identifyJsonDecoderWithPriorityEnumVsBasicDecoder[A]) // NOTE: was jsonSchemaDecoder[A]
				_ <- validateType(c, "array")
			} yield JsonSchema_S.array(items).embed
		}


	private def referenceJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
		Decoder[Reference].map((x: Reference) => JsonSchema_S.reference[A](x.ref).embed)


}