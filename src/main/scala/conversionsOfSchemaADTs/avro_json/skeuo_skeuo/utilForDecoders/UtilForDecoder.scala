package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.utilForDecoders




// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)
import io.circe._
import io.circe.Decoder
import io.circe.Decoder.{Result, resultInstance}
import io.circe.{Json => JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import cats.syntax.all._
//import cats.implicits._
//import cats.syntax._


import scala.language.postfixOps
import scala.language.higherKinds
//import scala.language.implicitConversions


/**
 *
 */
object UtilForDecoder {


	def validateType(c: HCursor, expected: String): Decoder.Result[Unit] =
		c.downField("type").as[String].flatMap {
			case `expected`: String => ().asRight
			case actual: String => DecodingFailure(s"$actual is not expected type $expected", c.history).asLeft
		}

	def propertyExists(c: HCursor, name: String): Decoder.Result[Unit] =
		c.downField(name)
			.success
			.fold(DecodingFailure(s"$name property does not exist", c.history).asLeft[Unit])(_ =>
				().asRight[DecodingFailure]
			)

	/*def isnamedtype(c: HCursor, name: String) ={
		val here: Any => Either[DecodingFailure, Nothing] =
			for {
				_ <- propertyExists(c, "name")
				_ <- propertyExists(c, "namespace")
			} yield _
	}*/


	// Added by @statisticallyfit
	def hasObjectProperty(c: HCursor): Boolean =
		validateType(c, "object").isRight &&
			propertyExists(c, "type").isRight

	def hasNameProperty(c: HCursor): Boolean =
	/*hasObjectProperty(c) &&*/
		propertyExists(c, "title").isRight

	def hasMapProperty(c: HCursor): Boolean =
		propertyExists(c, "additionalProperties").isRight


	 def isObject(c: HCursor): Boolean = {
		hasObjectProperty(c) &&
			propertyExists(c, "properties").isRight &&
			propertyExists(c, "required").isRight
	} /*||
			propertyExists(c, "allOf").isRight*/

	def isObjectNamed(c: HCursor): Boolean = {
		isObject(c) &&
			hasNameProperty(c)
	}

	def isObjectNamedMap(c: HCursor): Boolean =
		hasObjectProperty(c) && // does not include props, reqs
			hasNameProperty(c) &&
			hasMapProperty(c)

	// Added by @statisticallyfit
	def isObjectMap(c: HCursor): Boolean =
		hasObjectProperty(c) &&
			hasMapProperty(c)


}
