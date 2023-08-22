package conversionsOfSchemaADTs.avro_json.skeuo_skeuo

import higherkindness.droste._
import higherkindness.droste.data._
import higherkindness.skeuomorph.avro.AvroF._
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._

// TODO look here avro-json map of equivalent types: https://avro.apache.org/docs/1.11.1/specification/_print/

// Avro-json schema compatibility rules = https://docs.confluent.io/platform/current/schema-registry/fundamentals/serdes-develop/serdes-json.html#json-schema-compatibility-rules

// Airbyte conversion rules = https://docs.airbyte.com/understanding-airbyte/json-avro-conversion/#built-in-formats

// Airbyte: Reason for union of string and logical type in Avro schema = https://hyp.is/evtFxB_3Ee6gxAuTzBzpiw/docs.airbyte.com/understanding-airbyte/json-avro-conversion/
/**
 *
 */
object Skeuo_Skeuo {
	
	
	/*import Coalgebra_AvroToJson._
	import Coalgebra_JsonToAvro._
	import Algebra_AvroToJson._
	import Algebra_JsonToAvro._

	import Coalgebra_JsonFixedToAvro._*/
	import Algebra_AvroToJson_Fixed._
	import Trans_AvroToJson._
	
	
	// TODO: do all these conversions using Trans (because itis F[A => G[A]): https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/package.scala#L80
	
	/// -----------------------------
	
	
	object TransSchemaImplicits {
		
		/*implicit def skeuoJsonHasEmbed[T: TypeTag]: Embed[JsonSchema_S, T] = new Embed[JsonSchema_S, T] {
			
			// JsonSkeuo[T] => T
			def algebra: Algebra[JsonSchema_S, T] = Algebra {
				case IntegerF() ⇒ Int
			}
		}*/
		
