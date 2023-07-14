package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource

/**
 * Converted from json with this tool = https://cchandurkar.github.io/json-schema-to-case-class/
 */
object Class_TelraamDevsDatasourceLegacy_fromJson {
	
	
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
	 * @param id
	 * @param last_data_package
	 * @param pedestrian_avg
	 * @param bike_avg
	 * @param car_avg
	 * @param lorry_avg
	 * @param typical_data
	 */
	case class Properties(
						 id: Int,
						 last_data_package: Option[Any],
						 pedestrian_avg: Int,
						 bike_avg: Int,
						 car_avg: Int,
						 lorry_avg: Int,
						 typical_data: List[TypicalData]
					 )
	
	
	/**
	 * @param hour
	 * @param pedestrian
	 * @param bike
	 * @param car
	 * @param lorry
	 */
	case class TypicalData(
						  hour: String,
						  pedestrian: Double,
						  bike: Double,
						  car: Double,
						  lorry: Double
					  )
}
