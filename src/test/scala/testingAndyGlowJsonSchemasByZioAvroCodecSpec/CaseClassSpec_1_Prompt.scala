package testingAndyGlowJsonSchemasByZioAvroCodecSpec


import com.github.andyglow.json.JsonFormatter
import com.github.andyglow.jsonschema.AsValue
import json.{Json, Schema}

import data.ScalaCaseClassData._

import zio.json.JsonEncoder
import zio.schema.DeriveSchema
import zio.schema.codec.{JsonCodec, AvroCodec}


/**
 *
 */
object CaseClassSpec_1_Prompt extends App {


  // TESTING --- Zio part

  val zioPromptSchema: zio.schema.Schema[Prompt] = DeriveSchema.gen[Prompt]

  val zioPromptAvro: Either[String, String] = AvroCodec.encode(zioPromptSchema)
  val zioPromptAvroStr: String = zioPromptAvro.right.get

  val expectedAvroStr: String =
    """[
      |{"type":"record","name":"Multiple","fields":[{"name":"value","type":{"type":"array","items":"string"}}]},
      |{"type":"record","name":"Single","fields":[{"name":"value","type":"string"}]}
      |]""".stripMargin.replace("\n", "")


  assert(zioPromptAvroStr.equals(expectedAvroStr), "test 1")


  // TESTING ---- Andy glow part

  val andyPromptSchema: json.Schema[Prompt] = Json.schema[Prompt]
  val andyPromptJsonStr: String = JsonFormatter.format(AsValue.schema(andyPromptSchema, json.schema.Version.Draft04()))


  val expectedJsonStr: String =
    """{
      |  "$schema": "http://json-schema.org/draft-04/schema#",
      |  "oneOf": [
      |    {
      |      "additionalProperties": false,
      |      "properties": {
      |        "value": {
      |          "type": "array",
      |          "items": {
      |            "type": "string"
      |          }
      |        }
      |      },
      |      "required": [
      |        "value"
      |      ]
      |    },
      |    {
      |      "additionalProperties": false,
      |      "properties": {
      |        "value": {
      |          "type": "string"
      |        }
      |      },
      |      "required": [
      |        "value"
      |      ]
      |    }
      |  ]
      |}
      |""".stripMargin.trim()


  assert(andyPromptJsonStr.equals(expectedJsonStr), "test 2")

}
