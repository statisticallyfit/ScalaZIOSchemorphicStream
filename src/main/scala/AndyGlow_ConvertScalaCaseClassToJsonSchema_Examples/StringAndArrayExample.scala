//package AndyGlow_ConvertScalaCaseClassToJsonSchema_Examples
//
//import com.github.andyglow.json.JsonFormatter
//import com.github.andyglow.jsonschema.AsValue
//
///**
// *
// */
//object StringAndArrayExample extends App {
//
//
//	/*import json._
//
//	implicit val someStrSchema: json.Schema[Person] = Json.schema[Person].toDefinition("my-lovely-person")
//
//	implicit val someArrSchema: json.Schema[Array[Person]] = Json.schema[Array[Person]]
//
//	println(JsonFormatter.format(AsValue.schema(someArrSchema, json.schema.Version.Draft06(id="some id here"))))*/
//  import json._
//
//  implicit val someStrSchema: json.Schema[String] = Json.schema[String].toDefinition("my-lovely-string")
//
//  implicit val someArrSchema: json.Schema[Array[String]] = Json.schema[Array[String]]
//
//  println(JsonFormatter.format(AsValue.schema(someArrSchema, json.schema.Version.Draft06(id="some id here"))))
//
//
//	// TODO HELP why isn't schemea of string / integer recognized?
//
//
//  /*{
//    "$schema": "http://json-schema.org/draft-04/schema#"
//    ,
//    "type": "array"
//    ,
//    "items": {
//      "$ref": "#/definitions/my-lovely-string"
//    }
//    ,
//    "definitions": {
//      "my-lovely-string": {
//      "type": "string"
//    }
//    }
//  }*/
//}
