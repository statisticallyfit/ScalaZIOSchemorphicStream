
/*addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.2.1")
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "2.0.8")
//addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.3.1")
addSbtPlugin("nl.gn0s1s" % "sbt-dotenv" % "2.1.233")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")*/





// SOURCE OF BELOW CODE = https://gist.github.com/paulp/e8df663712fe5455b433f5dd71c043c6#file-plugins-sbt

lazy val isEnabled: Set[String] = sys.props.getOrElse("plugins", "").split(",").map(_.trim).toSet

def maybeEnable(pair: (String, ModuleID)): Seq[Setting[_]] =
	if (isEnabled(pair._1)) addSbtPlugin(pair._2) else Seq()

Seq[(String, ModuleID)](
	"COURSIER" -> "io.get-coursier" % "sbt-coursier" % "2.0.8",
	"GITHUBPACKAGES" -> "com.codecommit" % "sbt-github-packages" % "0.5.2",
	"DOTENV" -> "nl.gn0s1s" % "sbt-dotenv" % "3.0.0",
	"BUILDINFO" -> "com.eed3si9n" % "sbt-buildinfo" % "0.11.0"
).flatMap(maybeEnable)