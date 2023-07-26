package Examples_AndyGlow


import com.github.andyglow.json.JsonFormatter
import com.github.andyglow.jsonschema.AsValue


/**
 *
 */
object PersonExample extends App {
	
	
	// Create the scala case classes that we want to convert into json schemas
	sealed trait Gender
	
	object Gender {
		
		case object Male extends Gender
		
		case object Female extends Gender
	}
	
	case class Company(name: String)
	
	case class Car(name: String, manufacturer: Company)
	
	case class Person(
					  firstName: String,
					  middleName: Option[String],
					  lastName: String,
					  gender: Gender,
					  birthDay: java.time.LocalDateTime,
					  company: Company,
					  cars: Seq[Car]
				  )
	
	
	// -------------------------------------------------------------------------------------
	// Now Create the json schema:
	
	import json._
	
	
	val personSchema: json.Schema[Person] = Json.schema[Person]
	
	
	println(JsonFormatter.format(AsValue.schema(personSchema, json.schema.Version.Draft06(id = "some id here"))))
	
	/*
	{
	"$schema": "http://json-schema.org/draft-04/schema#",
	"type": "object",
	"additionalProperties": false,
	"properties": {
	  "middleName": {
	    "type": "string"
	  },
	  "cars": {
	    "type": "array",
	    "items": {
		 "type": "object",
		 "additionalProperties": false,
		 "properties": {
		   "name": {
			"type": "string"
		   },
		   "manufacturer": {
			"type": "object",
			"additionalProperties": false,
			"properties": {
			  "name": {
			    "type": "string"
			  }
			},
			"required": [
			  "name"
			]
		   }
		 },
		 "required": [
		   "name",
		   "manufacturer"
		 ]
	    }
	  },
	  "company": {
	    "type": "object",
	    "additionalProperties": false,
	    "properties": {
		 "name": {
		   "type": "string"
		 }
	    },
	    "required": [
		 "name"
	    ]
	  },
	  "lastName": {
	    "type": "string"
	  },
	  "firstName": {
	    "type": "string"
	  },
	  "birthDay": {
	    "type": "string",
	    "format": "date-time"
	  },
	  "gender": {
	    "type": "string",
	    "enum": [
		 "Male",
		 "Female"
	    ]
	  }
	},
	"required": [
	  "company",
	  "lastName",
	  "birthDay",
	  "gender",
	  "firstName",
	  "cars"
	]
   }
	 */
}
