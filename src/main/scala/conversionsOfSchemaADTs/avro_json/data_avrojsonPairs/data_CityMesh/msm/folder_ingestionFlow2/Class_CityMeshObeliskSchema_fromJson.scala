package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.dataFile_CityMesh_Tfmsm_ingestionFlow2

/**
 * Generated from .json using  = https://cchandurkar.github.io/json-schema-to-case-class/
 */
object Class_CityMeshObeliskSchema_fromJson {
	
	
	/**
	 * @param data
	 */
	case class CityMeshTrafficFlowObservedObeliskSchema(
												 data: Option[Data]
											 )
	
	
	/**
	 * @param columns
	 * @param values
	 */
	case class Data(
					columns: Option[List[Any]],
					values: Option[List[List[Any]]]
				)
	
}
