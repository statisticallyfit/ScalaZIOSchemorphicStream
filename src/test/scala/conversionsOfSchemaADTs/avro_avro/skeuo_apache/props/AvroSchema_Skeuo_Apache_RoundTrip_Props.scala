package conversionsOfSchemaADTs.avro_avro.skeuo_apache.props



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


import testData.ScalaCaseClassData._




import org.scalatest.Assertions._
import org.scalacheck._
import org.specs2._
import org.specs2.specification.core.SpecStructure


import conversionsOfSchemaADTs.avro_avro.Skeuo_Apache.{apacheToSkeuoAvroSchema, skeuoToApacheAvroSchema}
//import conversionsOfSchemaStrings.avro_json.Skeuo_JsonCirce.{skeuoAvroSchemaToJsonCirce, jsonCirceToSkeuoAvroSchema}



class AvroSchema_Skeuo_Apache_RoundTrip_Props extends Specification with ScalaCheck {
	
	// TODO import instances = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/avro/AvroSchemaSpec.scala#L20
	
	def is: SpecStructure =
		s2"""
			Converting between Skeuomorph and Apache libraries' avro schema ADTs (using property-based testing)
		"""
	
	
	//scheme.hylo(algebra_SkeuoApache, coalgebra_ApacheSkeuo) shouldBe a[SchemaAvro_Apache => SchemaAvro_Apache] // TODO idea prop-check - assert that apacheroundtrip meets the hylomorphism laws
	
}