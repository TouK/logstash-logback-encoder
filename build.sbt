organization := "pl.touk.nussknacker"
organizationName := "Nussknacker"
organizationHomepage := Some(new URL("https://nussknacker.io/"))
name := "logstash-logback-encoder"
description := "net.logstash.logback:logstash-logback-encoder with shaded Jackson"
version := logstashLogbackEncoderV
versionScheme := Some("pvp")

// make sure that these two are compatible, and that you use it with a compatible version of Logback
lazy val jacksonV = "2.14.3"
lazy val logstashLogbackEncoderV = "7.3"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonV,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonV,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonV,
  "net.logstash.logback" % "logstash-logback-encoder" % logstashLogbackEncoderV,
)

autoScalaLibrary := false
crossPaths := false

packageDoc / publishArtifact := false
packageSrc / publishArtifact := false

assembly / artifact := { (assembly / artifact).value.withClassifier(Some("shaded")) }
addArtifact(assembly / artifact, assembly)

assembly / assemblyMergeStrategy := {
  case PathList(ps @ _*) if ps.last == "module-info.class" => MergeStrategy.discard
  case x => MergeStrategy.defaultMergeStrategy(x)
}
assembly / assemblyShadeRules := Seq(
  ShadeRule.rename("com.fasterxml.jackson.**" -> "shaded.pl.touk.nussknacker.lle.com.fasterxml.jackson.@1").inAll,
)

publishMavenStyle := true
publishTo := sonatypePublishToBundle.value
homepage := Some(url("https://github.com/TouK/logstash-logback-encoder"))
licenses := Seq("Apache 2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
pomExtra := {
  <scm>
    <connection>scm:git:https://github.com/TouK/logstash-logback-encoder.git</connection>
    <developerConnection>scm:git:git@github.com:TouK/logstash-logback-encoder.git</developerConnection>
    <url>https://github.com/TouK/logstash-logback-encoder</url>
  </scm>
    <developers>
      <developer>
        <id>Nussknacker</id>
        <name>Nussknacker</name>
        <url>https://nussknacker.io/</url>
      </developer>
    </developers>
}
// remove dependencies that were shaded (i.e. all of them)
pomPostProcess := {
  import scala.xml.{Elem, Node}
  import xml.transform._
  new RuleTransformer(new RewriteRule {
    override def transform(n: Node): Node = n match {
      case e: Elem if e.label == "dependencies" => e.copy(child = Seq.empty)
      case other => other
    }
  })
}
