package conversionsOfSchemaADTs.avro_json.skeuo_skeuo


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
import testData.rawstringData.avroData.Data._
import testData.rawstringData.jsonData.Data._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._



/**
 *
 */

class ImplicitArgs (
				val rawAvroStr: String,
				val rawJsonStr: String,
				val jsonCirceCheck: JsonCirce, val avroS: AvroSchema_S[_], val tpeS: String,
				val avroC: AvroSchema_S[_], val tpeC: String,
				val avroFixS: Fix[AvroSchema_S], val jsonFixS: Fix[JsonSchema_S]
			)
object ImplicitArgs {
	implicit def applying(
						 implicit rawAvroStr: String,
						 rawJsonStr: String,
						 jsonCirceCheck: JsonCirce, avroS: AvroSchema_S[_], tpeS: String,
						 avroC: AvroSchema_S[_], tpeC: String,
						 avroFixS: Fix[AvroSchema_S], jsonFixS: Fix[JsonSchema_S]
					 ) = new ImplicitArgs(rawAvroStr.manicure, rawJsonStr.manicure, jsonCirceCheck, avroS, tpeS, avroC, tpeC, avroFixS, jsonFixS)
}