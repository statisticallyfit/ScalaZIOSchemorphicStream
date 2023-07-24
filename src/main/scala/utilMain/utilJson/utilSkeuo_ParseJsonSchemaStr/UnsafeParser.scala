package utilTest.utilJson.utilSkeuo_ParseJsonSchemaStr

import io.circe.{Json ⇒ JsonCirce}
import io.circe.parser.parse

import cats.syntax.all._ // for .valueOr


/**
 *
 */
object UnsafeParser {
	def unsafeParse: String => JsonCirce = parse(_).valueOr(x => sys.error(x.message))
}
