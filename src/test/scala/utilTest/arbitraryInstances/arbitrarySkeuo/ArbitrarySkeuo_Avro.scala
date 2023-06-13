package utilTest.arbitraryInstances.arbitrarySkeuo


import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import higherkindness.droste._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}

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
object ArbitrarySkeuo_Avro {
	
	
	implicit def avroArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[SchemaSkeuoAvro[T]] = {
		
		val orderGen: Gen[SchemaSkeuoAvro.Order] = Gen.oneOf(SchemaSkeuoAvro.Order.Ascending, SchemaSkeuoAvro.Order.Descending, SchemaSkeuoAvro.Order.Ignore)
		
		val fieldGen: Gen[SchemaSkeuoAvro.Field[T]] = (
			nonEmptyString,
			Gen.listOf(nonEmptyString),
			Gen.option(nonEmptyString),
			Gen.option(orderGen),
			T.arbitrary
		).mapN(SchemaSkeuoAvro.Field.apply[T])
		
		Arbitrary(
			Gen.oneOf(
				SchemaSkeuoAvro.`null`[T]().pure[Gen],
				SchemaSkeuoAvro.boolean[T]().pure[Gen],
				SchemaSkeuoAvro.int[T]().pure[Gen],
				SchemaSkeuoAvro.long[T]().pure[Gen],
				SchemaSkeuoAvro.float[T]().pure[Gen],
				SchemaSkeuoAvro.double[T]().pure[Gen],
				SchemaSkeuoAvro.bytes[T]().pure[Gen],
				SchemaSkeuoAvro.string[T]().pure[Gen],
				(nonEmptyString, nonEmptyString).mapN(SchemaSkeuoAvro.namedType[T]),
				T.arbitrary map SchemaSkeuoAvro.array[T],
				T.arbitrary map SchemaSkeuoAvro.map[T],
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.option(nonEmptyString),
					Gen.listOf(fieldGen)
				).mapN(SchemaSkeuoAvro.record[T]),
				Gen.nonEmptyListOf(T.arbitrary) map { l => SchemaSkeuoAvro.union[T](NonEmptyList.fromListUnsafe(l)) },
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString)
				).mapN(SchemaSkeuoAvro.enum[T]),
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.listOf(nonEmptyString),
					Gen.posNum[Int]
				).mapN(SchemaSkeuoAvro.fixed[T])
			)
		)
	}
}
