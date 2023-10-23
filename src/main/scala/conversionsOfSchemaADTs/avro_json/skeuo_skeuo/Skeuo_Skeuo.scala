package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

// Imports for the jsonSchemaDecoder (from JsonDecoders file from skeuomorph)
import io.circe._
import io.circe.Decoder
import io.circe.Decoder.{Result, resultInstance}
import io.circe.{Json => JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import cats.syntax.all._
//import cats.implicits._
//import cats.syntax._



import higherkindness.droste._
import higherkindness.droste.data._
//import higherkindness.droste.syntax.all._
import higherkindness.droste.syntax.embed._
import higherkindness.skeuomorph.openapi.schema._

import scala.language.postfixOps
import scala.language.higherKinds
//import scala.language.implicitConversions



import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}


import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
/**
 *
 */
object Skeuo_Skeuo {


	/*import Coalgebra_AvroToJson._
	import Coalgebra_JsonToAvro._
	import Algebra_AvroToJson._
	import Algebra_JsonToAvro._

	import Coalgebra_JsonFixedToAvro._*/
	import Algebra_AvroToJson_Fixed._
	import Trans_AvroToJson._


	// TODO: do all these conversions using Trans (because itis F[A => G[A]): https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/package.scala#L80

	/// -----------------------------



	// NOTE how to access Trans type (example here) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/mu/protocol.scala#L56C36-L56C36
	object ByTrans {
		/*def avroToJsonFunction[T: TypeTag]: AvroSchema_S[T] ⇒ JsonSchema_S[T] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]

			val runner: AvroSchema_S[T] ⇒ JsonSchema_S[T] = transVar.run

			runner.apply(_)

		}

		def avroToJson[T: TypeTag](av: AvroSchema_S[T]): JsonSchema_S[T] = transform_AvroToJsonSkeuo[T].run.apply(av)

		*/

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsEmbedProject._
		import embedImplicits.skeuoEmbed_AJ
		import projectImplicits.skeuoProject_JA



		// algebra: Embed: G[A] => A     :  AvroF [ Fix[JsonF] ] => Fix[JsonF]
		// coalgebra: Project: A => F[A]   :  Fix[JsonF] => JsonF [ Fix[JsonF] ]
		// CATA = algebra . coalgebra

		 // TODO: declare transA: Trans[JsonSchema, AvroSchema, Fix[?]] with corresponding implicits and see how they pick up at the decoders then see what comes out with the avro-skeuo -> circe -> skeuo conversion

		val avroToJson_byCataTransAlg: Fix[AvroSchema_S] => Fix[JsonSchema_S] = scheme.cata(transJAJ.algebra).apply(_)




		import embedImplicits.skeuoEmbed_JA
		import projectImplicits.skeuoProject_AJ

		// algebra: Embed:  G[A] => A:
		// JsonSchema_S[Fix[JsonSchema_S]] => Fix[JsonSchema_S]
		// coalgebra: Project:
		// A => F[A]
		// Fix[JsonSchema_S] => AvroSchema_S [ Fix[JsonSchema_S] ]

		// algebra: Embed: G[A] => A     :  JsonF [ Fix[JsonF] ] => Fix[JsonF]
		// coalgebra: Project: A => F[A]   :  Fix[JsonF] => AvroF [ Fix[JsonF] ]
		// ANA = algebra . coalgebra
		val jsonToAvro_byAnaTransCoalg: Fix[JsonSchema_S] ⇒ Fix[AvroSchema_S] = scheme.ana(transAJJ.coalgebra).apply(_)



		//val h: Fixed ⇒ Fixed = scheme.hylo(transJ.algebra, transJ.coalgebra)

		val roundTripAvro: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg compose avroToJson_byCataTransAlg

		val roundTripJson: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = avroToJson_byCataTransAlg compose jsonToAvro_byAnaTransCoalg


		/*def transFixWay[T: TypeTag] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]

			val ta = transVar.algebra
		}*/
	}


	object ByAlgebra {


		def avroToJson_Fixed: Fix[AvroSchema_S] ⇒ Fix[JsonSchema_S] =
			scheme.cata(algebra_AvroToJsonFixed).apply(_)


		/*def avroToJson_TYPED[T: TypeTag]: Fix[AvroSchema_S] ⇒ JsonSchema_S[T] =
			scheme.cata(algebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: Fix[AvroSchema_S] ⇒ JsonSchema_S[_] = scheme.cata(algebra_AvroToJson).apply(_)*/

		// NOT GOOD - "No implicits found for Functor[AvroF[_]]
		//def typed2[T: TypeTag] = scheme.cata(algebra2[T]).apply(_)

		/*def jsonToAvro_TYPED[T: TypeTag]: Fix[JsonSchema_S] ⇒ AvroSchema_S[T] =
			scheme.cata(algebra_JsonToAvro_TYPED[T]).apply(_)


		def jsonToAvro_UNTYPED: Fix[JsonSchema_S] ⇒ AvroSchema_S[_] = scheme.cata(algebra_JsonToAvro).apply(_)*/
	}


	object ByCoalgebra {

		/*def avroToJson_TYPED[T: TypeTag]: AvroSchema_S[T] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: AvroSchema_S[_] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson).apply(_)*/


		/*def jsonToAvro_TYPED[T: TypeTag]: JsonSchema_S[T] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro_TYPED[T]).apply(_)

		def jsonToAvro_UNTYPED: JsonSchema_S[_] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro).apply(_)*/
	}






	// AvroF -> Json[AvroF] -> AvroF
	//def roundTrip_AvroToAvro[T: TypeTag]: AvroSchema_S[T] ⇒ AvroSchema_S[T] = ByAlgebra.jsonToAvro_TYPED[T] compose ByCoalgebra.avroToJson_TYPED[T]

	//def roundTrip_AvroToAvro_CanonicalFix[T: TypeTag]: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = ByCoalgebra.jsonToAvro_TYPED[T] compose ByAlgebra.avroToJson_TYPED[T]


	// JsonF -> AvroF[JsonF] -> JsonF
	//def roundTrip_JsonToJson[T: TypeTag]: JsonSchema_S[T] ⇒ JsonSchema_S[T] = ByAlgebra.avroToJson_TYPED[T] compose ByCoalgebra.jsonToAvro_TYPED[T]

	//def roundTrip_JsonToJson_CanonicalFix[T: TypeTag]: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = ByCoalgebra.avroToJson_TYPED[T] compose ByAlgebra.jsonToAvro_TYPED[T]


}
