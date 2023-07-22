package utilMain.utilAvroJson.utilSkeuoSkeuo



import higherkindness.skeuomorph.avro.{AvroF ⇒ SchemaAvro_Skeuo}
import SchemaAvro_Skeuo._


import higherkindness.skeuomorph.openapi.{JsonSchemaF ⇒ SchemaJson_Skeuo}
import SchemaJson_Skeuo._


import SchemaAvro_Skeuo.{Field ⇒ FieldAvro}
import SchemaJson_Skeuo.{Property ⇒ PropertyJson}

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
