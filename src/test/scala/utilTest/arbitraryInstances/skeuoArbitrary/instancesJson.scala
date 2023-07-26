package utilTest.arbitraryInstances.skeuoArbitrary


import cats.syntax.all._
//import cats.implicits._

import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.scalacheck._
import org.scalacheck.cats.implicits._
import utilTest.arbitraryInstances.ArbitraryGeneral._


/**
 * Purpose - copying this here from skeuomorph's test folder because:
 * 	1. cannot access skeuomorph's test folder contents.
 *        2. also need to add to these instances by creating instances for Skeuo's types too.
 *
 * NOTE: file source == https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/instances.scala
 */
object instancesJson {
	
	
	implicit def JsonSchema_SOpenApiArbitrary: Arbitrary[Fix[JsonSchema_S]] = {
		import JsonSchema_S.Fixed
		
		val basicGen: Gen[Fix[JsonSchema_S]] = Gen.oneOf(
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
		
		def rec(depth: Int): Gen[Fix[JsonSchema_S]] =
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
	
	implicit def jsonSchemaOpenApiArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[JsonSchema_S[T]] = {
		val propertyGen: Gen[JsonSchema_S.Property[T]] = (nonEmptyString, T.arbitrary).mapN(JsonSchema_S.Property[T])
		val objectGen: Gen[JsonSchema_S[T]] = (
			Gen.listOf(propertyGen),
			Gen.listOf(nonEmptyString)
		).mapN(JsonSchema_S.`object`[T])
		
		Arbitrary(
			Gen.oneOf(
				JsonSchema_S.integer[T]().pure[Gen],
				JsonSchema_S.long[T]().pure[Gen],
				JsonSchema_S.float[T]().pure[Gen],
				JsonSchema_S.double[T]().pure[Gen],
				JsonSchema_S.string[T]().pure[Gen],
				JsonSchema_S.byte[T]().pure[Gen],
				JsonSchema_S.binary[T]().pure[Gen],
				JsonSchema_S.boolean[T]().pure[Gen],
				JsonSchema_S.date[T]().pure[Gen],
				JsonSchema_S.dateTime[T]().pure[Gen],
				JsonSchema_S.password[T]().pure[Gen],
				T.arbitrary map JsonSchema_S.array,
				Gen.listOf(nonEmptyString) map JsonSchema_S.enum[T],
				Gen.listOf(T.arbitrary) map JsonSchema_S.sum[T],
				objectGen,
				nonEmptyString.map(JsonSchema_S.reference[T])
			)
		)
	}
	
}
