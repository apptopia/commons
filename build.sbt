import sbt.Keys._

name := "commons"

val commonDeps = Seq(
  "commons-io" % "commons-io" % "2.5",
  "com.typesafe" % "config" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

val networkDeps = Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.24" % "provided",
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8.1"
)

val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.16" % "provided"
)

lazy val commonSettings = Seq(
  version := "1.0.5",
  isSnapshot := version.value.endsWith("SNAPSHOT"),
  organization := "com.github.karasiq",
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1"),
  libraryDependencies ++= commonDeps,
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ ⇒ false },
  licenses := Seq("Apache License, Version 2.0" → url("http://opensource.org/licenses/Apache-2.0")),
  homepage := Some(url("https://github.com/Karasiq/commons")),
  pomExtra := <scm>
    <url>git@github.com:Karasiq/commons.git</url>
    <connection>scm:git:git@github.com:Karasiq/commons.git</connection>
  </scm>
    <developers>
      <developer>
        <id>karasiq</id>
        <name>Piston Karasiq</name>
        <url>https://github.com/Karasiq</url>
      </developer>
    </developers>
)

lazy val rootSettings = Seq(
  name := "commons"
)

lazy val miscSettings = Seq(
  name := "commons-misc"
)

lazy val filesSettings = Seq(
  name := "commons-files"
)

lazy val networkSettings = Seq(
  name := "commons-network",
  libraryDependencies ++= networkDeps
)

lazy val akkaSettings = Seq(
  name := "commons-akka",
  libraryDependencies ++= akkaDeps ++ networkDeps
)

lazy val misc = Project("commons-misc", file("misc"))
  .settings(commonSettings, miscSettings)

lazy val files = Project("commons-files", file("files"))
  .settings(commonSettings, filesSettings)

lazy val network = Project("commons-network", file("network"))
  .settings(commonSettings, networkSettings)
  .dependsOn(misc, files)

lazy val akka = Project("commons-akka", file("akka"))
  .settings(commonSettings, akkaSettings)
  .dependsOn(network, files)

lazy val root = Project("commons", file("."))
  .settings(commonSettings, rootSettings)
  .dependsOn(misc, files, network)
  .aggregate(misc, files, network, akka)