package testingSkeuomorphAvroConversions


import org.apache.avro.{Schema ⇒ ApacheAvroSchema}


import higherkindness.skeuomorph.avro.{AvroF ⇒ SkeuomorphAvroSchema}
import higherkindness.skeuomorph.avro.AvroF.fromAvro

import scala.jdk.CollectionConverters._
//import scala.collection.JavaConverters._

/**
 *
 */
object Convert_ApacheAvroSchema_to_SkeuomorphAvroSchema extends App {



  val apacheEnumSchema: ApacheAvroSchema = ApacheAvroSchema.createEnum("Color",
    "color doc",
    "color namespace",
    Seq("red", "yellow", "blue", "green", "purple", "white", "black", "pink", "orange").asJava
  )
  //println(s"fields = ${apacheEnumSchema.getFields}")
  //println(s"value type = ${apacheEnumSchema.getValueType}")
  println(s"element type = ${apacheEnumSchema.getElementType}")
  println(s"type = ${apacheEnumSchema.getType}")

  println(s"enum schema = $apacheEnumSchema")


  val apacheStringSchema = ApacheAvroSchema.create(ApacheAvroSchema.Type.STRING)

  println(s"string schema = $apacheStringSchema")


  println("Converting to skeuomorph avro schema")


  val skSchema = SkeuomorphAvroSchema.fromAvro(apacheStringSchema)
  // TODO how to get the avroF?
  println(s"string = $skSchema")

  //println(s"enum = ${SkeuomorphAvroSchema.fromAvro(apacheEnumSchema)}")


  //println(s"to json = ${SkeuomorphAvroSchema.toJson(skSchema)}")
}
