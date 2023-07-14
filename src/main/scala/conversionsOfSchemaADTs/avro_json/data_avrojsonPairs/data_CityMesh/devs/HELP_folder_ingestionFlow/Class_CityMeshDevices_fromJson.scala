//package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_CityMesh.devs.HELP_folder_ingestionFlow
//
///**
// * Converted from .json with this tool = https://cchandurkar.github.io/json-schema-to-case-class/
// */
//object Class_CityMeshDevices_fromJson {
//
//
//	object TypeEnum extends Enumeration {
//		val Device = Value
//	}
//
//	/**
//	 * Context data about CityMesh devices
//	 *
//	 * @param id
//	 * @param type
//	 * @param refDeviceModel
//	 * @param adminState
//	 * @param healthState
//	 * @param areaCovered
//	 * @param maxTimeBetweenObservations
//	 * @param dateInstalled
//	 * @param source
//	 * @param location
//	 * @param category
//	 */
//	case class CityMeshDevicesSchema(
//								  id: String,
//								  `type`: Option[TypeEnum.Value],
//								  refDeviceModel: RefDeviceModel,
//								  adminState: Option[AdminState],
//								  healthState: Option[HealthState],
//								  areaCovered: AreaCovered,
//								  maxTimeBetweenObservations: Option[MaxTimeBetweenObservations],
//								  dateInstalled: Option[DateInstalled],
//								  source: Option[Source],
//								  location: Location,
//								  category: Option[Category]
//							  )
//
//	object TypeEnum extends Enumeration {
//		val Relationship = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class RefDeviceModel(
//							`type`: Option[TypeEnum.Value],
//							value: Option[String],
//							metadata: Option[Metadata]
//						)
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class AdminState(
//						 `type`: Option[String],
//						 value: Option[String],
//						 metadata: Option[Metadata]
//					 )
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class HealthState(
//						  `type`: Option[String],
//						  value: Option[String],
//						  metadata: Option[Metadata]
//					  )
//
//
//	/**
//	 */
//	/// TODO this file is weird - why re-create this class 3 times?
//	case class Metadata(
//				    )
//
//	// TODO meaning of geo:json in enum in the .json file???? Does not give the right type here.
//	object TypeEnum extends Enumeration {
//		val geo: json = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value Geoproperty. Geojson reference to the item
//	 */
//	case class AreaCovered(
//						  `type`: Option[TypeEnum.Value],
//						  value: Option[GeoJsonPolygon]
//					  )
//
//	object TypeEnum extends Enumeration {
//		val Polygon = Value
//	}
//
//	/**
//	 * Geoproperty. Geojson reference to the item
//	 *
//	 * @param type
//	 * @param coordinates
//	 * @param bbox
//	 * @param metadata
//	 */
//	case class GeoJsonPolygon(
//							`type`: TypeEnum.Value,
//							coordinates: List[List[List[Double]]],
//							bbox: Option[List[Double]],
//							metadata: Option[Metadata]
//						) {
//		assert(bbox.forall(_.length >= 4), "`bbox` must have minimum 4 item(s)")
//	}
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//	object TypeEnum extends Enumeration {
//		val Number = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class MaxTimeBetweenObservations(
//									  `type`: Option[TypeEnum.Value],
//									  value: Option[Double],
//									  metadata: Option[Metadata]
//								  )
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//	object TypeEnum extends Enumeration {
//		val DateTime = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class DateInstalled(
//						    `type`: Option[TypeEnum.Value],
//						    value: Option[String],
//						    metadata: Option[Metadata]
//					    )
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//	object TypeEnum extends Enumeration {
//		val String = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class Source(
//					  `type`: Option[TypeEnum.Value],
//					  value: Option[String],
//					  metadata: Option[Metadata]
//				  )
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//	object TypeEnum extends Enumeration {
//		val geo: json = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value Geoproperty. Geojson reference to the item
//	 */
//	case class Location(
//					    `type`: Option[TypeEnum.Value],
//					    value: Option[GeoJsonPoint]
//				    )
//
//	object TypeEnum extends Enumeration {
//		val Point = Value
//	}
//
//	/**
//	 * Geoproperty. Geojson reference to the item
//	 *
//	 * @param type
//	 * @param coordinates
//	 * @param bbox
//	 * @param metadata
//	 */
//	case class GeoJsonPoint(
//						   `type`: TypeEnum.Value,
//						   coordinates: List[Double],
//						   bbox: Option[List[Double]],
//						   metadata: Option[Metadata]
//					   ) {
//		assert(coordinates.length >= 2, "`coordinates` must have minimum 2 item(s)")
//		assert(bbox.forall(_.length >= 4), "`bbox` must have minimum 4 item(s)")
//	}
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//	object TypeEnum extends Enumeration {
//		val StructuredValue = Value
//	}
//
//	/**
//	 * @param type
//	 * @param value
//	 * @param metadata
//	 */
//	case class Category(
//					    `type`: Option[TypeEnum.Value],
//					    value: Option[List[String]],
//					    metadata: Option[Metadata]
//				    )
//
//
//	/**
//	 */
//	case class Metadata(
//				    )
//
//}
