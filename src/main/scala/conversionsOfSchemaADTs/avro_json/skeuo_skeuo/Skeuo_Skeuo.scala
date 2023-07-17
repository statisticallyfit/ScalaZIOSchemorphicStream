package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

import cats.data.NonEmptyList

import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.droste.implicits._

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._

import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import scala.reflect.runtime.universe._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
/**
 *
 */
object Skeuo_Skeuo {


	import conversionsOfSchemaADTs.avro_json.skeuo_skeuo._

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
	def avroToJsonFunction[T: TypeTag]: SchemaAvro_Skeuo[T] ⇒ SchemaJson_Skeuo[T] = {
		val transVar: Trans[SchemaAvro_Skeuo, SchemaJson_Skeuo, T] = transform_AvroToJsonSkeuo[T]
		
		val runner: SchemaAvro_Skeuo[T] ⇒ SchemaJson_Skeuo[T] = transVar.run
		
		runner.apply(_)
		
	}
	def aj[T: TypeTag] = avroToJsonFunction[T]
	
	def avroToJson[T: TypeTag](av: SchemaAvro_Skeuo[T]): SchemaJson_Skeuo[T] = transform_AvroToJsonSkeuo[T].run.apply(av)
	
	object ByAlgebra {
		
		
		def avroToJson_Fixed: Fix[SchemaAvro_Skeuo] ⇒ SchemaJson_Skeuo.Fixed =
			scheme.cata(algebra_AvroToJsonFixed).apply(_)


		/*def avroToJson_TYPED[T: TypeTag]: Fix[SchemaAvro_Skeuo] ⇒ SchemaJson_Skeuo[T] =
			scheme.cata(algebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: Fix[SchemaAvro_Skeuo] ⇒ SchemaJson_Skeuo[_] = scheme.cata(algebra_AvroToJson).apply(_)*/

		// NOT GOOD - "No implicits found for Functor[AvroF[_]]
		//def typed2[T: TypeTag] = scheme.cata(algebra2[T]).apply(_)

		/*def jsonToAvro_TYPED[T: TypeTag]: Fix[SchemaJson_Skeuo] ⇒ SchemaAvro_Skeuo[T] =
			scheme.cata(algebra_JsonToAvro_TYPED[T]).apply(_)


		def jsonToAvro_UNTYPED: Fix[SchemaJson_Skeuo] ⇒ SchemaAvro_Skeuo[_] = scheme.cata(algebra_JsonToAvro).apply(_)*/
	}



	object ByCoalgebra {

		/*def avroToJson_TYPED[T: TypeTag]: SchemaAvro_Skeuo[T] ⇒ Fix[SchemaJson_Skeuo] =
			scheme.ana(coalgebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: SchemaAvro_Skeuo[_] ⇒ Fix[SchemaJson_Skeuo] =
			scheme.ana(coalgebra_AvroToJson).apply(_)*/


		/*def jsonToAvro_TYPED[T: TypeTag]: SchemaJson_Skeuo[T] ⇒ Fix[SchemaAvro_Skeuo] =
			scheme.ana(coalgebra_JsonToAvro_TYPED[T]).apply(_)

		def jsonToAvro_UNTYPED: SchemaJson_Skeuo[_] ⇒ Fix[SchemaAvro_Skeuo] =
			scheme.ana(coalgebra_JsonToAvro).apply(_)*/
	}


	
	
	

	// AvroF -> Json[AvroF] -> AvroF
	//def roundTrip_AvroToAvro[T: TypeTag]: SchemaAvro_Skeuo[T] ⇒ SchemaAvro_Skeuo[T] = ByAlgebra.jsonToAvro_TYPED[T] compose ByCoalgebra.avroToJson_TYPED[T]

	//def roundTrip_AvroToAvro_CanonicalFix[T: TypeTag]: Fix[SchemaAvro_Skeuo] ⇒ Fix[SchemaAvro_Skeuo] = ByCoalgebra.jsonToAvro_TYPED[T] compose ByAlgebra.avroToJson_TYPED[T]


	// JsonF -> AvroF[JsonF] -> JsonF
	//def roundTrip_JsonToJson[T: TypeTag]: SchemaJson_Skeuo[T] ⇒ SchemaJson_Skeuo[T] = ByAlgebra.avroToJson_TYPED[T] compose ByCoalgebra.jsonToAvro_TYPED[T]

	//def roundTrip_JsonToJson_CanonicalFix[T: TypeTag]: Fix[SchemaJson_Skeuo] ⇒ Fix[SchemaJson_Skeuo] = ByCoalgebra.avroToJson_TYPED[T] compose ByAlgebra.jsonToAvro_TYPED[T]


}
