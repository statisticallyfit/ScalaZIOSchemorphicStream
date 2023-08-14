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
 * Source funspec structures = https://www.scalatest.org/at_a_glance/FunSpec
 */
class Array1IntSpecs extends  AnyFunSpec with Matchers with TraitInheritFunSpecAndMatchers  {
	
	
	
	
	val argsArray1Int: ExplicitArgs = new ExplicitArgs(rawAvroStr = array1IntAvro_R,
		rawJsonStr = array1IntJson_R,
		jsonCirceCheck = array1IntJson_C,
		avroS = array1IntAvro_S, tpeS = "AvroSchema_S[AvroSchema_S[Int]]",
		avroC = array1IntAvro_Circe_S, tpeC = "AvroSchema_S[AvroSchema_S[JsonCirce]]",
		avroFixS = array1IntAvro_Fix_S,
		jsonFixS = array1IntJson_Fix_S
	)
	
	
	TestFramework.testStructure(scenarioType = "array of int")(argsArray1Int)
	TestFramework.printOuts(scenarioType = "array of int")(argsArray1Int)
	
	
}

