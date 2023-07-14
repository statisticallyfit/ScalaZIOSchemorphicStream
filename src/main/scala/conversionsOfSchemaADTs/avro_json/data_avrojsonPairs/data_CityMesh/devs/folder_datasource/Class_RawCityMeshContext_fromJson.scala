package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_CityMesh.devs.folder_datasource

/**
 * From json file using = https://cchandurkar.github.io/json-schema-to-case-class/
 */
object Class_RawCityMeshContext_fromJson {
	
	/**
	 * Context data about CityMesh devices
	 *
	 * @param data
	 */
	case class RawCityMeshDevicesSchema(
									data: Option[Data]
								)
	
	
	/**
	 * @param locations
	 */
	case class Data(
					locations: Option[List[Locations]]
				)
	
	
	/**
	 * @param id
	 * @param name
	 * @param sensorName
	 * @param position
	 * @param symbol
	 */
	case class Locations(
						id: String,
						name: String,
						sensorName: String,
						position: Position,
						symbol: Option[Any]
					)
	
	
	/**
	 * @param coordinates
	 */
	case class Position(
					    coordinates: Option[List[Double]]
				    ) {
		assert(coordinates.forall(_.length <= 2), "`coordinates` must have maximum 2 item(s)")
		assert(coordinates.forall(_.length >= 2), "`coordinates` must have minimum 2 item(s)")
	}
	
}
