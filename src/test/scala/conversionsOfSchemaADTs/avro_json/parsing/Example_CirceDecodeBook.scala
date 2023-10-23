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

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsEmbedProject => impl}
import implicitsEmbedProject.embedImplicits.skeuoEmbed_AA
import implicitsEmbedProject.projectImplicits.skeuoProject_AA

import higherkindness.skeuomorph.avro.{AvroF => AvroSchema_S}
import AvroSchema_S.{Field => FieldAvro}

import scala.language.postfixOps
import scala.language.higherKinds


import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._



/**
 *
 */
object Example_CirceDecodeBook {



	case class Book(book: String)

	object Book {

		import io.circe.generic.semiauto._

		implicit val bookDecoderAuto: Decoder[Book] = deriveDecoder[Book]
		//implicit val bookDecoderManual
		val inputStringBook =
			"""
			  |[
			  | {"book": "Programming in Scala"},
			  | {"book": "How to Win Friends and Influence People"},
			  | {"book": "HomoSapiens"},
			  | {"book": "Scala OOP"}
			  |]
			  |""".stripMargin

		def doBookDecoding = {
			parser.decode[List[Book]](inputStringBook) match {
				case Right(books: List[Book]) => println(s"Here are the books ${books}")
				case Left(ex) => println(s"Ooops something error ${ex}")
			}
		}
	}


}


class Example_CirceDecodeBook_Runner  extends  AnyFunSpec with Matchers {

	import Example_CirceDecodeBook._

	Book.doBookDecoding
}
