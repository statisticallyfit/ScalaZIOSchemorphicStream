package utilMain.utilAvro.utilSkeuoApache

import org.apache.avro.{Schema ⇒ SchemaAvro_Apache}
import org.apache.avro.{LogicalType => LogicalTypeApache, LogicalTypes ⇒ LogicalTypesApache}

import scala.util.control.Exception._

/**
 * SOURCE for tutorials on using the scala.util.control.Exceptions
 *
 * https://www.baeldung.com/scala/exception-handling
 *
 * With Option: https://hyp.is/zRgVoBKZEe6gm488OdXRDQ/alvinalexander.com/scala/scala-exception-allcatch-examples-option-try-either
 */
object ValidateLogicalTypes {
	def isValidated(logicalType: LogicalTypeApache, schemaArg: SchemaAvro_Apache): Boolean = {
		
		val wasThrown: Option[Unit] = catching(classOf[IllegalArgumentException]) opt { logicalType.validate(schemaArg) }
		
		wasThrown match {
			case Some(_) ⇒ true  // error was not thrown, so the logical type is validated
			case None ⇒ false // error WAS thrown, so logical type is NOT validated
		}
		
	}
}
