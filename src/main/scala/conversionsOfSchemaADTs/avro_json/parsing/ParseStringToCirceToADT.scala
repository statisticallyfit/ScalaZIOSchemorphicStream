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
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT.Stepping
import utilMain.UtilMain.implicits._

/**
 *
 */
object ParseStringToCirceToADT {
	
	object SchemaSource extends Enumeration {
		type Source = Value
		
		val RawAvro, RawJson = Value
	}
	
	
	
	case class Stepping(rawAvroStr: String, rawJsonStr: String) {
		
		import Stepping._
		//import Stepping.SchemaType._
		
		/*def stepAnyStringToAvroSkeuo(/*rawAvroStr: String, rawJsonStr: String*/): AvroOutputInfo /*(AvroSchema_A, String, Fix[AvroSchema_S], Result[Fix[AvroSchema_S]], JsonCirce, JsonCirce, Result[Fix[AvroSchema_S]], Result[Fix[AvroSchema_S]])*/ = {
			
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			
			val jsonCirce_fromInputJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			val skeuoAvro_fromDecodingJsonStr: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputJsonStr)
			
			val skeuoAvro_fromDecodingAvroStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			val jsonCirce_fromInputAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromDecodingAvroStr)
			
			
			
			import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
			
			val skeuoAvro_fromDecodingAvroSkeuo: Result[Fix[AvroSchema_S]] = decodeAvroSkeuoToCirceToAvroSkeuo(skeuoAvro_fromDecodingAvroStr)
			
			val skeuoAvro_fromDecodingCirce: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputAvroSkeuo)
			
			AvroOutputInfo(parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro_fromDecodingAvroStr,
				skeuoAvro_fromDecodingJsonStr,
				jsonCirce_fromInputAvroSkeuo,
				jsonCirce_fromInputJsonStr,
				skeuoAvro_fromDecodingAvroSkeuo,
				skeuoAvro_fromDecodingCirce
			)
			
		}*/
		
		/*def stepStringToSkeuo() = {
			
			// Parsed apache str
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			
			// Circe + Starting Skeuo
			val jsonCirce_fromInputJsonStr: JsonCirce = unsafeParse(rawJsonStr)
			val skeuoAvro_fromDecodingJsonStr: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputJsonStr)
			val skeuoJson_fromDecodingJsonStr: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromInputJsonStr)
			
			val skeuoAvro_fromApache: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			val jsonCirce_fromInputAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromApache)
			val skeuoAvro_fromDecodingAvroStr: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputAvroSkeuo)
			val skeuoJson_fromDecodingAvroStr: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromInputAvroSkeuo)
			
			
			// Ending skeuo
			val skeuoJson_fromDecodingAvroSkeuo: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoToCirceToJsonSkeuo(skeuoAvro_fromDecodingAvroStr.right.get)
			
			val skeuoJson_fromDecodingCirce: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromInputAvroSkeuo)
			//import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.TransSchemaImplicits.skeuoEmbed_JA
			
			val skeuoAvro_fromDecodingAvroSkeuo: Result[Fix[AvroSchema_S]] = decodeAvroSkeuoToCirceToAvroSkeuo(skeuoAvro_fromApache)
			
			val skeuoAvro_fromDecodingCirce: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputAvroSkeuo)
			
			JsonOutputInfo(parsedApacheAvro,
				parsedApacheAvroStr,
				skeuoAvro_fromApache,
				skeuoAvro_fromDecodingJsonStr,
				jsonCirce_fromInputAvroSkeuo,
				jsonCirce_fromInputJsonStr,
				skeuoJson_fromDecodingAvroSkeuo,
				skeuoJson_fromDecodingCirce
			)
		}*/
		
		
		
		
		def stepStringToCirceToSkeuo(): Map[SchemaSource.Value, StringDecodeInfo] = {
			
			// From Avro part
			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
			
			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
			
			val skeuoAvro_fromApache: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
			
			val interimCirce_fromAvro: JsonCirce = libToJsonAltered(skeuoAvro_fromApache)
			
			// From Json part
			// Circe + Starting Skeuo
			val interimCirce_fromJson: JsonCirce = unsafeParse(rawJsonStr)
			
			val avroInfo: StringDecodeInfo = StringDecodeInfo(parsedApacheAvro, parsedApacheAvroStr, skeuoAvro_fromApache, interimCirce_fromAvro, interimCirce_fromJson, info = stepSkeuoToCirceToSkeuo(interimCirce_fromAvro))
			
			val jsonInfo: StringDecodeInfo = StringDecodeInfo(parsedApacheAvro, parsedApacheAvroStr, skeuoAvro_fromApache, interimCirce_fromAvro, interimCirce_fromJson, info = stepSkeuoToCirceToSkeuo(interimCirce_fromJson))
			
			Map(
				SchemaSource.RawAvro → avroInfo,
				SchemaSource.RawJson → jsonInfo
			)
			
		}
		/**
		 *
		 * @param interimCirce = the intermediate json circe that gets generated from the beginning avro / json strings. Used here to generate the skeuomorphs.
		 */
		def stepSkeuoToCirceToSkeuo(interimCirce: JsonCirce): SkeuoDecodeInfo = {
			
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
		
		
		
		
		//		def stepAnyStringToJsonSkeuo(/*rawAvroStr: String, rawJsonStr: String*/): JsonOutputInfo = {
//
//			/*val parsedJsonCirce: JsonCirce = unsafeParse(rawJsonStr)
//
//			val skeuoJson_decoded: Result[Fix[JsonSchema_S]] = Decoder[Fix[JsonSchema_S]].decodeJson(parsedJsonCirce)
//
//			(parsedJsonCirce,
//				skeuoJson_decoded)*/
//
//			val parsedApacheAvro: AvroSchema_A = new AvroSchema_A.Parser().parse(rawAvroStr) // TODO option for parsing failure?
//
//			val parsedApacheAvroStr: String = parsedApacheAvro.toString(true).manicure
//
//
//
//			val jsonCirce_fromInputJsonStr: JsonCirce = unsafeParse(rawJsonStr)
//			val skeuoAvro_fromDecodingJsonStr: Result[Fix[AvroSchema_S]] = funcCirceToAvroSkeuo(jsonCirce_fromInputJsonStr)
//
//			val skeuoAvro_fromDecodingAvroStr: Fix[AvroSchema_S] = apacheToSkeuoAvroSchema(parsedApacheAvro)
//			val jsonCirce_fromInputAvroSkeuo: JsonCirce = libToJsonAltered(skeuoAvro_fromDecodingAvroStr)
//
//
//
//			val skeuoJson_fromDecodingAvroSkeuo: Result[Fix[JsonSchema_S]] = decodeAvroSkeuoToCirceToJsonSkeuo(skeuoAvro_fromDecodingAvroStr)
//
//			val skeuoJson_fromDecodingCirce: Result[Fix[JsonSchema_S]] = funcCirceToJsonSkeuo(jsonCirce_fromInputAvroSkeuo)
//
//			JsonOutputInfo(parsedApacheAvro,
//				parsedApacheAvroStr,
//				skeuoAvro_fromDecodingAvroStr,
//				skeuoAvro_fromDecodingJsonStr,
//				jsonCirce_fromInputAvroSkeuo,
//				jsonCirce_fromInputJsonStr,
//				skeuoJson_fromDecodingAvroSkeuo,
//				skeuoJson_fromDecodingCirce
//			)
//		}
		
		val jsonStep: StringDecodeInfo = Stepping(rawAvroStr, rawJsonStr).stepStringToCirceToSkeuo()(SchemaSource.RawAvro)
		
		val avroStep: StringDecodeInfo = Stepping(rawAvroStr, rawJsonStr).stepStringToCirceToSkeuo()(SchemaSource.RawJson)
	}
	
	object Stepping  {
		
		
		case class SkeuoDecodeInfo(skeuoAvro_fromRaw: Result[Fix[AvroSchema_S]], jsonCirce_fromRawAvro: JsonCirce, skeuoAvro_fromDecodingAvro: Result[Fix[AvroSchema_S]], skeuoJson_fromDecodingAvro: Result[Fix[JsonSchema_S]], skeuoJson_fromRaw: Result[Fix[JsonSchema_S]], jsonCirce_fromRawJson: JsonCirce, skeuoAvro_fromDecodingJson: Result[Fix[AvroSchema_S]], skeuoJson_fromDecodingJson: Result[Fix[JsonSchema_S]])
		
		
		case class StringDecodeInfo(parsedApacheAvro: AvroSchema_A, parsedApacheAvroStr: String, skeuoAvro_fromApache: Fix[AvroSchema_S], interimCirce_fromAvro: JsonCirce, interimCirce_fromJson: JsonCirce, info: SkeuoDecodeInfo)
		
	}
	
	
	case class Decoding(rawAvroStr: String, rawJsonStr: String) {
		
		import Stepping._
		
		def decodeAvroStringToCirceToJsonSkeuo(rawAvroStr: String, rawJsonStr: String): Result[Fix[JsonSchema_S]] = {
			
			jsonStep.skeuoJson_fromDecodingAvro
		}
		
		def decodeAvroStringToCirceToAvroSkeuo(rawAvroStr: String, rawJsonStr: String): Result[Fix[AvroSchema_S]] = {
			
			avroStep.skeuoAvro_fromDecodingAvro
		}
		
		def decodeJsonStringToCirceToJsonSkeuo(rawAvroStr: String, rawJsonStr: String): Result[Fix[JsonSchema_S]] = {
			
			
			jsonStep.skeuoJson_fromDecodingJson
		}
		
		def decodeJsonStringToCirceToAvroSkeuo(rawAvroStr: String, rawJsonStr: String): Result[Fix[AvroSchema_S]] = {
			
			avroStep.skeuoAvro_fromDecodingJson
		}
	}
	
	
	
}
