package conversionsOfSchemaStrings.json_json

import scala.util.Try

import com.github.andyglow.json.{ParseJson, Value}

// NOTE replacing with my copy because cannot import his private function makeType
//import com.github.andyglow.jsonschema.ParseJsonSchema
import utilMain.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema

import com.github.andyglow.scalamigration._
import json.{Schema ⇒ SchemaJson_AndyGlow}
import SchemaJson_AndyGlow._ // for the `boolean`, `string` types etc.


/**
 *
 */
object RawJsonSchemaStr_To_AndyGlowJsonSchemaADT {
	def parseType(x: String): Try[SchemaJson_AndyGlow[_]] = {
		
		//val firstPart: Try[Value.obj] = (ParseJson(x) find { case e: Value.obj => e })
		
		(ParseJson(x) find { case e: Value.obj => e }) flatMap ParseJsonSchema.makeType
	}
}
