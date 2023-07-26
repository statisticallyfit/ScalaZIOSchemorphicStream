package utilTest.arbitraryInstances.skeuoArbitrary


import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
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
object instancesAvro {
	
	
	implicit def avroArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[AvroSchema_S[T]] = {
		
		val orderGen: Gen[AvroSchema_S.Order] = Gen.oneOf(AvroSchema_S.Order.Ascending, AvroSchema_S.Order.Descending, AvroSchema_S.Order.Ignore)
		
		val fieldGen: Gen[AvroSchema_S.Field[T]] = (
			nonEmptyString,
			Gen.listOf(nonEmptyString),
			Gen.option(nonEmptyString),
			Gen.option(orderGen),
			T.arbitrary
		).mapN(AvroSchema_S.Field.apply[T])
		
		Arbitrary(
			Gen.oneOf(
				AvroSchema_S.`null`[T]().pure[Gen],
				AvroSchema_S.boolean[T]().pure[Gen],
				AvroSchema_S.int[T]().pure[Gen],
				AvroSchema_S.long[T]().pure[Gen],
				AvroSchema_S.float[T]().pure[Gen],
				AvroSchema_S.double[T]().pure[Gen],
				AvroSchema_S.bytes[T]().pure[Gen],
				AvroSchema_S.string[T]().pure[Gen],
				(nonEmptyString, nonEmptyString).mapN(AvroSchema_S.namedType[T]),
				T.arbitrary map AvroSchema_S.array[T],
				T.arbitrary map AvroSchema_S.map[T],
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.option(nonEmptyString),
					Gen.listOf(fieldGen)
				).mapN(AvroSchema_S.record[T]),
				Gen.nonEmptyListOf(T.arbitrary) map { l => AvroSchema_S.union[T](NonEmptyList.fromListUnsafe(l)) },
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString)
				).mapN(AvroSchema_S.enum[T]),
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.posNum[Int]
				).mapN(AvroSchema_S.fixed[T])
			)
		)
	}
}
