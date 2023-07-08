name := "SchaemeowMorphism"

version := "0.1"

scalaVersion := "2.12.17" //"2.13.10" //"2.12.17"



//addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")


// PROJECTS


// global is the parent project, which aggregates all the other projects
lazy val global = project
	.in(file("."))
	.settings(
		name := "SchaemeowMorphism",
		mysettings,
		libraryDependencies ++= commonDependencies ++ Seq(
			
			allDependencies.scalaLibrary,
			allDependencies.scalaCompiler,
			allDependencies.scalaReflect,
			
			allDependencies.scalaCheck,
			allDependencies.scalaCheckCats,
			
			allDependencies.specs2Core,
			allDependencies.specs2ScalaCheck,
			
			allDependencies.scalaTest,
			
			//allDependencies.discipline,
			//allDependencies.discipline_core,
			//allDependencies.discipline_scalatest,
			//allDependencies.discipline_specs2,
			
			
			allDependencies.cats_core,
			allDependencies.cats_kernel,
			allDependencies.cats_laws,
			allDependencies.cats_free,
			allDependencies.cats_macros,
			allDependencies.cats_testkit,
			allDependencies.cats_effects,
			
			allDependencies.shapeless,
			//allDependencies.kindProjector,
			
			allDependencies.zio,
			allDependencies.zioSchema,
			allDependencies.zioSchemaAvro,
			allDependencies.zioSchemaJson,
			allDependencies.zioSchemaProtobuf,
			allDependencies.zioSchemaDerivation,
			allDependencies.zioStream,
			allDependencies.zioTest,
			
			allDependencies.matryoshka,
			
			allDependencies.drosteCore,
			allDependencies.drosteLaws,
			allDependencies.drosteMacros,
			allDependencies.drosteScalaCheck,
			
			allDependencies.skeuomorph,
			
			allDependencies.andyGlowScalaJsonSchema,
			allDependencies.andyGlow_jsonschema_Macros,
			allDependencies.andyGlow_jsonschema_PlayJson,
			allDependencies.andyGlow_jsonschema_SprayJson,
			allDependencies.andyGlow_jsonschema_CirceJson,
			allDependencies.andyGlow_jsonschema_Json4sJson,
			allDependencies.andyGlow_jsonschema_UJson,
			allDependencies.andyGlow_jsonschema_Joda,
			allDependencies.andyGlow_jsonschema_Cats,
			allDependencies.andyGlow_jsonschema_Refined,
			allDependencies.andyGlow_jsonschema_Derived,
			allDependencies.andyGlow_jsonschema_Enumeratum,
			allDependencies.andyGlow_jsonschema_Parser,
			
			
			allDependencies.avroTools_for_avdlToAvsc,
			
			
			allDependencies.avro4s_core,
			allDependencies.avro4s_json,
		)
	)
	.aggregate(
		//common,
		//OddsLibrary
		//multi2
	
	)
	.dependsOn(
		//OddsLibrary
	)




// DEPENDENCIES


