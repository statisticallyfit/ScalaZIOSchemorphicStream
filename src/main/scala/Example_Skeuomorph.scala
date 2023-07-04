

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache, Protocol => AvroProtocol, _}

import higherkindness.skeuomorph.mu.Transform.transformAvro
import higherkindness.skeuomorph.mu.MuF
import higherkindness.skeuomorph.mu.codegen
import higherkindness.skeuomorph.avro.AvroF.fromAvro
import higherkindness.droste._
import higherkindness.droste.data._
import higherkindness.droste.data.Mu._

import cats.implicits._

import scala.meta._




object MiniUtil {
  def cleanScalaTypeNames(str: String): String = {
    str.replace("_root_.java.lang.", "")
      .replace("_root_.scala.", "")
      .replace("_root_.", "")
  }
}

import MiniUtil._ // TODO find way to share between test and main folders


/**
 *
 */
object SkeuomorphExample extends App {



  val definition =
    """
     {
       "namespace": "example.avro",
       "type": "record",
       "name": "User",
       "fields": [
         {
           "name": "name",
           "type": "string"
         },
         {
           "name": "favorite_number",
           "type": [
             "int",
             "null"
           ]
         },
         {
           "name": "favorite_color",
           "type": [
             "string",
             "null"
           ]
         }
       ]
     }
     """

  val avroSchema: SchemaAvro_Apache = new SchemaAvro_Apache.Parser().parse(definition)

  val toMuSchema: SchemaAvro_Apache => Mu[MuF] =
    scheme.hylo(transformAvro[Mu[MuF]].algebra, fromAvro)

  val printSchemaAsScala: Mu[MuF] => Either[String, String] =
    codegen.schema(_).map(_.syntax)

  (toMuSchema >>> println)(avroSchema)
  println("=====")
  //(toMuSchema >>> printSchemaAsScala >>> println)(avroSchema)
  //val res: SchemaAvro_Apache => Either[String, String] = toMuSchema >>> printSchemaAsScala
  val res: Either[String, String] = (toMuSchema >>> printSchemaAsScala)(avroSchema)


  println(cleanScalaTypeNames(res.getOrElse("")))

}
