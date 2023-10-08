package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT
import ParseADTToCirceToADT._
import ParseADTToCirceToADT.CirceAvroToSkeuoAvro._
import ParseADTToCirceToADT.CirceAvroToSkeuoJson._
import ParseADTToCirceToADT.CirceJsonToSkeuoJson._
import ParseADTToCirceToADT.CirceJsonToSkeuoAvro._
import ParseADTToCirceToADT.LibFuncs._

import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF => AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF => JsonSchema_S}

import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._
import testData.schemaData.jsonData.circeData.Data._

import utilMain.UtilMain.implicits._


import io.circe.Decoder.Result
import io.circe.{Json => JsonCirce}


import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._

/**
 *
 */
class AvroToJson_SkeuoSkeuo_Specs extends  AnyFunSpec with Matchers with TraitInheritFunSpecAndMatchers {

	info(s"VARIABLE PRINT OUTS")

	val args_avroSkeuo: List[Fix[AvroSchema_S]] = List(nullAvro_Fix_S, strAvro_Fix_S, intAvro_Fix_S, booleanAvro_Fix_S, longAvro_Fix_S, floatAvro_Fix_S, doubleAvro_Fix_S, bytesAvro_Fix_S,
		array1IntAvro_Fix_S, array1StrAvro_Fix_S, array3IntAvro_Fix_S,
		map1IntAvro_Fix_S, map1StrAvro_Fix_S, map3IntAvro_Fix_S,
		recordStrAvro_Fix_S,
		recordExPositionAvro_Fix_S,// recordExLocationAvro_Fix_S
	)

	val args_avroStr: List[String] = List(nullAvro_R, strAvro_R, intAvro_R, booleanAvro_R, longAvro_R, floatAvro_R, doubleAvro_R, bytesAvro_R,
		array1IntAvro_R, array1StrAvro_R, array3IntAvro_R,
		map1IntAvro_R, map1StrAvro_R, map3IntAvro_R,
		recordStrAvro_R,
		recordExPositionAvro_conv_R//, recordExLocationAvro_R
	)



	val args_jsonSkeuo: List[Fix[JsonSchema_S]] = List(nullJson_Fix_S, strJson_Fix_S, intJson_Fix_S, booleanJson_Fix_S, longJson_Fix_S, floatJson_Fix_S, doubleJson_Fix_S, bytesJson_Fix_S,
		array1IntJson_Fix_S, array1StrJson_Fix_S, array3IntJson_Fix_S,
		//map1IntJson_Fix_S, map1StrJson_Fix_S, map3IntJson_Fix_S,
		//recordStrJson_Fix_S,
		recordExPositionJson_Fix_S, recordExLocationJson_Fix_S
	)






	def testCirceToSkeuo(title: String, theArgAvro: Fix[AvroSchema_S], theArgJson: Fix[JsonSchema_S]) ={

		info(s"\nTesting this kind: ${title}")

		val ca: JsonCirce = libToJsonAltered(theArgAvro)
		info(s"\n avro-skeuo ---> circe: \n${ca}")
		//val theAvroSkeuo = funcCirceToAvroSkeuo(theCirce)
		info(s"\navro-skeuo --> circe --> avro-skeuo: ${funcCirceAvroToSkeuoAvro(ca)}")
		info(s"\navro-skeuo --> circe --> json-skeuo: ${funcCirceAvroToSkeuoJson(ca)}")
		/*info(s"\navro-skeuo --> circe --> avro-skeuo: ${DecodingSkeuo.decodeAvroSkeuoToCirceToAvroSkeuo(theArgAvro)}")
		info(s"\navro-skeuo --> circe --> json-skeuo: ${DecodingSkeuo.decodeAvroSkeuoToCirceToJsonSkeuo(theArgAvro)}")*/


		// json-skeuo --> circe
		val cj: JsonCirce = libRenderAltered(theArgJson)
		info(s"\njson-skeuo -> circe: \n${cj}")
		// json-skeuo --> avro-skeuo
		info(s"\njson-skeuo --> circe -> avro-skeuo: ${funcCirceJsonToSkeuoAvro(cj)}")
		info(s"\njson-skeuo --> circe -> json-skeuo: ${funcCirceJsonToSkeuoJson(cj)}")
		/*info(s"\njson-skeuo --> circe -> avro-skeuo: ${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(theArgJson)}")
		info(s"\njson-skeuo --> circe -> json-skeuo: ${DecodingSkeuo.decodeJsonSkeuoToCirceToJsonSkeuo(theArgJson)}")*/
	}

