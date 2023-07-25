package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs


import scala.reflect.runtime.universe._

import fi.oph.scalaschema.{SchemaFactory, SchemaToJson, Schema ⇒ SchemaJson_Opetus}
//import org.json4s.package.JValue

import org.json4s.jackson.JsonMethods
import org.json4s.jackson.JsonMethods._ //asJsonNode
import org.json4s.JsonAST.{JObject, JNull, JInt, JString, JArray}
import org.json4s.JsonAST.JValue

//import org.json4s.{JNull, JInt, JString, JArray}


import com.github.fge.jsonschema.core.report.ListReportProvider
import com.github.fge.jsonschema.core.report.LogLevel.{ERROR, FATAL}
import com.github.fge.jsonschema.main.{JsonSchemaFactory, JsonValidator}


/**
 *
 */
object HELP_Opetushallitus_jdata_to_jschema {
	
	
	// TODO HERE - using opetushallitus to take json data -> json schema
	
	
	
	
	object helpers {
		def schemaOf(c: Class[_]) = SchemaFactory.default.createSchema(c)
		
		def jsonSchemaOf[T: TypeTag]: String = jsonSchemaOf(SchemaFactory.default.createSchema[T])
		
		def jsonSchemaOf(c: Class[_]): String = jsonSchemaOf(schemaOf(c))
		
		def jsonSchemaOf(s: SchemaJson_Opetus): String = {
			val schemaJson = s.toJson
			// Just check that the created schema is a valid JSON schema, ignore validation results
			jsonSchemaFactory.getJsonSchema(asJsonNode(SchemaToJson.toJsonSchema(s))).validate(asJsonNode(JObject()))
			JsonMethods.compact(schemaJson)
		}
		
		private lazy val jsonSchemaFactory = JsonSchemaFactory.newBuilder.setReportProvider(new ListReportProvider(ERROR, FATAL)).freeze()
	}
	import helpers._
	
	
	val j: JValue = JNull
	
	// HELP left off here issue with json4s import (not for scala 2.12)
	val nulljc: String = jsonSchemaOf(JNull.getClass)
	//val nulljc = jsonSchemaOf(classOf[JNull.type])
	val intjc = jsonSchemaOf[JInt]
	val strjc = jsonSchemaOf[JString]
	val arrjc = jsonSchemaOf[JArray]
	
	println(s"--- OPETUS HALLITUS: json data -> json schema circe")
	println(s"nulljc = $nulljc")
	println(s"intjc = $intjc")
	println(s"strjc = $strjc")
	println(s"arrjc = $arrjc")
}
