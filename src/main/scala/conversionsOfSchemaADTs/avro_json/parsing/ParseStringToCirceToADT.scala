package conversionsOfSchemaADTs.avro_json.parsing


import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import higherkindness.skeuomorph.openapi.JsonDecoders._

import org.apache.avro.{Schema ⇒ AvroSchema_A}

import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache.apacheToSkeuoAvroSchema

import io.circe.Decoder.Result
import io.circe.{Decoder, Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT.{Info, SchemaKind}

import utilMain.UtilMain.implicits._

/**
 *
 */
object ParseStringToCirceToADT {
	
	object SchemaKind extends Enumeration {
		type Source = Value
		
		val RawAvro, RawJson = Value
	}
	
	
	
	abstract class Info
	
	abstract class StringDecodeInfo(
		rawAvro: String, rawJson: String,
		interimCirce: JsonCirce,
		skInfo: SkeuoDecodeInfo
						  )
		extends Info
	
	case class AvroStringDecodeInfo(
		rawAvro: String,
		rawJsonOpt: Option[String],
		parsedApacheAvro: AvroSchema_A,
		parsedApacheAvroStr: String,
		skeuoAvro_fromApache: Fix[AvroSchema_S],
		interimCirce_fromAvroSKeuo: JsonCirce,
		skInfo: SkeuoDecodeInfo
	)
		extends StringDecodeInfo (rawAvro, if(rawJsonOpt.isDefined) rawJsonOpt.get.trim else "", interimCirce_fromAvroSKeuo, skInfo)
		
	
	case class JsonStringDecodeInfo(
		rawAvroOpt: Option[String],
		rawJson: String,
		interimCirce_fromJsonStr: JsonCirce,
		skInfo: SkeuoDecodeInfo
	) extends StringDecodeInfo(if(rawAvroOpt.isDefined) rawAvroOpt.get.trim else "", rawJson, interimCirce_fromJsonStr, skInfo)
	
	
	case class SkeuoDecodeInfo(skeuoAvro_fromRaw: Result[Fix[AvroSchema_S]],
	                           jsonCirce_fromAvroSkeuo: JsonCirce,
	                           skeuoAvro_fromDecodeAvroSkeuo: Result[Fix[AvroSchema_S]],
	                           skeuoJson_fromDecodeAvroSkeuo: Result[Fix[JsonSchema_S]],
	                           skeuoJson_fromRaw: Result[Fix[JsonSchema_S]],
	                           jsonCirce_fromJsonSkeuo: JsonCirce,
	                           skeuoAvro_fromDecodeJsonSkeuo: Result[Fix[AvroSchema_S]],
	                           skeuoJson_fromDecodeJsonSkeuo: Result[Fix[JsonSchema_S]]
						 )
		extends Info
	
	
	
	private object Info  {
		
		
		def infoStringToCirceToSkeuo(rawAvroOpt: Option[String], rawJsonOpt: Option[String]): Map[SchemaKind.Value, Option[StringDecodeInfo]] = {
			
			
			// Encapsulate the skeuo -> circe -> skeuo parts (SkeuoDecodeInfo) with the beginning str part (StringDecodeInfo).
			
			val avroInfoOpt: Option[AvroStringDecodeInfo] = rawAvroOpt.isDefined match {
				case false ⇒ None
				case true ⇒ {
					
					val rawAvro: String = rawAvroOpt.get.trim
					
					val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvro) // TODO option for parsing failure?
					
					val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
					
					val skeuoAvro_fromApache: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
					
					val interimCirce_fromAvroRaw: JsonCirce = libToJsonAltered(skeuoAvro_fromApache)
					
					Some(AvroStringDecodeInfo(rawAvro, rawJsonOpt,
						parsedApacheAvro,
						parsedApacheAvroStr,
						skeuoAvro_fromApache,
						interimCirce_fromAvroRaw,
						skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromAvroRaw)
					))
				}
			}
			
			
			val jsonInfoOpt: Option[JsonStringDecodeInfo] = rawJsonOpt.isDefined match {
				case false ⇒ None
				case true ⇒ {
					
					// Circe + Starting Skeuo
					val interimCirce_fromJsonRaw: JsonCirce = unsafeParse(rawJsonOpt.get)
					
					Some(JsonStringDecodeInfo(rawAvroOpt, rawJsonOpt.get,
						interimCirce_fromJsonRaw,
						skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromJsonRaw)
					))
				}
			}
			
			Map(
				SchemaKind.RawAvro → avroInfoOpt,
				SchemaKind.RawJson → jsonInfoOpt
			)
			
		}
		
		/**
		 *
		 * @param interimCirce = the intermediate json circe that gets generated from the beginning avro / json strings. Used here to generate the skeuomorphs.
		 */
		def infoSkeuoToCirceToSkeuo(interimCirce: JsonCirce): SkeuoDecodeInfo = {
			
			// Create the skeuos from str (using the interimCirce)
			val skeuoAvro_fromRaw: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(interimCirce)
			val skeuoJson_fromRaw: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(interimCirce)
			
			
			// Generate circes from the starting skeuos
			val jsonCirce_fromAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromRaw.right.get)
			val jsonCirce_fromJsonSkeuo: JsonCirce = libRender(skeuoJson_fromRaw.right.get)
			
			// Generate skeuos from the starting skeuos (for showing decoding capability)
			val skeuoAvro_fromDecodeAvroSkeuo: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromAvroSkeuo)
			val skeuoJson_fromDecodeAvroSkeuo: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromAvroSkeuo)
			
			val skeuoAvro_fromDecodeJsonSkeuo: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromJsonSkeuo)
			val skeuoJson_fromDecodeJsonSkeuo: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromJsonSkeuo)
			
			
			SkeuoDecodeInfo(
				skeuoAvro_fromRaw, jsonCirce_fromAvroSkeuo, skeuoAvro_fromDecodeAvroSkeuo, skeuoJson_fromDecodeAvroSkeuo,
				skeuoJson_fromRaw, jsonCirce_fromJsonSkeuo, skeuoAvro_fromDecodeJsonSkeuo, skeuoJson_fromDecodeJsonSkeuo
			)
		}
	}
	
	
	
	
	class Stepping(rawAvroOpt: Option[String],
	                    rawJsonOpt: Option[String]) {
		
		val info: Map[SchemaKind.Value, Option[StringDecodeInfo]] = Info.infoStringToCirceToSkeuo(rawAvroOpt, rawJsonOpt)
		
		// RULE: if starting from avro string in the test, then use avrostep, else if starting with json string in the test,  use jsonstep.
		
		val avroInfoOpt: Option[AvroStringDecodeInfo] = info(SchemaKind.RawAvro).asInstanceOf[Option[AvroStringDecodeInfo]]
		
		val jsonInfoOpt: Option[JsonStringDecodeInfo] = info(SchemaKind.RawJson).asInstanceOf[Option[JsonStringDecodeInfo]]
		
		// bad - recursive loop - stackoverflow error since these classes inherit from Stepping
		/*val avroStep: AvroStepping = AvroStepping(rawAvroOpt.get, rawJsonOpt)
		val jsonStep: JsonStepping = JsonStepping(rawJsonOpt.get, rawAvroOpt)*/
	}
	
	case class AvroStepping(
		rawAvro: String,
		rawJsonOpt: Option[String] = None
	) extends Stepping(Some(rawAvro), rawJsonOpt) {
		
		/*val avroInfo: AvroStringDecodeInfo = info(SchemaKind.RawAvro).asInstanceOf[AvroStringDecodeInfo]*/
	}
	
	case class JsonStepping(
		rawJson: String,
		rawAvroOpt: Option[String] = None
	) extends Stepping(rawAvroOpt, Some(rawJson)) {
		
		/*val jsonInfo: JsonStringDecodeInfo = info(SchemaKind.RawJson).asInstanceOf[JsonStringDecodeInfo]*/
	}
	
	
	object DecodingStr {
		
		//private lazy val step: Stepping = Stepping(rawAvro, rawJson)
		
		// NOTE: the 'circe' in the title is not the interim circe, so must treat the first skeuo as the FIRST one and then the last skeuo as the one we want to access
		
		def decodeAvroStringToCirceToJsonSkeuo(rawAvro: String, rawJsonOpt: Option[String] = None): Result[Fix[JsonSchema_S]] = {
			
			// NOTE: the beauty of having a separate AvroStepping class, not just Stepping, is that here can be sure that the option (avroInfoOpt) is defined (because the class is Avro-type so the arg-str that is passed is a plain avro-str and not an option (like for json))
			
			AvroStepping(rawAvro, rawJsonOpt).avroInfoOpt.get
				.skInfo
				.skeuoJson_fromDecodeAvroSkeuo
		}
		
		def decodeAvroStringToCirceToAvroSkeuo(rawAvro: String, rawJsonOpt: Option[String] = None): Result[Fix[AvroSchema_S]] = {
			
			AvroStepping(rawAvro, rawJsonOpt).avroInfoOpt.get
				.skInfo
				.skeuoAvro_fromDecodeAvroSkeuo
				
		}
		
		def decodeJsonStringToCirceToJsonSkeuo(rawJson: String, rawAvroOpt: Option[String] = None): Result[Fix[JsonSchema_S]] = {
			
			JsonStepping(rawJson, rawAvroOpt).jsonInfoOpt.get
				.skInfo
				.skeuoJson_fromDecodeJsonSkeuo
				
		}
		
		def decodeJsonStringToCirceToAvroSkeuo(rawJson: String, rawAvroOpt: Option[String] = None): Result[Fix[AvroSchema_S]] = {
			
			JsonStepping(rawJson, rawAvroOpt).jsonInfoOpt.get
				.skInfo
				.skeuoAvro_fromDecodeJsonSkeuo
		}
	}
	
}
