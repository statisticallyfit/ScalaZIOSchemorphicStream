package utilTest.arbitraryInstances.skeuoArbitrary


import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import higherkindness.droste._
import higherkindness.skeuomorph.mu.MuF

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
object instancesMu {
	
	
	implicit def muCoproductArbitrary[T](withTNull: Boolean)(implicit B: Basis[MuF, T]): Arbitrary[MuF.TCoproduct[T]] =
		Arbitrary {
			val nonNullPrimitives: Gen[MuF[T]] = Gen.oneOf(
				List(
					MuF.TString[T](),
					MuF.TBoolean[T](),
					MuF.TByteArray[T](MuF.Length.Arbitrary),
					MuF.TDouble[T](),
					MuF.TFloat[T](),
					MuF.int[T](),
					MuF.long[T]()
				)
			)
			
			(
				nonNullPrimitives,
				if (withTNull) Gen.const(MuF.TNull[T]()) else nonNullPrimitives,
				sampleBool
			).mapN((t1, t2, reversed) =>
				MuF.TCoproduct(
					if (reversed) NonEmptyList.of(B.algebra(t2), B.algebra(t1))
					else NonEmptyList.of(B.algebra(t1), B.algebra(t2))
				)
			)
		}
	
	implicit def muArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[MuF[T]] = {
		
		def fieldGen: Gen[MuF.Field[T]] =
			(
				nonEmptyString,
				Gen.lzy(T.arbitrary),
				Gen.posNum[Int].map(i => Some(List(i)))
			).mapN(MuF.Field.apply)
		
		def lengthGen: Gen[MuF.Length] = {
			val fixedGen = for {
				posInt <- Gen.posNum[Int]
				name <- Gen.alphaStr
				namespace <- Gen.option(Gen.alphaStr)
			} yield MuF.Length.Fixed(name, namespace, posInt)
			
			Gen.oneOf(MuF.Length.Arbitrary.pure[Gen], fixedGen)
		}
		
		Arbitrary(
			Gen.oneOf(
				MuF.`null`[T]().pure[Gen],
				MuF.double[T]().pure[Gen],
				MuF.float[T]().pure[Gen],
				MuF.int[T]().pure[Gen],
				MuF.long[T]().pure[Gen],
				MuF.boolean[T]().pure[Gen],
				MuF.string[T]().pure[Gen],
				lengthGen.map(l => MuF.byteArray[T](l)),
				(Gen.listOf(nonEmptyString), nonEmptyString) mapN MuF.namedType[T],
				T.arbitrary map MuF.option[T],
				(T.arbitrary, T.arbitrary) mapN { (a, b) => MuF.either(a, b) },
				T.arbitrary map MuF.list[T],
				T.arbitrary map (t => MuF.map[T](None, t)),
				T.arbitrary map MuF.required[T],
				(T.arbitrary, Gen.listOf(T.arbitrary)) mapN { (a, b) => MuF.generic[T](a, b) },
				(T.arbitrary, Gen.listOf(T.arbitrary)) mapN { (a, b) => MuF.generic[T](a, b) },
				Gen.nonEmptyListOf(T.arbitrary) map { l => MuF.coproduct[T](NonEmptyList.fromListUnsafe(l)) },
				(
					nonEmptyString,
					Gen.option(nonEmptyString),
					Gen.nonEmptyListOf(Gen.lzy(fieldGen)),
					Gen.listOfN(1, T.arbitrary),
					Gen.listOfN(1, T.arbitrary)
				).mapN((n, ns, f, p, c) => MuF.product(n, ns, f, p, c))
			)
		)
	}
	
	
	def muCoproductWithTNullGen[T](implicit B: Basis[MuF, T]): Gen[MuF.TCoproduct[T]] =
		muCoproductArbitrary(withTNull = true).arbitrary
	
	def muCoproductWithoutTNullGen[T](implicit B: Basis[MuF, T]): Gen[MuF.TCoproduct[T]] =
		muCoproductArbitrary(withTNull = false).arbitrary

}
