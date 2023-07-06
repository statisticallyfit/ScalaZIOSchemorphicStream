package utilTest.arbitraryInstances.skeuoArbitrary


import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import higherkindness.droste._
import higherkindness.skeuomorph.openapi._

import org.scalacheck._ // Arbitrary, Gen
import org.scalacheck.cats.implicits._


import scala.jdk.CollectionConverters._

import utilTest.arbitraryInstances.ArbitraryGeneral._


/**
 * Purpose - copying this here from skeuomorph's test folder because:
 * 	1. cannot access skeuomorph's test folder contents.
 *        2. also need to add to these instances by creating instances for Skeuo's types too.
 *
 * NOTE: file source == https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/instances.scala
 */
object instancesJson {
	
	
	implicit def jsonSchemaFOpenApiArbitrary: Arbitrary[JsonSchemaF.Fixed] = {
		import JsonSchemaF.Fixed
		
		val basicGen: Gen[JsonSchemaF.Fixed] = Gen.oneOf(
			Fixed.integer().pure[Gen],
			Fixed.long().pure[Gen],
			Fixed.float().pure[Gen],
			Fixed.double().pure[Gen],
			Fixed.string().pure[Gen],
			Fixed.byte().pure[Gen],
			Fixed.binary().pure[Gen],
			Fixed.boolean().pure[Gen],
			Fixed.date().pure[Gen],
			Fixed.dateTime().pure[Gen],
			Fixed.password().pure[Gen],
			Gen.listOfN(2, nonEmptyString) map Fixed.enum,
			nonEmptyString map Fixed.reference
		)
		
		def rec(depth: Int): Gen[JsonSchemaF.Fixed] =
			depth match {
				case 1 => basicGen
				case n =>
					Gen.oneOf(
						rec(n - 1) map Fixed.array,
						Gen.listOfN(3, Gen.zip(nonEmptyString, rec(n - 1))) map { n =>
							Fixed.`object`(n, n.take(n.size - 1).map(_._1))
						}
					)
			}
		
		Arbitrary(rec(2))
	}
	
	implicit def jsonSchemaOpenApiArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[JsonSchemaF[T]] = {
		val propertyGen: Gen[JsonSchemaF.Property[T]] = (nonEmptyString, T.arbitrary).mapN(JsonSchemaF.Property[T])
		val objectGen: Gen[JsonSchemaF[T]] = (
			Gen.listOf(propertyGen),
			Gen.listOf(nonEmptyString)
		).mapN(JsonSchemaF.`object`[T])
		
		Arbitrary(
			Gen.oneOf(
				JsonSchemaF.integer[T]().pure[Gen],
				JsonSchemaF.long[T]().pure[Gen],
				JsonSchemaF.float[T]().pure[Gen],
				JsonSchemaF.double[T]().pure[Gen],
				JsonSchemaF.string[T]().pure[Gen],
				JsonSchemaF.byte[T]().pure[Gen],
				JsonSchemaF.binary[T]().pure[Gen],
				JsonSchemaF.boolean[T]().pure[Gen],
				JsonSchemaF.date[T]().pure[Gen],
				JsonSchemaF.dateTime[T]().pure[Gen],
				JsonSchemaF.password[T]().pure[Gen],
				T.arbitrary map JsonSchemaF.array,
				Gen.listOf(nonEmptyString) map JsonSchemaF.enum[T],
				Gen.listOf(T.arbitrary) map JsonSchemaF.sum[T],
				objectGen,
				nonEmptyString.map(JsonSchemaF.reference[T])
			)
		)
	}
	
}
