package conversionsOfSchemaADTs.avro_json.parsing



import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.Decoder
import io.circe.Decoder._
import higherkindness.droste._
import higherkindness.droste.data._


//import higherkindness.droste.syntax.embed._
//import cats.syntax.all._


import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForDialects.Decoder_InputAvroDialect_OutputAvroSkeuo._

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForSkeuoAlgCoalg => impl}
import impl.embedImplicits.skeuoEmbed_AA
import impl.projectImplicits.skeuoProject_AA

import higherkindness.skeuomorph.avro.{AvroF => AvroSchema_S}
import AvroSchema_S.{Field => FieldAvro}

import scala.language.postfixOps
import scala.language.higherKinds


import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

/**
 *
 */
object Example_CirceDecodeFields {


	//def doFieldDecoding2[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]

	// TODO find way to decode the FieldAvro (edward huang website) and then use the below parsing method

	//implicit def fieldDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[FieldAvro[A]] = deriveDecoder[FieldAvro[A]]


	// Method 3 - decode manual list of blobs
	implicit def fieldDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[FieldAvro[A]] = (hCursor: HCursor) => {

		val result: Result[FieldAvro[A]] = for {
			name: String <- hCursor.downField("name").as[Option[String]]. map(_.getOrElse("")) //.get[String]("name")
			theType: A <- hCursor.downField("type").as[A](identifyTRUEAvroDecoderWithPriorityBasicDecoder[A])
			aliases: List[String] <- hCursor.downField("aliases").as[Option[List[String]]].map(_.getOrElse(List.empty))
		} yield FieldAvro[A](name, aliases, None, None, theType)


		// HELP DEBUG STATEMENTS
		println(s"\n\nINSIDE fieldDecoder (avro -> avro):")

		println(s"hcursor = $hCursor")
		println(s"c.downField(name) = ${hCursor.downField("name").as[Option[String]]. map(_.getOrElse(""))}")
		println(s"hCursor.downField(type).as[A](identifyTRUEAvroDecoderWithPriorityBasicDecoder[A]) = ${
			hCursor.downField("type").as[A](identifyTRUEAvroDecoderWithPriorityBasicDecoder[A])
		}")
		println(s"c.downArray = ${hCursor.downArray}")
		println(s"c.values.get.toList = ${hCursor.values }")/*.get.toList}")*/
		println(s"c.value = ${hCursor.value}")
		println(s"c.keys.get.toList = ${hCursor.keys .get.toList}")
		println(s"c.key = ${hCursor.key}")

		result
	}


	import cats.Traverse
	import cats.syntax.all._

	implicit def fieldListDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[(List[String], List[List[A]])] = new Decoder[(List[String], List[List[A]])] {

		override def apply(hCursor: HCursor):  Result[(List[String], List[List[A]])] = {
			val result: Result[(List[String], List[List[A]])] = for {

				theFieldsJson: List[Json] <- hCursor.downField("fields").as[List[Json]]

				digNames: List[String] <- Traverse[List].traverse(theFieldsJson)(
					(itj: Json) => itj.hcursor.downField("name").as[String]
				)


				digTypes: List[List[A]] <- Traverse[List].traverse(theFieldsJson)(
					(itj: Json) => {

						val resOptMap: Result[Option[Map[String, A]]] = itj.hcursor.downField("type").as[Option[Map[String, A]]](Decoder.decodeOption(Decoder.decodeMap[String, A](KeyDecoder.decodeKeyString, identifyTRUEAvroDecoderWithPriorityBasicDecoder[A])))

						val resMap: Result[Map[String, A]] = resOptMap.map(_.getOrElse(Map.empty))

						val typesList: Result[List[A]] = resMap.map(_.values.toList)

						typesList
					}

					/*itj.hcursor.downField("type").as[A]*/
				)

			} yield (digNames, digTypes)

			result
		}
	}
}


class Example_CirceDecodeFields_Runner  /*App*/ extends  AnyFunSpec with Matchers {


	import Example_CirceDecodeFields._

	val inputStringField =
		"""
		  |[
		  |  {
		  |    "name": "coordinates",
		  |    "type": {
		  |      "type": "array",
		  |      "items": "float"
		  |    }
		  |  },
		  |  {
		  |    "name": "type",
		  |    "type": "string"
		  |  }
		  |]
		  |""".stripMargin


	val inputStringField2_startFields =
		"""
		  |{
		  |  "fields": [
		  |    {
		  |      "name": "coordinates",
		  |      "type": {
		  |        "type": "array",
		  |        "items": "float"
		  |      }
		  |    },
		  |    {
		  |      "name": "type",
		  |      "type": "string"
		  |    }
		  |  ]
		  |}
		  |""".stripMargin

	val inputStringField2_float =
		"""
		  |{
		  |  "fields": [
		  |    {
		  |      "name": "coordinates",
		  |      "type": "float"
		  |    },
		  |    {
		  |      "name": "type",
		  |      "type": "string"
		  |    }
		  |  ]
		  |}
		  |""".stripMargin


	/*def parseMethod1(input: String) = {
		val r: Either[Error, List[FieldAvro[Fix[AvroSchema_S]]]] = for {
			doc: Json <- parse(input)
			e: List[FieldAvro[Fix[AvroSchema_S]]] <- doc.as[List[FieldAvro[Fix[AvroSchema_S]]]]
		} yield e

		r
	}

	def parseMethod2(input: String) = {
		parser.decode[List[FieldAvro[Fix[AvroSchema_S]]]](input) match {
			case Right(fs) => s"fields = ${fs}"
			case Left(ex) => s"OOPS something went wrong (2): ${ex}"
		}
	}*/

	type PreField = (List[String], List[List[Fix[AvroSchema_S]]])

	// SOURCE of parsing method = https://hyp.is/cs-SzlruEe6tkc9GektlMA/archive.ph/galSn
	// NOTE: uses fieldDecoder
	def parseMethod1(input: String) = {
		val r: Either[Error, List[FieldAvro[Fix[AvroSchema_S]]]] = for {
			doc: Json <- parse(input)
			e: List[FieldAvro[Fix[AvroSchema_S]]] <- doc.as[List[FieldAvro[Fix[AvroSchema_S]]]]
		} yield e

		r
	}

	// SOURCE of parsing method = https://hyp.is/4CvsPFruEe6L5XOiwPDmNQ/www.edward-huang.com/scala/tech/soft-development/etl/circe/2019/11/28/6-quick-tips-to-parse-json-with-circe/
	// NOTE: uses fieldDecoder
	def parseMethod2(input: String) = {
		parser.decode[List[FieldAvro[Fix[AvroSchema_S]]]](input) match {
			case Right(fs) => s"fields = ${fs}"
			case Left(ex) => s"OOPS something went wrong (2): ${ex}"
		}
	}

	def parsestrings(input: String) = {
		parser.decode[PreField](input) match {
			case Right(lst: PreField) => s"lst = ${lst._1}\n\n${lst._2}"
			case Left(ex) => s"OOPS 3: ${ex}"
		}
	}

	info(s"\n\ndecoded result 1 = ${parseMethod1(inputStringField)}")
	info(s"\n\ndecoded result 2 = ${parseMethod2(inputStringField)}")
	info(s"\n\ndecoded result 3 = ${parsestrings(inputStringField2_startFields)}")
}
