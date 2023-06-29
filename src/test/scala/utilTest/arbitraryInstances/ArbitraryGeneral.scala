package utilTest.arbitraryInstances

import java.util.UUID


import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import org.scalacheck._
import org.scalacheck.cats.implicits._


/**
 * Purpose - copying this here from skeuomorph's test folder because:
 * 	1. cannot access skeuomorph's test folder contents.
 *        2. also need to add to these instances by creating instances for Skeuo's types too.
 *
 * NOTE: file source == https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/instances.scala
 */
object ArbitraryGeneral {
	
	
	lazy val nonEmptyString: Gen[String] = Gen.alphaStr.filter(_.nonEmpty)
	
	lazy val smallNumber: Gen[Int] = Gen.choose(1, 10)
	
	lazy val sampleBool: Gen[Boolean] = Gen.oneOf(true, false)
	
	
	def eitherGen[A, B](left: Gen[A], right: Gen[B]): Gen[Either[A, B]] =
		Gen.oneOf(left.map(_.asLeft[B]), right.map(_.asRight[A]))
	
	
	// TODO: after Scalacheck 1.14.1 upgrade this generator doesn't work anymore
	// def mapStringToGen[A](gen: Gen[A]): Gen[Map[String, A]] = Gen.mapOfN(2, Gen.zip(nonEmptyString.map(_.take(4)), gen))
	def mapStringToGen[A](gen: Gen[A]): Gen[Map[String, A]] = gen.map(a => Map(UUID.randomUUID.toString.take(4) -> a))
}
