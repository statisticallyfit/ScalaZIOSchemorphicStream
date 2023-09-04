package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)
import io.circe._
import io.circe.Decoder
import io.circe.Decoder.Result

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
import JsonSchema_S._
import AvroSchema_S._

import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro, _}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
/**
 *
 */
object Skeuo_Skeuo {


	/*import Coalgebra_AvroToJson._
	import Coalgebra_JsonToAvro._
	import Algebra_AvroToJson._
	import Algebra_JsonToAvro._

	import Coalgebra_JsonFixedToAvro._*/
	import Algebra_AvroToJson_Fixed._
	import Trans_AvroToJson._


	// TODO: do all these conversions using Trans (because itis F[A => G[A]): https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/package.scala#L80

	/// -----------------------------


	private def validateType(c: HCursor, expected: String): Decoder.Result[Unit] =
		c.downField("type").as[String].flatMap {
			case `expected`: String => ().asRight
			case actual: String => DecodingFailure(s"$actual is not expected type $expected", c.history).asLeft
		}

	private def propertyExists(c: HCursor, name: String): Decoder.Result[Unit] =
		c.downField(name)
			.success
			.fold(DecodingFailure(s"$name property does not exist", c.history).asLeft[Unit])(_ =>
				().asRight[DecodingFailure]
			)




	// Added by @statisticallyfit
	private def hasObjectProperty(c: HCursor): Boolean =
		validateType(c, "object").isRight &&
			propertyExists(c, "type").isRight

	private def hasNameProperty(c: HCursor): Boolean =
		/*hasObjectProperty(c) &&*/
			propertyExists(c, "title").isRight

	private def hasMapProperty(c: HCursor): Boolean =
		propertyExists(c, "additionalProperties").isRight


	private def isObject(c: HCursor): Boolean = {
		hasObjectProperty(c) &&
			propertyExists(c, "properties").isRight &&
			propertyExists(c, "required").isRight
	} /*||
			propertyExists(c, "allOf").isRight*/

	private def isObjectNamed(c: HCursor): Boolean = {
		isObject(c) &&
			hasNameProperty(c)
	}

	private def isObjectNamedMap(c: HCursor): Boolean =
		hasObjectProperty(c) && // does not include props, reqs
		hasNameProperty(c) &&
			hasMapProperty(c)

	// Added by @statisticallyfit
	private def isObjectMap(c: HCursor): Boolean =
		isObject(c) &&
			hasMapProperty(c)




