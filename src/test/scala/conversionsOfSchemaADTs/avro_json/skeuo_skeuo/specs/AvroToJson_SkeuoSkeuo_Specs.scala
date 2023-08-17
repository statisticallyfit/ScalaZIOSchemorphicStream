package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._
import testData.schemaData.jsonData.circeData.Data._
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._
import utilMain.UtilMain
import utilMain.UtilMain.implicits._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo._
import io.circe.Decoder.Result
import org.apache.avro.Schema
import org.specs2.reporter.Reporter




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
	
	
	
	
	
	
	def testCirceToAvroSkeuo(title: String, theArgAvro: Fix[AvroSchema_S], theArgJson: Fix[JsonSchema_S]) ={
		
		info(s"\nTesting this kind: ${title}")
		
		val theCirce = libToJsonAltered(theArgAvro)
		info(s"\n avro-skeuo ---> json-circe: ${theCirce}")
		val theAvroSkeuo = funcCirceToAvroSkeuo(theCirce)
		info(s"\ncirce --> avro-skeuo: ${theAvroSkeuo}")
		//printAvroStringToCirceToAvroSkeuo(args_avroStr)
		
		
		// json-skeuo --> circe
		info(s"\njson-skeuo -> circe: ${libRender(theArgJson)}")
		// json-skeuo --> avro-skeuo
		info(s"\njson-skeuo -> circe -> avro-skeuo: ${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(theArgJson)}")
	}
	
	/*val c = unsafeParse(map1StrJson_R)
	info(s"map: json-str -> circe: ${c.manicure}")
	val skj = funcCirceToJsonSkeuo(c)
	val ska = funcCirceToAvroSkeuo(c)
	info(s"-> json-skeuo: $skj")
	info(s"---> json-str: ${libRender(skj.right.get)}")
	info(s"-> avro-skeuo: $ska")
	info(s"-> apache-str: ${skeuoToApacheAvroSchema(ska.right.get).toString(true).manicure}")
	info(s"---> avro-str (libtoJsonAl): ${libToJsonAltered(ska.right.get)}")*/
	
	//testCirceToAvroSkeuo("map thing: map (str avro) --> circe -> avro-skeuo", map1IntAvro_Fix_S, map1IntJson_objectname_Fix_S)
	printAvroStringToCirceToAvroSkeuo(List(map1StrAvro_R))
	val stp = AvroStepping(map1StrAvro_R).avroInfoOpt.get
	info(s"str -> apache: ${stp.parsedApacheAvroStr}")
	info(s"apache -> skeuo: ${stp.skeuoAvro_fromApache}")
	info(s"skeuo -> circe: ${stp.interimCirce_fromAvroSKeuo}")
	info(s"circe -> avro-skeuo: ${funcCirceToAvroSkeuo(stp.interimCirce_fromAvroSKeuo)}")
	info(s"circe -> json-skeuo: ${funcCirceToJsonSkeuo(stp.interimCirce_fromAvroSKeuo)}")
	
	
	def printAvroStringToCirceToAvroSkeuo(listAvroStrings: List[String]): Unit = {
		
		def printer(arg: String) = info(s"${DecodingStr.decodeAvroStringToCirceToAvroSkeuo(arg)}")
		
		listAvroStrings.map(printer(_))
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
	
	def printAvroSkeuoToJsonSkeuo(listAvroSkeuo: List[Fix[AvroSchema_S]]): Unit = {
		
		// avro-skeuo --> circe (decoder) --> json-skeuo
		def printer(arg: Fix[AvroSchema_S]) = info(s"${DecodingSkeuo.decodeAvroSkeuoToCirceToJsonSkeuo(arg)}")
		
		listAvroSkeuo.map(printer(_))
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
			info(libRender(arg).manicure)
		}
		
		listJsonSkeuo.map(printer(_))
	}
	
	def printJsonSkeuoToAvroSkeuo(listJsonSkeuo: List[Fix[JsonSchema_S]]): Unit = {
		
		// json-skeuo --> circe (decoder) --> avro-skeuo
		def printer(arg: Fix[JsonSchema_S]) = info(s"${DecodingSkeuo.decodeJsonSkeuoToCirceToAvroSkeuo(arg)}")
		
		listJsonSkeuo.map(printer(_))
	}
	
	/*info(s"record position (json circe): \n${libRender(recordEXPositionJson_Fix_S).manicure}")
	info(s"record location (json circe): \n${libRender(recordEXLocationJson_Fix_S).manicure}")
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
