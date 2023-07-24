package conversionsOfSchemaADTs.avro_json.skeuo_skeuo




import cats.data.NonEmptyList

import higherkindness.droste.{Embed, Trans}
import higherkindness.droste._
import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._

import AvroSchema_S.{Field ⇒ FieldAvro}
import JsonSchema_S.{Property ⇒ PropertyJson}

import scala.reflect.runtime.universe._


import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._


/**
 * Using droste Trans to convert F[A] => G[A}
 * SOURCE inspiration = https://github.com/higherkindness/skeuomorph/blob/cc739d3dcdc07ead250461b6ecc6fa4daf2ba988/src/main/scala/higherkindness/skeuomorph/mu/Transform.scala#L59
 *
 * DEF of Trans in droste:
 * 	gtrans - https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/trans.scala#L6
 * trans = https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/package.scala#L76
 */
object Trans_AvroToJson {

	def transJ/*[T: TypeTag]*/: Trans[AvroSchema_S, JsonSchema_S, Fix[JsonSchema_S]] = Trans {
		
		case TNull() ⇒ ObjectF(List(), List())
		case TInt() ⇒ IntegerF()
		
		case TString() ⇒ StringF()
		
		case TArray(inner: Fixed) ⇒ ArrayF(inner)
		
		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fixed]]) ⇒ {
			
			val ps: List[Property[Fix[JsonSchema_S]]] =  fields.map((f: FieldAvro[Fix[JsonSchema_S]]) ⇒ field2Property(f))
			val rs: List[String] = fields.map(f ⇒ f.name)
			
			ObjectF(ps, rs)
			
		}
	}
	
	
	def transform_AvroToJsonSkeuo[T: TypeTag]: Trans[AvroSchema_S, JsonSchema_S, T] = Trans {
		
		// NOTE: in the Avro file here (CityMesh - devs - datasource) the 'symbol' has type 'null' and in json the 'symbol' has type 'object' with  required = [], and properties = {}
		// path = /development/projects/statisticallyfit/github/learningdataflow/SchaemeowMorphism/src/test/scala/testData/testDataPrivateTati/asset-schemas/sdp-asset-schemas-citymesh/src/main/trafficflow/ctm.tf_devs/ctm.tf_devs.datasource/
		
		case TNull() ⇒ ObjectF[T](
			properties = List[Property[T]](/*Property(name = "", tpe = null.asInstanceOf[T])*/),
			required = List[String]()
		)
		case TInt() ⇒ IntegerF[T]()

		case TBoolean() ⇒ BooleanF[T]()

		case TString() ⇒ StringF[T]()

		case TFloat() ⇒ FloatF[T]()

		case TLong() ⇒ LongF[T]()

		case TDouble() ⇒ DoubleF[T]()

		case TBytes() ⇒ ByteF[T]()
		
		case TArray(innerSchema: T) ⇒ ArrayF(innerSchema)
		
		// TODO where does 'name' go?
		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[T]]) ⇒ {

			ObjectF(
				properties = fields.map((f: FieldAvro[T]) ⇒ field2Property(f)),
				required = fields.map(f ⇒ f.name)
			)

		}
		
	}
}
