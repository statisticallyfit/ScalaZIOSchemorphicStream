package testUtil.utilZioApache

import testUtil.Util
import zio.schema.{TypeId, Schema ⇒ ZioSchema}
import zio.{Chunk ⇒ ZioChunk}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._


/**
 *
 */
object MakeZioSchemaStructures {


  implicit def makeZioSeqSchema[A](implicit schemaA: ZioSchema[A]): ZioSchema[Seq[A]] =
    ZioSchema.Sequence[Seq[A], A, String](schemaA, _.toSeq, ZioChunk.fromIterable(_), ZioChunk.empty, "Seq")


  implicit def makeEnumCase[R: TypeTag : ClassTag, A: TypeTag : ClassTag](s: ZioSchema[A])
  /*(id: String = Util.inspect[A],
                         schema: ZioSchema[A] = DeriveSchema.gen[A])*/ : ZioSchema.Case[R, A] = {
    //val s: ZioSchema[A] = DeriveSchema.gen[A]

    ZioSchema.Case[R, A](
      id = Util.inspectType[A], // String
      schema = s, // ZioSchema[A]
      unsafeDeconstruct = (r: R) => r.asInstanceOf[A], // R => A
      construct = (a: A) => a.asInstanceOf[R], // A => R
      isCase = (r: R) => r.isInstanceOf[A] // R => Boolean
    )
  }


  implicit def makeEnum2[A1: TypeTag : ClassTag, A2: TypeTag : ClassTag, Z: TypeTag : ClassTag](
                                                                                                 s1: ZioSchema[A1],
                                                                                                 s2: ZioSchema[A2]
                                                                                               )
  /*(id: TypeId = TypeId.parse(Util.inspect[Z]),
                                    case1: ZioSchema.Case[Z, A1] = makeEnumCase[Z, A1],
                                    case2: ZioSchema.Case[Z, A2] = makeEnumCase[Z, A2]
                                   )*/ : ZioSchema.Enum2[A1, A2, Z] = {
    ZioSchema.Enum2[A1, A2, Z](
      id = TypeId.parse(Util.inspectType[Z]), // TypeId
      case1 = makeEnumCase[Z, A1](s1), // ZioSchema.Case[Z, A1]
      case2 = makeEnumCase[Z, A2](s2) //ZioSchema.Case[Z, A2]
    )
  }


  // NOTE this does not simplify things because still have to declare the get0 and set0 functions, cannot do it at meta level....
  /*implicit def makeZioField[R, A: TypeTag]( //name0: String = Util.inspect[A],
                                            schema: ZioSchema[A],
                                            getFunc: R => A,
                                            setFunc: (R, A) => R
                                          ): ZioSchema.Field[R, A] = {

    val name0: String = Util.inspect[A]

    ZioSchema.Field.apply[R, A](
      name0 = name0.head.toString.toLowerCase() ++ name0.tail,
      schema0 = schema,
      get0 = getFunc,
      set0 = setFunc
    )
  }*/
}
