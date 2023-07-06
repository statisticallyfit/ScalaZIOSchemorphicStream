package Examples_AndyGlow

import com.github.andyglow.json.JsonFormatter
import com.github.andyglow.jsonschema.AsValue
import json.schema.Predef


/**
 *
 */
object StringAndArrayExample extends App {


  /*import json._

  implicit val someStrSchema: json.Schema[Person] = Json.schema[Person].toDefinition("my-lovely-person")

  implicit val someArrSchema: json.Schema[Array[Person]] = Json.schema[Array[Person]]

  println(JsonFormatter.format(AsValue.schema(someArrSchema, json.schema.Version.Draft06(id="some id here"))))*/

  import json._

  import json.Json.auto._
  import json.Schema
  import json.Schema._
  import json.schema.definition._
  import json.schema.Predef._
  import com.github.andyglow.jsonschema._
  import com.github.andyglow.jsonschema.RefinedSupport._
  import com.github.andyglow.jsonschema.EnumeratumSupport._

  import scala.language.experimental.macros


  // TODO HELP why isn't schemea of string / integer recognized?
  /*implicit val strPredef: Predef[String] = Json.auto.derived[String]
  implicit val strSchema: json.Schema[String] = strPredef.schema

  println(JsonFormatter.format(AsValue.schema(strSchema, json.schema.Version.Draft06(id = "some str id"))))*/
  //.derivePredef[String] //Json.schema[String]//.toDefinition("my-lovely-string")

  implicit val someArrSchema: json.Schema[Array[String]] = Json.schema[Array[String]]

  println(JsonFormatter.format(AsValue.schema(someArrSchema, json.schema.Version.Draft06(id = "some id here"))))




  /*{
    "$schema": "http://json-schema.org/draft-04/schema#"
    ,
    "type": "array"
    ,
    "items": {
      "$ref": "#/definitions/my-lovely-string"
    }
    ,
    "definitions": {
      "my-lovely-string": {
      "type": "string"
    }
    }
  }*/
}
