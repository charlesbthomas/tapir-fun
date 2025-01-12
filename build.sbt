val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "parvus",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.11.12",
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % "1.11.12",
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.11.12",
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.11.12",
    libraryDependencies ++= Seq(
      "com.softwaremill.ox" %% "core" % "0.5.8"
    )
  )
