package TEMP_Zio_JsonCodecSpecTryOuts



import zio.json.{JsonEncoder, EncoderOps}
import zio.schema.{DeriveSchema, Schema, StandardType}
import zio.schema.codec.JsonCodec._
import zio.schema.codec.JsonCodec
import zio.stream.ZPipeline

/**
 *
 */
object temp_zio_jsoncodecspectryout extends App {


  object Domain {
    sealed trait PaymentMethod

    final case class Person(name: String, age: Int)

    final case class Customer(person: Person, paymentMethod: PaymentMethod)

    object PaymentMethod {
      final case class CreditCard(number: String, expirationMonth: Int, expirationYear: Int) extends PaymentMethod

      final case class WireTransfer(accountNumber: String, bankCode: String) extends PaymentMethod
    }
  }

  import Domain._

  // NOTE: Making the schemas
  val schemaPerson: Schema[Person] = DeriveSchema.gen[Person]

  val schemaEnum: Schema.Enum3[String, String, String, String] = SharedTestData.makeSchemaEnumSimple
    //SharedTestData.staticAnnotationToSchemaEnum(JsonAnnotations.name("MyEnum"))

  val schemaString: Schema[String] = Schema.Primitive(StandardType.StringType)
  //val schemaString2: Schema[String] = DeriveSchema.gen[String]

  // NOTE: Printing the schemas

  println(s"schemaEnum = $schemaEnum")
  println(s"schemaString = $schemaString")
  //println(s"schemaString2 = $schemaString2")
  println(s"schemaPerson = $schemaPerson")



  // NOTE: making the json encoders from the schemas

  implicit val jstr: JsonEncoder[String] = JsonCodec.jsonEncoder(schemaString) //#todo align names to AvroCodec.encode
  //implicit val jstr2: JsonEncoder[String] = JsonCodec.jsonEncoder(schemaString2)
  implicit val jenum: JsonEncoder[String] = JsonCodec.jsonEncoder(schemaEnum)
  implicit val jperson: JsonEncoder[Domain.Person] = JsonCodec.jsonEncoder(schemaPerson)


  // NOTE: printing the json encoders

  println(s"jsoncodec.jsonencoder(schemaperson) = $jperson")
  println(s"jsoncodec.jsonencoder(schemastring) = $jstr")
  //println(s"jsoncodec.jsonencoder(schemastring2) = $jstr2")
  println(s"jsnocodec.jsonencoder(schemaenum) = $jenum")
  println(s"jsperson to json ast = ${jperson.toJsonAST(Person("blank", 10))}")

  // NOTE follow by Example1
  val jstr3: ZPipeline[Any, Nothing, String, Byte] = JsonCodec.schemaBasedBinaryCodec[String](schemaString).streamEncoder

  println(s"jstr3 (schemabasedbinarycodec) = $jstr3")


  // NOTE: .toJson functions

  val p1 = Person("Person 1", 10)
  println(s"person1.toJson = ${p1.toJson}")
  println(s"person1.toJsonPretty = ${p1.toJsonPretty}")
  println(s"person1.toJsonAST = ${p1.toJsonAST}")

}
