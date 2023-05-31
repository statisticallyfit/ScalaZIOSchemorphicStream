package testingSkeuomorphAvroConversions



/**
 *
 */
//object Convert_ApacheAvroSchema_to_SkeuomorphAvroSchema extends App {
//
//
//
//  val apacheEnumSchema: ApacheAvroSchema = ApacheAvroSchema.createEnum("Color",
//    "color doc",
//    "color namespace",
//    Seq("red", "yellow", "blue", "green", "purple", "white", "black", "pink", "orange").asJava
//  )
//  io.circe.Json
//  println(s"enum schema = $apacheEnumSchema")
//
//
//  val apacheStringSchema = ApacheAvroSchema.create(ApacheAvroSchema.Type.STRING)
//
//  println(s"string schema = $apacheStringSchema")
//
//
//  println("Converting to skeuomorph avro schema")
//
//
//  val skSchema = SkeuomorphAvroSchema.fromAvro(apacheStringSchema)
//  // TODO how to get the avroF?
//  println(s"string = $skSchema")
//
//  //println(s"enum = ${SkeuomorphAvroSchema.fromAvro(apacheEnumSchema)}")
//
//
//  //println(s"to json = ${SkeuomorphAvroSchema.toJson(skSchema)}")
//}



/**
 * NOTE: file source == https://github.com/higherkindness/skeuomorph/blob/main/src/test/scala/higherkindness/skeuomorph/avro/AvroSchemaSpec.scala#L35-L42
 */

import org.scalacheck.{cats ⇒ scats}
import org.scalacheck._

import org.specs2._
class Convert_ApacheAvroSchema_to_SkeuomorphAvroSchema extends Specification with ScalaCheck {


  import org.apache.avro.{Schema ⇒ ApacheAvroSchema}


  import higherkindness.skeuomorph.avro.{AvroF ⇒ SkeuomorphAvroSchema}
  import higherkindness.skeuomorph.avro.AvroF.fromAvro

  import higherkindness.droste._
  import testingSkeuomorphAvroConversions.instances._
  //import org.apache.avro.ApacheAvroSchema

  import scala.jdk.CollectionConverters._


  def is =
    s2"""
    Avro ApacheAvroSchema

    It should be possible to create a ApacheAvroSchema from org.apache.avro.ApacheAvroSchema. $convertSchema
    """



  def checkSchema(sch: ApacheAvroSchema): Algebra[SkeuomorphAvroSchema, Boolean] =
    Algebra {
      case SkeuomorphAvroSchema.TNull() => sch.getType should_== ApacheAvroSchema.Type.NULL
      case SkeuomorphAvroSchema.TBoolean() => sch.getType should_== ApacheAvroSchema.Type.BOOLEAN
      case SkeuomorphAvroSchema.TInt() => sch.getType should_== ApacheAvroSchema.Type.INT
      case SkeuomorphAvroSchema.TLong() => sch.getType should_== ApacheAvroSchema.Type.LONG
      case SkeuomorphAvroSchema.TFloat() => sch.getType should_== ApacheAvroSchema.Type.FLOAT
      case SkeuomorphAvroSchema.TDouble() => sch.getType should_== ApacheAvroSchema.Type.DOUBLE
      case SkeuomorphAvroSchema.TBytes() => sch.getType should_== ApacheAvroSchema.Type.BYTES
      case SkeuomorphAvroSchema.TString() => sch.getType should_== ApacheAvroSchema.Type.STRING

      case SkeuomorphAvroSchema.TNamedType(_, _) => false
      case SkeuomorphAvroSchema.TArray(_) => sch.getType should_== ApacheAvroSchema.Type.ARRAY
      case SkeuomorphAvroSchema.TMap(_) => sch.getType should_== ApacheAvroSchema.Type.MAP
      case SkeuomorphAvroSchema.TRecord(name, namespace, _, doc, fields) =>
        (sch.getName should_== name)
          .and(sch.getNamespace should_== namespace.getOrElse(""))
          .and(sch.getDoc should_== doc.getOrElse(""))
          .and(
            sch.getFields.asScala.toList.map(f => (f.name, f.doc)) should_== fields
              .map(f => (f.name, f.doc.getOrElse("")))
          )

      case SkeuomorphAvroSchema.TEnum(_, _, _, _, _) => true
      case SkeuomorphAvroSchema.TUnion(_) => true
      case SkeuomorphAvroSchema.TFixed(_, _, _, _) => true
      case sch => throw new IllegalArgumentException(s"ApacheAvroSchema ${sch} not handled")
    }

