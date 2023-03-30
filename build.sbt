name := "ScalaZIOSchemorphicStream"

version := "0.1"

scalaVersion := "2.13.10"


//addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")


// PROJECTS


// global is the parent project, which aggregates all the other projects
lazy val global = project
	.in(file("."))
	.settings(
		name := "ScalaZIOSchemorphicStream",
		settings,
		libraryDependencies ++= commonDependencies ++ Seq(

			allDependencies.scalaLibrary,
			allDependencies.scalaCompiler,
			allDependencies.scalaReflect,

			allDependencies.scalaCheck,

			allDependencies.specs2Core,
			allDependencies.specs2ScalaCheck,

			allDependencies.scalaTest,

			//allDependencies.discipline,
			allDependencies.discipline_core,
			allDependencies.discipline_scalatest,
			allDependencies.discipline_specs2,


			allDependencies.cats_core,
			allDependencies.cats_kernel,
			allDependencies.cats_laws,
			allDependencies.cats_free,
			allDependencies.cats_macros,
			allDependencies.cats_testkit,
			allDependencies.cats_effects,

			//allDependencies.kindProjector,

			allDependencies.zioSchema,
			allDependencies.zioSchemaJson,
			allDependencies.zioSchemaProtobuf,
			allDependencies.zioSchemaDerivation,

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
		val versionOfScala = "2.13.10"

		val versionOfScalaTest = "3.2.15" //"3.3.0-SNAP2"

		val versionOfScalaCheck = "1.17.0"

		val versionOfSpecs2 = "4.19.2" //4.9.4

		//val versionOfDiscipline = "0.11.1"
		val versionOfDiscipline_core = "1.5.1" //"1.0.2"
		val versionOfDiscipline_scalatest = "2.2.0" //"1.0.1"
		val versionOfDiscipline_specs2 = "1.4.0" //"1.1.0"

		//val versionOfSpire = "0.17.0-M1"

		//val versionOfAlgebra =  "2.0.0" //"2.0.1"

		val versionOfCats = "2.9.0" // "2.2.0-M3"
		val versionOfCats_effects = "3.4.8"
		val versionOfCats_macros = "2.1.1"

		//val versionOfKindProjector = "0.13.2"

		val versionOfZIO_schema = "0.4.8"

		val versionOfShapeless = "2.3.10"

		val versionOfDroste = "0.9.0"
		val versionOfMatryoshka = "0.21.3"

		//------------------

		// Listing the different dependencies
		val scalaLibrary = "org.scala-lang" % "scala-library" % versionOfScala
		val scalaCompiler = "org.scala-lang" % "scala-compiler" % versionOfScala
		val scalaReflect = "org.scala-lang" % "scala-reflect" % versionOfScala


		//val scalactic = "org.scalactic" %% "scalactic" % versionOfScalactic

		val scalaTest = "org.scalatest" %% "scalatest" % versionOfScalaTest % Test

		val scalaCheck = "org.scalacheck" %% "scalacheck" % versionOfScalaCheck % Test

		val specs2Core = "org.specs2" %% "specs2-core" % versionOfSpecs2 % Test
		val specs2ScalaCheck = "org.specs2" %% "specs2-scalacheck" % versionOfSpecs2 % Test

		//val discipline = "org.typelevel" %% "discipline" % versionOfDiscipline
		val discipline_core = "org.typelevel" %% "discipline-core" % versionOfDiscipline_core
		val discipline_scalatest = "org.typelevel" %% "discipline-scalatest" % versionOfDiscipline_scalatest % Test
		val discipline_specs2 = "org.typelevel" %% "discipline-specs2" % versionOfDiscipline_specs2 % Test


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
		val zioSchema = "dev.zio" %% "zio-schema" % versionOfZIO_schema
		val zioSchemaJson = "dev.zio" %% "zio-schema-json" % versionOfZIO_schema
		val zioSchemaProtobuf = "dev.zio" %% "zio-schema-protobuf" % versionOfZIO_schema
		// Required for automatic generic derivation of schemas
		val zioSchemaDerivation = "dev.zio" %% "zio-schema-derivation" % versionOfZIO_schema

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

lazy val settings =
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
	resolvers ++= Seq(
		//"Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
		Resolver.sonatypeRepo("releases"),
		Resolver.sonatypeRepo("snapshots"),
		/*// Resolver for Rainier library
		Resolver.bintrayRepo("rainier", "maven"),
		// Resolver for evilplot (dependency of Rainier)
		Resolver.bintrayRepo("cibotech", "public")*/
	)
)
