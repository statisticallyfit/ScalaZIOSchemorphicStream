package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.dataFile_CityMesh_Tfmsm_datasource

/**
 * Class generated from the .json schema using this online tool by cchandurkar = https://cchandurkar.github.io/json-schema-to-case-class/
 */
object Class_RawCityMeshSchema_fromJson {
	
	/**
	 * @param data
	 */
	case class RawCityMeshMeasurementsSchema(
										data: Data
									)
	
	
	/**
	 * @param overview
	 */
	case class Data(
					overview: Overview
				)
	
	
	/**
	 * @param visitorTypeAmount
	 */
	case class Overview(
					    visitorTypeAmount: VisitorTypeAmount
				    )
	
	
	/**
	 * @param uniqueVisitorAmount
	 */
	case class VisitorTypeAmount(
							   uniqueVisitorAmount: Int
						   )
	
	
}
