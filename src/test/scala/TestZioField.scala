import org.specs2.mutable.Specification


import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._


import util.Util._

import zio._
import zio.schema.codec.DecodeError
import zio.schema.{DeriveSchema, Schema, TypeId}
import zio.stream.ZPipeline

import Example1_PaymentWireTransfer._
import Domain._
import Domain.PaymentMethod._
import zio.schema.Schema._
import ManualConstruction._

/**
 *
 */
class TestZioField extends Specification {



	"function test" should {
		"return function argument type" in {

			getFuncType(typeId_creditCard) must beEqualTo("zio.schema.TypeId")
			getShortFuncType(typeId_creditCard) must beEqualTo("TypeId")


			val longName1 = "zio.schema.Schema.Field[Example1_PaymentWireTransfer.Domain.PaymentMethod.CreditCard,String]"
			getFuncType(field1_creditCard) must beEqualTo (longName1)
			getShortFuncType(field1_creditCard) must beEqualTo ("Field[CreditCard,String]")

			val longName2 = "zio.schema.Schema.Field[Example1_PaymentWireTransfer.Domain.PaymentMethod.CreditCard,zio.RuntimeFlags]"
			getFuncType(field2_creditCard) must beEqualTo (longName2)
			getShortFuncType(field2_creditCard) must beEqualTo ("Field[CreditCard,RuntimeFlags]")


			val longName3 = "zio.schema.Schema.Field[Example1_PaymentWireTransfer.Domain.PaymentMethod.CreditCard,zio.RuntimeFlags]"
			getFuncType(field3_creditCard) must beEqualTo (longName3)
			getShortFuncType(field3_creditCard) must beEqualTo ("Field[CreditCard,RuntimeFlags]")


			val longName4 = "(String, Int, Int) => Example1_PaymentWireTransfer.Domain.PaymentMethod.CreditCard"
			getFuncType(construct0_creditCard) must	beEqualTo(longName4)
			getShortFuncType(construct0_creditCard) must beEqualTo("(String, Int, Int) => CreditCard")
		}
	}

}