	object TEMP_AvroSchemaDecoderImplicit {
		implicit def identifyAvroDecoderWithPriorityBasicDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

			Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).flatMap {

				case ("integer", _) | ("number", _) | ("string", _) | ("boolean", _) => basicAvroSchemaDecoder[A]

				//case (x, _) => s"$x is not well formed type".asLeft
				case _ => avroSchemaDecoder[A]
			}
		}


		private def avroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {
			logicalTypeAvroSchemaDecoder orElse
			arrayAvroSchemaDecoder orElse
			mapAvroSchemaDecoder orElse
			recordAvroSchemaDecoder
		} /*orElse
				mapAvroSchemaDecoder orElse
				recordAvroSchemaDecoder orElse
				namedTypeAvroSchemaDecoder orElse
				unionAvroSchemaDecoder orElse
				fixedAvroSchemaDecoder orElse
				enumAvroSchemaDecoder*/


		private def logicalTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

			import AvroSchema_S._


			Decoder.forProduct4[(String, Option[String], Option[Int], Option[Int]), String, Option[String], Option[Int], Option[Int]]("type", "logicalType", "precision", "scale")(Tuple4.apply).emap {

				case ("int", Some("date"), None, None) => date[A]().embed.asRight
				case ("long", Some("timestamp-millis"), None, None) => timestampMillis[A]().embed.asRight
				case ("int", Some("time-millis"), None, None) => timeMillis[A]().embed.asRight
				case ("bytes", Some("decimal"), Some(precision), Some(scale)) => decimal[A](precision, scale).embed.asRight

				case (x, _, _, _) => s"$x is not well formed logical-type".asLeft
			}
		}

		private def basicAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = {

			import AvroSchema_S._


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

		private def arrayAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] =
			Decoder.instance { c: HCursor =>
				for {
					items: A <- c.downField("items").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A])
					//_ <- validateType(c, "array")
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
				def makeAvroCirceMap: Result[A] = {

					val tmap: A => AvroSchema_S[A] = (inner) => AvroSchema_S.map[A](inner)


					val resultArgs: Result[A] = for {

						inner: A <- {

							val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyAvroDecoderWithPriorityBasicDecoder[A])

							val resMap: Result[TMap[A]] = resTpe.map(tpe => AvroSchema_S.TMap(tpe))

							println(s"\n\nINSIDE makeAvroCirceObjectNamedMap:")
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

				makeAvroCirceMap
			}
		}

		private def namedTypeAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???


		private def recordAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = Decoder.instance { c: HCursor =>


			def makeAvroCirceObjectNamed: Result[A] = {

				val trecord: (String, Option[String], List[String], Option[String], List[Field[A]]) => AvroSchema_S[A] = (title, namespace, aliases, doc, fields) => AvroSchema_S.record[A](title, namespace, aliases, doc, fields)

				val resultArgs: Result[(String, Option[String], List[String], Option[String], List[Field[A]])] =
					for {
						title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

						namespace: Option[String] <- c.downField("namespace").as[Option[Option[String]]].map(_.getOrElse(None))

						aliases: List[String] <- c.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))

						doc: Option[String] <- c.downField("doc").as[Option[Option[String]]].map(_.getOrElse(None))

						properties: List[AvroSchema_S.Field[A]] <- {
							//.as[Option[A]]
							val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
								Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyAvroDecoderWithPriorityBasicDecoder[A]))
							)

							val resMap: Result[Map[String, A]] = resOptMap.map(_.getOrElse(Map.empty))

							val resListProps: Result[List[AvroSchema_S.Field[A]]] = resMap
								.map((mapStrA: Map[String, A]) => mapStrA.toList.map((AvroSchema_S.Field.apply[A] _).tupled /*{
								val funcTup: ((String, A)) => JsonSchema_S.Property[A] = (JsonSchema_S.Property.apply[A] _).tupled
								funcTup(tup)
							}*/
								)
								)


							println(s"\n\nINSIDE makeAvroCirceObjectNamed:")
							println(s"resOptMap  = $resOptMap")
							println(s"resMap  = $resMap")
							println(s"resListProps = $resListProps")

							resListProps
						}
					} yield (title, namespace, aliases, doc, fields)

				//(String, Option[String], List[String], Option[String], List[Field[A]]) => AvroSchema_S.TRecord[A] = (title, namespace, aliases, doc, fields)

				val result_noEmbed: Result[AvroSchema_S[A]] = resultArgs.map { case (title, namespace, aliases, doc, fields) => trecord(title, namespace, aliases, doc, fields) }
				val result_embed: Result[A] = result_noEmbed.map(_.embed)

				println(s"\n\nINSIDE makeJsonCirceObjectNamed:")
				println(s"result (not embed) = $result_noEmbed")
				println(s"result (embed) = $result_embed")

				result_embed

			}
		}
		private def unionAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???
		private def fixedAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???

		private def enumAvroSchemaDecoder[A: Embed[AvroSchema_S, *]]: Decoder[A] = ???
	}

	object TEMP_JsonSchemaDecoderImplicit_fromSkeuoProject {


		implicit def identifyJsonDecoderWithPriorityBasicDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {


			Decoder.forProduct2[(String, Option[String]), String, Option[String]]("type", "format")(Tuple2.apply).flatMap {

				case ("integer", _) | ("number", _) | ("string", _) | ("boolean", _) => basicJsonSchemaDecoder[A]

				//case (x, _) => s"$x is not well formed type".asLeft
				case _ => jsonSchemaDecoder[A]
			}
		}


		private def jsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
			    // TODO fix in case the 'type' for objectmap is NOT a simple type - must have a checker checking for the simple types and then decide to pass to jsonschemadecoder (here)
		     //identifyDecoderWithPriorityBasicDecoder orElse
			referenceJsonSchemaDecoder orElse
			sumJsonSchemaDecoder orElse
			arrayJsonSchemaDecoder orElse
			objectJsonSchemaDecoder orElse
			enumJsonSchemaDecoder /*orElse
			basicJsonSchemaDecoder*/


		private def objectJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =

			Decoder.instance { c: HCursor =>


				// Added by @statisticallyfit:

				/**
				 * Extract the string { "type": "something" } that comes after 'additionalProperties'
				 * @return
				 */


				// Added by @statisticallyfit:
				// Separating by different kinds of object classes so can properly interpret a MAP from avro into json
				def makeJsonCirceObjectMap: Result[A] = {

					val objmap: JsonSchema_S.AdditionalProperties[A] => JsonSchema_S[A] = ( addProps) => JsonSchema_S.objectMap[A](additionalProperties = addProps)


					val resultArgs: Result[JsonSchema_S.AdditionalProperties[A]] = for {

						// NOTE: got withFilter error here in for-comprehension so using this plugin = https://github.com/oleg-py/better-monadic-for

						//title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

						addProps: AdditionalProperties[A] <- {

							val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyJsonDecoderWithPriorityBasicDecoder[A])

							val resAddProps: Result[JsonSchema_S.AdditionalProperties[A]] = resTpe.map(tpe => JsonSchema_S.AdditionalProperties(tpe = tpe))

							println(s"\n\nINSIDE makeJsonCirceObjectMap:")
							println(s"resTpe = $resTpe")
							println(s"resAddProps = $resAddProps")

							resAddProps
						}

					} yield /*objnamedmap*/ (addProps) //JsonSchema_S.objectNamedMap[A](name = title, additionalProperties = addProps).embed


					val result_noEmbed: Result[JsonSchema_S[A]] = resultArgs.map { case ( addProps) => objmap(addProps) }
					val result_embed: Result[A] = result_noEmbed.map(_.embed)

					println(s"\n\nINSIDE makeJsonCirceObjectNamedMap:")
					println(s"result (not embed) = $result_noEmbed")
					println(s"result (embed) = $result_embed")

					result_embed
				}


				// Added by @statisticallyfit:
				// Separating by different kinds of object classes so can properly interpret a MAP from avro into json
				def makeJsonCirceObjectNamedMap: Result[A] = {

					val objnamedmap: (String, JsonSchema_S.AdditionalProperties[A]) => JsonSchema_S[A] = (title, addProps) => JsonSchema_S.objectNamedMap[A](name = title, additionalProperties = addProps)


					val resultArgs: Either[DecodingFailure, (String, JsonSchema_S.AdditionalProperties[A])] = for {

						title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))

						addProps: AdditionalProperties[A] <- {

							val resTpe: Result[A] = c.downField("additionalProperties").as[A](identifyJsonDecoderWithPriorityBasicDecoder[A])

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


				def makeJsonCirceObjectNamed: Result[A] = {

					val objnamed: (String, List[JsonSchema_S.Property[A]], List[String]) => JsonSchema_S[A] = (title, properties, required) => JsonSchema_S.objectNamed[A](name = title, properties = properties, required = required)

					val resultArgs: Result[(String, List[JsonSchema_S.Property[A]], List[String])] =
						for {
							title: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))
							required: List[String] <- c.downField("required").as[Option[List[String]]].map(_.getOrElse(List.empty))
							properties: List[JsonSchema_S.Property[A]] <- {
								//.as[Option[A]]
								val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
									Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyJsonDecoderWithPriorityBasicDecoder[A]))
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


				def makeJsonCirceObjectSimple = {

					val objsimple: (List[JsonSchema_S.Property[A]], List[String]) => JsonSchema_S[A] = (properties, required) => JsonSchema_S.`object`[A](properties, required)


					val resultArgs: Result[(List[JsonSchema_S.Property[A]], List[String])] =
						for {
							//itle: String <- c.downField("title").as[Option[String]].map(_.getOrElse(""))
							required: List[String] <- c.downField("required").as[Option[List[String]]].map(_.getOrElse(List.empty))
							properties: List[JsonSchema_S.Property[A]] <- {
								//.as[Option[A]]
								val resOptMap: Result[Option[Map[String, A]]] = c.downField("properties").as[Option[Map[String, A]]](
									Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyJsonDecoderWithPriorityBasicDecoder[A]))
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
						case true => makeJsonCirceObjectMap // no name, map
						case false => makeJsonCirceObjectSimple // no name, no map
					}
					case true => isObjectNamedMap(c) match {
						case true => makeJsonCirceObjectNamedMap
						case false => makeJsonCirceObjectNamed
					}
				}

			}





		implicit val referenceDecoder: Decoder[Reference] = Decoder.forProduct1(s"$$ref")(Reference.apply)

		implicit def orReferenceDecoder[A: Decoder]: Decoder[Either[A, Reference]] =
			Decoder[Reference].map(_.asRight[A]) orElse Decoder[A].map(_.asLeft[Reference])




		private def basicJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] = {
			import JsonSchema_S._

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



		private def enumJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
			Decoder.instance(c =>
				for {
					values <- c.downField("enum").as[List[String]]
					_ <- validateType(c, "string")
				} yield JsonSchema_S.enum[A](values).embed
			)

		private def sumJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
			Decoder.instance(_.downField("oneOf").as[List[A]].map(JsonSchema_S.sum[A](_).embed))


		private def arrayJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
			Decoder.instance { c: HCursor =>
				for {
					items <- c.downField("items").as[A](identifyJsonDecoderWithPriorityBasicDecoder[A]) // NOTE: was jsonSchemaDecoder[A]
					_ <- validateType(c, "array")
				} yield JsonSchema_S.array(items).embed
			}


		private def referenceJsonSchemaDecoder[A: Embed[JsonSchema_S, *]]: Decoder[A] =
			Decoder[Reference].map((x: Reference) => JsonSchema_S.reference[A](x.ref).embed)



	}


	object TransSchemaImplicits {

		/*implicit def skeuoJsonHasEmbed[T: TypeTag]: Embed[JsonSchema_S, T] = new Embed[JsonSchema_S, T] {

			// JsonSkeuo[T] => T
			def algebra: Algebra[JsonSchema_S, T] = Algebra {
				case IntegerF() ⇒ Int
			}
		}*/



		implicit def skeuoEmbed_AJ: Embed[AvroSchema_S, Fix[JsonSchema_S]] = new Embed[AvroSchema_S, Fix[JsonSchema_S]] {

			// AvroSchema_S [ Fix[JsonSchema_S]] => Fix[JsonSchema_S]
			def algebra: Algebra[AvroSchema_S, Fix[JsonSchema_S]] = Algebra {

				case TNull() => Fix(ObjectF(List(), List()))

				case TInt() => Fix(IntegerF())
				case TString() => Fix(StringF())
				case TBoolean() => Fix(BooleanF())
				case TLong() => Fix(LongF())
				case TFloat() => Fix(FloatF())
				case TDouble() => Fix(DoubleF())
				case TBytes() => Fix(ByteF())
				case TArray(inner: Fix[JsonSchema_S]) => Fix(ArrayF(inner))


				case TMap(inner: Fix[JsonSchema_S]) => 	{

					Fix(ObjectMapF(additionalProperties = AdditionalProperties[Fix[JsonSchema_S]](inner)))
				}

				case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fix[JsonSchema_S]]]) => {

					Fix(ObjectNamedF(name = name, properties = fields.map(f => field2Property(f)), required = List()))
				}
			}
		}


		/*implicit def basisAvroAvro(implicit ev: Basis[AvroSchema_S, Fix[AvroSchema_S]]): Basis[AvroSchema_S, Fix[AvroSchema_S]] = {
			def algebra: Algebra[AvroSchema_S, Fix[AvroSchema_S]] = Algebra {

				case TInt() => Fix(TInt())

				case TArray(inner: Fix[AvroSchema_S]) => inner
			}

			def coalgebra: Coalgebra[AvroSchema_S, Fix[AvroSchema_S]] = Coalgebra {
				case Fix(TInt()) => TInt()
				case Fix(ta@TArray(inner: Fix[AvroSchema_S])) => ta
			}
		}*/

		implicit def skeuoEmbed_AA: Embed[AvroSchema_S, Fix[AvroSchema_S]] = new Embed[AvroSchema_S, Fix[AvroSchema_S]] {
			def algebra: Algebra[AvroSchema_S, Fix[AvroSchema_S]] = Algebra {
				case TInt() => Fix(TInt())

				case TArray(inner: Fix[AvroSchema_S]) => inner
			}
		}
		implicit def skeuoProject_AA: Project[AvroSchema_S, Fix[AvroSchema_S]] = new Project[AvroSchema_S, Fix[AvroSchema_S]] {
			def coalgebra: Coalgebra[AvroSchema_S, Fix[AvroSchema_S]] = Coalgebra {
				case Fix(TInt()) => TInt()
				case Fix(ta@TArray(inner: Fix[AvroSchema_S])) => ta
			}
		}
		/*implicit def basis_AA: Basis[AvroSchema_S, Fix[AvroSchema_S]] = new Basis[AvroSchema_S, Fix[AvroSchema_S]] {
			def algebra: Algebra[AvroSchema_S, Fix[AvroSchema_S]] = Algebra {

				case TInt() => Fix(TInt())

				case TArray(inner: Fix[AvroSchema_S]) => inner
			}

			def coalgebra: Coalgebra[AvroSchema_S, Fix[AvroSchema_S]] = Coalgebra {
				case Fix(TInt()) => TInt()
				case Fix(ta @ TArray(inner: Fix[AvroSchema_S])) => ta
			}
		}*/

		/**
		 * Trans[F[_], G[_], A]
		 *
		 * algebra, Embed is: Embed[G[_], A]
		 * coalgebra, Project is: Project[F[_], A]
		 * @return
		 */

		implicit def skeuoEmbed_JA: Embed[JsonSchema_S, Fix[AvroSchema_S]] = new Embed[JsonSchema_S, Fix[AvroSchema_S]] {

			// JsonSchema_S [ Fix[AvroSchema_S]] => Fix[AvroSchema_S]
			def algebra: Algebra[JsonSchema_S, Fix[AvroSchema_S]] = Algebra {
				// Null
				//case ObjectF(List(), List()) ⇒ Fix(TNull())
				// Integer
				case IntegerF() ⇒ Fix(TInt())
				// String
				case StringF() ⇒ Fix(TString())
				// Boolean
				case BooleanF() ⇒ Fix(TBoolean())
				// Long
				case LongF() ⇒ Fix(TLong())
				// Float
				case FloatF() ⇒ Fix(TFloat())
				// Double
				case DoubleF() ⇒ Fix(TDouble())
				// Byte
				case ByteF() ⇒ Fix(TBytes())
				// Array
				case ArrayF(inner: Fix[AvroSchema_S]) ⇒ Fix(TArray(inner)) // TODO just inner or wrap with TArray?


				// Object with name
				case ObjectNamedF(name: String, props: List[Property[Fix[AvroSchema_S]]],
				reqs: List[String]) ⇒ {

					println(s"ObjectNamedF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nname = $name" +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)

					Fix(
						TRecord(name = name, namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					)

				}

				// Map

				// METHOD 1: using the 'additionalProperties' way of declaring a map
				case ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[Fix[AvroSchema_S]]) ⇒ {

					println(s"ObjectNamedMapF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nname = $name" +
					        s"\nadditionalProperties = $additionalProperties"
					)

					// TODO what about name now?
					Fix(TMap(additionalProperties.tpe))
				}

				case ObjectMapF(additionalProperties: AdditionalProperties[Fix[AvroSchema_S]]) ⇒ {

					println(s"ObjectMapF: INSIDE EMBED'S ALGEBRA: " +
						//s"\nname = $name" +
						s"\nadditionalProperties = $additionalProperties"
					)

					// TODO what about name now?
					Fix(TMap(additionalProperties.tpe))
				}

				// Record
				case ObjectF(props: List[Property[Fix[AvroSchema_S]]],
				reqs: List[String]) ⇒ {

					println(s"ObjectF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)


					val result: Fix[AvroSchema_S] = if(props.isEmpty && reqs.isEmpty) {
						Fix(TNull())
					}
					// METHOD 2: the 'properties' / 'values' way
					/*else if(props.length == 1 && props.head.name == "values"){
						Fix(TMap(props.head.tpe))
					}*/ else {
						Fix(
							TRecord(name = /*null*/ "record", namespace = None, aliases = List(), doc = None,
								fields = props.map(p ⇒ property2Field(p))
							)
						)
					}
					result
				}
			}
		}


		/**
		 * Algebra[JsonS, Fix[JsonS]] === JsonS[Fix[JsonS]] => Fix[JsonS]]
		 *
		 * Calling cata: Fix[JsonS] => Fix[JsonS] ???
		 *
		 * @return
		 */
		/*implicit def skeuoEmbed_JJ: Embed[JsonSchema_S, Fix[JsonSchema_S]] = new Embed[JsonSchema_S, Fix[JsonSchema_S]] {
			def algebra: Algebra[JsonSchema_S, Fix[JsonSchema_S]] = ???
		}
		*/


		/**
		 * Coalgebra[F[_], A]
		 * A => F[A]
		 * Fix[AvroSchema_S] ===> JsonSchema_S[ Fix[AvroSchema_S] ]
		 *
		 * @return
		 */
		implicit def skeuoProject_JA: Project[JsonSchema_S, Fix[AvroSchema_S]] = new Project[JsonSchema_S, Fix[AvroSchema_S]] {

			def coalgebra: Coalgebra[JsonSchema_S, Fix[AvroSchema_S]] = Coalgebra {

				case Fix(TInt()) => IntegerF()
				case Fix(TString()) => StringF()
				case Fix(TBoolean()) => BooleanF()


				case Fix(TArray(inner: Fix[AvroSchema_S])) => ArrayF(inner)

				case Fix(TMap(inner: Fix[AvroSchema_S])) => ObjectMapF(additionalProperties = AdditionalProperties[Fix[AvroSchema_S]](tpe = inner))

				case Fix(TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fix[AvroSchema_S]]])) => {

					ObjectNamedF(name = name, properties = fields.map(f => field2Property(f)), required = List())
				}
			}
		}
		/**
		 * Trans[F[_], G[_], A]
		 *
		 * algebra, Embed is: Embed[G[_], A]
		 * coalgebra, Project is: Project[F[_], A]
		 *
		 * @return
		 */
		/**
		 * Coalgebra means:
		 * Fix[JsonSchema_S] => AvroSchema_S[ Fix[JsonSchema_S] ]
		 *
		 * @return
		 */
		implicit def skeuoProject_AJ: Project[AvroSchema_S, Fix[JsonSchema_S]] = new Project[AvroSchema_S, Fix[JsonSchema_S]] {

			def coalgebra: Coalgebra[AvroSchema_S, Fix[JsonSchema_S]] = Coalgebra {
				// Null
				//case Fix(ObjectF(List(), List())) ⇒ TNull()
				// Integer
				case Fix(IntegerF()) ⇒ TInt()
				// String
				case Fix(StringF()) ⇒ TString()
				// Boolean
				case Fix(BooleanF()) ⇒ TBoolean()
				// Long
				case Fix(LongF()) ⇒ TLong()
				// Double
				case Fix(DoubleF()) ⇒ TDouble()
				// Float
				case Fix(FloatF()) ⇒ TFloat()
				// Bytes
				case Fix(ByteF()) ⇒ TBytes()
				// Array
				case Fix(ArrayF(inner: Fix[JsonSchema_S])) ⇒ TArray(inner)

				// Map
				case Fix(ObjectNamedMapF(name: String, additionalProperties: AdditionalProperties[Fix[JsonSchema_S]])) ⇒ {

					println(s"ObjectNamedMapF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nname = $name" +
					        s"\naddedproperties = $additionalProperties"
					)

					TMap(additionalProperties.tpe)
				}

				// Map
				case Fix(ObjectMapF(additionalProperties: AdditionalProperties[Fix[JsonSchema_S]])) ⇒ {

					println(s"ObjectNamedMapF: INSIDE PROJECT'S COALGEBRA: " +
						s"\naddedproperties = $additionalProperties"
					)

					TMap(additionalProperties.tpe)
				}

				// Object Name
				case Fix(ObjectNamedF(name: String, props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒ {

					println(s"ObjectNamedF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nname = $name" +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)

					TRecord(name = name, namespace = None, aliases = List(), doc = None,
						fields = props.map(p ⇒ property2Field(p))
					)
				}

				// Record
				case Fix(ObjectF(props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒  {


					println(s"ObjectF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs")


					val result: AvroSchema_S[Fix[JsonSchema_S]] = if (props.isEmpty && reqs.isEmpty) {
						TNull()
					}
					// METHOD 2: the 'properties' / 'values' way
					/*else if (props.length == 1 && props.head.name == "values") {
						TMap(props.head.tpe)
					}*/ else {

						TRecord(name = /*null*/ "TODO_OBJ_NAME", namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					}
					result
				}
			}
		}
	}


	// NOTE how to access Trans type (example here) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/mu/protocol.scala#L56C36-L56C36
	object ByTrans {
		/*def avroToJsonFunction[T: TypeTag]: AvroSchema_S[T] ⇒ JsonSchema_S[T] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]

			val runner: AvroSchema_S[T] ⇒ JsonSchema_S[T] = transVar.run

			runner.apply(_)

		}

		def avroToJson[T: TypeTag](av: AvroSchema_S[T]): JsonSchema_S[T] = transform_AvroToJsonSkeuo[T].run.apply(av)

		*/

		import TransSchemaImplicits.{ skeuoEmbed_AJ, skeuoProject_JA} // skeuoProject_AJ

		// algebra: Embed: G[A] => A     :  AvroF [ Fix[JsonF] ] => Fix[JsonF]
		// coalgebra: Project: A => F[A]   :  Fix[JsonF] => JsonF [ Fix[JsonF] ]
		// CATA = algebra . coalgebra

		 // TODO: declare transA: Trans[JsonSchema, AvroSchema, Fix[?]] with corresponding implicits and see how they pick up at the decoders then see what comes out with the avro-skeuo -> circe -> skeuo conversion

		val avroToJson_byCataTransAlg: Fix[AvroSchema_S] => Fix[JsonSchema_S] = scheme.cata(transJAJ.algebra).apply(_)


		import TransSchemaImplicits.{skeuoEmbed_JA, skeuoProject_AJ}

		// algebra: Embed:  G[A] => A:
		// JsonSchema_S[Fix[JsonSchema_S]] => Fix[JsonSchema_S]
		// coalgebra: Project:
		// A => F[A]
		// Fix[JsonSchema_S] => AvroSchema_S [ Fix[JsonSchema_S] ]

		// algebra: Embed: G[A] => A     :  JsonF [ Fix[JsonF] ] => Fix[JsonF]
		// coalgebra: Project: A => F[A]   :  Fix[JsonF] => AvroF [ Fix[JsonF] ]
		// ANA = algebra . coalgebra
		val jsonToAvro_byAnaTransCoalg: Fix[JsonSchema_S] ⇒ Fix[AvroSchema_S] = scheme.ana(transAJJ.coalgebra).apply(_)



		//val h: Fixed ⇒ Fixed = scheme.hylo(transJ.algebra, transJ.coalgebra)

		val roundTripAvro: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg compose avroToJson_byCataTransAlg

		val roundTripJson: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = avroToJson_byCataTransAlg compose jsonToAvro_byAnaTransCoalg


		/*def transFixWay[T: TypeTag] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]

			val ta = transVar.algebra
		}*/
	}


	object ByAlgebra {


		def avroToJson_Fixed: Fix[AvroSchema_S] ⇒ Fix[JsonSchema_S] =
			scheme.cata(algebra_AvroToJsonFixed).apply(_)


		/*def avroToJson_TYPED[T: TypeTag]: Fix[AvroSchema_S] ⇒ JsonSchema_S[T] =
			scheme.cata(algebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: Fix[AvroSchema_S] ⇒ JsonSchema_S[_] = scheme.cata(algebra_AvroToJson).apply(_)*/

		// NOT GOOD - "No implicits found for Functor[AvroF[_]]
		//def typed2[T: TypeTag] = scheme.cata(algebra2[T]).apply(_)

		/*def jsonToAvro_TYPED[T: TypeTag]: Fix[JsonSchema_S] ⇒ AvroSchema_S[T] =
			scheme.cata(algebra_JsonToAvro_TYPED[T]).apply(_)


		def jsonToAvro_UNTYPED: Fix[JsonSchema_S] ⇒ AvroSchema_S[_] = scheme.cata(algebra_JsonToAvro).apply(_)*/
	}


	object ByCoalgebra {

		/*def avroToJson_TYPED[T: TypeTag]: AvroSchema_S[T] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: AvroSchema_S[_] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson).apply(_)*/


		/*def jsonToAvro_TYPED[T: TypeTag]: JsonSchema_S[T] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro_TYPED[T]).apply(_)

		def jsonToAvro_UNTYPED: JsonSchema_S[_] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro).apply(_)*/
	}






	// AvroF -> Json[AvroF] -> AvroF
	//def roundTrip_AvroToAvro[T: TypeTag]: AvroSchema_S[T] ⇒ AvroSchema_S[T] = ByAlgebra.jsonToAvro_TYPED[T] compose ByCoalgebra.avroToJson_TYPED[T]

	//def roundTrip_AvroToAvro_CanonicalFix[T: TypeTag]: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = ByCoalgebra.jsonToAvro_TYPED[T] compose ByAlgebra.avroToJson_TYPED[T]


	// JsonF -> AvroF[JsonF] -> JsonF
	//def roundTrip_JsonToJson[T: TypeTag]: JsonSchema_S[T] ⇒ JsonSchema_S[T] = ByAlgebra.avroToJson_TYPED[T] compose ByCoalgebra.jsonToAvro_TYPED[T]

	//def roundTrip_JsonToJson_CanonicalFix[T: TypeTag]: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = ByCoalgebra.avroToJson_TYPED[T] compose ByAlgebra.jsonToAvro_TYPED[T]


}
