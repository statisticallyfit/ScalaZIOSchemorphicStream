package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs

import cats.syntax.all._

import higherkindness.droste.data.Fix
import higherkindness.droste._
import higherkindness.droste.syntax.all._


import org.scalatest.{GivenWhenThen}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._


import utilMain.UtilMain

import conversionsOfSchemaADTs.json_json.Skeuo_AndyGlow._
import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._


import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import SchemaAvro_Skeuo.{Field ⇒ FieldAvro}
import SchemaJson_Skeuo.{Property ⇒ PropertyJson}

import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._

import scala.reflect.runtime.universe._

import io.circe.{Json ⇒ JsonCirce}
import io.circe.parser.parse

/**
 *
 */
class AvroToJsonSchema_SkeuoSkeuo_Spec  extends AnyFeatureSpec with GivenWhenThen with Matchers  {


	Feature("Convert skeuo-avro-adt to skeuo-json-adt (basic primitives)"){
		
		Scenario("null"){
			
			Given("avro null in skeuo-adt")
			info(s"avro null = $nullAvro_Skeuo")
			
			
			When("converting to json by applying the function")
			val result: SchemaJson_Skeuo[Null] = avroToJson(nullAvro_Skeuo)
			val resultC: SchemaJson_Skeuo[JsonCirce] = avroToJson[JsonCirce](nullAvro_SkeuoC)
			
			val resultF: SchemaJson_Skeuo[Fix[SchemaAvro_Skeuo]] = avroToJson(nullAvro_SkeuoFix.unfix)
			
			info(s"nullAvro_Skeuo value = $nullAvro_Skeuo")
			info(s"nullAvro_SkeuoFix = $nullAvro_SkeuoFix")
			info(s"nullAvro_SkeuoFix ---> nullAvro_Apache (string) = ${skeuoToApacheAvroSchema(nullAvro_SkeuoFix)}")
			info(s"nullJson_Skeuo value = $result")
			info(s"func type = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Null])}")
			info(s"value type = ${UtilMain.getFuncTypeSubs(result)}")
			info(s"avro skeuo -> json circe (C) = ${SchemaAvro_Skeuo.toJson(nullAvro_SkeuoC)}")
			info(s"json skeuo -> json circe (C) = ${SchemaJson_Skeuo.render(nullJson_SkeuoC)}")
			info("\n")
			info(s"nullAvro_SkeuoC value = $nullAvro_SkeuoC")
			info(s"nullJson_Skeuo value = $resultC")
			info(s"func type (C) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce])}")
			info(s"value type (C) = ${UtilMain.getFuncTypeSubs(resultC)}")
			info(s"avro skeuo -> json circe (C) = ${SchemaAvro_Skeuo.toJson(nullAvro_SkeuoC)}")
			info(s"json skeuo -> json circe (C) = ${SchemaJson_Skeuo.render(nullJson_SkeuoC)}")
			
			
			// TODO next:
			// 1) str -> json circe -> skeuo-json-fixed (to be able to create tests from desired string output to skeuo and then check if that skeuo is matching my generated skeuo)
			// 2) str -> apache avro (parser!) -> skeuo-avro (create tests from desired avro string to skeuo-avro)
			// 3) ... from 2), get skeuo-avro -> json circe (using toJson call) (reason: to get from avro-str -> json circe)
			// 4) skeuo-json -> json circe (using render() call)
			
			
			avroToJsonFunction[Null] shouldBe a [SchemaAvro_Skeuo[_] => SchemaJson_Skeuo[_]]
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) should equal ("SchemaAvro_Skeuo[Null] => SchemaJson_Skeuo[Null]")
			
			
			// TODO why does the function take typetag as parameter?????
			//val func = avroToJsonFunction[Null](nullAvro_Skeuo)
			
			
			Then("json null (skeuo-adt) should be correctly generated")
			
			result shouldBe a [SchemaJson_Skeuo[_]]
			UtilMain.getFuncTypeSubs(result) shouldEqual "SchemaJson_Skeuo[Null]"
			
			List(result,
				nullJson_Skeuo,
				ObjectF(properties = List(), required = List())
			).distinct.length should equal (1)
			
			result should equal(nullJson_Skeuo)
			result shouldEqual ObjectF(properties = List(), required = List())
			
			resultC should equal(nullJson_SkeuoC)
		}
	}
}
