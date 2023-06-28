package utilTest.utilJson.utilAndyGlow_ParseJsonSchema

/**
 * CODE COPIED FROM = https://github.com/statisticallyfit/AndyGlow_scala-jsonschema/blob/master/core/src/test/scala-2.13/com/github/andyglow/testsupport.scala
 *
 * REASON = this code is imported into the parsejsonschema file (in andy glow's repo) but I cannot do that because the testingsupport code is in his 'tests' folder so I can't access that.
 */
//package com.github.andyglow

import org.scalatest.matchers.should.Matchers.fail
import scala.util.Try


object testsupport {
	
	implicit class TestTryOps[T](private val t: Try[T]) extends AnyVal {
		
		def value: T = t.fold(err => fail("", err), identity)
	}
}
