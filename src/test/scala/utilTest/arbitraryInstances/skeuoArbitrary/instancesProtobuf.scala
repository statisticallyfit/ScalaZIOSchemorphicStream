package utilTest.arbitraryInstances.skeuoArbitrary


import cats.syntax.all._
//import cats.implicits._

import higherkindness.skeuomorph.protobuf._
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
object instancesProtobuf {
	
	
	def protobufFMessage[T](implicit T: Arbitrary[T]): Gen[ProtobufF.TMessage[T]] = {
		
		val sampleField: Gen[FieldF.Field[T]] = {
			for {
				name <- nonEmptyString
				tpe <- T.arbitrary
				position <- smallNumber
			} yield FieldF.Field(name, tpe, position, List(), false, isMapField = false)
		}
		
		for {
			name <- nonEmptyString
			field <- sampleField
			nestedMessages <- Gen.listOfN(1, T.arbitrary)
			nestedEnums <- Gen.listOfN(1, T.arbitrary)
		} yield ProtobufF.TMessage(name, List(field), Nil, nestedMessages, nestedEnums)
	}
	
	val genOption: Gen[ProtobufF.OptionValue] = (nonEmptyString, nonEmptyString).mapN(ProtobufF.OptionValue.apply)
	
	def protobufFEnum[T]: Gen[ProtobufF.TEnum[T]] =
		(
			nonEmptyString,
			Gen.listOf((nonEmptyString, Gen.posNum[Int]).tupled),
			Gen.listOf(genOption),
			Gen.listOf((nonEmptyString, Gen.posNum[Int]).tupled)
		).mapN(ProtobufF.TEnum[T])
	
	implicit def protoArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[ProtobufF[T]] = {
		Arbitrary(
			Gen.oneOf(
				ProtobufF.double[T]().pure[Gen],
				ProtobufF.float[T]().pure[Gen],
				ProtobufF.int32[T]().pure[Gen],
				ProtobufF.int64[T]().pure[Gen],
				ProtobufF.uint32[T]().pure[Gen],
				ProtobufF.uint64[T]().pure[Gen],
				ProtobufF.sint32[T]().pure[Gen],
				ProtobufF.sint64[T]().pure[Gen],
				ProtobufF.fixed32[T]().pure[Gen],
				ProtobufF.fixed64[T]().pure[Gen],
				ProtobufF.sfixed32[T]().pure[Gen],
				ProtobufF.sfixed64[T]().pure[Gen],
				ProtobufF.bool[T]().pure[Gen],
				ProtobufF.string[T]().pure[Gen],
				ProtobufF.bytes[T]().pure[Gen],
				(Gen.listOf(nonEmptyString), nonEmptyString) mapN ProtobufF.namedType[T],
				T.arbitrary map ProtobufF.repeated[T],
				protobufFEnum[T],
				protobufFMessage[T]
			)
		)
	}
}
