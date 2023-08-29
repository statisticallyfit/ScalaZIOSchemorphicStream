package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


import higherkindness.droste.Trans
import higherkindness.droste.data.Fix
//import higherkindness.droste.syntax.all._

import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldAvro, _}
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.JsonSchemaF._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import utilMain.utilAvroJson.utilSkeuoSkeuo.FieldToPropertyConversions._


/**
 * Using droste Trans to convert F[A] => G[A}
 * SOURCE inspiration = https://github.com/higherkindness/skeuomorph/blob/cc739d3dcdc07ead250461b6ecc6fa4daf2ba988/src/main/scala/higherkindness/skeuomorph/mu/Transform.scala#L59
 *
 * DEF of Trans in droste:
 * gtrans - https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/trans.scala#L6
 * trans = https://github.com/higherkindness/droste/blob/76b206db3ee073aa2ecbf72d4e85d5595aabf913/modules/core/src/main/scala/higherkindness/droste/package.scala#L76
 */
object Trans_AvroToJson {

	def transJ /*[T: TypeTag]*/ : Trans[AvroSchema_S, JsonSchema_S, Fix[JsonSchema_S]] = Trans {

		// TODO find out if this mapping is correct
		case TNull() ⇒
			ObjectF(List(
				Property(name = "null", tpe = Fix(StringF()))
			), List())

		case TInt() ⇒ IntegerF()

		case TString() ⇒ StringF()

		case TBoolean() ⇒ BooleanF()

		case TLong() ⇒ LongF()

		case TFloat() ⇒ FloatF()

		case TDouble() ⇒ DoubleF()

		case TBytes() ⇒ ByteF()

		case TArray(inner: Fix[JsonSchema_S]) ⇒ ArrayF(inner)

		// Source: avro map -> json map = https://hyp.is/ixmlxio5Ee6pv28sNQ9XaA/docs.airbyte.com/understanding-airbyte/json-avro-conversion/

		case TMap(inner: Fix[JsonSchema_S]) ⇒ {

			ObjectMapF(additionalProperties = AdditionalProperties[Fix[JsonSchema_S]](tpe = inner))
		}

		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[Fixed]]) ⇒ {

			val ps: List[Property[Fix[JsonSchema_S]]] = fields.map((f: FieldAvro[Fix[JsonSchema_S]]) ⇒ field2Property(f))
			val rs: List[String] = fields.map(f ⇒ f.name)

			//ObjectF(ps, rs)
			ObjectNamedF(
				name = name,
				properties = ps,
				required = rs
			)

		}

		// TODO HELP  cannot create property because no tpe: A object is provided ...
		//case TNamedType(namespace: String, name: String) ⇒ transJ(TRecord)


		case TEnum(name: String,
		namespace: Option[String],
		aliases: List[String],
		doc: Option[String],
		symbols: List[String]) ⇒ {

			// TODO what happens to the rest of the info?
			//EnumF(cases = symbols)
			ObjectF(
				properties = List(
					Property(name = "enum", tpe = Fix(ObjectF(
						properties = List(
							Property(name = name, tpe = Fix(EnumF(cases = symbols)))),
						required = List()
					))
					),
					Property(name = "namespace", tpe = Fix(ObjectF(
						properties = List(
							Property(name = namespace.getOrElse(""), tpe = Fix(StringF()))
						),
						required = List()
					)))
				),
				required = List("name", "namespace", "symbols") // TODO
			)
		}

		// Source: unions (avro) --> arrays (json)
		// Source 2: toJson (data) function = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L274
		case TUnion(options: cats.data.NonEmptyList[Fix[JsonSchema_S]]) ⇒
			ArrayF(options.head)
		// TODO check how to get just one value to get the type Fix[JsonSchema_S] for array



		case TFixed(name: String, namespace: Option[String], aliases: List[String], size: Int) ⇒ {

			// TODO trying out a new layering strategy:
			val ps: List[Property[Fix[JsonSchema_S]]] = List(
				Property(name = "fixed", tpe = Fix(ObjectF(
					properties = List(Property(name = name, tpe = Fix(StringF()))), // TODO name in here?
					required = aliases // TODO?
				))),
				Property(name = "name", tpe = Fix(StringF())), // TODO or name out here?
				Property(name = "size", tpe = Fix(IntegerF()))
			)
			val rs: List[String] = List() // TODO ??

			ObjectF(ps, rs)
		}

		case TDate() ⇒ ObjectF(
			properties = List(
				Property(name = "type", tpe = Fix(IntegerF())),
				Property(name = "logicalType", tpe = Fix(DateF()))
			),
			required = List()
		)
		case TTimestampMillis() ⇒ ObjectF(
			properties = List(
				Property(name = "type", tpe = Fix(LongF())),
				Property(name = "logicalType", tpe = Fix(DateTimeF()))
			),
			required = List()
		)
		case TTimeMillis() ⇒ ObjectF(
			properties = List(
				Property(name = "type", tpe = Fix(IntegerF())),
				Property(name = "logicalType", tpe = Fix(DateTimeF()))
			),
			required = List()
		)
		case TDecimal(precision: Int, scale: Int) ⇒ ObjectF(
			properties = List(
				Property(name = "type", tpe = Fix(ByteF())), // TODO byte or fixed? (make another objectf to be fixed instead of byte here)
				Property(name = "logicalType", tpe = Fix(DateTimeF())),
				Property(name = "precision", tpe = Fix(IntegerF())),
				Property(name = "scale", tpe = Fix(IntegerF()))
			),
			required = List() //List("decimal", "precision", "scale") // TODO ??
		)
	}


	/*def transform_AvroToJsonSkeuo[T: TypeTag]: Trans[AvroSchema_S, JsonSchema_S, T] = Trans {

		// NOTE: in the Avro file here (CityMesh - devs - datasource) the 'symbol' has type 'null' and in json the 'symbol' has type 'object' with  required = [], and properties = {}
		// path = /development/projects/statisticallyfit/github/learningdataflow/SchaemeowMorphism/src/test/scala/testData/testDataPrivateTati/asset-schemas/sdp-asset-schemas-citymesh/src/main/trafficflow/ctm.tf_devs/ctm.tf_devs.datasource/

		case TNull() ⇒ ObjectF[T](
			properties = List[Property[T]](/*Property(name = "", tpe = null.asInstanceOf[T])*/),
			required = List[String]()
		)
		case TInt() ⇒ IntegerF[T]()

		case TBoolean() ⇒ BooleanF[T]()

		case TString() ⇒ StringF[T]()

		case TFloat() ⇒ FloatF[T]()

		case TLong() ⇒ LongF[T]()

		case TDouble() ⇒ DoubleF[T]()

		case TBytes() ⇒ ByteF[T]()

		case TArray(innerSchema: T) ⇒ ArrayF(innerSchema)

		// TODO where does 'name' go?
		case TRecord(name: String, namespace: Option[String], aliases: List[String], doc: Option[String], fields: List[FieldAvro[T]]) ⇒ {

			ObjectF(
				properties = fields.map((f: FieldAvro[T]) ⇒ field2Property(f)),
				required = fields.map(f ⇒ f.name)
			)

		}

	}*/
}
