package conversionsOfSchemaADTs.avro_json.parsing


import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.{Base64, UUID}

import scala.util.Try
/**
 * GOAL = understand how to convert list of {} {} into decoded class form (so can convert "fields: [ {"name":, ... }, {"name":, ...} ] into List[Field[A]] in this project of avro -> json skeuo
 *
 * SOURCE of this code = https://archive.ph/galSn
 */
object Example_CirceDecodeBlobs {


	case class Blob(uuid: UUID, url: String, checksum: Array[Byte])

	case class Catalog(uuid: UUID, name: Option[String], blobs: List[Blob])

	implicit val byteArrayEncoder: Encoder[Array[Byte]] = new Encoder[Array[Byte]] {
		final def apply(xs: Array[Byte]): Json =
			Json.fromString(new String(Base64.getEncoder().encode(xs)))
	}
	implicit val byteArrayDecoder: Decoder[Array[Byte]] = Decoder
		.decodeString
		.emapTry(str => Try(Base64.getDecoder.decode(str.getBytes)))

	implicit val blobDecoder: Decoder[Blob] = deriveDecoder
	implicit val blobEncoder: Encoder[Blob] = deriveEncoder

	implicit val catalogDecoder: Decoder[Catalog] = deriveDecoder
	implicit val catalogEncoder: Encoder[Catalog] = deriveEncoder


	//val json = Files.readString(Paths.get("CirceDemo.json"))

}

object Example_CirceDecodeBlobs_Runner extends App {

	import Example_CirceDecodeBlobs._

	val demoJsonString: String =
		"""
		  |[
		  |  {
		  |    "uuid": "037fa69d-cc52-4d9c-90e1-68d12c455fbd",
		  |    "name": "My Name",
		  |    "blobs" : [
		  |      {
		  |        "uuid": "77bcfc71-1c2a-4872-b11d-501d65afd01a",
		  |        "url": "https://www.foo.bar/path/to/blob1",
		  |        "checksum": "MTIzNDU2Nzg5"
		  |      },
		  |      {
		  |        "uuid": "59bac7a1-7ccd-4ce4-9d4e-c6759a003293",
		  |        "url": "https://www.baz.io/some/blob2",
		  |        "checksum": "YWJjZGVmZ2hp"
		  |      }
		  |    ]
		  |  },
		  |  {
		  |    "uuid": "7a2b9630-85b9-4925-af2e-8386006b6da2",
		  |    "blobs" : [
		  |      {
		  |        "uuid": "487588e6-57a4-413a-a575-cc3b74bb5f8d",
		  |        "url": "https://blobs.r.us/myblob",
		  |        "checksum": "amtsbW5vcHFy"
		  |      }
		  |    ]
		  |  }
		  |]
		  |""".stripMargin
	val r: Either[Error, List[Catalog]] = for {
		doc: Json <- parse(demoJsonString)
		e: List[Catalog] <- doc.as[List[Catalog]]
	} yield e

	println(s"decoded result = $r")

	r match {
		case Left(e) =>
			println(e)
		case Right(v) =>
			println(v.asJson)
	}

	decode[List[Catalog]](demoJsonString) match {
		case Left(e) =>
			println(e)
		case Right(v) =>
			println(v.asJson)
	}

}
