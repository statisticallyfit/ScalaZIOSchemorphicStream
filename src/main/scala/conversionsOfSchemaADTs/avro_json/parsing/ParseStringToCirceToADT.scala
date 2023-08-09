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
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT.{SchemaKind, Info}
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
		rawJson: String,
		parsedApacheAvro: AvroSchema_A,
		parsedApacheAvroStr: String,
		skeuoAvro_fromApache: Fix[AvroSchema_S],
		interimCirce: JsonCirce,
		skInfo: SkeuoDecodeInfo
	)
		extends StringDecodeInfo (rawAvro, rawJson, interimCirce, skInfo)
		
	
	case class JsonStringDecodeInfo(
		rawAvro: String,
		rawJson: String,
		interimCirce: JsonCirce,
		skInfo: SkeuoDecodeInfo
	) extends StringDecodeInfo(rawAvro, rawJson, interimCirce, skInfo)
	
	
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
		
		
		def infoStringToCirceToSkeuo(rawAvro: String, rawJson: String): Map[SchemaKind.Value, StringDecodeInfo] = {
			
			// From Avro part
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvro) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro_fromApache: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val interimCirce_fromAvroRaw: JsonCirce = libToJsonAltered(skeuoAvro_fromApache)
			
			// From Json part
			// Circe + Starting Skeuo
			val interimCirce_fromJsonRaw: JsonCirce = unsafeParse(rawJson)
			
			
			// Encapsulate the skeuo -> circe -> skeuo parts (SkeuoDecodeInfo) with the beginning str part (StringDecodeInfo).
			val avroInfo: AvroStringDecodeInfo = AvroStringDecodeInfo(rawAvro, rawJson,
				parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro_fromApache,
				interimCirce_fromAvroRaw,
				skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromAvroRaw))
			
			val jsonInfo: JsonStringDecodeInfo = JsonStringDecodeInfo(rawAvro, rawJson,
				interimCirce_fromJsonRaw,
				skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromJsonRaw))
			
			Map(
				SchemaKind.RawAvro → avroInfo,
				SchemaKind.RawJson → jsonInfo
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
	
	
	case class Stepping(rawAvro: String, rawJson: String) {
		
		val info: Map[SchemaKind.Value, StringDecodeInfo] = Info.infoStringToCirceToSkeuo(rawAvro, rawJson)
		
		// RULE: if starting from avro string in the test, then use avrostep, else if starting with json string in the test,  use jsonstep.
		
		val jsonStep: JsonStringDecodeInfo = info(SchemaKind.RawJson).asInstanceOf[JsonStringDecodeInfo]
		
		val avroStep: AvroStringDecodeInfo = info(SchemaKind.RawAvro).asInstanceOf[AvroStringDecodeInfo]
	}
	
	
	object Decoding {
		
		//private lazy val step: Stepping = Stepping(rawAvro, rawJson)
		
		// NOTE: the 'circe' in the title is not the interim circe, so must treat the first skeuo as the FIRST one and then the last skeuo as the one we want to access
		
		def decodeAvroStringToCirceToJsonSkeuo(rawAvro: String, rawJson: String): Result[Fix[JsonSchema_S]] = {
			
			
			Stepping(rawAvro, rawJson).avroStep
				.skInfo
				.skeuoJson_fromDecodeAvroSkeuo
		}
		
		def decodeAvroStringToCirceToAvroSkeuo(rawAvro: String, rawJson: String): Result[Fix[AvroSchema_S]] = {
			
			Stepping(rawAvro, rawJson).avroStep
				.skInfo
				.skeuoAvro_fromDecodeAvroSkeuo
				
		}
		
		def decodeJsonStringToCirceToJsonSkeuo(rawAvro: String, rawJson: String): Result[Fix[JsonSchema_S]] = {
			
			Stepping(rawAvro, rawJson).avroStep
				.skInfo
				.skeuoJson_fromDecodeJsonSkeuo
				
		}
		
		def decodeJsonStringToCirceToAvroSkeuo(rawAvro: String, rawJson: String): Result[Fix[AvroSchema_S]] = {
			
			Stepping(rawAvro, rawJson)
				.jsonStep
				.skInfo
				.skeuoAvro_fromDecodeJsonSkeuo
		}
	}
	
}
