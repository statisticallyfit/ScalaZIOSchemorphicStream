package conversionsOfSchemaStrings.avro_json



import cats.data.NonEmptyList
import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}

import io.circe.Json


import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}


import zio.schema._
import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType} // TODO move the zio <-> apache tests in the other file
import zio.schema.codec.AvroCodec

import scala.jdk.CollectionConverters._


import utilMain.utilSkeuoApache.FieldAndOrderConversions._
import utilMain.utilSkeuoApache.ValidateUtil._


/**
 *
 */
object Skeuo_JsonCirce {
	
	/**
	 * Algebra type is:
	 * Skeuo[Apache] => Apache
	 *
	 * @return
	 */
	def algebra_Skeuo_JsonCirce: Algebra[SchemaSkeuoAvro, Json] = SchemaSkeuoAvro.toJson
	
	/**
	 * Coalgebra type is:
	 * Apache => Skeuo[Apache]
	 *
	 * @return
	 */
	def coalgebra_JsonCirce_Skeuo: Coalgebra[SchemaSkeuoAvro, Json] = ??? // TODO must build it
	
	
	
	def skeuoAvroSchemaToJsonString: Fix[SchemaSkeuoAvro] ⇒ Json = scheme.cata(algebra_Skeuo_JsonCirce).apply(_)
	
	// Simpler name
	def avroSchemaToJsonString = skeuoAvroSchemaToJsonString
	
	
	def jsonStringToSkeuoAvroSchema: Json ⇒ Fix[SchemaSkeuoAvro] = scheme.ana(coalgebra_JsonCirce_Skeuo).apply(_)
	
	
	
	
	
}
