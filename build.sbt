val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "parvus",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,

    // Tapir
    libraryDependencies ++= Seq(
      "com.softwaremill.ox" %% "core" % "0.5.8",
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.11.12",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.11.12",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.11.12",
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % "1.11.12"
    ),

    // Slick DB
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.5.1",
      "org.slf4j" % "slf4j-nop" % "1.7.26",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
      "org.postgresql" % "postgresql" % "42.3.1"
    ),

    // Conf
    libraryDependencies += "com.github.pureconfig" %% "pureconfig-core" % "0.17.8",
    libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.6.4" % "provided"
  )
