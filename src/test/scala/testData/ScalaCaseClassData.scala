package testData

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
	
	
	type FirstName = String
	type MiddleName = Option[String]
	type LastName = String
	type BirthDay = java.time.LocalDateTime
	
	case class Person(
					  firstName: FirstName,
					  middleName: MiddleName,
					  lastName: LastName,
					  gender: Gender,
					  birthday: BirthDay,
					  company: Company,
					  cars: Seq[Car]
				  )
	
	
	
	
	
	// -----------------------------------------------------------------
	
	case class Basket[T](item: T)
	
	abstract class Fruit
	abstract class Citrus extends Fruit
	case class Apple() extends Fruit
	case class Banana() extends Fruit
	case class Tangelo() extends Citrus
	case class Grapefruit() extends Citrus
}
