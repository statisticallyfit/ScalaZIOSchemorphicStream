package testingAndyGlowJsonSchemasByZioAvroCodecSpec


import data.ScalaCaseClassData._

import util.{Util, UtilSchema}


/**
 *
 */

object PersonType {
  object implicits {

    import zio.schema.{StandardType => ZioStandardType}
    import zio.schema.{DeriveSchema, TypeId, Schema => ZioSchema}

    val firstNameSchema: ZioSchema[FirstName] = ZioSchema.primitive[FirstName]
    val middleNameSchema: ZioSchema[MiddleName] = ZioSchema.Optional(ZioSchema.primitive(ZioStandardType.StringType)) // source: line 339 AvroCodecSpec
    val lastNameSchema: ZioSchema[LastName] = ZioSchema.primitive[LastName]
    val birthdaySchema: ZioSchema[BirthDay] = ZioSchema.primitive[BirthDay]
    implicit val genderSchema: ZioSchema[Gender] = DeriveSchema.gen[Gender]
    implicit val companySchema: ZioSchema[Company] = DeriveSchema.gen[Company]
    implicit val carSchema: ZioSchema[Car] = DeriveSchema.gen[Car]
  }
}


object CaseClassSpec_2_GenderCarCompanyPerson extends App {

  object ZIOPersonSchemaDependencies {

    import zio.schema.{DeriveSchema, TypeId, Schema => ZioSchema}


    /*UtilSchema.makeEnum2[Gender.Male.type, Gender.Female.type, Gender](
      s1 = DeriveSchema.gen[Gender.Male.type],
      s2 = DeriveSchema.gen[Gender.Female.type]
    )*/
    // TODO why get error, why cannot have derive schema internally via the type?????

    /*implicit val genderSchema: ZioSchema[Gender] = {
      ZioSchema.Enum2[Gender.Male.type, Gender.Female.type, Gender](

        id = TypeId.parse("data.ScalaCaseClassData.Gender"),

        case1 = ZioSchema.Case[Gender, Gender.Male.type](
          id = "Male", //TODO check : Gender.Male.type.toString
          schema = DeriveSchema.gen[Gender.Male.type],
          // Gender => Male.type
          unsafeDeconstruct = (gender: Gender) => gender.asInstanceOf[Gender.Male.type],
          // Male.type => Gender
          construct = (male: Gender.Male.type) => male.asInstanceOf[Gender],
          // Gender => Boolean
          isCase = (gender: Gender) => gender.isInstanceOf[Gender.Male.type]
        ),

        case2 = ZioSchema.Case[Gender, Gender.Female.type](
          id = "Female",
          schema = DeriveSchema.gen[Gender.Female.type],
          // Gender => Female.type
          unsafeDeconstruct = (gender: Gender) => gender.asInstanceOf[Gender.Female.type],
          // Female.type => Gender
          construct = (female: Gender.Female.type) => female.asInstanceOf[Gender],
          // Gender => Boolean
          isCase = (gender: Gender) => gender.isInstanceOf[Gender.Female.type]
        )
      )

    }*/

    //implicit val zioPersonSchema_separated: ZioSchema[Person] = DeriveSchema.gen[Person]

    // Constructing the zioperson schema the manual way since gives error when using the .gen way:
    //val firstNameSchema: ZioSchema[String] = DeriveSchema.gen[FirstName]


    /**
     * NOTE: reason for referring to this: userSchema here shows how to derive schemas / fields for primitive types
     *
     * Source = zio's Example 6 Reified Optics
     */
    /*
    implicit val userSchema: Schema.CaseClass2[String, Int, User] = Schema.CaseClass2[String, Int, User](
      TypeId.parse("dev.zio.schema.example.example6.Domain.User"),
      field01 = Field("name", Schema.primitive[String], get0 = _.name, set0 = (p, v) => p.copy(name = v)),
      field02 = Field("age", Schema.primitive[Int], get0 = _.age, set0 = (p, v) => p.copy(age = v)),
      construct0 = (name, years) => User(name, years)
    )
    */

  }




  // TESTING -- ZIO way 1 (separated)

