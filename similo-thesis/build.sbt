ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file(".")).settings(
  name := "similo-thesis"
)

// **COMPILER OPTIONS**
scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-feature",
  "-language:implicitConversions",
  "-language:existentials",
  "-unchecked",
  "-deprecation",
  "-Xmax-inlines",
  "256",
  "-Xfatal-warnings",
)

javaOptions ++= Seq(
  "-Xmx16G",
  "-Xms4G",
  "-source",
  "17",
  "-target",
  "17",
)

// **DEPENDENCIES**

// Testing
libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.16",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test"
)

// Cats
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.typelevel" %% "cats-effect" % "3.5.0"
)

// Circe (JSON)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.5",
  "io.circe" %% "circe-generic" % "0.14.5",
  "io.circe" %% "circe-parser" % "0.14.5"
)

// XML
libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
)

// Config
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.2"
)

// Logging
libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.7"
)

// HTTP
libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M4"
)

// Parallelism
libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-parallel-collections_3" % "1.0.4"
)

// Jenetics
libraryDependencies ++= Seq(
  "io.jenetics" % "jenetics" % "7.2.0"
)

// Selenium
libraryDependencies ++= Seq(
  "org.seleniumhq.selenium" % "selenium-java" % "4.8.1"
)

// Apache Commons Text
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-text" % "1.10.0"
)

// Cache
libraryDependencies ++= Seq(
  "com.github.ben-manes.caffeine" % "caffeine" % "3.1.5"
)

// **CUSTOM SCRIPTS**
