package testUtil.utilZioApache


import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
import org.apache.avro.LogicalTypes
import zio.schema.DeriveSchema

import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType}
import zio.schema.codec.{AvroCodec, RecordType}
import zio.Chunk
import zio.schema.CaseSet.Aux
import zio.schema.Schema.{ Record, _ }
import zio.schema._

import zio.schema.codec.{AvroAnnotations, AvroPropMarker, StringType, IntType, RecordType} //StringType, RecordType
import zio.schema.codec.AvroAnnotations._
import zio.schema.codec.AvroPropMarker._



import scala.jdk.CollectionConverters._
import scala.util.{Right, Try}
import scala.annotation.StaticAnnotation
import scala.collection.immutable.ListMap


import testUtil.utilZioApache.ImplicitSchemaExtensionClasses._

/**
 * Purpose of this file: Copying the code from zio's function here bcause in zio it is PRIVATE (cannot call)
 */
object ApacheToZioFunctions {



  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L49-L212
   *
   * @param avroApacheSchema
   * @return
   *
   * Previous name = toZioScehma
   */
  def apacheAvroSchemaToZioSchema(avroApacheSchema: SchemaApacheAvro): scala.util.Either[String, ZioSchema[_]] =
    for {
      // make sure to parse logical types with throwing exceptions enabled,
      // otherwise parsing errors on invalid logical types might be lost
      _ <- Try {
        LogicalTypes.fromSchema(avroApacheSchema)
      }.toEither.left.map(e => e.getMessage)
      result <- avroApacheSchema.getType match {
        case SchemaApacheAvro.Type.RECORD =>
          RecordType.fromAvroRecord(avroApacheSchema) match {
            case Some(RecordType.Period) => Right(ZioSchema.primitive(ZioStandardType.PeriodType))
            case Some(RecordType.YearMonth) => Right(ZioSchema.primitive(ZioStandardType.YearMonthType))
            case Some(RecordType.Tuple) => toZioTuple(avroApacheSchema)
            case Some(RecordType.MonthDay) => Right(ZioSchema.primitive(ZioStandardType.MonthDayType))
            case Some(RecordType.Duration) => Right(ZioSchema.primitive(ZioStandardType.DurationType))
            case None => toZioRecord(avroApacheSchema)
          }
        case SchemaApacheAvro.Type.ENUM => toZioStringEnum(avroApacheSchema)
        case SchemaApacheAvro.Type.ARRAY =>
          apacheAvroSchemaToZioSchema(avroApacheSchema.getElementType).map(ZioSchema.list(_)) // returns ZioSchema[List[A]]
        case SchemaApacheAvro.Type.MAP =>
          apacheAvroSchemaToZioSchema(avroApacheSchema.getValueType).map(ZioSchema.map(ZioSchema.primitive(ZioStandardType.StringType), _))
        case SchemaApacheAvro.Type.UNION =>
          avroApacheSchema match {
            case OptionUnion(optionSchema) => apacheAvroSchemaToZioSchema(optionSchema).map(ZioSchema.option(_))
            case EitherUnion(left, right) =>
              apacheAvroSchemaToZioSchema(left).flatMap(l => apacheAvroSchemaToZioSchema(right).map(r => ZioSchema.either(l, r)))
            case _ => toZioEnumeration(avroApacheSchema)
          }
        case SchemaApacheAvro.Type.FIXED =>
          val fixed = if (avroApacheSchema.getLogicalType == null) {
            Right(ZioSchema.primitive(ZioStandardType.BinaryType))
          } else if (avroApacheSchema.getLogicalType.isInstanceOf[LogicalTypes.Decimal]) {
            val size = avroApacheSchema.getFixedSize
            toZioDecimal(avroApacheSchema, DecimalType.Fixed(size))
          } else {
            // TODO: Java implementation of Apache Avro does not support logical type Duration yet:
            //  AVRO-2123 with PR https://github.com/apache/avro/pull/1263
            Left(s"Unsupported fixed logical type ${avroApacheSchema.getLogicalType}")
          }
          fixed.map(_.addAllAnnotations(buildZioAnnotations(avroApacheSchema)))
        case SchemaApacheAvro.Type.STRING =>
          StringType.fromAvroString(avroApacheSchema) match {
            case Some(stringType) =>
              val dateTimeFormatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, stringType)
              dateTimeFormatter
                .map(_.dateTimeFormatter)
                .flatMap(_ => {
                  stringType match {
                    case StringType.ZoneId => Right(ZioSchema.primitive(ZioStandardType.ZoneIdType))
                    case StringType.Instant =>
                      Right(
                        ZioSchema
                          .primitive(ZioStandardType.InstantType)
                          .annotate(AvroAnnotations.formatToString)
                      )
                    case StringType.LocalDate =>
                      Right(
                        ZioSchema
                          .primitive(ZioStandardType.LocalDateType)
                          .annotate(AvroAnnotations.formatToString)
                      )
                    case StringType.LocalTime =>
                      Right(
                        ZioSchema
                          .primitive(ZioStandardType.LocalTimeType)
                          .annotate(AvroAnnotations.formatToString)
                      )
                    case StringType.LocalDateTime =>
                      Right(
                        ZioSchema
                          .primitive(ZioStandardType.LocalDateTimeType)
                          .annotate(AvroAnnotations.formatToString)
                      )
                    case StringType.OffsetTime =>
                      Right(ZioSchema.primitive(ZioStandardType.OffsetTimeType))
                    case StringType.OffsetDateTime =>
                      Right(ZioSchema.primitive(ZioStandardType.OffsetDateTimeType))
                    case StringType.ZoneDateTime =>
                      Right(ZioSchema.primitive(ZioStandardType.ZonedDateTimeType))
                  }
                })
            case None =>
              if (avroApacheSchema.getLogicalType == null) {
                Right(ZioSchema.primitive(ZioStandardType.StringType))
              } else if (avroApacheSchema.getLogicalType.getName == LogicalTypes.uuid().getName) {
                Right(ZioSchema.primitive(ZioStandardType.UUIDType))
              } else {
                Left(s"Unsupported string logical type: ${avroApacheSchema.getLogicalType.getName}")
              }
          }
        case SchemaApacheAvro.Type.BYTES =>
          if (avroApacheSchema.getLogicalType == null) {
            Right(ZioSchema.primitive(ZioStandardType.BinaryType))
          } else if (avroApacheSchema.getLogicalType.isInstanceOf[LogicalTypes.Decimal]) {
            toZioDecimal(avroApacheSchema, DecimalType.Bytes)
          } else {
            Left(s"Unsupported bytes logical type ${avroApacheSchema.getLogicalType.getName}")
          }
        case SchemaApacheAvro.Type.INT =>
          IntType.fromAvroInt(avroApacheSchema) match {
            case Some(IntType.Char) => Right(ZioSchema.primitive(ZioStandardType.CharType))
            case Some(IntType.DayOfWeek) => Right(ZioSchema.primitive(ZioStandardType.DayOfWeekType))
            case Some(IntType.Year) => Right(ZioSchema.primitive(ZioStandardType.YearType))
            case Some(IntType.Short) => Right(ZioSchema.primitive(ZioStandardType.ShortType))
            case Some(IntType.Month) => Right(ZioSchema.primitive(ZioStandardType.MonthType))
            case Some(IntType.ZoneOffset) => Right(ZioSchema.primitive(ZioStandardType.ZoneOffsetType))
            case None =>
              if (avroApacheSchema.getLogicalType == null) {
                Right(ZioSchema.primitive(ZioStandardType.IntType))
              } else
                avroApacheSchema.getLogicalType match {
                  case _: LogicalTypes.TimeMillis =>
                    val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                    formatter.map(
                      _ => ZioSchema.primitive(ZioStandardType.LocalTimeType)
                    )
                  case _: LogicalTypes.Date =>
                    val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                    formatter.map(
                      _ => ZioSchema.primitive(ZioStandardType.LocalDateType)
                    )
                  case _ => Left(s"Unsupported int logical type ${avroApacheSchema.getLogicalType.getName}")
                }
          }
        case SchemaApacheAvro.Type.LONG =>
          if (avroApacheSchema.getLogicalType == null) {
            Right(ZioSchema.primitive(ZioStandardType.LongType))
          } else
            avroApacheSchema.getLogicalType match {
              case _: LogicalTypes.TimeMicros =>
                val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                formatter.map(
                  _ => ZioSchema.primitive(ZioStandardType.LocalTimeType)
                )
              case _: LogicalTypes.TimestampMillis =>
                val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                formatter.map(
                  _ => ZioSchema.primitive(ZioStandardType.InstantType)
                )
              case _: LogicalTypes.TimestampMicros =>
                val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                formatter.map(
                  _ => ZioSchema.primitive(ZioStandardType.InstantType)
                )
              case _: LogicalTypes.LocalTimestampMillis =>
                val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                formatter.map(
                  _ => ZioSchema.primitive(ZioStandardType.LocalDateTimeType)
                )
              case _: LogicalTypes.LocalTimestampMicros =>
                val formatter = Formatter.fromAvroStringOrDefault(avroApacheSchema, avroApacheSchema.getLogicalType)
                formatter.map(
                  _ => ZioSchema.primitive(ZioStandardType.LocalDateTimeType)
                )
              case _ => Left(s"Unsupported long logical type ${avroApacheSchema.getLogicalType.getName}")
            }
        case SchemaApacheAvro.Type.FLOAT => Right(ZioSchema.primitive(ZioStandardType.FloatType))
        case SchemaApacheAvro.Type.DOUBLE => Right(ZioSchema.primitive(ZioStandardType.DoubleType))
        case SchemaApacheAvro.Type.BOOLEAN => Right(ZioSchema.primitive(ZioStandardType.BoolType))
        case SchemaApacheAvro.Type.NULL => Right(ZioSchema.primitive(ZioStandardType.UnitType))
        case null => Left(s"Unsupported type ${avroApacheSchema.getType}")
      }
    } yield result


  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L737-L767
   *
   * @param avroApacheSchema
   * @tparam A
   * @tparam Z
   * @return
   */

  def toZioEnumeration[A, Z](avroApacheSchema: SchemaApacheAvro): scala.util.Either[String, ZioSchema[Z]] = {
    val cases = avroApacheSchema.getTypes.asScala
      .map(t => {
        val inner =
          if (t.getType == SchemaApacheAvro.Type.RECORD && t.getFields.size() == 1 && t
                                                                                  .getObjectProp(UnionWrapper.propName) == true) {
            t.getFields.asScala.head.schema() // unwrap nested union
          } else t
        apacheAvroSchemaToZioSchema(inner).map(
          s =>
            ZioSchema.Case[Z, A](
              t.getFullName,
              s.asInstanceOf[ZioSchema[A]],
              _.asInstanceOf[A],
              _.asInstanceOf[Z],
              (z: Z) => z.isInstanceOf[A@unchecked]
            )
        )
      })
    val caseSet = cases.toList.map(_.merge).partition {
      case _: String => true
      case _ => false
    } match {
      case (Nil, right: Seq[Case[_, _]@unchecked]) =>
        Try {
          CaseSet(right: _*).asInstanceOf[CaseSet {type EnumType = Z}]
        }.toEither.left.map(_.getMessage)
      case (left, _) => Left(left.mkString("\n"))
    }
    caseSet.map(cs => ZioSchema.enumeration(TypeId.parse(avroApacheSchema.getName), cs))
  }


  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L711-L735
   * @param avroApacheSchema
   * @param decimalType
   * @return
   */
  def toZioDecimal(
                    avroApacheSchema: SchemaApacheAvro,
                    decimalType: DecimalType
                  ): scala.util.Either[String, ZioSchema[_]] = {
    val decimalTypeAnnotation = AvroAnnotations.decimal(decimalType)
    val decimalLogicalType = avroApacheSchema.getLogicalType.asInstanceOf[LogicalTypes.Decimal]
    val precision = decimalLogicalType.getPrecision
    val scale = decimalLogicalType.getScale
    if (precision - scale > 0) {
      Right(
        ZioSchema
          .primitive(StandardType.BigDecimalType)
          .annotate(AvroAnnotations.scale(scale))
          .annotate(AvroAnnotations.precision(precision))
          .annotate(decimalTypeAnnotation)
      )
    } else {
      Right(
        ZioSchema
          .primitive(StandardType.BigIntegerType)
          .annotate(AvroAnnotations.scale(scale))
          .annotate(decimalTypeAnnotation)
      )
    }
  }


  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L769-L837
   * @param avroApacheSchema
   * @return
   */
  def toZioRecord(avroApacheSchema: SchemaApacheAvro): scala.util.Either[String, ZioSchema[_]] =
    if (avroApacheSchema.getObjectProp(UnionWrapper.propName) != null) {
      avroApacheSchema.getFields.asScala.headOption match {
        case Some(value) => apacheAvroSchemaToZioSchema(value.schema())
        case None => Left("ZIO schema wrapped record must have a single field")
      }
    } else if (avroApacheSchema.getObjectProp(EitherWrapper.propName) != null) {
      avroApacheSchema.getFields.asScala.headOption match {
        case Some(value) =>
          apacheAvroSchemaToZioSchema(value.schema()).flatMap {
            case enu: Enum[_] =>
              enu.cases.toList match {
                case first :: second :: Nil => Right(ZioSchema.either(first.schema, second.schema))
                case _ => Left("ZIO schema wrapped either must have exactly two cases")
              }
            case e: ZioSchema.Either[_, _] => Right(e)
            case c: CaseClass0[_] => Right(c)
            case c: CaseClass1[_, _] => Right(c)
            case c: CaseClass2[_, _, _] => Right(c)
            case c: CaseClass3[_, _, _, _] => Right(c)
            case c: CaseClass4[_, _, _, _, _] => Right(c)
            case c: CaseClass5[_, _, _, _, _, _] => Right(c)
            case c: CaseClass6[_, _, _, _, _, _, _] => Right(c)
            case c: CaseClass7[_, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass8[_, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass9[_, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass10[_, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass11[_, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass12[_, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass13[_, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: CaseClass22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _] => Right(c)
            case c: Dynamic => Right(c)
            case c: GenericRecord => Right(c)
            case c: Map[_, _] => Right(c)
            case c: Sequence[_, _, _] => Right(c)
            case c: Set[_] => Right(c)
            case c: Fail[_] => Right(c)
            case c: Lazy[_] => Right(c)
            case c: Optional[_] => Right(c)
            case c: Primitive[_] => Right(c)
            case c: Transform[_, _, _] => Right(c)
            case c: Tuple2[_, _] => Right(c)

          }
        case None => Left("ZIO schema wrapped record must have a single field")
      }
    } else {
      val annotations = buildZioAnnotations(avroApacheSchema)
      extractZioFields(avroApacheSchema).map { (fs: List[Field[ListMap[String, _], _]]) =>
        ZioSchema.record(TypeId.parse(avroApacheSchema.getName), fs: _*).addAllAnnotations(annotations)
      }
    }

  private def extractZioFields[Z](avroApacheSchema: SchemaApacheAvro): scala.util.Either[String, List[Field[Z, _]]] =
    avroApacheSchema.getFields.asScala.map(toZioField).toList.map(_.merge).partition {
      case _: String => true
      case _ => false
    } match {
      case (Nil, right: List[Field[Z, _]@unchecked]) => Right(right)
      case (left, _) => Left(left.mkString("\n"))
    }


  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L839-L850
   *
   * @param field
   * @return
   */
  def toZioField(field: SchemaApacheAvro.Field): scala.util.Either[String, Field[ListMap[String, _], _]] =
    apacheAvroSchemaToZioSchema(field.schema())
      .map(
        (s: ZioSchema[_]) =>
          Field(
            field.name(),
            s.asInstanceOf[ZioSchema[Any]],
            buildZioAnnotations(field),
            get0 = (p: ListMap[String, _]) => p(field.name()),
            set0 = (p: ListMap[String, _], v: Any) => p.updated(field.name(), v)
          )
      )


  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L852-L892
   *
   * @param schema
   * @return
   */
  def toZioTuple(schema: SchemaApacheAvro): scala.util.Either[String, ZioSchema[_]] =
    for {
      _ <- scala.util.Either
        .cond(schema.getFields.size() == 2, (), "Tuple must have exactly 2 fields:" + schema.toString(false))
      _1 <- apacheAvroSchemaToZioSchema(schema.getFields.get(0).schema())
      _2 <- apacheAvroSchemaToZioSchema(schema.getFields.get(1).schema())
    } yield ZioSchema.Tuple2(_1, _2, buildZioAnnotations(schema))


  private def buildZioAnnotations(schema: SchemaApacheAvro): Chunk[StaticAnnotation] = {
    val name = AvroAnnotations.name(schema.getName)
    val namespace = Try {
      Option(schema.getNamespace).map(AvroAnnotations.namespace.apply)
    }.toOption.flatten
    val doc = if (schema.getDoc != null) Some(AvroAnnotations.doc(schema.getDoc)) else None
    val aliases = Try {
      if (schema.getAliases != null && !schema.getAliases.isEmpty)
        Some(AvroAnnotations.aliases(schema.getAliases.asScala.toSet))
      else None
    }.toOption.flatten
    val error = Try {
      if (schema.isError) Some(AvroAnnotations.error) else None
    }.toOption.flatten
    val default = Try {
      if (schema.getEnumDefault != null) Some(MyAvroAnnotations.myDefault(schema.getEnumDefault)) else None
    }.toOption.flatten
    Chunk(name) ++ namespace ++ doc ++ aliases ++ error ++ default
  }


  /**
   * HELP ISSUE: the case class `default` from `AvroAnnotations` is private and cannot call it outsdie of codec package
   * TEMP solution: putting another one here - hope it acts the same
   */
  object MyAvroAnnotations {
    final case class myDefault(javaDefaultObject: java.lang.Object) extends StaticAnnotation
  }



  private def buildZioAnnotations(field: SchemaApacheAvro.Field): Chunk[Any] = {
    val nameAnnotation = Some(AvroAnnotations.name(field.name))
    val docAnnotation = if (field.doc() != null) Some(AvroAnnotations.doc(field.doc)) else None
    val aliasesAnnotation =
      if (!field.aliases().isEmpty) Some(AvroAnnotations.aliases(field.aliases.asScala.toSet)) else None
    val default = Try {
      if (field.hasDefaultValue) Some(MyAvroAnnotations.myDefault(field.defaultVal())) else None
    }.toOption.flatten
    val orderAnnotation = Some(AvroAnnotations.fieldOrder(FieldOrderType.fromAvroOrder(field.order())))
    val annotations: Seq[StaticAnnotation] =
      List(nameAnnotation, docAnnotation, aliasesAnnotation, orderAnnotation, default).flatten
    Chunk.fromIterable(annotations)
  }





  /**
   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L894-L902
   *
   * @param avroApacheSchema
   * @return
   */
  def toZioStringEnum(avroApacheSchema: SchemaApacheAvro): scala.util.Either[String, ZioSchema[_]] = {
    val cases =
      avroApacheSchema.getEnumSymbols.asScala
        .map(s => ZioSchema.Case[String, String](s, ZioSchema[String], identity, identity, _.isInstanceOf[String]))
        .toSeq
    val caseSet = CaseSet[String](cases: _*).asInstanceOf[Aux[String]]
    val enumeration: ZioSchema[String] = ZioSchema.enumeration(TypeId.parse("org.apache.avro.ZioSchema"), caseSet)
    Right(enumeration.addAllAnnotations(buildZioAnnotations(avroApacheSchema)))
  }


  private case object OptionUnion {

    def unapply(schema: SchemaApacheAvro): Option[SchemaApacheAvro] =
      if (schema.getType == SchemaApacheAvro.Type.UNION) {
        val types = schema.getTypes
        if (types.size == 2) {
          if (types.get(0).getType == SchemaApacheAvro.Type.NULL ||
              types.get(1).getType == SchemaApacheAvro.Type.NULL) {
            if (types.get(1).getType != SchemaApacheAvro.Type.NULL) {
              Some(types.get(1))
            } else if (types.get(0).getType != SchemaApacheAvro.Type.NULL) {
              Some(types.get(0))
            } else {
              None
            }
          } else {
            None
          }
        } else {
          None
        }
      } else {
        None
      }
  }

  private case object EitherUnion {

    def unapply(schema: SchemaApacheAvro): Option[(SchemaApacheAvro, SchemaApacheAvro)] =
      if (schema.getType == SchemaApacheAvro.Type.UNION &&
          schema.getObjectProp(EitherWrapper.propName) == EitherWrapper.value) {
        val types = schema.getTypes
        if (types.size == 2) {
          Some(types.get(0) -> types.get(1))
        } else {
          None
        }
      } else {
        None
      }
  }

}