  def getSchema(sch: ApacheAvroSchema): Algebra[SkeuomorphAvroSchema, ApacheAvroSchema] =
    Algebra { // F[B] --> B === AvroF[Schema] -> Schema

      case SkeuomorphAvroSchema.TNull() => ApacheAvroSchema.create(sch.getType) // sch.getType // should_== ApacheAvroSchema.Type.NULL
      case SkeuomorphAvroSchema.TBoolean() => ApacheAvroSchema.create(sch.getType)//sch.getType // should_== ApacheAvroSchema.Type.BOOLEAN
      case SkeuomorphAvroSchema.TInt() => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.INT
      case SkeuomorphAvroSchema.TLong() => ApacheAvroSchema.create(sch.getType)//should_== ApacheAvroSchema.Type.LONG
      case SkeuomorphAvroSchema.TFloat() => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.FLOAT
      case SkeuomorphAvroSchema.TDouble() => ApacheAvroSchema.create(sch.getType)//should_== ApacheAvroSchema.Type.DOUBLE
      case SkeuomorphAvroSchema.TBytes() => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.BYTES
      case SkeuomorphAvroSchema.TString() => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.STRING

      case SkeuomorphAvroSchema.TNamedType(_, _) => ApacheAvroSchema.create(sch.getType)//false
      case SkeuomorphAvroSchema.TArray(_) => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.ARRAY
      case SkeuomorphAvroSchema.TMap(_) => ApacheAvroSchema.create(sch.getType) //should_== ApacheAvroSchema.Type.MAP
      case SkeuomorphAvroSchema.TRecord(name, namespace, _, doc, fields) => ApacheAvroSchema.create(sch.getType)
        /*(sch.getName should_== name)
          .and(sch.getNamespace should_== namespace.getOrElse(""))
          .and(sch.getDoc should_== doc.getOrElse(""))
          .and(
            sch.getFields.asScala.toList.map(f => (f.name, f.doc)) should_== fields
              .map(f => (f.name, f.doc.getOrElse("")))
          )*/

      case SkeuomorphAvroSchema.TEnum(_, _, _, _, _) => ApacheAvroSchema.create(sch.getType)//true
      case SkeuomorphAvroSchema.TUnion(_) => ApacheAvroSchema.create(sch.getType)//true
      case SkeuomorphAvroSchema.TFixed(_, _, _, _) => ApacheAvroSchema.create(sch.getType)//true
      case sch => throw new IllegalArgumentException(s"ApacheAvroSchema ${sch} not handled")
    }

  def myConvertSchema: ApacheAvroSchema ⇒ SkeuomorphAvroSchema = (schema: ApacheAvroSchema) ⇒ {
    scheme.hylo(getSchema(schema), SkeuomorphAvroSchema.fromAvro)
  }


  def convertSchema: Prop = {
    Prop.forAll { (schema: ApacheAvroSchema) =>
      val test: ApacheAvroSchema ⇒ Boolean = scheme.hylo(checkSchema(schema), SkeuomorphAvroSchema.fromAvro)

      test(schema)
    }
    // fromAvro: Colalgebra[F[_], A] = A --> F[A]
    // ---> fromAvro: Coalgebra[ApacheAvroSchema, ApacheAvroSchema] = ApacheAvroSchema ---> SkeuomorphAvroSchema[ApacheAvroSchema]

    // checkSchema: Algebra[F[_], B]] = F[B] --> B
    // ----> checkSchema: Algebra[SkeuomorphAvroSchema, Boolean] = SkeuomorphAvroSchema[Boolean] --> Boolean

    // hylo = ana . cata: (A --> R) . (R --> B) = A --> B
    // ---> hylo = checkSchema . fromAvro
  }

}
/*

object temp {

  import cats._
  import cats.Functor


  import cats.implicits._


  type Algebra[F[_], A] = F[A] => A
  type Coalgebra[F[_], A] = A => F[A]

  def cata1[F[_] : Functor, S, B](algebra: Algebra[F, B])(project: Coalgebra[F, S]): S => B =
    new(S => B) {
      kernel: (S ⇒ B) =>
      def apply(input: S): B =
        algebra(project(input).fmap(s ⇒ kernel(s)))
    }

  def cata2[F[_] : Functor, S, B](f: F[B] => B)(project: S => F[S]): S => B =
    new(S => B) {
      kernel: (S => B) =>
      def apply(init: S): B = f(project(init).fmap(kernel))
    }
}*/
