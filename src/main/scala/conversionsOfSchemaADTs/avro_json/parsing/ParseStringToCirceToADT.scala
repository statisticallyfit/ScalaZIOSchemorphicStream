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
		/*parsedApacheAvro: AvroSchema_A,
		parsedApacheAvroStr: String,
		skeuoAvro_fromApache: Fix[AvroSchema_S],
		interimCirce_fromAvro: JsonCirce,*/
		rawAvro: String, rawJson: String,
		interimCirce_fromJson: JsonCirce,
		skInfo: SkeuoDecodeInfo
						  )
		extends Info
	
	case class AvroStringDecodeInfo(
		rawAvro: String,
		rawJson: String,
		parsedApacheAvro: AvroSchema_A,
		parsedApacheAvroStr: String,
		skeuoAvro_fromApache: Fix[AvroSchema_S],
		interimCirce_fromAvro: JsonCirce,
		interimCirce_fromJson: JsonCirce,
		skInfo: SkeuoDecodeInfo
	)
		extends StringDecodeInfo (rawAvro, rawJson, interimCirce_fromJson, skInfo)
		
	
	case class JsonStringDecodeInfo(
		rawAvro: String,
		rawJson: String,
		interimCirce_fromJson: JsonCirce,
		skInfo: SkeuoDecodeInfo
	) extends StringDecodeInfo(rawAvro, rawJson, interimCirce_fromJson, skInfo)
	
	
	case class SkeuoDecodeInfo(skeuoAvro_fromRaw: Result[Fix[AvroSchema_S]],
						  jsonCirce_fromRawAvro: JsonCirce,
						  skeuoAvro_fromDecodingAvro: Result[Fix[AvroSchema_S]],
						  skeuoJson_fromDecodingAvro: Result[Fix[JsonSchema_S]],
						  skeuoJson_fromRaw: Result[Fix[JsonSchema_S]],
						  jsonCirce_fromRawJson: JsonCirce,
						  skeuoAvro_fromDecodingJson: Result[Fix[AvroSchema_S]],
						  skeuoJson_fromDecodingJson: Result[Fix[JsonSchema_S]]
						 )
		extends Info
	
	
	
	private object Info  {
		
		
		def infoStringToCirceToSkeuo(rawAvro: String, rawJson: String): Map[SchemaKind.Value, StringDecodeInfo] = {
			
			// From Avro part
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvro) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro_fromApache: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val interimCirce_fromAvro: JsonCirce = libToJsonAltered(skeuoAvro_fromApache)
			
			// From Json part
			// Circe + Starting Skeuo
			val interimCirce_fromJson: JsonCirce = unsafeParse(rawJson)
			
			
			// Encapsulate the skeuo -> circe -> skeuo parts (SkeuoDecodeInfo) with the beginning str part (StringDecodeInfo).
			val avroInfo: AvroStringDecodeInfo = AvroStringDecodeInfo(rawAvro, rawJson,
				parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro_fromApache,
				interimCirce_fromAvro,
				interimCirce_fromJson,
				skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromAvro))
			
			val jsonInfo: JsonStringDecodeInfo = JsonStringDecodeInfo(rawAvro, rawJson,
				interimCirce_fromJson,
				skInfo = infoSkeuoToCirceToSkeuo(interimCirce_fromJson))
			
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
			val jsonCirce_fromRawAvro: JsonCirce = libToJsonAltered(skeuoAvro_fromRaw.right.get)
			val jsonCirce_fromRawJson: JsonCirce = libRender(skeuoJson_fromRaw.right.get)
			
			// Generate skeuos from the starting skeuos (for showing decoding capability)
			val skeuoAvro_fromDecodingAvro: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromRawAvro)
			val skeuoJson_fromDecodingAvro: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromRawAvro)
			
			val skeuoAvro_fromDecodingJson: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromRawJson)
			val skeuoJson_fromDecodingJson: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromRawJson)
			
			
			SkeuoDecodeInfo(
				skeuoAvro_fromRaw, jsonCirce_fromRawAvro, skeuoAvro_fromDecodingAvro, skeuoJson_fromDecodingAvro,
				skeuoJson_fromRaw, jsonCirce_fromRawJson, skeuoAvro_fromDecodingJson, skeuoJson_fromDecodingJson
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
				.skeuoJson_fromDecodingAvro
		}
		
		def decodeAvroStringToCirceToAvroSkeuo(rawAvro: String, rawJson: String): Result[Fix[AvroSchema_S]] = {
			
			Stepping(rawAvro, rawJson).avroStep
				.skInfo
				.skeuoAvro_fromDecodingAvro
				
		}
		
		def decodeJsonStringToCirceToJsonSkeuo(rawAvro: String, rawJson: String): Result[Fix[JsonSchema_S]] = {
			
			Stepping(rawAvro, rawJson).avroStep
				.skInfo
				.skeuoJson_fromDecodingJson
				
		}
		
		def decodeJsonStringToCirceToAvroSkeuo(rawAvro: String, rawJson: String): Result[Fix[AvroSchema_S]] = {
			
			Stepping(rawAvro, rawJson)
				.jsonStep
				.skInfo
				.skeuoAvro_fromDecodingJson
		}
	}
	
}
