package utilMain.utilAvroJson.utilSkeuoSkeuo



import higherkindness.skeuomorph.avro.{AvroF ⇒ AvroSchema_S}
import AvroSchema_S._


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ JsonSchema_S}
import JsonSchema_S._


import AvroSchema_S.{Field ⇒ FieldAvro}
import JsonSchema_S.{Property ⇒ PropertyJson}

import scala.reflect.runtime.universe._

/**
 *
 */
object FieldToPropertyConversions {

	
	// TODO convert avro field (skeuo) --> json property (skeuo)
	
	def property2Field[A/*: TypeTag*/](property: PropertyJson[A]): FieldAvro[A] = property match {
		
		case PropertyJson(name: String, tpe: A) ⇒ {
			FieldAvro(
				name = name,
				aliases = List(),
				doc = None,
				order = None,
				tpe = tpe
			)
		}
	}
	
	def field2Property[A/*: TypeTag*/](fieldA: FieldAvro[A]): PropertyJson[A] = fieldA match {
		
		case FieldAvro(name: String, aliases: List[String], doc: Option[String], order: Option[Order], tpe: A) ⇒ {
			
			PropertyJson[A](
				name = name,
				tpe = tpe
			)
		}
	}
	
	/*def field2Property[F[_]: TypeTag, A: TypeTag](fieldA: FieldAvro[F[A]]): PropertyJson[A] = fieldA match {
		case FieldAvro(name: String, aliases: List[String], doc: Option[String], order: Option[Order], tpe: F[A]) ⇒ {
			
			val res: PropertyJson[F[A]] = PropertyJson(
				name = name,
				tpe = tpe
			)
			
			res
			
		}
	}*/
}
