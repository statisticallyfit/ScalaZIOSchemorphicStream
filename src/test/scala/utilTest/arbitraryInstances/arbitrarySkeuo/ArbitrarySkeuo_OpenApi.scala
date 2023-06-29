package utilTest.arbitraryInstances.arbitrarySkeuo




import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import higherkindness.droste._
import higherkindness.skeuomorph.openapi._
import higherkindness.skeuomorph.openapi.schema.OpenApi

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
object ArbitrarySkeuo_OpenApi {
	
	
	implicit def openApiArbitrary[T](implicit T: Arbitrary[T]): Arbitrary[OpenApi[T]] = {
		import higherkindness.skeuomorph.openapi.schema._
		
		val optionStringGen = Gen.option(nonEmptyString)
		val infoGen: Gen[Info] = (nonEmptyString, optionStringGen, nonEmptyString).mapN(Info)
		val serverVariableGen: Gen[Server.Variable] =
			(Gen.listOfN(2, nonEmptyString), nonEmptyString, optionStringGen).mapN(Server.Variable)
		val serverGen: Gen[Server] =
			(nonEmptyString, Gen.option(nonEmptyString), mapStringToGen(serverVariableGen))
				.mapN(Server.apply)
		val serversGen = Gen.listOfN(2, serverGen)
		val externalDocsGen: Gen[ExternalDocs] = (nonEmptyString, optionStringGen).mapN(ExternalDocs)
		val tagGen: Gen[Tag] = (nonEmptyString, optionStringGen, Gen.option(externalDocsGen)).mapN(Tag)
		val referenceGen: Gen[Reference] = nonEmptyString map Reference
		val mediaTypeGen: Gen[MediaType[T]] =
			(Gen.option(T.arbitrary), Map.empty[String, Encoding[T]].pure[Gen]).mapN(MediaType.apply)
		val headerGen: Gen[Header[T]] = (nonEmptyString, T.arbitrary).mapN(Header.apply)
		val requestGen: Gen[Request[T]] = (optionStringGen, mapStringToGen(mediaTypeGen), sampleBool).mapN(Request.apply)
		val responseGen: Gen[Response[T]] =
			(nonEmptyString, mapStringToGen(eitherGen(headerGen, referenceGen)), mapStringToGen(mediaTypeGen))
				.mapN(Response.apply)
		val responsesGen = mapStringToGen(eitherGen(responseGen, referenceGen))
		
		val locationGen: Gen[Location] = Gen.oneOf(Location.all)
		val parameterGen: Gen[Parameter[T]] =
			(
				nonEmptyString,
				locationGen,
				Gen.option(nonEmptyString),
				Gen.option(sampleBool),
				Gen.option(sampleBool),
				Gen.option(nonEmptyString),
				Gen.option(sampleBool),
				Gen.option(sampleBool),
				Gen.option(sampleBool),
				T.arbitrary
			).mapN(Parameter.apply)
		
		val parameters: Gen[List[Either[Parameter[T], Reference]]] = Gen.listOfN(2, eitherGen(parameterGen, referenceGen))
		
		val operationGen: Gen[Path.Operation[T]] =
			(
				Gen.listOfN(2, nonEmptyString),
				Gen.option(nonEmptyString),
				Gen.option(nonEmptyString),
				Gen.option(externalDocsGen),
				Gen.option(nonEmptyString),
				parameters,
				Gen.option(eitherGen(requestGen, referenceGen)),
				responsesGen,
				Map.empty[String, Either[Callback[T], Reference]].pure[Gen],
				sampleBool,
				serversGen
			).mapN(Path.Operation.apply)
		
		val itemObjectsGen: Gen[Path.ItemObject[T]] =
			(
				Gen.option(nonEmptyString),
				Gen.option(nonEmptyString),
				Gen.option(nonEmptyString),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				Gen.option(operationGen),
				serversGen,
				parameters
			).mapN(Path.ItemObject.apply)
		
		val componentsGen: Gen[Components[T]] =
			(
				mapStringToGen(T.arbitrary),
				responsesGen,
				mapStringToGen(eitherGen(requestGen, referenceGen)),
				mapStringToGen(eitherGen(parameterGen, referenceGen))
			).mapN(Components.apply)
		
		Arbitrary(
			(
				nonEmptyString,
				infoGen,
				Gen.listOfN(2, serverGen),
				mapStringToGen(itemObjectsGen),
				Gen.option(componentsGen),
				Gen.listOfN(5, tagGen),
				Gen.option(externalDocsGen)
			).mapN(OpenApi[T])
		)
	}

}