lazy val allDependencies =
	new {
		
		// Listing the versions as values
		val versionOfScala = "2.12.17" //"2.13.10" //"2.12.17" //"2.13.10" // TODO how to use the `scalaVersion` variable above?
		
		val versionOfScalaTest = "3.2.15" //"3.3.0-SNAP2"
		
		val versionOfScalaCheck = "1.17.0"
		
		val versionOfScalaCheckCats = "0.3.2"
		
		val versionOfSpecs2 = "4.19.2" //4.9.4
		
		//val versionOfDiscipline = "0.11.1"
		//val versionOfDiscipline_core = "1.5.1" //"1.0.2"
		//val versionOfDiscipline_scalatest = "2.2.0" //"1.0.1"
		//val versionOfDiscipline_specs2 = "1.4.0" //"1.1.0"
		
		//val versionOfSpire = "0.17.0-M1"
		
		//val versionOfAlgebra =  "2.0.0" //"2.0.1"
		
		val versionOfCats = "2.9.0" // "2.2.0-M3"
		val versionOfCats_effects = "3.4.8"
		val versionOfCats_macros = "2.1.1"
		
		//val versionOfKindProjector = "0.13.2" // TODO how to get back to using it?
		
		val versionOfZIO = "2.0.13"
		val versionOfZIO_streams = "2.0.13"
		val versionOfZIO_test = "2.0.13"
		
		val versionOfZIO_schema = "0.4.11" //"0.4.8"
		
		val versionOfShapeless = "2.3.10"
		
		val versionOfDroste = "0.9.0"
		val versionOfMatryoshka = "0.21.3"
		
		
		val versionOfSkeuomorph = "0.2.1"
		val versionOfAndyGlowScalaJsonSchema = "0.7.9"
		
		val versionOfAvroTools = "1.11.1"
		
		val versionOfAvro4S = "4.1.1"
		
		//------------------
		
		// Listing the different dependencies
		val scalaLibrary = "org.scala-lang" % "scala-library" % versionOfScala
		val scalaCompiler = "org.scala-lang" % "scala-compiler" % versionOfScala
		val scalaReflect = "org.scala-lang" % "scala-reflect" % versionOfScala
		
		
		//val scalactic = "org.scalactic" %% "scalactic" % versionOfScalactic
		
		val scalaTest = "org.scalatest" %% "scalatest" % versionOfScalaTest % Test
		
		val scalaCheck = "org.scalacheck" %% "scalacheck" % versionOfScalaCheck % Test
		// https://mvnrepository.com/artifact/io.chrisdavenport/cats-scalacheck
		val scalaCheckCats = "io.chrisdavenport" %% "cats-scalacheck" % "0.3.2" % Test
		
		
		val specs2Core = "org.specs2" %% "specs2-core" % versionOfSpecs2 % Test
		val specs2ScalaCheck = "org.specs2" %% "specs2-scalacheck" % versionOfSpecs2 % Test
		// TODO - difference between specs2-scalacheck and the ordinary scalacheck???
		
		
		//val discipline = "org.typelevel" %% "discipline" % versionOfDiscipline
		//val discipline_core = "org.typelevel" %% "discipline-core" % versionOfDiscipline_core
		//val discipline_scalatest = "org.typelevel" %% "discipline-scalatest" % versionOfDiscipline_scalatest % Test
		//val discipline_specs2 = "org.typelevel" %% "discipline-specs2" % versionOfDiscipline_specs2 % Test
		
		
		val cats_core = "org.typelevel" %% "cats-core" % versionOfCats
		val cats_kernel = "org.typelevel" %% "cats-kernel" % versionOfCats
		val cats_laws = "org.typelevel" %% "cats-laws" % versionOfCats % Test
		val cats_free = "org.typelevel" %% "cats-free" % versionOfCats
		val cats_macros = "org.typelevel" %% "cats-macros" % versionOfCats_macros //versionOfCats
		//versionOfCats_macros
		val cats_testkit = "org.typelevel" %% "cats-testkit" % versionOfCats % Test
		val cats_effects = "org.typelevel" %% "cats-effect" % versionOfCats_effects % Test
		
		//Shapeless
		val shapeless = "com.chuusai" %% "shapeless" % versionOfShapeless
		
		// Kind projector plugin
		// technicalities here = https://github.com/typelevel/kind-projector
		//val kindProjector = compilerPlugin("org.typelevel" %% "kind-projector" % versionOfKindProjector)
		//val kindProjector = "org.typelevel" %% "kind-projector" % versionOfKindProjector
		//val kindProjector = "org.typelevel" %% "kind-projector" % versionOfKindProjector
		//"org.typelevel" %% "kind-projector" %
		//val kindProjector = compilerPlugin("org.spire-math" %% "kind-projector" % versionOfKindProjector)
		
		// ZIO-schema
		val zio = "dev.zio" %% "zio" % versionOfZIO
		val zioSchema = "dev.zio" %% "zio-schema" % versionOfZIO_schema
		val zioSchemaAvro = "dev.zio" %% "zio-schema-avro" % versionOfZIO_schema
		val zioSchemaJson = "dev.zio" %% "zio-schema-json" % versionOfZIO_schema
		val zioSchemaProtobuf = "dev.zio" %% "zio-schema-protobuf" % versionOfZIO_schema
		// Required for automatic generic derivation of schemas
		val zioSchemaDerivation = "dev.zio" %% "zio-schema-derivation" % versionOfZIO_schema
		val zioStream = "dev.zio" %% "zio-streams" % versionOfZIO_streams
		val zioTest = "dev.zio" %% "zio-test" % versionOfZIO_test
		
		
		// Matryoshka recursion schemes
		val matryoshka = "com.slamdata" %% "matryoshka-core" % "0.21.3"
		// TODO WARNING matryoshka is the only lib that doesn't support over scala 2.12
		
		//Droste recursion schemes
		val drosteCore = "io.higherkindness" %% "droste-core" % versionOfDroste
		val drosteLaws = "io.higherkindness" %% "droste-laws" % versionOfDroste
		val drosteMacros = "io.higherkindness" %% "droste-macros" % versionOfDroste
		/*"io.higherkindness" %% "droste-meta" % "0.8.0",
		"io.higherkindness" %% "droste-reftree" % "0.8.0",*/
		val drosteScalaCheck = "io.higherkindness" %% "droste-scalacheck" % versionOfDroste
		
		
		// Other schema libraries
		val skeuomorph = "io.higherkindness" %% "skeuomorph" % versionOfSkeuomorph
		
		val andyGlowScalaJsonSchema = "com.github.andyglow" %% "scala-jsonschema" % versionOfAndyGlowScalaJsonSchema
		val andyGlow_jsonschema_Macros = "com.github.andyglow" %% "scala-jsonschema-macros" % versionOfAndyGlowScalaJsonSchema % Provided // <-- transitive
		// json bridge. pick one
		val andyGlow_jsonschema_PlayJson = "com.github.andyglow" %% "scala-jsonschema-play-json" % versionOfAndyGlowScalaJsonSchema // <-- optional
		val andyGlow_jsonschema_SprayJson = "com.github.andyglow" %% "scala-jsonschema-spray-json" % versionOfAndyGlowScalaJsonSchema // <-- optional
		val andyGlow_jsonschema_CirceJson = "com.github.andyglow" %% "scala-jsonschema-circe-json" % versionOfAndyGlowScalaJsonSchema // <-- optional
		val andyGlow_jsonschema_Json4sJson = "com.github.andyglow" %% "scala-jsonschema-json4s-json" % versionOfAndyGlowScalaJsonSchema // <-- optional
		val andyGlow_jsonschema_UJson = "com.github.andyglow" %% "scala-jsonschema-ujson" % versionOfAndyGlowScalaJsonSchema // <-- optional
		// joda-time support
		val andyGlow_jsonschema_Joda = "com.github.andyglow" %% "scala-jsonschema-joda-time" % versionOfAndyGlowScalaJsonSchema // <-- optional
		// cats support
		val andyGlow_jsonschema_Cats = "com.github.andyglow" %% "scala-jsonschema-cats" % versionOfAndyGlowScalaJsonSchema // <-- optional
		// refined support
		val andyGlow_jsonschema_Refined = "com.github.andyglow" %% "scala-jsonschema-refined" % versionOfAndyGlowScalaJsonSchema // <-- optional
		
		val andyGlow_jsonschema_Derived = "com.github.andyglow" %% "scala-jsonschema-derived" % versionOfAndyGlowScalaJsonSchema
		// enumeratum support
		val andyGlow_jsonschema_Enumeratum = "com.github.andyglow" %% "scala-jsonschema-enumeratum" % versionOfAndyGlowScalaJsonSchema // <-- optional
		// zero-dependency json and jsonschema parser
		val andyGlow_jsonschema_Parser = "com.github.andyglow" %% "scala-jsonschema-parser" % versionOfAndyGlowScalaJsonSchema // <-- optional
		
		// https://mvnrepository.com/artifact/org.apache.avro/avro-tools
		val avroTools_for_avdlToAvsc = "org.apache.avro" % "avro-tools" % versionOfAvroTools
		
		val avro4s_core = "com.sksamuel.avro4s" %% "avro4s-core" % versionOfAvro4S
		val avro4s_json = "com.sksamuel.avro4s" %% "avro4s-json" % versionOfAvro4S
	}


