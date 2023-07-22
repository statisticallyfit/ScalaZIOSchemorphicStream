package conversionsOfSchemaADTs.avro_json.skeuo_skeuo.specs

import cats.syntax.all._

import higherkindness.droste.data.Fix
import higherkindness.droste.syntax.all._

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should._

import utilMain.UtilMain
import utilMain.UtilMain.implicits._


import conversionsOfSchemaADTs.avro_json.skeuo_skeuo.Skeuo_Skeuo._
import conversionsOfSchemaADTs.avro_avro.skeuo_apache.Skeuo_Apache._

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}

import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._
import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._

import io.circe.{Json ⇒ JsonCirce}

import testData.schemaData.avroData.skeuoData.Data._
import testData.schemaData.jsonData.skeuoData.Data._





/**
 * Testing skeuo-avro -> skeuo-json
 *
 */


class AvroToJsonSchema_SkeuoSkeuo_Spec  extends AnyFeatureSpec with GivenWhenThen with Matchers  {


	Feature("Convert skeuo-avro-adt to skeuo-json-adt (basic primitives)"){
		
		Scenario("null"){
			
			Given("skeuo-avro-schema")
			
			// value-level check
			nullAvro_Skeuo shouldEqual TNull()
			nullAvro_Skeuo_F shouldEqual TNull()
			nullAvro_Skeuo_C shouldEqual TNull()
			
			// type-level check
			nullAvro_Skeuo shouldBe a[SchemaAvro_Skeuo[_]]
			UtilMain.getFuncTypeSubs(nullAvro_Skeuo) shouldEqual "SchemaAvro_Skeuo[Null]"
			
			// HELP error says: no classtag available for Fix
			//nullAvro_Skeuo_F shouldBe a[Fix[SchemaAvro_Skeuo]]
			UtilMain.getFuncTypeSubs(nullAvro_Skeuo_F) should equal("Fix[SchemaAvro_Skeuo]")
			
			nullAvro_Skeuo_C shouldBe a[SchemaAvro_Skeuo[_]]
			UtilMain.getFuncTypeSubs(nullAvro_Skeuo_C) should equal("SchemaAvro_Skeuo[JsonCirce]")
			
			
			info(s"CHECK skuo-fix == skeuo-simple = ${nullAvro_Skeuo === nullAvro_Skeuo_F}")
			
			/*info(s"skeuo-avro = $nullAvro_Skeuo | type = ${UtilMain.getFuncTypeSubs(nullAvro_Skeuo)}")
			info(s"skeuo-avro (fix) = $nullAvro_SkeuoFix | type = ${UtilMain.getFuncTypeSubs(nullAvro_SkeuoFix)}")
			info(s"skeuo-avro (circe) = $nullAvro_SkeuoC | type = ${UtilMain.getFuncTypeSubs(nullAvro_SkeuoC)}")*/
			
			
			When("converting to skeuo-json-schema by applying the function")
			
			List(
				avroToJsonFunction[Null],
				avroToJsonFunction[JsonCirce],
				avroToJsonFunction[Fix[SchemaAvro_Skeuo]]
			).map(
				func ⇒ func shouldBe a [SchemaAvro_Skeuo[_] => SchemaJson_Skeuo[_]]
			)
			
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Null]) shouldEqual "SchemaAvro_Skeuo[Null] => SchemaJson_Skeuo[Null]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce]) shouldEqual "SchemaAvro_Skeuo[JsonCirce] => SchemaJson_Skeuo[JsonCirce]"
			
			UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[SchemaAvro_Skeuo]]) shouldEqual "SchemaAvro_Skeuo[Fix[SchemaAvro_Skeuo]] => SchemaJson_Skeuo[Fix[SchemaAvro_Skeuo]]"
			
			/*info(s"func type = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Null])}")
			info(s"func type (circe) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[JsonCirce])}")
			info(s"func type (fix) = ${UtilMain.getFuncTypeSubs(avroToJsonFunction[Fix[SchemaAvro_Skeuo]])}")*/
			
			
			// TODO next:
			// 1) str -> json circe -> skeuo-json-fixed (to be able to create tests from desired string output to skeuo and then check if that skeuo is matching my generated skeuo)
			// 2) str -> apache avro (parser!) -> skeuo-avro (create tests from desired avro string to skeuo-avro)
			// 3) ... from 2), get skeuo-avro -> json circe (using toJson call) (reason: to get from avro-str -> json circe)
			// 4) skeuo-json -> json circe (using render() call)
			
			
			
			Then("skeuo-json-schema should be correctly generated")
			
			val jsonSkeuo: SchemaJson_Skeuo[Null] = avroToJson(nullAvro_Skeuo)
			val jsonSkeuo_C: SchemaJson_Skeuo[JsonCirce] = avroToJson[JsonCirce](nullAvro_Skeuo_C)
			val jsonSkeuo_UF: SchemaJson_Skeuo[Fix[SchemaAvro_Skeuo]] = avroToJson(nullAvro_Skeuo_F.unfix)
			
			
			// value-level check
			List(jsonSkeuo, jsonSkeuo_C, jsonSkeuo_UF,
				nullJson_Skeuo,
				ObjectF(properties = List(), required = List())
			).distinct.length should equal(1)
			
			// type-level check
			List(
				jsonSkeuo, jsonSkeuo_C, jsonSkeuo_UF
			).map(js ⇒ js shouldBe a [SchemaJson_Skeuo[_]])
			
			UtilMain.getFuncTypeSubs(jsonSkeuo) shouldEqual "SchemaJson_Skeuo[Null]"
			UtilMain.getFuncTypeSubs(jsonSkeuo_C) shouldEqual "SchemaJson_Skeuo[JsonCirce]"
			UtilMain.getFuncTypeSubs(jsonSkeuo_UF) shouldEqual "SchemaJson_Skeuo[Fix[SchemaAvro_Skeuo]]"
			
			
			
			
			And("--- skeuo-avro (fix) --> apache-avro: \nThe avro-apache string should coincide with the json circe string")
			
			def roundTripVerify_AvroStrAndJsonCirce(avroStrStart: String, avroSkeuoMidCheck: SchemaAvro_Skeuo[JsonCirce], jsonCirceEnd: JsonCirce): Boolean = {
			
				// step 1 - parse avro str to avro-apache-schema
				val schemaAvro_Apache: SchemaAvro_Apache = new SchemaAvro_Apache.Parser().parse(avroStrStart)
			
				// step 2 - check skeuos are equal (generated vs. given)
				val schemaAvro_Skeuo: Fix[SchemaAvro_Skeuo] = apacheToSkeuoAvroSchema(schemaAvro_Apache)
				
				schemaAvro_Skeuo shouldEqual avroSkeuoMidCheck
				
				// step 3 - generate json circe
				val schemaJson_Circe: JsonCirce = SchemaAvro_Skeuo.toJson(avroSkeuoMidCheck)
				
				// step 4 - check json circe are equal (generated vs. given)
				schemaJson_Circe.manicure shouldEqual jsonCirceEnd.manicure
				
				schemaAvro_Skeuo == avroSkeuoMidCheck && schemaJson_Circe.manicure == jsonCirceEnd.manicure
			}
			
			
			info(s"--- skeuo-avro (fix) --> apache-avro = ${skeuoToApacheAvroSchema(nullAvro_Skeuo_F).toString(true)}")
			info(s"--- skeuo-avro --> skeuo-json = $jsonSkeuo")
			info(s"--- skeuo-avro -> json circe = ${SchemaAvro_Skeuo.toJson(nullAvro_Skeuo_C).manicure}")
			
			// TODO HERE - using opetushallitus to take json data -> json schema
			import scala.reflect.runtime.universe._
			import fi.oph.scalaschema.{SchemaFactory, SchemaToJson, Schema ⇒ SchemaJson_Opetus}
			import org.json4s.jackson.JsonMethods
			import org.json4s.jackson.JsonMethods.asJsonNode
			import org.json4s.JsonAST.{JObject}
			import org.json4s.{JNull, JInt, JString, JArray}
			
			import com.github.fge.jsonschema.core.report.ListReportProvider
			import com.github.fge.jsonschema.core.report.LogLevel.{ERROR, FATAL}
			import com.github.fge.jsonschema.main.{JsonSchemaFactory, JsonValidator}
			
			object helpers {
				def schemaOf(c: Class[_]) = SchemaFactory.default.createSchema(c)
				
				def jsonSchemaOf[T: TypeTag]: String = jsonSchemaOf(SchemaFactory.default.createSchema[T])
				
				def jsonSchemaOf(c: Class[_]): String = jsonSchemaOf(schemaOf(c))
				
				def jsonSchemaOf(s: SchemaJson_Opetus): String = {
					val schemaJson = s.toJson
					// Just check that the created schema is a valid JSON schema, ignore validation results
					jsonSchemaFactory.getJsonSchema(asJsonNode(SchemaToJson.toJsonSchema(s))).validate(asJsonNode(JObject()))
					JsonMethods.compact(schemaJson)
				}
				
				private lazy val jsonSchemaFactory = JsonSchemaFactory.newBuilder.setReportProvider(new ListReportProvider(ERROR, FATAL)).freeze()
			}
			import helpers._
			
			val nulljc = jsonSchemaOf(classOf[JNull.type])
			val intjc = jsonSchemaOf[JInt]
			val strjc = jsonSchemaOf[JString]
			val arrjc = jsonSchemaOf[JArray]
			
			info(s"--- OPETUS HALLITUS: json data -> json schema circe")
			info(s"nulljc = $nulljc")
			info(s"intjc = $intjc")
			info(s"strjc = $strjc")
			info(s"arrjc = $arrjc")
			
			info(s"--- skeuo-json -> json circe = \n${SchemaJson_Skeuo.render(nullJson_Skeuo_C).manicure}")
			
			
			// TODO HELP FIX
			skeuoToApacheAvroSchema(nullAvro_Skeuo_F).toString(true) should equal ("\"null\"")
			
			jsonSkeuo shouldEqual ObjectF(properties = List(), required = List())
			
			// TODO fix: SchemaAvro_Skeuo.toJson(nullAvro_Skeuo_C).manicure should equal ("Null")
			
			
			// TODO fix
			/*SchemaJson_Skeuo.render(nullJson_Skeuo_C).manicure should equal (
				"""
				  |{
				  |  "type": "object",
				  |  "properties": {},
				  |  "required": []
				  |}
				  |""".stripMargin)*/
		}
	}
}
