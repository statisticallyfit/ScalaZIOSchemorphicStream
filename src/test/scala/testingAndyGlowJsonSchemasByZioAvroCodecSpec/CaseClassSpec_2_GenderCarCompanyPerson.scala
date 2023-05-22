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
object CaseClassSpec_2_GenderCarCompanyPerson extends App {


  // Andy glow part
  // TODO why say implicit lazy val instead of just val? https://github.com/andyglow/scala-jsonschema#regular
  implicit val genderSchema: json.Schema[Gender] = Json.schema[Gender]

  implicit val companySchema: json.Schema[Company] = Json.schema[Company]

  implicit val carSchema: json.Schema[Car] = Json.schema[Car]

  implicit val personSchema: json.Schema[Person] = Json.schema[Person]

}
