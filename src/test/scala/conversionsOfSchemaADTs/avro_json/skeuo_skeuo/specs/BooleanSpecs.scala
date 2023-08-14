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
class BooleanSpecs  extends  AnyFunSpec with Matchers with TraitInheritFunSpecAndMatchers {
	
	val argsBoolean: ExplicitArgs = new ExplicitArgs(
		rawAvroStr = booleanAvro_R,
		rawJsonStr = booleanJson_R,
		jsonCirceCheck = booleanJson_C,
		avroS = booleanAvro_S, tpeS = "AvroSchema_S[Boolean]",
		avroC = booleanAvro_Circe_S, tpeC = "AvroSchema_S[JsonCirce]",
		avroFixS = booleanAvro_Fix_S,
		jsonFixS = booleanJson_Fix_S
	)
	TestFramework.testStructure(scenarioType = "boolean type")(argsBoolean)
	TestFramework.printOuts(scenarioType = "boolean type")(argsBoolean)
}