  def ZioWay1_Separate = {


    import zio.schema.{TypeId, Schema => ZioSchema}
    import zio.schema.codec.{AvroCodec, JsonCodec}
    import ZIOPersonSchemaDependencies._
    import PersonType.implicits._

    implicit val personSchema: ZioSchema[Person] = ZioSchema.CaseClass7[FirstName, MiddleName, LastName, Gender, BirthDay, Company, Seq[Car], Person](

      id0 = TypeId.parse("data.ScalaCaseClassData.Person"),

      field01 = ZioSchema.Field.apply[Person, FirstName](
        name0 = "firstName",
        schema0 = firstNameSchema,
        //get0: R => A ---- Person => FirstName
        get0 = (person: Person) => person.firstName,
        //set0: (R, A) => R ----> (Person, FirstName) => Person
        set0 = (person: Person, setFirstName: FirstName) => person.copy(firstName = setFirstName)
      ),
      /*makeZioField[Person, FirstName](schema = firstNameSchema,
        getFunc = _.firstName,
        setFunc = (person: Person, setFirstName: FirstName) => person.copy(firstName = setFirstName)
      )*/

      field02 = ZioSchema.Field.apply[Person, MiddleName]( // [R, A]
        name0 = "middleName",
        schema0 = middleNameSchema,
        //get0: R => A
        get0 = (person: Person) => person.middleName,
        // set0: (R, A) => R
        set0 = (person: Person, setMiddleName: MiddleName) => person.copy(middleName = setMiddleName)
      ),

      field03 = ZioSchema.Field.apply[Person, LastName]( // [R, A]
        name0 = "lastName",
        schema0 = lastNameSchema,
        //get0: R => A
        get0 = (person: Person) => person.lastName,
        // set0: (R, A) => R
        set0 = (person: Person, setLastName: LastName) => person.copy(lastName = setLastName)
      ),

      field04 = ZioSchema.Field.apply[Person, Gender]( // [R, A]
        name0 = "gender",
        schema0 = genderSchema,
        // get0: R => A
        get0 = (person: Person) => person.gender,
        // set0: (R, A) => R
        set0 = (person: Person, setGender: Gender) => person.copy(gender = setGender)
      ),

      field05 = ZioSchema.Field.apply[Person, BirthDay]( //[R, A]
        name0 = "birthday",
        schema0 = birthdaySchema,
        get0 = (person: Person) => person.birthday, // R => A
        set0 = (person: Person, setBirthDay: BirthDay) => person.copy(birthday = setBirthDay) //(R, A) => R
      ),

      /*field06 = */ZioSchema.Field.apply[Person, Company]( //[R, A]
        "company",
        companySchema,
        get0 = (p: Person) => p.company, // R => A
        set0 = (p: Person, setNewCompany: Company) => p.copy(company = setNewCompany) //(R, A) => R
      ),

      field07 = ZioSchema.Field.apply[Person, Seq[Car]]( //[R, A] so A = Seq[Car]
        name0 = "cars",
        //schema0 = ZioSchema.list(zioCarSchema_separated).toSeq,
        // NOTE: writing  a "Schema.seq" similar to how zio does Schema.list (from Schema.scala, line 302
        schema0 = UtilSchema.makeZioSeqSchema[Car](carSchema),
        get0 = (p: Person) => p.cars, // R => A
        set0 = (p: Person, setNewCars: Seq[Car]) => p.copy(cars = setNewCars) // (R, A) => R
      ),

      construct0 = (firstName, middleName, lastName, gender, birthday, company, cars) => Person(firstName, middleName, lastName, gender, birthday, company, cars) //(A1, A2, A3, A4, A5, A6, A7) => Z
    )

    /**
     * NOTE: schemaCustomer has PaymentMethod trait in parameter type list (like person schema has Gender trait in type parameter list)
     * Source = from zio's Example 1
     */
    /*val schemaCustomer: Schema[Customer] = Schema.CaseClass2[Person, PaymentMethod, Customer](
      TypeId.parse("dev.Zioexample.example1.Domain.Customer"),
      field01 =
        Schema.Field[Customer, Person]("person", schemaPerson, get0 = _.person, set0 = (p, v) => p.copy(person = v)),
      field02 = Schema.Field[Customer, PaymentMethod](
        "paymentMethod",
        schemaPaymentMethod,
        get0 = _.paymentMethod,
        set0 = (p, v) => p.copy(paymentMethod = v)
      ),
      construct0 = (person, paymentMethod) => Customer(person, paymentMethod)
    )*/

    /**
     * NOTE; inspiration reason: companySchema has list of user address objects in type parameter list, like person has list of cars in type parameter list.
     * source: from zio Example 6 Reified Optics
     */
    /*implicit val companySchema: CaseClass2[User, List[UserAddress], Company] =
      Schema.CaseClass2[User, List[UserAddress], Company](
        TypeId.parse("dev.Zioexample.example6.Domain.Company"),
        field01 = Field("boss", userSchema, get0 = _.boss, set0 = (p, v) => p.copy(boss = v)),
        field02 =
          Field("employees", Schema.list(userAddressSchema), get0 = _.employees, set0 = (p, v) => p.copy(employees = v)),
        construct0 = (boss, employees) => Company(boss, employees)
      )*/


    // Getting the avro strings
    val genderAvro: Either[String, String] = AvroCodec.encode(genderSchema)
    val genderAvro_str: String = genderAvro.right.get

    val carAvro: Either[String, String] = AvroCodec.encode(carSchema)
    val carAvro_str: String = carAvro.right.get

    val companyAvro: Either[String, String] = AvroCodec.encode(companySchema)
    val companyAvro_str: String = companyAvro.right.get

    val personAvro: Either[String, String] = AvroCodec.encode(personSchema)
    val personAvro_str: String = personAvro.right.get

    val expectedAvro_str: String =
      """
        |
        |""".stripMargin

    println("ZIO WAY 1 (separated):\n")
    println(s"genderSchema = $genderSchema")
    println(s"companySchema = $companySchema")
    println(s"carSchema = $carSchema")
    println(s"personSchema = $personSchema")
    println()
    println(s"zioGenderAvroStr_separated = \n$genderAvro_str")
    println(s"zioCompanyAvroStr_separated = \n$companyAvro_str")
    println(s"zioCarAvroStr_separated = \n$carAvro_str")
    println(s"zioPersonAvroStr_separated = \n$personAvro_str")
    //assert(zioPersonAvroStr_inlined.equals(expectedPersonAvroStr_inlined), "test: zio 1, inlined")
  }