	// HELP: not converting avro-skeuo -> circe -> skeuo properly (result is null)

	def testCONTROLLEDDECODER_CirceToSkeuo(arg: Fix[AvroSchema_S]) = {

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.implicitsForSkeuoAlgCoalg._
		import embedImplicits.skeuoEmbed_AA
		import projectImplicits.skeuoProject_AA
		import io.circe.Decoder

		import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.{implicitsForDialects => impl}

		//import impl.Decoder_InputJsonDialect_OutputAvroSkeuo._
		import impl.Decoder_InputAvroDialect_OutputAvroSkeuo._
		//import impl.Decoder_InputJsonDialect_OutputJsonSkeuo._

		val decoderI: AvroDialect ⇒ Result[Fix[AvroSchema_S]] = Decoder[Fix[AvroSchema_S]](identifyAvroDecoderWithPriorityBasicDecoder).decodeJson(_)

		val avroStrCirce: AvroDialect = libToJsonAltered(arg)
		val as = decoderI(avroStrCirce)
		val js = decoderI(avroStrCirce)

		info("\n\nCONTROLLED DECODER: " +
			s"\navro-skeuo -> circe -> avro-skeuo: ${as}" +
			s"\navro-skeuo -> circe -> json-skeuo: ${js}")
	}



	def testSkeuoToCirceToApacheToSkeuoAgain(arg: Fix[AvroSchema_S]) = {
		import org.apache.avro.{Schema => AvroSchema_A}
		import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache
		type AvroDialectStr = String

		// Skeuo avro -> circe -> string -> apache -> skeuo avro
		val avroCirce: AvroDialect = libToJsonAltered(arg)
		val avroStr: AvroDialectStr = avroCirce.toString()
		val avroApache: AvroSchema_A = new AvroSchema_A.Parser().parse(avroStr)
		// STEP 2: convert apache-avro -> skeuo-avro
		val avroSkeuo: Fix[AvroSchema_S] = Skeuo_Apache.apacheToSkeuoAvroSchema(avroApache)

		info(s"\n\nAPACHE STRING TO SKEUO: ${avroSkeuo}")
	}


	testCirceToSkeuo("map : skeuo -> circe -> skeuo",
		//map3IntAvro_Fix_S,
		//nullAvro_Fix_S,
		//array1IntAvro_Fix_S,
		//namedTypeAvro_Fix_S, //unionAvro_R, //fixedAvro_R,
		intAvro_Fix_S,
		//map1IntAvro_Fix_S,
		//enumAvro_Fix_S,
		//recordExPositionAvro_Fix_S,
		//map1PosRecordAvro_Fix_S,
		//namedTypeAvro_Fix_S,
		map1PosRecordJson_Fix_S
		//nullJson_Fix_S,
		//array1IntJson_Fix_S
		//enumJson_Fix_S
		//map1IntJson_Fix_S
		//intJson_Fix_S
	)

	testCONTROLLEDDECODER_CirceToSkeuo(array1IntAvro_Fix_S)

	testSkeuoToCirceToApacheToSkeuoAgain(array1IntAvro_Fix_S)


	// NOTE first debug object simple first to see how decodermap is extracting the fields - only then do object map (below)
	/*val stpJsonLocation = JsonStepping(recordExLocationJson_R).jsonInfoOpt.get
	info(s"\n\n\njson circe -> avro-skeuo: ${stpJsonLocation.skInfo.skeuoAvro_fromRaw}")
	info(s"s\njson circe -> json-skeuo: ${stpJsonLocation.skInfo.skeuoJson_fromRaw}")
	//------------

	val stpA = AvroStepping(map1IntAvro_R).avroInfoOpt.get

	// TODO OBJECTMAPMAKER GIVES ERROR BECAUSE NO CORRECT? DECODER FOR ADDITIONAL PROPERTIES?
	// NOTE debugging object map
	val stpJ = JsonStepping(map1IntJson_R).jsonInfoOpt.get

	info(s"\n\n\nstr -> apache: ${stpA.parsedApacheAvroStr}")
	info(s"apache -> skeuo: ${stpA.skeuoAvro_fromApache}")
	info(s"skeuo -> circe: ${stpA.interimCirce_fromAvroSKeuo}")
	info(s"circe -> avro-skeuo: ${stpA.skInfo.skeuoAvro_fromRaw }")
	info(s"circe -> json-skeuo: ${stpA.skInfo.skeuoJson_fromRaw }")*/

