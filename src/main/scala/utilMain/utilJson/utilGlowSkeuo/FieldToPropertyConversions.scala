package utilMain.utilJson.utilGlowSkeuo

import com.github.andyglow.json.Value

import json.Schema.`object`.{Field => FieldJson}

import higherkindness.skeuomorph.openapi.JsonSchemaF.{Property => PropertyJson}


import scala.reflect.runtime.universe._


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
//import JsonSchema_S._

import json.{Schema ⇒ JsonSchema_G}

/**
 *
 */
object FieldToPropertyConversions {

	type RequiredBool = Boolean

	def field2Property[A: TypeTag](field: FieldJson[A]): (PropertyJson[A], RequiredBool) = {

		// TODO must change andy glow libraryto have a Field with simple tpe not layered because cannot extract it to pass to skeuo.

		
		val tt = typeTag[A].tpe.typeSymbol

		field match {

			case FieldJson(name: String, tpeSchema: JsonSchema_G[A], requiredBool: Boolean, default: Option[Value], description: Option[String], rwMode: FieldJson.RWMode) => (PropertyJson(name, tpe), requiredBool)
		}
	}


	/*def property2Field[A](prop: (PropertyJson[A], RequiredBool)): FieldJson[A] = prop match {

		case (PropertyJson(name: String, tpe: A), requiredBool: Boolean) => new FieldJson(name = name, tpe = tpe, required = requiredBool, default = None, description = None, rwMode = FieldJson.RWMode.ReadWrite)
	}*/

	def property2Field[A](prop: PropertyJson[A]): FieldJson[A] = prop match {
		case PropertyJson(name: String, tpe: A) => new FieldJson[A](name = name, tpe = tpe, required = true, default = None, description = None, rwMode = FieldJson.RWMode.ReadWrite)
	}

}
