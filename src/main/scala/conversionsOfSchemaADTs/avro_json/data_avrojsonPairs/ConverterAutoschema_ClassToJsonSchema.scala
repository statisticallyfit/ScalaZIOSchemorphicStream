package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs


import org.coursera.autoschema.AutoSchema
import play.api.libs.json.JsObject


/**
 *
 */
object ConverterAutoschema_ClassToJsonSchema extends App {
	
	
	/*def conv[T <: Object]: String = {
		val schema: SchemaJson_Glow[T] = Json.schema[T]
		
		JsonFormatter.format(AsValue.schema(schema, json.schema.Version.Draft04()))
	}*/
	
	
	// HELP Problem - cannot use the non-legacy version because Any in the parameter list - causing this error:
	/*
	schema for Any is not supported, conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource.Class_TelraamDevsDatasourceLegacy_fromJson.TelraamActiveSegmentsDataSchema :: List[conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource.Class_TelraamDevsDatasourceLegacy_fromJson.Features] :: conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource.Class_TelraamDevsDatasourceLegacy_fromJson.Features :: conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource.Class_TelraamDevsDatasourceLegacy_fromJson.Properties
	[error] 	implicit val telraamJsonSchema: SchemaJson_Glow[TelraamActiveSegmentsDataSchema] = Json.schema[TelraamActiveSegmentsDataSchema]
	[error] 	                                                                                              ^
	[error] one error found


	 */
	
	
	import conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_CityMesh.devs.folder_datasource.Class_RawCityMeshContext_fromJson._
	import conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Telraam.devs.datasource.Class_TelraamDevsDatasource_fromJson._
	
	
	/*implicit val anySchema = Json.schema[Any]
	
	implicit val propertiesSchema: SchemaJson_Glow[Properties] = Json.schema[Properties]
	
	implicit val featuresSchema: SchemaJson_Glow[Features] = Json.schema[Features]
	
	implicit val telraamJsonSchema: SchemaJson_Glow[TelraamActiveSegmentsDataSchema] = Json.schema[TelraamActiveSegmentsDataSchema]*/
	
	
	//println(JsonFormatter.format(AsValue.schema(telraamJsonSchema, json.schema.Version.Draft04())))
	// TODO test with autoschema (saul )
	
	val telraamJsonSchema: JsObject = AutoSchema.createSchema[TelraamActiveSegmentsDataSchema]
	println(s"telraamJsonSchema = ${telraamJsonSchema.toString()}")
	
	
	val rawCityMeshJsonSchema = AutoSchema.createSchema[RawCityMeshDevicesSchema]
	println(s"raw city mesh json schema = ${rawCityMeshJsonSchema}")
	//conv[TelraamActiveSegmentsDataSchema]
}
