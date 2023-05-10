package ZioSchemaExamples

/**
 * SOURCE CODE = https://github.com/zio/zio-schema/blob/main/zio-schema-examples/shared/src/main/scala/dev/zio/schema/example/example1/Example1.scala
 */
object Example1_PaymentWireTransfer {

	import zio._
	import zio.schema.{Schema, TypeId}

	object Domain {
		sealed trait PaymentMethod

		final case class Person(name: String, age: Int)

		final case class Customer(person: Person, paymentMethod: PaymentMethod)

		object PaymentMethod {
			final case class CreditCard(number: String, expirationMonth: Int, expirationYear: Int) extends PaymentMethod

			final case class WireTransfer(accountNumber: String, bankCode: String) extends PaymentMethod
		}
	}

	//import dev.zio.schema.example.example1.Domain._

	import Domain.PaymentMethod._
	import Domain._
	import zio.schema.Schema._

	object ManualConstruction {

		val typeId_creditCard: TypeId = TypeId.parse("dev.zio.schema.example.example1.Domain.PaymentMethod.CreditCard")

		val field1_creditCard: Field[CreditCard, String] = Schema.Field[CreditCard, String](
			name0 = "number",
			schema0 = Schema.primitive[String],
			//get0: (R => A) --- (CreditCard => String)
			get0 = (r: CreditCard) => r.number,
			// set0: (R, A) => R ----- (CreditCard, String) => CreditCard
			set0 = (cc: CreditCard, s: String) => cc.copy(number = s)
		)

		val field2_creditCard: Field[CreditCard, RuntimeFlags] = Schema.Field[CreditCard, Int](
			name0 = "expirationMonth",
			Schema.primitive[Int],
			get0 = (cc: CreditCard) => cc.expirationMonth,
			set0 = (cc: CreditCard, rf: RuntimeFlags) => cc.copy(expirationMonth = rf)
		)

		val field3_creditCard: Field[CreditCard, RuntimeFlags] = Schema.Field[CreditCard, Int](
			"expirationYear",
			Schema.primitive[Int],
			get0 = (cc: CreditCard) => cc.expirationYear,
			set0 = (cc: CreditCard, rf: RuntimeFlags) => cc.copy(expirationYear = rf)
		)

		val construct0_creditCard: (String, Int, Int) => CreditCard =
			(number: String, expirationMonth: Int, expirationYear: Int) =>
				PaymentMethod.CreditCard(number, expirationMonth, expirationYear)

		//TODO ASSERT  properties of zioschema constrcuts - e.g. that caseclass3 has 3 fields and a construct (assert type
		// params using reflection/typetag)
		val schemaPaymentMethodCreditCard: Schema[CreditCard] = Schema.CaseClass3[String, Int, Int, CreditCard](
			id0 = typeId_creditCard,
			field01 = field1_creditCard,
			field02 = field2_creditCard,
			field03 = field3_creditCard,
			construct0 = construct0_creditCard
		)
	}
}
