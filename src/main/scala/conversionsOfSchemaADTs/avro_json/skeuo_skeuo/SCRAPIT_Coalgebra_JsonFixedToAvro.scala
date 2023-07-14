//package conversionsOfSchemaADTs.avro_json.skeuo_skeuo
//
//
//
//
//import cats.data.NonEmptyList
//import higherkindness.droste._
//import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._
//
//import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
//import SchemaAvro_Skeuo._
//
//import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
//import SchemaJson_Skeuo._
////import SchemaJson_Skeuo.Fixed
//
//import scala.reflect.runtime.universe._
//
//
///// TODO: try using JsonSchemaF.Fixed instead of just JsonSchemaF[T]
//// ---- JFixed => AvroF[JFixed]
//// ---- AvroF[JFixed] => JFixed
//
///**
// * Coalgebra[SchemaAvro, SchemaJsonFixed]
// * JsonFixed => Avro[JsonFixed]
// *
// * @return
// */
//object Coalgebra_JsonFixedToAvro {
//
//
//
//
//	def coalgebra_JsonFixedToAvro: Coalgebra[SchemaAvro_Skeuo, SchemaJson_Skeuo.Fixed] = Coalgebra {
//			// TODO which json maps to TNull() avro?
//			//case _ ⇒ TNull()
//
//		// TODO why do these all turn red?
//			case Fixed.integer() ⇒ TInt()
//			case Fixed.long() ⇒ TLong()
//			case Fixed.float() ⇒ TFloat()
//			case Fixed.double() ⇒ TDouble()
//			case Fixed.string() ⇒ TString()
//			case Fixed.byte() ⇒ TBytes() // TODO is json bytes meaning avro bytes UNION with avro decimal?? https://hyp.is/pHQnBh_6Ee6j8yvgHD5x9g/avro.apache.org/docs/1.11.1/specification/
//			case Fixed.binary() ⇒ ??? // TODO
//			case Fixed.boolean() ⇒ TBoolean()
//
//			/**
//			 * TODO verify: json date to avro date
//			 * 1) https://hyp.is/wJznXh_3Ee6nebPz2wEyyw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
//			 * 2) if the TDate() (skeuo) contains the logical type info (int, date) like in apache-avro schema or if you have to add TInt() next to Date() in this List as well???? (implications: then TDate() is not same as apache-avro schema even though it gets mapped that way in MY conversion function)
//			 */
//
//			case Fixed.date() ⇒ TUnion(NonEmptyList(TNull(), List(TString(), TDate())))
//
//
//			/**
//			 * TODO where is the Time in json schema?
//			 * https://hyp.is/nK88eB_5Ee6b-mub_jo24A/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
//			 */
//
//			/**
//			 * TODO verify: Json date time -> avro: Union (long+ timestampmillis, null, string)
//			 * 1) https://hyp.is/PUYlCB_5Ee6kOOPNKtp-zQ/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
//			 * 2) check if must include Long with Timestampmillis in AvroF or if the Long is already included in the Timestampmillis (like it is in apache avro)
//			 * 3) if must include the TLong with TTimestampmillis then how to do it - with the list or with another UNion?
//			 */
//
//			case Fixed.dateTime() ⇒ TUnion(NonEmptyList(TNull(), List(TString(), /*TLong(),*/ TTimestampMillis())))
//
//
//			case Fixed.password() ⇒ ??? // TODO what is password to avro?
//
//			// TODO why is there problem matching Fixed.array?
//			case Fixed.array(inner: Fixed)) ⇒ TArray(inner) // TODO or TArray(fa) ?
//
//			//case Fixed.`enum`(args: List[String]) ⇒ ???
//			case fe @ Fix(`enum`(cases: List[String])) ⇒ TEnum(fe)
//
//		}
//
//
//
//}
//
//
