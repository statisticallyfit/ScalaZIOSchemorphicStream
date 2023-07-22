package utilData

/**
 *
 */
object UtilData {

	
	// Arguments for the Util.getShortFuncType from utilTest
	val keepPckgs: Option[List[String]] = None
		//Some(List("io.circe.Json"/*, "json.Schema", "org.apache.avro.Schema"*/))
	
	val classesToSubs: Option[Map[String, String]] = Some(Map(
		"AvroF" → "SchemaAvro_Skeuo",
		"io.circe.Json" → "JsonCirce",
		"org.apache.avro.Schema" → "SchemaAvro_Apache",
		"json.Schema" → "SchemaJson_Glow",
		"JsonSchemaF" → "SchemaJson_Skeuo")
	)
}