  // TESTING -- ZIO way 2 (inlined)

  /*def ZioWay2_Inlined  = {

    //  HELP ERROR:  Failed to derive schema for Seq[data.ScalaCaseClassData.Car]. Can only derive Schema for case class or sealed trait
    //    val personSchema: ZioSchema[Person] = DeriveSchema.gen[Person]

    import PersonSchemaDependencies._

    val personSchema: ZioSchema[Person] = DeriveSchema.gen[Person]

    val personAvro: Either[String, String] = AvroCodec.encode(personSchema)
    val personAvro_str: String = personAvro.right.get

    val expectedAvro_str: String =
      """""".stripMargin.replace("\n", "")

    println("\nZIO WAY 2 (inlined)")

    println(s"personSchema = $personSchema")
    println(s"zioPersonAvroStr_inlined = $personAvro_str")
    //assert(zioPersonAvroStr_separated.equals(expectedPersonAvroStr_separated), "test: zio 2, separated")
  }*/


  // TESTING --- Andy glow way 1 (inlined) ------------------------------------------------------------------

  def AndyGlowWay1_Inlined = {


    import com.github.andyglow.json.JsonFormatter
    import com.github.andyglow.jsonschema.AsValue
    import json._
    import json.{Json, Schema => AndyGlowSchema}


    // source = https://github.com/andyglow/scala-jsonschema#in-lined
    val personSchema: AndyGlowSchema[Person] = Json.schema[Person]

    val personJson_str: String = JsonFormatter.format(
      AsValue.schema(personSchema, json.schema.Version.Draft04())
    )

    println("\nANDY GLOW WAY 1 (INLINED)")
    println(s"personSchema = $personSchema")
    println(s"personJson_str = $personJson_str")

    val expectedJson_str: String =
      """
        |{
        |  "$schema": "http://json-schema.org/draft-04/schema#",
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "middleName": {
        |      "type": "string"
        |    },
        |    "cars": {
        |      "type": "array",
        |      "items": {
        |        "type": "object",
        |        "additionalProperties": false,
        |        "properties": {
        |          "name": {
        |            "type": "string"
        |          },
        |          "manufacturer": {
        |            "type": "object",
        |            "additionalProperties": false,
        |            "properties": {
        |              "name": {
        |                "type": "string"
        |              }
        |            },
        |            "required": [
        |              "name"
        |            ]
        |          }
        |        },
        |        "required": [
        |          "name",
        |          "manufacturer"
        |        ]
        |      }
        |    },
        |    "company": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "name": {
        |          "type": "string"
        |        }
        |      },
        |      "required": [
        |        "name"
        |      ]
        |    },
        |    "lastName": {
        |      "type": "string"
        |    },
        |    "firstName": {
        |      "type": "string"
        |    },
        |    "birthDay": {
        |      "type": "string",
        |      "format": "date-time"
        |    },
        |    "gender": {
        |      "type": "string",
        |      "enum": [
        |        "Male",
        |        "Female"
        |      ]
        |    }
        |  },
        |  "required": [
        |    "company",
        |    "lastName",
        |    "birthDay",
        |    "gender",
        |    "firstName",
        |    "cars"
        |  ]
        |}""".stripMargin.trim()
  }


