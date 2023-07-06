//package utilMain.utilZio
//
//
//import org.apache.avro.{Schema ⇒ SchemaApacheAvro}
//import org.apache.avro.LogicalTypes
//
//
//import zio.schema.Schema.{Dynamic, Enum, Fail, Lazy, Optional, Primitive, Record, Transform, Tuple2}
//import zio.schema.{Schema ⇒ ZioSchema, StandardType ⇒ ZioStandardType}
//
//import zio.schema.codec.AvroPropMarker.{EitherWrapper, IntDiscriminator, RecordDiscriminator, StringDiscriminator, UnionWrapper}
//import zio.schema.codec.AvroAnnotations._
//import zio.schema.codec.{ RecordType}
//
//import zio.schema.meta.MetaSchema
//
//
//import zio.schema.DeriveSchema
//
//
//
//import zio.Chunk
//import zio.schema.CaseSet.Aux
//import zio.schema.Schema.{Record, _}
//import zio.schema._
//
//
//import scala.jdk.CollectionConverters._
//import scala.util.{Right, Try}
//import scala.annotation.StaticAnnotation
//import scala.collection.immutable.ListMap
//
///**
// *
// */
//object ZioToApacheFunctions {
//
//
//  /**
//   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#LL30C16-L30C16
//   *
//   * @param schema
//   * @return
//   *
//   * Previous name: def encode
//   */
//  def zioSchemaToAvroString(schema: ZioSchema[_]): scala.util.Either[String, String] =
//    toAvroSchema(schema).map(_.toString)
//
//  /**
//   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#L33
//   *
//   * @param schema
//   * @return
//   *
//   * Previous name: def encodeToApacheAvro
//   */
//  def zioSchemaToApacheAvroSchema(schema: ZioSchema[_]): scala.util.Either[String, SchemaApacheAvro] =
//    toAvroSchema(schema)
//
//
//  /**
//   * SOURCE = https://github.com/zio/zio-schema/blob/4e1e00193a59e5d3465fbb76433be5e680df21d7/zio-schema-avro/shared/src/main/scala/zio/schema/codec/AvroCodec.scala#LL278C11-L419C4
//   *
//   * @param schema
//   * @return
//   *
//   * Previous name: toAvroSchema
//   */
//  def toAvroSchema(schema: ZioSchema[_]): scala.util.Either[String, SchemaApacheAvro] = {
//    schema match {
//      case e: Enum[_] => toAvroEnum(e)
//      case record: Record[_] => toAvroRecord(record)
//      case map: Schema.Map[_, _] => toAvroMap(map)
//      case seq: Schema.Sequence[_, _, _] => toAvroSchema(seq.elementSchema).map(SchemaApacheAvro.createArray)
//      case set: Schema.Set[_] => toAvroSchema(set.elementSchema).map(SchemaApacheAvro.createArray)
//      case Transform(codec, _, _, _, _) => toAvroSchema(codec)
//      case Primitive(standardType, _) =>
//        standardType match {
//          case ZioStandardType.UnitType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.NULL))
//          case ZioStandardType.StringType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING))
//          case ZioStandardType.BoolType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.BOOLEAN))
//          case ZioStandardType.ShortType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.Short)))
//          case ZioStandardType.ByteType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT))
//          case ZioStandardType.IntType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT))
//          case ZioStandardType.LongType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.LONG))
//          case ZioStandardType.FloatType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.FLOAT))
//          case ZioStandardType.DoubleType => Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.DOUBLE))
//          case ZioStandardType.BinaryType => Right(toAvroBinary(schema))
//          case ZioStandardType.CharType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.Char)))
//          case ZioStandardType.UUIDType =>
//            Right(LogicalTypes.uuid().addToSchema(SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING)))
//          case ZioStandardType.BigDecimalType => toAvroDecimal(schema)
//          case ZioStandardType.BigIntegerType => toAvroDecimal(schema)
//          case ZioStandardType.DayOfWeekType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.DayOfWeek)))
//          case ZioStandardType.MonthType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.Month)))
//          case ZioStandardType.YearType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.Year)))
//          case ZioStandardType.ZoneIdType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.STRING).addMarkerProp(StringDiscriminator(StringType.ZoneId)))
//          case ZioStandardType.ZoneOffsetType =>
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.INT).addMarkerProp(IntDiscriminator(IntType.ZoneOffset)))
//          case ZioStandardType.MonthDayType =>
//            //TODO 1
//            //Right(SchemaApacheAvro.create(monthDayStructure).addMarkerProp(RecordDiscriminator(RecordType.MonthDay)))
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.RECORD))
//          case ZioStandardType.PeriodType =>
//            //TODO 2
//            //toAvroSchema(periodStructure).map(_.addMarkerProp(RecordDiscriminator(RecordType.Period)))
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.RECORD))
//          case ZioStandardType.YearMonthType =>
//            //TODO 3
//            //toAvroSchema(yearMonthStructure).map(_.addMarkerProp(RecordDiscriminator(RecordType.YearMonth)))
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.RECORD))
//          case ZioStandardType.DurationType =>
//            // TODO: Java implementation of Apache Avro does not support logical type Duration yet:
//            //  AVRO-2123 with PR https://github.com/apache/avro/pull/1263
//            //TODO 4
//            //val chronoUnitMarker =
//            //DurationChronoUnit.fromTemporalUnit(temporalUnit).getOrElse(DurationChronoUnit.default)
//            //toAvroSchema(durationStructure).map(
//            //  _.addMarkerProp(RecordDiscriminator(RecordType.Duration)).addMarkerProp(chronoUnitMarker))
//            Right(SchemaApacheAvro.create(SchemaApacheAvro.Type.RECORD))
//
//          case ZioStandardType.InstantType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.Instant))
//            )
//          case ZioStandardType.LocalDateType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.LocalDate))
//            )
//          case ZioStandardType.LocalTimeType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.LocalTime))
//            )
//          case ZioStandardType.LocalDateTimeType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.LocalDateTime))
//            )
//          case ZioStandardType.OffsetTimeType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.OffsetTime))
//            )
//          case ZioStandardType.OffsetDateTimeType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.OffsetDateTime))
//            )
//          case ZioStandardType.ZonedDateTimeType =>
//            Right(
//              SchemaApacheAvro
//                .create(SchemaApacheAvro.Type.STRING)
//                .addMarkerProp(StringDiscriminator(StringType.ZoneDateTime))
//            )
//        }
//      case Optional(codec, _) =>
//        for {
//          codecName <- getName(codec)
//          codecAvroSchema <- toAvroSchema(codec)
//          wrappedAvroSchema = codecAvroSchema match {
//            case schema: SchemaApacheAvro if schema.getType == SchemaApacheAvro.Type.NULL =>
//              wrapAvro(schema, codecName, UnionWrapper)
//            case schema: SchemaApacheAvro if schema.getType == SchemaApacheAvro.Type.UNION =>
//              wrapAvro(schema, codecName, UnionWrapper)
//            case schema => schema
//          }
//        } yield SchemaApacheAvro.createUnion(SchemaApacheAvro.create(SchemaApacheAvro.Type.NULL), wrappedAvroSchema)
//      case Fail(message, _) => Left(message)
//      case tuple: Tuple2[_, _] =>
//        toAvroSchema(tuple.toRecord).map(
//          _.addMarkerProp(RecordDiscriminator(RecordType.Tuple))
//        )
//      case e@Schema.Either(left, right, _) =>
//        val eitherUnion = for {
//          l <- toAvroSchema(left)
//          r <- toAvroSchema(right)
//          lname <- getName(left)
//          rname <- getName(right)
//          leftSchema = if (l.getType == SchemaApacheAvro.Type.UNION) wrapAvro(l, lname, UnionWrapper) else l
//          rightSchema = if (r.getType == SchemaApacheAvro.Type.UNION) wrapAvro(r, rname, UnionWrapper) else r
//          _ <- if (leftSchema.getFullName == rightSchema.getFullName)
//            Left(s"Left and right schemas of either must have different fullnames: ${leftSchema.getFullName}")
//          else Right(())
//        } yield SchemaApacheAvro.createUnion(leftSchema, rightSchema)
//
//        // Unions in Avro can not hold additional properties, so we need to wrap the union in a record
//        for {
//          union <- eitherUnion
//          name <- getName(e)
//        } yield wrapAvro(union, name, EitherWrapper)
//
//      case Lazy(schema0) => toAvroSchema(schema0())
//      case Dynamic(_) => toAvroSchema(Schema[MetaSchema])
//    }
//  }
//}
