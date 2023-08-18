package extensions.extendSkeuoObjectWithName


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._


/**
 *
 */
// case class ObjectF[A](properties: List[Property[A]], required: List[String]) extends JsonSchemaF[A]
/*case class ObjectWithNameF[A](name: String, override val properties: List[Property[A]], override val required: List[String]) extends ObjectF[A](properties, required)*/


object SkeuoObjectExtensions {
	
	
	/*case class ObjectWithNameF[A](name: String, override val properties: List[Property[A]], override val required: List[String]) extends ObjectF[A](properties, required)
*/
	def trythis[A](variableHere: ObjectWithNameF[A]): Unit = println(variableHere)
	
	/*sealed trait MyJsonSchema_S[A]
	case class SecondJSchema[A](name: String, obj: JsonSchema_S[A]) extends MyJsonSchema_S[A]
	/*case class ObjectWithNameF[A](name: String, ps: List[Property[A]], rs: List[String])  extends SecondJSchema[A]*/

	implicit def jsonschemaToMine[A](tup: Tuple2[String, JsonSchema_S[A]]) = SecondJSchema(tup._1, tup._2)


	val js: JsonSchema_S[Nothing] = ObjectF(List(Property(name = "coordinates", tpe = StringF())), required = List())
	val second = ("the name", js)
*/
	//implicit def objConv[A](name: String, objInterim: ObjectInterimF[A]): ObjectWithNameF[A] = ObjectWithNameF[A](name, objInterim)

	/*implicit class ObjectImplicits[A](obj: ObjectF[A]) {

		def withName(name: String)

	}*/
}