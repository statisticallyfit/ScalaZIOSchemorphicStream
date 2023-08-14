package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs


import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._

import higherkindness.droste.data.Fix

import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
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




/**
 *
 */
class AvroToJson_SkeuoSkeuo_Specs extends  AnyFunSpec with Matchers with TraitInheritFunSpecAndMatchers {
	
	info(s"VARIABLE PRINT OUTS")
	
	val args_avroSkeuo: List[Fix[AvroSchema_S]] = List(nullAvro_Fix_S, strAvro_Fix_S, intAvro_Fix_S, booleanAvro_Fix_S, longAvro_Fix_S, floatAvro_Fix_S, doubleAvro_Fix_S, bytesAvro_Fix_S,
		array1IntAvro_Fix_S, array1StrAvro_Fix_S, array3IntAvro_Fix_S,
		recordStrAvro_Fix_S,
		//recordEXPositionAvro_Fix_S, recordEXLocationAvro_Fix_S
	)
	
	def printAvroSkeuoToAvroString(): Unit = {
		
		def printer(arg: Fix[AvroSchema_S]) = info(s"${skeuoToApacheAvroSchema(arg).toString(true).manicure}")
		
		args_avroSkeuo.map(arg ⇒ printer(arg))
	}
	
	info(s"${skeuoToApacheAvroSchema(intAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(strAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(booleanAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(array1IntAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(array1StrAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(array3IntAvro_Fix_S).toString(true).manicure}")
	info(s"${skeuoToApacheAvroSchema(recordStrAvro_Fix_S).toString(true).manicure}")
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
	
	
	//val b = new BooleanSpecs
	val arr = new ArraySpecs
	
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
