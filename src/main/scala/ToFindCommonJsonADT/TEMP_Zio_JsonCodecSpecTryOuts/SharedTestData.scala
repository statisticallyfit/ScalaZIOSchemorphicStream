package ToFindCommonJsonADT.TEMP_Zio_JsonCodecSpecTryOuts

/**
 *
 */

import zio.Chunk
import zio.schema._

import scala.annotation.StaticAnnotation


object SharedTestData {

	def staticAnnotationToSchemaEnum(s: StaticAnnotation): Schema.Enum3[String, String, String, String] = {
		val caseA = Schema.Case[String, String](
			"A",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			(_: String).isInstanceOf[String]
		)
		val caseB = Schema.Case[String, String](
			"B",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			_.isInstanceOf[String]
		)
		val caseC = Schema.Case[String, String](
			"C",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			_.isInstanceOf[String]
		)



		// NOTE: returning an Enum3 (zio) containing the three cases.

		Schema.Enum3(TypeId.Structural, caseA, caseB, caseC, Chunk(s))
	}

	def makeSchemaEnumSimple: Schema.Enum3[String, String, String, String] = {
		val caseA = Schema.Case[String, String](
			"A",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			(_: String).isInstanceOf[String]
		)
		val caseB = Schema.Case[String, String](
			"B",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			_.isInstanceOf[String]
		)
		val caseC = Schema.Case[String, String](
			"C",
			Schema.primitive(StandardType.StringType),
			identity,
			identity,
			_.isInstanceOf[String]
		)



		// NOTE: returning an Enum3 (zio) containing the three cases.

		Schema.Enum3(TypeId.Structural, caseA, caseB, caseC /*, Chunk(s)*/)
	}
}