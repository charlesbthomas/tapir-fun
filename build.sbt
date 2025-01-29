val scala3Version = "3.6.2"

lazy val runMigrate = taskKey[Unit]("Migrates the database schema.")
addCommandAlias("run-db-migrations", "runMigrate")

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

    // Logging
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.3.5",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
    ),

    // Postgres Stuff
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.5.1",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1",
      "com.github.tminglei" %% "slick-pg" % "0.22.2",
      "com.github.tminglei" %% "slick-pg_circe-json" % "0.22.2",
      "org.postgresql" % "postgresql" % "42.3.1",
      "org.flywaydb" % "flyway-core" % "10.22.0",
      "org.flywaydb" % "flyway-database-postgresql" % "10.22.0"
    ),

    // Trying out quill too
    libraryDependencies += "io.getquill" %% "quill-jdbc" % "4.8.5",

    // Conf
    libraryDependencies += "com.github.pureconfig" %% "pureconfig-core" % "0.17.8",
    libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.6.4" % "provided",

    // Crypto
    libraryDependencies += "de.mkammerer" % "argon2-jvm" % "2.11",

    // Database migrations
    fullRunTask(runMigrate, Compile, "dev.parvus.RunPostgresMigrations"),
    fork in runMigrate := true
  )
