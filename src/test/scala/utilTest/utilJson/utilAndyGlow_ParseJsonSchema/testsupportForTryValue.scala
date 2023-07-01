package utilTest.utilJson.utilAndyGlow_ParseJsonSchema

import scala.util.Try

//import com.github.andyglow.testsupport._
import org.scalatest.matchers.should.Matchers.fail

/**
 * CODE COPIED FROM = https://github.com/andyglow/scala-jsonschema/blob/7051682e787e9358d4c542027b48da7832998213/core/src/test/scala-2.13/com/github/andyglow/testsupport.scala#L7
 *
 * Reason = this is in the test folder of andy glow so cannot import it
 */

object testsupportForTryValue {
	
	implicit class TestTryOps[T](private val t: Try[T]) extends AnyVal {
		
		def value: T = t.fold(err => fail("", err), identity)
	}
}


