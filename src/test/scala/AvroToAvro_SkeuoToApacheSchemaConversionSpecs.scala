import cats.data.NonEmptyList


import higherkindness.droste._
import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._
import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaSkeuoAvro}
import higherkindness.skeuomorph.avro.AvroF.{Field ⇒ FieldSkeuo, Order ⇒ OrderSkeuo}
import io.circe.Json


/*import matryoshka._
// NOTE: need this to avoid error of "No implicits found for Corecursive[T]" when doing anamorphism
// MEANING: Fix needs to implement BirecursiveT typeclass
import matryoshka.data._
import matryoshka.implicits._*/

import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}
import org.apache.avro.Schema.{Field ⇒ FieldApache}
import org.apache.avro.Schema.Field.{Order ⇒ OrderApache}
import org.apache.avro.LogicalTypes

import zio.schema._
import zio.schema.DeriveSchema
import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType} // TODO move the zio <-> apache tests in the other file
import zio.schema.codec.AvroCodec

import scala.jdk.CollectionConverters._
import java.util

import testData.ScalaCaseClassData._

import testUtil.utilSkeuoApache.FieldAndOrderConversions._




import org.scalatest.Assertions._
import org.scalacheck._
import org.specs2._

/**
 *
 */
class AvroToAvro_SkeuoToApacheSpecs extends Specification with ScalaCheck  {
	
	def isValidated(logicalType: LogicalTypeApache, schemaArg: SchemaApacheAvro): Boolean = {
		val thrown = intercept[IllegalArgumentException] {
			logicalType.validate(schemaArg)
		}
		thrown.getMessage.isEmpty
	}
	
	def avroFToApache: Algebra[SchemaSkeuoAvro, SchemaApacheAvro] =
		Algebra { // Algebra[Skeuo, Apache] ----- MEANING ---->  Skeuo[Apache] => Apache
			
			case SchemaSkeuoAvro.TNull() => SchemaApacheAvro.create(SchemaApacheAvro.Type.NULL)
			
			case SchemaSkeuoAvro.TBoolean() => SchemaApacheAvro.create(SchemaApacheAvro.Type.BOOLEAN)
			
			case SchemaSkeuoAvro.TInt() => SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
			
			case SchemaSkeuoAvro.TLong() => SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG)
			
			case SchemaSkeuoAvro.TFloat() => SchemaApacheAvro.create(SchemaApacheAvro.Type.FLOAT)
			
			case SchemaSkeuoAvro.TDouble() => SchemaApacheAvro.create(SchemaApacheAvro.Type.DOUBLE)
			
			case SchemaSkeuoAvro.TBytes() => SchemaApacheAvro.create(SchemaApacheAvro.Type.BYTES)
			
