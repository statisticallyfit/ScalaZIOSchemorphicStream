package utilMain.utilSkeuoApache


import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}

import scala.util.control.Exception._

/**
 *
 */
object ValidateUtil {
	def isValidated(logicalType: LogicalTypeApache, schemaArg: SchemaApacheAvro): Boolean = {
		
		val wasThrown: Option[Unit] = catching(classOf[IllegalArgumentException]) opt { logicalType.validate(schemaArg) }
		
		wasThrown match {
			case Some(_: Unit) ⇒ false  // error WAS thrown, so logical type is NOT validated
			case None ⇒ true // error was not thrown, so the logical type is validated
		}
		
	}
}
