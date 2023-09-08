package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs

import cats.implicits._
import higherkindness.droste._
import higherkindness.droste.data.Mu._
import higherkindness.droste.data._
import higherkindness.skeuomorph.avro.AvroF.fromAvro
import higherkindness.skeuomorph.mu.Transform.transformAvro
import higherkindness.skeuomorph.mu.{MuF, codegen}
import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import utilMain.UtilMain

import scala.meta._


/**
 *
 */
object SkeuomorphExample extends App {


	val definition =
		"""
		  |{
		  |  "type": "enum",
		  |  "name": "Colors",
		  |  "symbols": [
		  |    "Red",
		  |    "Orange", "Pink", "Yellow", "Green", "Blue", "Indigo", "Violet"
		  |  ]
		  |}
		  |""".stripMargin
		/*"""
		  |{
		  |  "type": "record",
		  |  "name": "RawCityMeshMeasurementsSchema",
		  |  "fields": [
		  |    {
		  |      "name": "data",
		  |      "type": {
		  |        "type": "record",
		  |        "name": "Data",
		  |        "fields": [
		  |          {
		  |            "name": "overview",
		  |            "type": {
		  |              "type": "record",
		  |              "name": "Overview",
		  |              "fields": [
		  |                {
		  |                  "name": "visitorTypeAmount",
		  |                  "type": {
		  |                    "type": "record",
		  |                    "name": "VisitorTypeAmount",
		  |                    "fields": [
		  |                      {
		  |                        "name": "uniqueVisitorAmount",
		  |                        "type": "int"
		  |                      }
		  |                    ]
		  |                  }
		  |                }
		  |              ]
		  |            }
		  |          }
		  |        ]
		  |      }
		  |    }
		  |  ]
		  |}""".stripMargin*/

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


	println(UtilMain.cleanScalaTypeNames(res.getOrElse("")))


	// HELP result is just:
	/**
	 * Mu(TProduct(RawCityMeshMeasurementsSchema,None,List(Field(data,Mu(TProduct(Data,None,List(Field(overview,Mu(TProduct(Overview,None,List(Field(visitorTypeAmount,Mu(TProduct(VisitorTypeAmount,None,List(Field(uniqueVisitorAmount,Mu(TSimpleInt(_32)),None)),List(),List())),None)),List(),List())),None)),List(),List())),None)),List(),List()))
	 * =====
	 * final case class RawCityMeshMeasurementsSchema(data: Data)
	 *
	 *
	 * HELP so that means it doesn't give the entire class nesting, so I DON'T have a way to convert avro schema -> case class.
	 */

}
