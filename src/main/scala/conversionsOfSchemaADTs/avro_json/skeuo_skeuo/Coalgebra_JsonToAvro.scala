package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

import higherkindness.droste._
//import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}

import scala.reflect.runtime.universe._


/**
 * Coalgebra type is:
 * Json => Avro[Json]
 *
 * @return
 */

object Coalgebra_JsonToAvro {
	
	def coalgebra_JsonToAvro_TYPED[T: TypeTag]: Coalgebra[SchemaAvro_Skeuo, SchemaJson_Skeuo[T]] = ???
	
	def coalgebra_JsonToAvro: Coalgebra[SchemaAvro_Skeuo, SchemaJson_Skeuo[_]] = ???
}
