package utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr

import cats.syntax.all._
import io.circe.parser.parse
import io.circe.{Json ⇒ JsonCirce} // for .valueOr


/**
 *
 */
object UnsafeParser {
	def unsafeParse: String => JsonCirce = parse(_).valueOr(x => sys.error(x.message))
}
