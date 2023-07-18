package utilTest.utilJson.utilAndyGlow_ParseJsonSchemaStr

import com.github.andyglow.json.{ParseJson, Value}

import scala.util.Try

// NOTE replacing with my copy because cannot import his private function makeType
//import com.github.andyglow.jsonschema.ParseJsonSchema

import com.github.andyglow.scalamigration._
import json.{Schema ⇒ SchemaJson_Glow}

//import utilTest.utilJson.utilAndyGlow_ParseJsonSchema.ParseJsonSchema

/**
 *
 */
object ParseStrToADT {
	def parseType(x: String): Try[SchemaJson_Glow[_]] = {
		
		//val firstPart: Try[Value.obj] = (ParseJson(x) find { case e: Value.obj => e })
		
		(ParseJson(x) find { case e: Value.obj => e }) flatMap ParseJsonSchema.makeType
	}
}
