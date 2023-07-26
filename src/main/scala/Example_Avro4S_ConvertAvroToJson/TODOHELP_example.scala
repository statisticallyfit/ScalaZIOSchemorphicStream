package Example_Avro4S_ConvertAvroToJson


import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
///import com.github.massmutual.streaming.example.util.StringUtil
import com.sksamuel.avro4s._
import org.apache.avro.Schema

import scala.collection.JavaConverters._


/**
 *
 */
object TODOHELP_example extends App {
	
	
	def getFooAvroOutputStream(data: Seq[Foo]): ByteArrayOutputStream = {
		val stream = new ByteArrayOutputStream()
		val os = AvroOutputStream.data[Foo].to(stream).build()
		os.write(data)
		os.flush()
		os.close()
		stream
	}
	
	def getFooFromAvro(stream: ByteArrayOutputStream): List[Foo] = {
		val schema = AvroSchema[Foo]
		val inputStream = new ByteArrayInputStream(stream.toByteArray)
		val is = AvroInputStream.data[Foo].from(inputStream).build(schema)
		val data = is.iterator.toList
		is.close()
		data
	}
	
	sealed trait Foo
	
	case class Bar(i: Int) extends Foo
	
	case class Baz(z: Int) extends Foo
	
	case class Fighter(k: List[Foo]) extends Foo
	
	//leaf types
	val barSchema: Schema = AvroSchema[Bar]
	val bazSchema: Schema = AvroSchema[Baz]
	
	//create the record type for fighter schema
	//fill in the meta stuff
	val fighterSchema: Schema = Schema
		.createRecord("Fighter", "my cool fighter", "foo.fighter", false)
	
	val fooSchema: Schema = Schema.createUnion(barSchema, bazSchema, fighterSchema)
	
	//
	//notice when we created `fighterSchema` above it was never given field information
	//let's set its field for `k`
	//
	fighterSchema.setFields(
		Seq(
			//k is an array that contains possible Foo's
			new Schema.Field("k", Schema.createArray(fooSchema))
		).asJava
	)
	
	val fooSchemaFor: SchemaFor[Foo] = SchemaFor(fooSchema)
	val fighterSchemaFor: SchemaFor[Fighter] = SchemaFor(fighterSchema)
	
	val avroFooSchema = AvroSchema[Foo](fooSchemaFor)
	val avroFighterSchema = AvroSchema[Fighter](fighterSchemaFor)
	
	println(avroFooSchema)
	println(avroFighterSchema)
	
	//test data
	val bar = Bar(5)
	val baz = Baz(65)
	val fighter = Fighter(k = bar :: baz :: Nil)
	
	val data: List[Foo] = List(fighter, bar, baz)
	
	val outputStream = getFooAvroOutputStream(data)
	val readFromAvro = getFooFromAvro(outputStream)
	
	println(readFromAvro)
	
	//easier way of composing avro4s record
	val format = RecordFormat[Foo]
	val fighterRecord: Record = format.to(fighter)
	val barRecord: Record = format.to(bar)
	
	//com.github.massmutual.streaming.example.util.StringUtil
	// TODO help how to get this library? cannot find it anywhere
	//println(StringUtil.avroToJson(fighterRecord))
	//println(StringUtil.avroToJson(barRecord))
}
