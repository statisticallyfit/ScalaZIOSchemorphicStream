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
	/*implicit def fieldDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[FieldAvro[A]] = (hCursor: HCursor) => {

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
		println(s"c.values.get.toList = ${hCursor.values /*.get.toList*/}")
		println(s"c.value = ${hCursor.value}")
		println(s"c.keys.get.toList = ${hCursor.keys /*.get.toList*/}")
		println(s"c.key = ${hCursor.key}")

		result
	}*/


	import cats.Traverse
	import cats.syntax.all._

	implicit def fieldListDecoder[A: Embed[AvroSchema_S, *] : Project[AvroSchema_S, *]]: Decoder[List[FieldAvro[A]]] = new Decoder[List[FieldAvro[A]]] {

		override def apply(hCursor: HCursor):  Result[List[FieldAvro[A]]] = {

			val result: Result[List[FieldAvro[A]]] = for {

				theFieldsJson: List[Json] <- hCursor.downField("fields").as[List[Json]]

				digName: List[String] <- Traverse[List].traverse(theFieldsJson)(
					(itj: Json) => itj.hcursor.downField("name").as[String]
				)

				digType: List[A] <- Traverse[List].traverse(theFieldsJson)(
					(itj: Json) => itj.hcursor.downField("type").as[A]
				)
			} yield digName.zip(digType).map { case (n, t) => FieldAvro[A](n, List(), None, None, t)}

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


	def parseMethod1(input: String) = {
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
	}

	info(s"\n\ndecoded result 1 = ${parseMethod1(inputStringField2_startFields)}")
	info(s"\n\ndecoded result 2 = ${parseMethod2(inputStringField2_startFields)}")
}
