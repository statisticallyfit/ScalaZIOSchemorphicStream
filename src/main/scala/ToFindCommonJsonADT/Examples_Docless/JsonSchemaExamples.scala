package ToFindCommonJsonADT.Examples_Docless


import com.timeout.docless.schema._
//import com.timeout.docless.schema.Primitives
/**
 * Sources
 * https://github.com/timeoutdigital/docless/tree/master
 * https://github.com/timeoutdigital/docless/blob/master/src/main/scala/com/timeout/docless/schema/Primitives.scala
 * https://github.com/timeoutdigital/docless/blob/master/src/test/scala/com/timeout/docless/schema/JsonSchemaTest.scala#L212
 */
object JsonSchemaExamples extends App {


	/**
	 * HELP - why cannot call Primitives?
	 */


	/**
	 * NOTE: Tutorial examples = https://github.com/timeoutdigital/docless/tree/master
	 */


	// Example: Pet Store
	case class Pet(id: Int, name: String, tag: Option[String])

	val petSchema: JsonSchema[Pet] = JsonSchema.deriveFor[Pet]

	println(s"pet schema = $petSchema")
	//println(s"petSchema.definition = ${petSchema.definition}")


	// HELP why errors when calling these methods?
	//println(s"petSchema.NamedDefinition(fieldName = \"field name\") = ${petSchema.NamedDefinition(fieldName = "field name")}")
	//println(s"petSchema.fieldDefinitions = ${petSchema.fieldDefinitions}")
	//println(s"petSchema.relatedDefinitions = ${petSchema.relatedDefinitions.map((d: JsonSchema.Definition) => d.id)}")
	//println(s"petSchema.definitions = ${petSchema.definitions}")

	println(s"petSchema.asJson = ${petSchema.asJson}")


	// Example: Email, Phone

	import com.timeout.docless.schema.derive.{Config, Combinator}

	sealed trait Contact

	case class EmailAndPhoneNum(email: String, phoneNum: String) extends Contact

	case class EmailOnly(email: String) extends Contact

	case class PhoneOnly(phoneNum: String) extends Contact

	object Contact {
		implicit val conf: Config = Config(Combinator.OneOf)
		val schema = JsonSchema.deriveFor[Contact]
	}

	println(s"Contact.schema.asJson = ${Contact.schema.asJson}")
	//println(s"Contact.schema.relatedDefinitions.map(_.id) = ${Contact.schema.relatedDefinitions.map(_.id)}")
}
