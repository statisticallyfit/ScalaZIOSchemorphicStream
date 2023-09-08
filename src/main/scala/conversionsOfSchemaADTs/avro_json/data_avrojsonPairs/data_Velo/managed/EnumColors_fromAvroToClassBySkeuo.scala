package conversionsOfSchemaADTs.avro_json.data_avrojsonPairs.data_Velo.managed

/**
 *
 */
object EnumColors_fromAvroToClassBySkeuo{

	sealed abstract class TheColorsEnum(val value: Int) extends enumeratum.values.IntEnumEntry

	object TheColorsEnum extends enumeratum.values.IntEnum[TheColorsEnum] {
		case object Red extends TheColorsEnum(0)

		case object Orange extends TheColorsEnum(1)

		case object Pink extends TheColorsEnum(2)

		case object Yellow extends TheColorsEnum(3)

		case object Green extends TheColorsEnum(4)

		case object Blue extends TheColorsEnum(5)

		case object Indigo extends TheColorsEnum(6)

		case object Violet extends TheColorsEnum(7)

		val values = findValues
	}
}
