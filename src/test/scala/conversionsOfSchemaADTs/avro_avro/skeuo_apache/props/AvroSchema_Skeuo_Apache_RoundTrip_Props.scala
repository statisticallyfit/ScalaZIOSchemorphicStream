package conversionsOfSchemaADTs.avro_avro.skeuo_apache.props


import org.specs2._
import org.specs2.specification.core.SpecStructure
//import conversionsOfSchemaStrings.avro_json.Skeuo_JsonCirce.{skeuoAvroSchemaToJsonCirce, jsonCirceToSkeuoAvroSchema}


class AvroSchema_Skeuo_Apache_RoundTrip_Props extends Specification with ScalaCheck {
	
	// TODO import instances = https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/avro/AvroSchemaSpec.scala#L20
	
	def is: SpecStructure =
		s2"""
			Converting between Skeuomorph and Apache libraries' avro schema ADTs (using property-based testing)
		"""
	
	
	//scheme.hylo(algebra_SkeuoApache, coalgebra_ApacheSkeuo) shouldBe a[SchemaAvro_Apache => SchemaAvro_Apache] // TODO idea prop-check - assert that apacheroundtrip meets the hylomorphism laws
	
}