	//printAvroSkeuoToAvroString(List(enumAvro_Fix_S))
	//printAvroStringToCirceToAvroSkeuo(List(enumAvro_R))
	//printAvroStringToCirceToJsonSkeuo(List(enumAvro_R))
	//printJsonStringToCirceToAvroSkeuo(List(enumJson_R))
	//printJsonStringToCirceToJsonSkeuo(List(enumJson_R))


	// TODO MAJOR NOW
	// 1. make tocirceavrostring_fromAvroSkeuo function - check if it works with int, string, array, map
	// 2. add record, enum -- check if working still
	// 3. Make sure this is the one that gets called in the funcCirceAvro function
	// 4. if this tocirceavrostring_fromAvroSkeuo does nto work: consider doing tocirceavrostring_fromJsonSkeuo (and the other combo as well).


	def printAvroStringToCirceToAvroSkeuo(listAvroStrings: List[String]): Unit = {

		def printer(arg: String) = info(s"${DecodingStr.decodeAvroStringToCirceToAvroSkeuo(arg)}")

		listAvroStrings.map(printer(_))
	}

	def printAvroStringToCirceToJsonSkeuo(listAvroStrings: List[String]): Unit = {

		def printer(arg: String) = info(s"${DecodingStr.decodeAvroStringToCirceToJsonSkeuo(arg)}")

		listAvroStrings.map(printer(_))
	}

	def printJsonStringToCirceToAvroSkeuo(listJsonStrings: List[String]): Unit = {

		def printer(arg: String) = info(s"${DecodingStr.decodeJsonStringToCirceToAvroSkeuo(arg)}")

		listJsonStrings.map(printer(_))
	}

	def printJsonStringToCirceToJsonSkeuo(listJsonStrings: List[String]): Unit = {

		def printer(arg: String) = info(s"${DecodingStr.decodeJsonStringToCirceToJsonSkeuo(arg)}")

		listJsonStrings.map(printer(_))
	}

	////////------------------------------

	def printAvroSkeuoToAvroString(listAvroSkeuo: List[Fix[AvroSchema_S]]): Unit = {

		def printer(arg: Fix[AvroSchema_S]) = info(s"${skeuoToApacheAvroSchema(arg).toString(true).manicure}")

		listAvroSkeuo.map(arg ⇒ printer(arg))
	}

	def printAvroSkeuoToCirceJsonString(listAvroSkeuo: List[Fix[AvroSchema_S]]): Unit = {
		def printer(arg: Fix[AvroSchema_S]) = info(libToJsonAltered(arg).manicure)

		listAvroSkeuo.map(arg ⇒ printer(arg))
	}



	/// ------------------------

	def printJsonSkeuoToAvroString(listJsonSkeuo: List[Fix[JsonSchema_S]]): Unit = {

		// json skeuo -> circe -> avro-skeuo -> apache -> avro-str
		def printer(arg: Fix[JsonSchema_S]) = {
			val avroSkeuo: Result[Fix[AvroSchema_S]] = DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(arg)

			// TODO how to gracefully get the value (error if Left)
			val avroStr: String = skeuoToApacheAvroSchema(avroSkeuo.right.get).toString(true).manicure

			info(s"${avroStr}")
		}

		listJsonSkeuo.map(printer(_))
	}

	def printJsonSkeuoToCirceJsonString(listJsonSkeuo: List[Fix[JsonSchema_S]]): Unit = {

		// json skeuo --> circe --> json-str
		def printer(arg: Fix[JsonSchema_S]): Unit = {
			info(libRenderAltered(arg).manicure)
		}

		listJsonSkeuo.map(printer(_))
	}

