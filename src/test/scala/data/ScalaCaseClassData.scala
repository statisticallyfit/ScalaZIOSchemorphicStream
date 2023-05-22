package data

import zio.schema.{DeriveSchema, Schema}
import zio.schema.annotation.noDiscriminator


/**
 * Source: JsonCodecSpec.scala
 */
object ScalaCaseClassData {

  @noDiscriminator sealed trait Prompt

  object Prompt {
    final case class Single(value: String) extends Prompt

    final case class Multiple(value: List[String]) extends Prompt

    //implicit lazy val schema: Schema[Prompt] = DeriveSchema.gen[Prompt]
  }


  // ------------------------------------------------------------

  sealed trait Gender

  object Gender {

    case object Male extends Gender

    case object Female extends Gender
  }

  case class Company(name: String)

  case class Car(name: String, manufacturer: Company)

  case class Person(
                     firstName: String,
                     middleName: Option[String],
                     lastName: String,
                     gender: Gender,
                     birthDay: java.time.LocalDateTime,
                     company: Company,
                     cars: Seq[Car])
}
