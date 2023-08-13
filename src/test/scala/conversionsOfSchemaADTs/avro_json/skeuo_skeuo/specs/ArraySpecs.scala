package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs



import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo.ByTrans.{avroToJson_byCataTransAlg, jsonToAvro_byAnaTransCoalg}
import higherkindness.droste.data.Fix
import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import org.apache.avro.{Schema ⇒ AvroSchema_A}
import io.circe.Decoder.Result
import io.circe.{Json ⇒ JsonCirce}
import utilMain.utilJson.utilSkeuo_ParseJsonSchemaStr.UnsafeParser._
import conversionsOfSchemaADTs.avro_json.parsing.ParseADTToCirceToADT._
import conversionsOfSchemaADTs.avro_json.parsing.ParseStringToCirceToADT._

import org.scalatest.Inspectors._
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

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.ImplicitArgs

import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs._

/**
 * Source funspec structures = https://www.scalatest.org/at_a_glance/FunSpec
 */
class ArraySpecs extends  AnyFunSpec with Matchers with TraitInheritFunSpecAndMatchers  {
	
	
	
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
	)
	
	
	testStructure(scenarioType = "boolean type",
		rawAvroStr = booleanAvro_R,
		rawJsonStr = booleanJson_R,
		jsonCirceCheck = booleanJson_C,
		avroS = booleanAvro_S, tpeS = "AvroSchema_S[Boolean]",
		avroC = booleanAvro_Circe_S, tpeC = "AvroSchema_S[JsonCirce]",
		avroFixS = booleanAvro_Fix_S,
		jsonFixS = booleanJson_Fix_S
	)*/
	
	val argsArray1Int: ExplicitArgs = new ExplicitArgs(rawAvroStr = array1IntAvro_R,
		rawJsonStr = array1IntJson_R,
		jsonCirceCheck = array1IntJson_C,
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFixS = array1IntAvro_Fix_S,
		jsonFixS = array1IntJson_Fix_S
	)
	
	
	testStructure(scenarioType = "array of int")(argsArray1Int)
	printOuts(scenarioType = "array of int")(argsArray1Int)
	
	/*var v = VariableWrapper(argsArray1Int)
	
	info(v.Vars.dcommon.showResults())
	info(v.Vars.d1.showResults())
	info(v.Vars.d2.showResults())
	info(v.Vars.d3a.showResults())
	info(v.Vars.d3b.showResults())
	info(v.Vars.d3c.showResults())
	info(v.Vars.d3d.showResults())*/
	
	
	/*val testArray1Int: TestStructure = TestStructure(scenarioType = "array of int",
		rawAvroStr = array1IntAvro_R,
		rawJsonStr = array1IntJson_R,
		jsonCirceCheck = array1IntJson_C,
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFixS = array1IntAvro_Fix_S,
		jsonFixS = array1IntJson_Fix_S
	)
	testArray1Int.testStructure()
	testArray1Int.printOuts()
	
	info(testArray1Int.Vars.dcommon.showResults())*/
	
}

