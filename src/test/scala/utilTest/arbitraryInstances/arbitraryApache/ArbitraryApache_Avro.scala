package utilTest.arbitraryInstances.arbitraryApache

import cats.data.NonEmptyList
import cats.syntax.all._
//import cats.implicits._

import org.scalacheck._
import org.scalacheck.cats.implicits._

import utilTest.arbitraryInstances.ArbitraryGeneral._

import scala.jdk.CollectionConverters._


import org.apache.avro.{Schema ⇒ SchemaApacheAvro}


/**
 * Purpose - copying this here from skeuomorph's test folder because:
 * 	1. cannot access skeuomorph's test folder contents.
 *        2. also need to add to these instances by creating instances for Skeuo's types too.
 *
 * NOTE: file source == https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/instances.scala
 */
object ArbitraryApache_Avro {
	
	
	implicit val avroSchemaArbitrary: Arbitrary[SchemaApacheAvro] = Arbitrary {
		val primitives: Gen[SchemaApacheAvro] = Gen.oneOf(
			List(
				SchemaApacheAvro.Type.STRING,
				SchemaApacheAvro.Type.BOOLEAN,
				SchemaApacheAvro.Type.BYTES,
				SchemaApacheAvro.Type.DOUBLE,
				SchemaApacheAvro.Type.FLOAT,
				SchemaApacheAvro.Type.INT,
				SchemaApacheAvro.Type.LONG,
				SchemaApacheAvro.Type.NULL
			).map(SchemaApacheAvro.create)
		)
		
		val arrayOrMap: Gen[SchemaApacheAvro] =
			Gen.oneOf(primitives.map(SchemaApacheAvro.createMap), primitives.map(SchemaApacheAvro.createArray))
		
		val union: Gen[SchemaApacheAvro] =
			Gen.nonEmptyContainerOf[Set, SchemaApacheAvro](primitives).map(l => SchemaApacheAvro.createUnion(l.toList.asJava))
		
		def field(name: String): Gen[SchemaApacheAvro.Field] =
			for {
				schema <- Gen.oneOf(primitives, arrayOrMap, union)
				doc <- nonEmptyString
			} yield new SchemaApacheAvro.Field(name, schema, doc, null.asInstanceOf[Any])
		
		val record: Gen[SchemaApacheAvro] = (
			nonEmptyString,
			nonEmptyString,
			nonEmptyString,
			Gen.nonEmptyContainerOf[Set, String](nonEmptyString).map(_.toList) flatMap { l: List[String] =>
				l.traverse(field)
			}
		).mapN { case (name, doc, namespace, fields) =>
			SchemaApacheAvro.createRecord(name, doc, namespace, false, fields.asJava)
		}
		
		Gen.oneOf(primitives, arrayOrMap, union, record)
	}

}
