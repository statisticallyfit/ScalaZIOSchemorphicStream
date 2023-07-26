package testData

/**
 *
 */
object ScalaEnumData {
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
					  cars: Seq[Car]
				  )
}
