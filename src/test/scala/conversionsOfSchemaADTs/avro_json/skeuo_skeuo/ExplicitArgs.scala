package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import io.circe.{Json ⇒ JsonCirce}
import utilMain.UtilMain.implicits._


/**
 *
 */

class ExplicitArgs(
	val rawAvroStr: String,
	val rawJsonStr: String,
	val jsonCirceCheck: JsonCirce, val avroS: AvroSchema_S[_], val tpeS: String,
	val avroC: AvroSchema_S[_], val tpeC: String,
	val avroFixS: Fix[AvroSchema_S], val jsonFixS: Fix[JsonSchema_S]
)
object ExplicitArgs {
	def applying(
		rawAvroStr: String,
		rawJsonStr: String,
		jsonCirceCheck: JsonCirce, avroS: AvroSchema_S[_], tpeS: String,
		avroC: AvroSchema_S[_], tpeC: String,
		avroFixS: Fix[AvroSchema_S], jsonFixS: Fix[JsonSchema_S]
	) = new ExplicitArgs(rawAvroStr.manicure, rawJsonStr.manicure, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
}