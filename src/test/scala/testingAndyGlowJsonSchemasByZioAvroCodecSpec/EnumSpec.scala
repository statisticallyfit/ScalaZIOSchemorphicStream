package testingAndyGlowJsonSchemasByZioAvroCodecSpec

import com.github.andyglow.json.JsonFormatter
import com.github.andyglow.jsonschema.AsValue
import json.{Json, Schema}

import data.ScalaEnumData._

import zio.json.JsonEncoder
import zio.schema.DeriveSchema
import zio.schema.codec.{JsonCodec, AvroCodec}


/**
 * //Source = https://github.com/andyglow/scala-jsonschema#enumerations
 */
object EnumSpec extends App {

  // TODO - avrocodec spec "union with nesting"


  // zio derivations - avro only
  val zioEnumSchema: zio.schema.Schema[Gender] = DeriveSchema.gen[Gender]

  // NOTE - useless, won't work
  val zioEnumJsonStr: JsonEncoder[Gender] = JsonCodec.jsonEncoder(zioEnumSchema)

  val zioEnumAvro: Either[String, String] = AvroCodec.encode(zioEnumSchema)
  val zioEnumAvroStr: String = zioEnumAvro.right.get



  // TODO male comes first in the declaration (data) so why does female come first in the result from zio?
  val expectedAvroStr: String = """[{"type":"record","name":"Female","fields":[]},{"type":"record","name":"Male","fields":[]}]""".stripMargin.trim()
 // """[{"type":"record","name":"Male","fields":[]},{"type":"record","name":"Female","fields":[]}]""" //.stripMargin.trim()

  /*println(s"zioenum avro str\n ${zioEnumAvroStr}")
  println("expected\n" + expectedAvroStr)
  println(zioEnumAvroStr.length, expectedAvroStr.length)
  println(s"diff = ${zioEnumAvroStr.diff(expectedAvroStr)}")
  println(s"diff length = ${zioEnumAvroStr.diff(expectedAvroStr).length}")*/

  //println(zioEnumAvroStr == expectedAvroStr)
  assert(zioEnumAvroStr.equals(expectedAvroStr))




  // Andy glow derivations - json only
  val andyEnumSchema: json.Schema[Gender] = Json.schema[Gender]

  val andyEnumJsonStr: String = JsonFormatter.format(AsValue.schema(andyEnumSchema, json.schema.Version.Draft04())) //.Draft06(id="some id here")))


  val expectedJsonStr: String =
    """
      |{
      |  "$schema": "http://json-schema.org/draft-04/schema#",
      |  "type": "string",
      |  "enum": [
      |    "Female",
      |    "Male"
      |  ]
      |}
      |""".stripMargin.trim()



  /*println(andyEnumJsonStr)
  println(expectedJsonStr)
  println(andyEnumJsonStr.diff(expectedJsonStr))*/
  assert(andyEnumJsonStr.equals(expectedJsonStr))
}
