package utilMain.utilAvro.utilZioApache

import utilMain.UtilMain
import zio.schema.{TypeId, Schema ⇒ SchemaZIO}
import zio.{Chunk ⇒ ChunkZIO}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._


/**
 *
 */
object MakeSchemaZIOStructures {


  implicit def makeZioSeqSchema[A](implicit schemaA: SchemaZIO[A]): SchemaZIO[Seq[A]] =
    SchemaZIO.Sequence[Seq[A], A, String](schemaA, _.toSeq, ChunkZIO.fromIterable(_), ChunkZIO.empty, "Seq")


  implicit def makeEnumCase[R: TypeTag : ClassTag, A: TypeTag : ClassTag](s: SchemaZIO[A])
  /*(id: String = Util.inspect[A],
                         schema: SchemaZIO[A] = DeriveSchema.gen[A])*/ : SchemaZIO.Case[R, A] = {
    //val s: SchemaZIO[A] = DeriveSchema.gen[A]

    SchemaZIO.Case[R, A](
      id = UtilMain.inspectType[A], // String
      schema = s, // SchemaZIO[A]
      unsafeDeconstruct = (r: R) => r.asInstanceOf[A], // R => A
      construct = (a: A) => a.asInstanceOf[R], // A => R
      isCase = (r: R) => r.isInstanceOf[A] // R => Boolean
    )
  }


  implicit def makeEnum2[A1: TypeTag : ClassTag, A2: TypeTag : ClassTag, Z: TypeTag : ClassTag](
                                                                                                 s1: SchemaZIO[A1],
                                                                                                 s2: SchemaZIO[A2]
                                                                                               )
  /*(id: TypeId = TypeId.parse(Util.inspect[Z]),
                                    case1: SchemaZIO.Case[Z, A1] = makeEnumCase[Z, A1],
                                    case2: SchemaZIO.Case[Z, A2] = makeEnumCase[Z, A2]
                                   )*/ : SchemaZIO.Enum2[A1, A2, Z] = {
    SchemaZIO.Enum2[A1, A2, Z](
      id = TypeId.parse(UtilMain.inspectType[Z]), // TypeId
      case1 = makeEnumCase[Z, A1](s1), // SchemaZIO.Case[Z, A1]
      case2 = makeEnumCase[Z, A2](s2) //SchemaZIO.Case[Z, A2]
    )
  }


  // NOTE this does not simplify things because still have to declare the get0 and set0 functions, cannot do it at meta level....
  /*implicit def makeZioField[R, A: TypeTag]( //name0: String = Util.inspect[A],
                                            schema: SchemaZIO[A],
                                            getFunc: R => A,
                                            setFunc: (R, A) => R
                                          ): SchemaZIO.Field[R, A] = {

    val name0: String = Util.inspect[A]

    SchemaZIO.Field.apply[R, A](
      name0 = name0.head.toString.toLowerCase() ++ name0.tail,
      schema0 = schema,
      get0 = getFunc,
      set0 = setFunc
    )
  }*/
}