		implicit def skeuoEmbed_JA: Embed[JsonSchema_S, Fix[AvroSchema_S]] = new Embed[JsonSchema_S, Fix[AvroSchema_S]] {
			
			// JsonSchema_S [ Fix[AvroSchema_S]] => Fix[AvroSchema_S]
			def algebra: Algebra[JsonSchema_S, Fix[AvroSchema_S]] = Algebra {
				// Null
				//case ObjectF(List(), List()) ⇒ Fix(TNull())
				// Integer
				case IntegerF() ⇒ Fix(TInt())
				// String
				case StringF() ⇒ Fix(TString())
				// Boolean
				case BooleanF() ⇒ Fix(TBoolean())
				// Long
				case LongF() ⇒ Fix(TLong())
				// Float
				case FloatF() ⇒ Fix(TFloat())
				// Double
				case DoubleF() ⇒ Fix(TDouble())
				// Byte
				case ByteF() ⇒ Fix(TBytes())
				// Array
				case ArrayF(inner: Fix[AvroSchema_S]) ⇒ Fix(TArray(inner)) // TODO just inner or wrap with TArray?
				
				
				// Object with name
				case ObjectNameF(name: String, props: List[Property[Fix[AvroSchema_S]]],
				reqs: List[String]) ⇒ {
					
					println(s"ObjectNameF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nname = $name" +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)
					
					Fix(
						TRecord(name = name, namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					)
					
				}
				
				// Map
				
				// METHOD 1: using the 'additionalProperties' way of declaring a map
				case ObjectMapF(name: String, additionalProperties: AdditionalProperties[Fix[AvroSchema_S]]) ⇒ {
					
					println(s"ObjectMapF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nname = $name" +
					        s"\nadditionalProperties = $additionalProperties"
					)
					
					// TODO what about name now?
					Fix(TMap(additionalProperties.tpe))
				}
				
				// Record
				case ObjectF(props: List[Property[Fix[AvroSchema_S]]],
				reqs: List[String]) ⇒ {
					
					println(s"ObjectF: INSIDE EMBED'S ALGEBRA: " +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)
					
					
					val result: Fix[AvroSchema_S] = if(props.isEmpty && reqs.isEmpty) {
						Fix(TNull())
					}
					// METHOD 2: the 'properties' / 'values' way
					/*else if(props.length == 1 && props.head.name == "values"){
						Fix(TMap(props.head.tpe))
					}*/ else {
						Fix(
							TRecord(name = /*null*/ "record", namespace = None, aliases = List(), doc = None,
								fields = props.map(p ⇒ property2Field(p))
							)
						)
					}
					result
				}
			}
		}
		
		
		/**
		 * Algebra[JsonS, Fix[JsonS]] === JsonS[Fix[JsonS]] => Fix[JsonS]]
		 *
		 * Calling cata: Fix[JsonS] => Fix[JsonS] ???
		 *
		 * @return
		 */
		/*implicit def skeuoEmbed_JJ: Embed[JsonSchema_S, Fix[JsonSchema_S]] = new Embed[JsonSchema_S, Fix[JsonSchema_S]] {
			def algebra: Algebra[JsonSchema_S, Fix[JsonSchema_S]] = ???
		}
		*/
		
		/**
		 * Coalgebra means:
		 * Fix[JsonSchema_S] => AvroSchema_S[ Fix[JsonSchema_S] ]
		 *
		 * @return
		 */
		implicit def skeuoProject_AJ: Project[AvroSchema_S, Fix[JsonSchema_S]] = new Project[AvroSchema_S, Fix[JsonSchema_S]] {
			
			def coalgebra: Coalgebra[AvroSchema_S, Fix[JsonSchema_S]] = Coalgebra {
				// Null
				//case Fix(ObjectF(List(), List())) ⇒ TNull()
				// Integer
				case Fix(IntegerF()) ⇒ TInt()
				// String
				case Fix(StringF()) ⇒ TString()
				// Boolean
				case Fix(BooleanF()) ⇒ TBoolean()
				// Long
				case Fix(LongF()) ⇒ TLong()
				// Double
				case Fix(DoubleF()) ⇒ TDouble()
				// Float
				case Fix(FloatF()) ⇒ TFloat()
				// Bytes
				case Fix(ByteF()) ⇒ TBytes()
				// Array
				case Fix(ArrayF(inner: Fix[JsonSchema_S])) ⇒ TArray(inner)
				
				// Map
				case Fix(ObjectMapF(name: String, additionalProperties: AdditionalProperties[Fix[JsonSchema_S]])) ⇒ {
					
					println(s"ObjectMapF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nname = $name" +
					        s"\naddedproperties = $additionalProperties"
					)
					
					TMap(additionalProperties.tpe)
				}
				
				// Object Name
				case Fix(ObjectNameF(name: String, props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒ {
					
					println(s"ObjectNameF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nname = $name" +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs"
					)
					
					TRecord(name = name, namespace = None, aliases = List(), doc = None,
						fields = props.map(p ⇒ property2Field(p))
					)
				}
				
				// Record
				case Fix(ObjectF(props: List[Property[Fix[JsonSchema_S]]], reqs: List[String])) ⇒  {
					
					
					println(s"ObjectF: INSIDE PROJECT'S COALGEBRA: " +
					        s"\nproperties = $props" +
					        s"\nrequired = $reqs")
					
					
					val result: AvroSchema_S[Fix[JsonSchema_S]] = if (props.isEmpty && reqs.isEmpty) {
						TNull()
					}
					// METHOD 2: the 'properties' / 'values' way
					/*else if (props.length == 1 && props.head.name == "values") {
						TMap(props.head.tpe)
					}*/ else {
					
						TRecord(name = /*null*/ "TODO_OBJ_NAME", namespace = None, aliases = List(), doc = None,
							fields = props.map(p ⇒ property2Field(p))
						)
					}
					result
				}
			}
		}
	}
	
	
	// NOTE how to access Trans type (example here) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/mu/protocol.scala#L56C36-L56C36
	object ByTrans {
		/*def avroToJsonFunction[T: TypeTag]: AvroSchema_S[T] ⇒ JsonSchema_S[T] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]
			
			val runner: AvroSchema_S[T] ⇒ JsonSchema_S[T] = transVar.run
			
			runner.apply(_)
			
		}
		
		def avroToJson[T: TypeTag](av: AvroSchema_S[T]): JsonSchema_S[T] = transform_AvroToJsonSkeuo[T].run.apply(av)
		
		*/
		
		
		val avroToJson_byCataTransAlg: Fix[AvroSchema_S] ⇒ Fix[JsonSchema_S] = scheme.cata(transJ.algebra).apply(_)
		
		import TransSchemaImplicits.{skeuoEmbed_JA, skeuoProject_AJ} // skeuoProject_AJ
		//import TransSchemaImplicits.skeuoProject_AJ
		
		val jsonToAvro_byAnaTransCoalg: Fix[JsonSchema_S] ⇒ Fix[AvroSchema_S] = scheme.ana(transJ.coalgebra).apply(_)
		
		
		
		//val h: Fixed ⇒ Fixed = scheme.hylo(transJ.algebra, transJ.coalgebra)
		
		val roundTripAvro: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = jsonToAvro_byAnaTransCoalg compose avroToJson_byCataTransAlg
		
		val roundTripJson: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = avroToJson_byCataTransAlg compose jsonToAvro_byAnaTransCoalg
		
		
		/*def transFixWay[T: TypeTag] = {
			val transVar: Trans[AvroSchema_S, JsonSchema_S, T] = transform_AvroToJsonSkeuo[T]
			
			val ta = transVar.algebra
		}*/
	}
	
	
	object ByAlgebra {
		
		
		def avroToJson_Fixed: Fix[AvroSchema_S] ⇒ Fix[JsonSchema_S] =
			scheme.cata(algebra_AvroToJsonFixed).apply(_)
		
		
		/*def avroToJson_TYPED[T: TypeTag]: Fix[AvroSchema_S] ⇒ JsonSchema_S[T] =
			scheme.cata(algebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: Fix[AvroSchema_S] ⇒ JsonSchema_S[_] = scheme.cata(algebra_AvroToJson).apply(_)*/
		
		// NOT GOOD - "No implicits found for Functor[AvroF[_]]
		//def typed2[T: TypeTag] = scheme.cata(algebra2[T]).apply(_)
		
		/*def jsonToAvro_TYPED[T: TypeTag]: Fix[JsonSchema_S] ⇒ AvroSchema_S[T] =
			scheme.cata(algebra_JsonToAvro_TYPED[T]).apply(_)


		def jsonToAvro_UNTYPED: Fix[JsonSchema_S] ⇒ AvroSchema_S[_] = scheme.cata(algebra_JsonToAvro).apply(_)*/
	}
	
	
	object ByCoalgebra {
		
		/*def avroToJson_TYPED[T: TypeTag]: AvroSchema_S[T] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson_TYPED[T]).apply(_)


		def avroToJson_UNTYPED: AvroSchema_S[_] ⇒ Fix[JsonSchema_S] =
			scheme.ana(coalgebra_AvroToJson).apply(_)*/
		
		
		/*def jsonToAvro_TYPED[T: TypeTag]: JsonSchema_S[T] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro_TYPED[T]).apply(_)

		def jsonToAvro_UNTYPED: JsonSchema_S[_] ⇒ Fix[AvroSchema_S] =
			scheme.ana(coalgebra_JsonToAvro).apply(_)*/
	}
	
	
	
	
	
	
	// AvroF -> Json[AvroF] -> AvroF
	//def roundTrip_AvroToAvro[T: TypeTag]: AvroSchema_S[T] ⇒ AvroSchema_S[T] = ByAlgebra.jsonToAvro_TYPED[T] compose ByCoalgebra.avroToJson_TYPED[T]
	
	//def roundTrip_AvroToAvro_CanonicalFix[T: TypeTag]: Fix[AvroSchema_S] ⇒ Fix[AvroSchema_S] = ByCoalgebra.jsonToAvro_TYPED[T] compose ByAlgebra.avroToJson_TYPED[T]
	
	
	// JsonF -> AvroF[JsonF] -> JsonF
	//def roundTrip_JsonToJson[T: TypeTag]: JsonSchema_S[T] ⇒ JsonSchema_S[T] = ByAlgebra.avroToJson_TYPED[T] compose ByCoalgebra.jsonToAvro_TYPED[T]
	
	//def roundTrip_JsonToJson_CanonicalFix[T: TypeTag]: Fix[JsonSchema_S] ⇒ Fix[JsonSchema_S] = ByCoalgebra.avroToJson_TYPED[T] compose ByAlgebra.jsonToAvro_TYPED[T]
	
	
}
