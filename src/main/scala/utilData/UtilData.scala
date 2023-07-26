package utilData

/**
 *
 */
object UtilData {
	
	
	// Arguments for the Util.getShortFuncType from utilTest
	val keepPckgs: Option[List[String]] = None
	//Some(List("io.circe.Json"/*, "json.Schema", "org.apache.avro.Schema"*/))
	
	val classesToSubs: Option[Map[String, String]] = Some(Map(
		"AvroF" → "AvroSchema_S",
		"io.circe.Json" → "JsonCirce",
		"org.apache.avro.Schema" → "AvroSchema_A",
		"json.Schema" → "JsonSchema_G",
		"JsonSchemaF" → "JsonSchema_S")
	)
}

