package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource

/**
 * Converted from json with this tool = https://cchandurkar.github.io/json-schema-to-case-class/
 */
object Class_TelraamDevsDatasource_fromJson {
	
	/**
	 * @param features
	 */
	case class TelraamActiveSegmentsDataSchema(
										  features: Option[List[Features]]
									  )
	
	
	/**
	 * @param properties
	 */
	case class Features(
					    properties: Option[Properties]
				    )
	
	
	/**
	 * @param segment_id
	 * @param last_data_package
	 * @param pedestrian
	 * @param bike
	 * @param car
	 * @param heavy
	 * @param uptime
	 * @param timezone
	 * @param period
	 * @param date
	 */
	case class Properties(
						 segment_id: Int,
						 last_data_package: Option[Any],
						 pedestrian: Any,
						 bike: Any,
						 car: Any,
						 heavy: Any,
						 uptime: Any,
						 timezone: String,
						 period: String,
						 date: Any
					 )
	
}