			case SchemaSkeuoAvro.TString() => SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)
			
			
			/**
			 * Apache Map Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L243
			 */
			case SchemaSkeuoAvro.TMap(sch: SchemaApacheAvro) => SchemaApacheAvro.createMap(sch)
			
			
			/**
			 * Apache array = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L238
			 *
			 * HELP: figure out why inner part is INT and not array because it is supposed to be array since it is the schema inside.
			 *
			 * HELP: see here `toJson` seems like inner part is INT = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L249
			 */
			case SchemaSkeuoAvro.TArray(apacheSchema: SchemaApacheAvro) ⇒ {
				
				println("Inside avroFToApache ARRAY converter: ")
				println(s"apacheSchema = $apacheSchema")
				println(s"apacheSchema.getType = ${apacheSchema.getType}")
				
				SchemaApacheAvro.createArray(apacheSchema)
			}
			/*apacheSchema.getType match {
			case n @ SchemaApacheAvro.Type.NULL ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(n))
			case b @ SchemaApacheAvro.Type.BOOLEAN ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(b))
			case i @ SchemaApacheAvro.Type.INT ⇒ {
				println(s"apacheSchema = $apacheSchema")
				SchemaApacheAvro.createArray(SchemaApacheAvro.create(i))
			}
			case l @ SchemaApacheAvro.Type.LONG ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(l))
			case f @ SchemaApacheAvro.Type.FLOAT ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(f))
			case d @ SchemaApacheAvro.Type.DOUBLE ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(d))
			case by @ SchemaApacheAvro.Type.BYTES ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(by))
			case s @ SchemaApacheAvro.Type.STRING ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.create(s))
			case SchemaApacheAvro.Type.MAP ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createMap(apacheSchema.getValueType))
			case SchemaApacheAvro.Type.ARRAY ⇒ SchemaApacheAvro.createArray(SchemaApacheAvro.createArray(apacheSchema.getElementType))
			case SchemaApacheAvro.Type.RECORD ⇒ {
				val a: SchemaApacheAvro = apacheSchema
				/*val res: SchemaApacheAvro = SchemaApacheAvro.createArray(SchemaApacheAvro.createRecord(a.getName, a.getDoc, a.getNamespace, a.isError, a.getFields))*/
				val res2 = SchemaApacheAvro.createArray(a)
				res2
			} //createRecord(String name, String doc, String namespace, boolean isError, List<Field> fields)
			case SchemaApacheAvro.Type.ENUM ⇒ {
				val a = apacheSchema
				//val ea: SchemaApacheAvro = apacheSchema.asInstanceOf[SchemaApacheAvro.Type.ENUM]
				SchemaApacheAvro.createArray(SchemaApacheAvro.createEnum(a.getName, a.getDoc, a.getNamespace, a.getEnumSymbols))
			}
				//createEnum(String name, String doc, String namespace, List<String> values)
			}*/
			
			
			/**
			 * Apache's Named Record == skeuomorph's TNamedType
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L211
			 */
			case SchemaSkeuoAvro.TNamedType(namespace: String, name: String) ⇒ {
				SchemaApacheAvro.createRecord(name, "NO DOC", namespace, /*isError =*/ false)
			}
			
			/**
			 * Apache's Named Record with Fields == skeuomorph's TRecord
			 * SOURCE = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L222-L225
			 */
			case SchemaSkeuoAvro.TRecord(skeuoName: String,
			skeuoNamespace: Option[String],
			aliases: List[String],
			skeuoDoc: Option[String],
			skeuoFields: List[FieldS[SchemaApacheAvro]]) ⇒ {
				
				// Skeuo Order = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L61-L64
				// Apache Order = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L530
				
				// Skeuo Field = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L75-L90
				// Apache Field = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L507-L697
				
				// field2Field: Skeuo converts ApacheField to SkeuoField = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L51
				
				// order2Order: Skeuo converts ApacheOrder to SkeuoOrder = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L37-L42
				
				
				// Create the record
				// HELP: apache has `isError` while skeuo does not and skeuo has `aliases` while apache does not.... how to fix?
				//  TODO say by default that this record is NOT an error type? = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L364
				
				val recordSchema: SchemaApacheAvro = SchemaApacheAvro.createRecord(skeuoName, skeuoDoc.getOrElse("NO DOC"), skeuoNamespace.getOrElse("NO NAMESPACE"), /*isError =*/ false, skeuoFields.map(f ⇒ field2Field_SA(f)).asJava)
				
				aliases.foreach(a ⇒ recordSchema.addAlias(a))
				
				recordSchema
			}
			
			
			/**
			 * Apache Union Type = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L248-L254
			 */
			case SchemaSkeuoAvro.TUnion(schemaList: NonEmptyList[SchemaApacheAvro]) ⇒ {
				SchemaApacheAvro.createUnion(schemaList.toList.asJava)
			}
			
			/**
			 * Apache Enum schema = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L232-L235
			 *
			 * HELP: Skeuo has 'aliases' while apache does not -- meaning? - check meaning of aliases vs. symbols - which contains the enum subcases?
			 */
			case SchemaSkeuoAvro.TEnum(name, namespaceOpt, aliases, docOpt, symbols) => {
				val enumSchema = SchemaApacheAvro.createEnum(name,
					docOpt.getOrElse("NO DOC"),
					namespaceOpt.getOrElse("NO NAMESPACE"),
					symbols.asJava
				)
				
				aliases.foreach(a ⇒ enumSchema.addAlias(a))
				
				enumSchema
			}
			
			/**
			 * apache fixed = https://github.com/apache/avro/blob/master/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L258
			 * HELP: apache has `doc` while skeuo does not
			 * TODO CHECK IF MY IMPLEMENTATION IS CORREC: skeuo has `aliases` while apache does not --- construct Fixed first then addAliases = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L233
			 * NOTE: skeup has `namespaceOption` while apache has `space`` -- they are the same (namespace wrapped in option)
			 */
			case SchemaSkeuoAvro.TFixed(name, namespaceOpt, aliases, size) => {
				val fixedSchema: SchemaApacheAvro = SchemaApacheAvro.createFixed(name, "NO DOC", namespaceOpt.getOrElse("NO SPACE (from namespace)"), size)
				
				aliases.foreach(a => fixedSchema.addAlias(a))
				
				fixedSchema
			}
			
			
			
			// Logical Types: Date, Time, Decimal etc
			
			case SchemaSkeuoAvro.TDate() => {
				val intSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
				val dateLogicalType: LogicalTypeApache = LogicalTypesApache.date()
				assert(/*dateLogicalType.validate(intSchema)*/ isValidated(dateLogicalType, intSchema),
					s"${dateLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val dateSchema: SchemaApacheAvro = dateLogicalType.addToSchema(intSchema)
				
				dateSchema
			}
			case SchemaSkeuoAvro.TTimeMillis() ⇒ {
				
				val intSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
				val millisLogicalType: LogicalTypeApache = LogicalTypesApache.timeMillis()
				assert(isValidated(millisLogicalType, intSchema), s"${millisLogicalType.getName} Logical Type uses ${intSchema.getType} schema only") // check if compatible
				
				val millisSchema: SchemaApacheAvro = millisLogicalType.addToSchema(intSchema)
				
				millisSchema
			}
			
			case SchemaSkeuoAvro.TTimestampMillis() => {
				
				val longSchema: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG)
				val timestampMillisLogicalType: LogicalTypeApache = LogicalTypesApache.timestampMillis()
				assert(isValidated(timestampMillisLogicalType, longSchema), s"${timestampMillisLogicalType.getName} Logical Type uses ${longSchema.getType} schema only") // check if compatible
				
				val timestampMillisSchema: SchemaApacheAvro = timestampMillisLogicalType.addToSchema(longSchema)
				
				timestampMillisSchema
			}
			
			case SchemaSkeuoAvro.TDecimal(precision: Int, scale: Int) ⇒ {
				
				val fixedSchema: SchemaApacheAvro = SchemaApacheAvro.createFixed("decimal_fixed", "doc_decimal_fixed", "decimal_namespace", 5)
				// TODO is this OK?
				
				val decimalLogicalType: LogicalTypeApache = LogicalTypesApache.decimal(precision, scale)
				assert(isValidated(decimalLogicalType, fixedSchema), s"${decimalLogicalType.getName} Logical Type uses ${fixedSchema.getType} schema only") // check if compatible
				
				val decimalSchema: SchemaApacheAvro = decimalLogicalType.addToSchema(fixedSchema)
				
				decimalSchema
			}
			
			
			case sch => throw new IllegalArgumentException(s"Schema ${sch} not handled")
		}
	
	
	val strApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)
	
	val intApache: SchemaApacheAvro = SchemaApacheAvro.create(SchemaApacheAvro.Type.INT)
	val arrayApache: SchemaApacheAvro = SchemaApacheAvro.createArray(intApache)
	
	val enumApache: SchemaApacheAvro = SchemaApacheAvro.createEnum("Color", "doc", "namespace", List("red", "yellow", "blue").asJava)
	
	
	// Apache avro schema
	println(s"apache avro String (string): $strApache")
	println(s"(ARRAY AVRO APACHE-STRING) apache avro Array (string): $arrayApache")
	println(s"apache avro Enum (string): $enumApache")
	
	// SchemaApacheAvro --> skeuomorph avro schema
	// NOTE noting the types here when applying ana / cata morphisms
	val schemeAna: SchemaApacheAvro ⇒ Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro)
	val avroToJson: Fix[SchemaSkeuoAvro] ⇒ Json = scheme.cata(SchemaSkeuoAvro.toJson)
	
	// NOTE: not implemented in skeuo code
	//println(s"ENUM JSON: enumApache -> skeuoAvro -> Json circe = ${avroToJson(schemeAna(enumApache))}")
	println(s"(ARRAY JSON CIRCE): arrayApache -> arraySkeuo adt -> array Json circe: ${avroToJson(schemeAna(arrayApache))}")
	
	val strSkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(strApache)
	println(s"skeuo avro string = $strSkeuo")
	
	val arraySkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(arrayApache)
	println(s"(ARRAY AVRO SKEUO-ADT): skeuo avro array = $arraySkeuo")
	
	import testUtil.utilZioApache.ApacheToZioFunctions._
	
	
	val arrayZio: Either[String, Any] = apacheAvroSchemaToZioSchema(arrayApache)
	val arrayZio2: Either[String, ZioSchema[_]] = AvroCodec.decodeFromApacheAvro(arrayApache)
	println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio")
	println(s"(ARRAY ZIO-ADT): zio avro array = $arrayZio2")
	
	
	val enumSkeuo: Fix[SchemaSkeuoAvro] = scheme.ana(SchemaSkeuoAvro.fromAvro).apply(enumApache)
	println(s"skeuo avro enum: ${enumSkeuo.toString}")
	println(s"skeuo avro enum unfix: ${enumSkeuo.unfix}")
	
	// inverse:
	val schemeCata: Fix[SchemaSkeuoAvro] ⇒ SchemaApacheAvro = scheme.cata(avroFToApache)
	
	val strApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(strSkeuo)
	println(s"strApacheBack = $strApacheBack ")
	val arrayApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(arraySkeuo)
	println(s"arrayApacheBack = $arrayApacheBack")
	val enumApacheBack: SchemaApacheAvro = scheme.cata(avroFToApache).apply(enumSkeuo) // assert is equal to enumApache
	println(s"enumApacheBack = $enumApacheBack")
	
	
	// --------------------------------------------------------------------------------------------------------------------------------
	
	println("\n----------------------------------------------------------------")
	println("Printing : scala case class --> zio schema --> apache avro string ")
	
	
	// TODO left off here - use another case class (fruit banana example)
	val schema: ZioSchema[Tangelo] = DeriveSchema.gen[Tangelo]
	val adt: Either[String, SchemaApacheAvro] = AvroCodec.encodeToApacheAvro(schema)
	val result: Either[String, String] = AvroCodec.encode(schema)
	
	println(s"zioschema tangelo = $schema")
	println(s"avro adt tangelo (apache) = $adt")
	println(s"avro string tangelo = $result")
	
	
	// TODO left off here - plan idea outlined here:
	// 1) start from wiem el abadine's schemaF to learn to print out the schema from schmeaF using hylomorphism (trick: migrate matryoshka to droste) since hylo is different.
	// 2) feed it values see print out
	// 3) adapt to the skeuomorph AvroF -> SchemaApacheAvro
	// 4) feed THAT values
	// 5) see the differences between the schema (#1)
	// 5) see how the avro string gets printed from the schema
	// 6) repeat for the skeuomorph json side
	// 7) compare syntax between json/avro strings (#2)
	
	
	/**
	 * Wiem El Abadine's `SchemaS` = https://github.com/wi101/recursion-schemes-lc2018/blob/master/src/main/scala/solutions/1-schema.scala#L11-L22
	 */
	
	/**
	 * Skeuomorph's `AvroF` = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L34-L122
	 */
	
	/**
	 * Zio's `Schema` =https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema/shared/src/main/scala/zio/schema/Schema.scala#L287-L373
	 */
	
	// TODO - check what is "Fixed" type in zio / apache avro / skeuomorph - key insight tati says to howt he schemas differ between each other.
	
	// Fixed builder in org.apache.avro = https://github.com/apache/avro/blob/cdfd66fed1cb366400a41aa7dcbec19d1fad8a09/lang/java/avro/src/main/java/org/apache/avro/SchemaBuilder.java#L291
	
	// FixedSchema in org.apache.avro = https://github.com/apache/avro/blob/cdfd66fed1cb366400a41aa7dcbec19d1fad8a09/lang/java/avro/src/main/java/org/apache/avro/Schema.java#L1283
	
	// write fixed from a byte buffer (org.apache avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/Encoder.java#L183
	//read fixed from a byte buffer (org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/Decoder.java#L158
	
	// GenericFixed (org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/Conversions.java#L103
	
	// create fixed reader from writerschema (org.apache.avro) =https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/FastReaderBuilder.java#L500
	
	// writefixed (json encoder file, org.apache.avro) = https://github.com/apache/avro/blob/57f1d5278427e2b095ec8d539d32bb9b309f9e07/lang/java/avro/src/main/java/org/apache/avro/io/JsonEncoder.java#L225
	
	/// ---------------
	
	// Fixed in skeuomorph (avro adt) = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L118
	
	// Fixed in skeuomorph (mu adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/mu/schema.scala#L40
	
	// Fixed in skeuomorph (protobuf adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/protobuf/schema.scala#L81
	
	// Fixed in skeuomorph (protobuf -> mu adt) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/mu/Transform.scala#L41-L44
	
	// Fixed (mu -> avro schema) = https://github.com/higherkindness/skeuomorph/blob/485c49f55e955468053ef4f92ec6c33a33df031b/src/main/scala/higherkindness/skeuomorph/avro/Protocol.scala#L136
	
	// Fixed in skeuomorph (apache avro FIXED --> AvroF Fixed)  = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L229-L235
	
	// skeuomorph Fixed --> io.circe.Json's Fixed string = https://github.com/higherkindness/skeuomorph/blob/main/src/main/scala/higherkindness/skeuomorph/avro/schema.scala#L275-L279
}