  // TESTING --- Andy glow way 2 (regular way, or the way of separated definitions)
  def AndyGlowWay2_Separate = {

    import com.github.andyglow.json.JsonFormatter
    import com.github.andyglow.jsonschema.AsValue
    import json._
    import json.{Json, Schema => AndyGlowSchema}

    // source = https://github.com/andyglow/scala-jsonschema#regular

    // TODO why say implicit lazy val instead of just val? https://github.com/andyglow/scala-jsonschema#regular

    // Defining the json schemas
    val genderSchema: AndyGlowSchema[Gender] = Json.schema[Gender]

    val companySchema: AndyGlowSchema[Company] = Json.schema[Company]

    val carSchema: AndyGlowSchema[Car] = Json.schema[Car]

    val personSchema: AndyGlowSchema[Person] = Json.schema[Person]

    // Defining the json strings
    val genderJson_str: String = JsonFormatter.format(
      AsValue.schema(genderSchema, json.schema.Version.Draft04())
    )
    val companyJson_str: String = JsonFormatter.format(
      AsValue.schema(companySchema, json.schema.Version.Draft04())
    )
    val carJson_str: String = JsonFormatter.format(
      AsValue.schema(personSchema, json.schema.Version.Draft04())
    )
    val personJson_str: String = JsonFormatter.format(
      AsValue.schema(personSchema, json.schema.Version.Draft04())
    )


    println("\nANDY GLOW WAY 2 (SEPARATED)")
    println(s"genderSchema = $genderSchema")
    println(s"companySchema = $companySchema")
    println(s"carSchema = $carSchema")
    println(s"personSchema = $personSchema")
    println()
    println(s"genderJson_str = $genderJson_str")
    println(s"companyJson_str = $companyJson_str")
    println(s"carJson_str = $carJson_str")
    println(s"personJson_str = $personJson_str")


    val expectedJson_str: String =
      """
        |{
        |  "$schema": "http://json-schema.org/draft-04/schema#",
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "middleName": {
        |      "type": "string"
        |    },
        |    "cars": {
        |      "type": "array",
        |      "items": {
        |        "$ref": "#/definitions/com.github.andyglow.jsonschema.ExampleMsg.Car"
        |      }
        |    },
        |    "company": {
        |      "$ref": "#/definitions/com.github.andyglow.jsonschema.ExampleMsg.Company"
        |    },
        |    "lastName": {
        |      "type": "string"
        |    },
        |    "firstName": {
        |      "type": "string"
        |    },
        |    "birthDay": {
        |      "type": "string",
        |      "format": "date-time"
        |    },
        |    "gender": {
        |      "$ref": "#/definitions/com.github.andyglow.jsonschema.ExampleMsg.Gender"
        |    }
        |  },
        |  "required": [
        |    "company",
        |    "lastName",
        |    "birthDay",
        |    "gender",
        |    "firstName",
        |    "cars"
        |  ],
        |  "definitions": {
        |    "com.github.andyglow.jsonschema.ExampleMsg.Company": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "name": {
        |          "type": "string"
        |        }
        |      },
        |      "required": [
        |        "name"
        |      ]
        |    },
        |    "com.github.andyglow.jsonschema.ExampleMsg.Car": {
        |      "type": "object",
        |      "additionalProperties": false,
        |      "properties": {
        |        "name": {
        |          "type": "string"
        |        },
        |        "manufacturer": {
        |          "$ref": "#/definitions/com.github.andyglow.jsonschema.ExampleMsg.Company"
        |        }
        |      },
        |      "required": [
        |        "name",
        |        "manufacturer"
        |      ]
        |    },
        |    "com.github.andyglow.jsonschema.ExampleMsg.Gender": {
        |      "type": "string",
        |      "enum": [
        |        "Male",
        |        "Female"
        |      ]
        |    }
        |  }
        |}""".stripMargin.trim()
  }


  ZioWay1_Separate
  //ZioWay2_Inlined
  AndyGlowWay1_Inlined
  AndyGlowWay2_Separate
}
