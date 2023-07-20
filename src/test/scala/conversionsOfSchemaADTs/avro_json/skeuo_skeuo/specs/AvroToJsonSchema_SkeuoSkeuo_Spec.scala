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
			
			nullAvro_Skeuo shouldBe a [SchemaAvro_Skeuo[_]]
			UtilMain.getFuncTypeSubs(nullAvro_Skeuo) shouldEqual "SchemaAvro_Skeuo[Null]"
			
			
			When("converting to json by applying the function")
			val jsonSkeuo: SchemaJson_Skeuo[Null] = avroToJson(nullAvro_Skeuo)
			val jsonSkeuo_C: SchemaJson_Skeuo[JsonCirce] = avroToJson[JsonCirce](nullAvro_SkeuoC)
			
			val jsonSkeuo_F: SchemaJson_Skeuo[Fix[SchemaAvro_Skeuo]] = avroToJson(nullAvro_SkeuoFix.unfix)
			
			info(s"skeuo-avro = $nullAvro_Skeuo | type = ${UtilMain.getFuncTypeSubs(nullAvro_Skeuo)}")
			info(s"skeuo-avro (fix) = $nullAvro_SkeuoFix | type = ${UtilMain.getFuncTypeSubs(nullAvro_SkeuoFix)}")
			info(s"skeuo-avro (circe) = $nullAvro_SkeuoC | type = ${UtilMain.getFuncTypeSubs(nullAvro_SkeuoC)}")
			
			
			info(s"skeuo-json = $jsonSkeuo | type = ${UtilMain.getFuncTypeSubs(jsonSkeuo)}")
			info(s"skeuo-json (unfix) = $jsonSkeuo_F | type = ${UtilMain.getFuncTypeSubs(jsonSkeuo_F)}")
			info(s"skeuo-json (circe) = $jsonSkeuo_C | type = ${UtilMain.getFuncTypeSubs(jsonSkeuo_C)}")
			
			
			info(s"func type (self) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Null])}")
			//info(s"func type (fix) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce])}") //TODO
			info(s"func type (circe) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce])}")
			
			
			info(s"--- skeuo-avro (fix) --> apache-avro = ${skeuoToApacheAvroSchema(nullAvro_SkeuoFix)}")
			info(s"--- skeuo-avro -> json circe = ${SchemaAvro_Skeuo.toJson(nullAvro_SkeuoC)}")
			info(s"--- skeuo-json -> json circe = ${SchemaJson_Skeuo.render(nullJson_SkeuoC)}")
			
			
			
			
			
			// TODO next:
			// 1) str -> json circe -> skeuo-json-fixed (to be able to create tests from desired string output to skeuo and then check if that skeuo is matching my generated skeuo)
			// 2) str -> apache avro (parser!) -> skeuo-avro (create tests from desired avro string to skeuo-avro)
			// 3) ... from 2), get skeuo-avro -> json circe (using toJson call) (reason: to get from avro-str -> json circe)
			// 4) skeuo-json -> json circe (using render() call)
			
			
			/*avroToJsonFunction[Null] shouldBe a [SchemaAvro_Skeuo[_] => SchemaJson_Skeuo[_]]
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) should equal ("SchemaAvro_Skeuo[Null] => SchemaJson_Skeuo[Null]")*/
			
			
			// TODO why does the function take typetag as parameter?????
			//val func = avroToJsonFunction[Null](nullAvro_Skeuo)
			
			
			Then("json null (skeuo-adt) should be correctly generated")
			
			jsonSkeuo shouldBe a [SchemaJson_Skeuo[_]]
			UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "SchemaJson_Skeuo[Null]"
			
			List(jsonSkeuo,
				nullJson_Skeuo,
				ObjectF(properties = List(), required = List())
			).distinct.length should equal (1)
			
			jsonSkeuo should equal(nullJson_Skeuo)
			jsonSkeuo shouldEqual ObjectF(properties = List(), required = List())
			
			jsonSkeuo_C should equal(nullJson_SkeuoC)
		}
	}
}