lazy val commonDependencies = Seq(
	/*dependencies.logback,
	dependencies.logstash,
	dependencies.scalaLogging,
	dependencies.slf4j,
	dependencies.typesafeConfig,
	dependencies.akka,
	dependencies.scalatest  % "test",
	dependencies.scalacheck % "test"*/
)





// SETTINGS

lazy val mysettings =
	commonSettings /*++
		wartremoverSettings ++
		scalafmtSettings*/

// Recommended scala 2.13 compiler options = https://nathankleyn.com/2019/05/13/recommended-scalac-flags-for-2-13/
lazy val compilerOptions = Seq(
	"-deprecation",
	"-unchecked",
	"-feature",
	"-language:existentials",
	"-language:higherKinds",
	"-language:implicitConversions",
	"-language:postfixOps",
	// TODO try putting Xnojline:off = https://hyp.is/Ard1uM71Ee2sWMf7uSXXaQ/docs.scala-lang.org/overviews/compiler-options/index.html
	
	//"-XJline:off" // TODO trying to stop this message from appearing on REPL: warning: -Xnojline is
	// deprecated: Replaced by -Xjline:off
	//"-Ypartial-unification" //todo got error in sbt compilation " error: bad option" why?
	//"-encoding",	//"utf8"
)

lazy val commonSettings = Seq(
	scalacOptions ++= compilerOptions,
	resolvers ++= (Resolver.sonatypeOssRepos("releases") ++ Resolver.sonatypeOssRepos("snapshots")) /*Seq(
//"Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
Resolver.sonatypeOssRepos("releases"), //Resolver.sonatypeRepo("releases"),
Resolver.sonatypeRepo("snapshots") //Resolver.sonatypeRepo("snapshots"),
// Resolver for Rainier library
//Resolver.bintrayRepo("rainier", "maven"),
// Resolver for evilplot (dependency of Rainier)
//Resolver.bintrayRepo("cibotech", "public")
)*/
)