	// ------------------

	def printAvroSkeuoToJsonSkeuo(listAvroSkeuo: List[Fix[AvroSchema_S]]): Unit = {

		// avro-skeuo --> circe (decoder) --> json-skeuo
		def printer(arg: Fix[AvroSchema_S]) = info(s"${DecodingSkeuo.decodeAvroSkeuoToCirceToJsonSkeuo(arg)}")

		listAvroSkeuo.map(printer(_))
	}

	def printJsonSkeuoToAvroSkeuo(listJsonSkeuo: List[Fix[JsonSchema_S]]): Unit = {

		// json-skeuo --> circe (decoder) --> avro-skeuo
		def printer(arg: Fix[JsonSchema_S]) = info(s"${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(arg)}")

		listJsonSkeuo.map(printer(_))
	}

	/*info(s"record position (json circe): \n${libRenderAltered(recordEXPositionJson_Fix_S).manicure}")
	info(s"record location (json circe): \n${libRenderAltered(recordEXLocationJson_Fix_S).manicure}")
	info(s"record position (avro-skeuo): \n${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(recordEXPositionJson_Fix_S)}")
	info(s"record location (avro-skeuo): \n${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(recordEXLocationJson_Fix_S)}")
	info(s"record position (avro-str): \n${skeuoToApacheAvroSchema(DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(recordEXPositionJson_Fix_S).right.get).toString(true).manicure}")
	info(s"record location (avro-str): \n${skeuoToApacheAvroSchema(DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(recordEXLocationJson_Fix_S).right.get).toString(true).manicure}")*/

	/*testStructure(scenarioType = "null type",
		rawAvroStr = nullAvro_R,
		rawJsonStr = nullJson_R,
		jsonCirceCheck = nullJson_C,
		avroS = nullAvro_S, tpeS = "AvroSchema_S[Null]",
		avroC = nullAvro_Circe_S, tpeC = "AvroSchema_S[JsonCirce]",
		avroFixS = nullAvro_Fix_S,
		jsonFixS = nullJson_Fix_S
	)*/


	/*testStructure(scenarioType = "integer type",
		rawAvroStr = intAvro_R,
		rawJsonStr = intJson_R,
		jsonCirceCheck = intJson_C,
		avroS = intAvro_S, tpeS = "AvroSchema_S[Int]",
		avroC = intAvro_Circe_S, tpeC = "AvroSchema_S[JsonCirce]",
		avroFixS = intAvro_Fix_S,
		jsonFixS = intJson_Fix_S
	)

	testStructure(scenarioType = "string type",
		rawAvroStr = strAvro_R,
		rawJsonStr = strJson_R,
		jsonCirceCheck = strJson_C,
		avroS = strAvro_S, tpeS = "AvroSchema_S[String]",
		avroC = strAvro_Circe_S, tpeC = "AvroSchema_S[JsonCirce]",
		avroFixS = strAvro_Fix_S,
		jsonFixS = strJson_Fix_S
	)*/

	// TODO
	// HELP - how to call the individual spec classes from here, programmatically?

	val pckName = "conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs"
	val arrPath = pckName + "." + "Array1IntSpecs"

	// HELP - way 1
//	import org.scalatest.tools.Runner
//	Runner.run(Array(arrPath))

	// HELP - way 2
	//val b = new BooleanSpecs
//	val arr = new Array1IntSpecs
//
//
//	import org.scalatest.{Args, events, Reporter}
//
//	val rep: Reporter = new Reporter { def apply(e:events.Event) = info(s"MESSAGE FROM REPORTER: ${e}") }
//
//	arr.run(Some(arrPath), Args(rep)).succeeds

	/*val argsArray1Int: ExplicitArgs = new ExplicitArgs(rawAvroStr = array1IntAvro_R,
		rawJsonStr = array1IntJson_R,
		jsonCirceCheck = array1IntJson_C,
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFixS = array1IntAvro_Fix_S,
		jsonFixS = array1IntJson_Fix_S
	)


	TestFramework.testStructure(scenarioType = "array of int")(argsArray1Int)
	TestFramework.printOuts(scenarioType = "array of int")(argsArray1Int)*/


}
