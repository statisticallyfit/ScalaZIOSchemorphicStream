package utilData

/**
 *
 */
object UtilData {

	
	// Arguments for the Util.getShortFuncType from utilTest
	val keepPckgs: Option[List[String]] = Some(List("io.circe.Json"/*, "json.Schema", "org.apache.avro.Schema"*/))
	
	val classesToSubs: Option[Map[String, String]] = Some(Map(
		"AvroF" → "AvroSchema_Skeuo",
		"org.apache.avro.Schema" → "AvroSchema_Apache",
		"json.Schema" → "JsonSchema_Glow",
		"JsonSchemaF" → "JsonSchema_Skeuo")
	)
}

