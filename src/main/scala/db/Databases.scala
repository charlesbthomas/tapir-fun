package dev.parvus.db

// Use H2Profile to connect to an H2 database
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api.*

import javax.sql.DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import pureconfig.ConfigSource
import dev.parvus.DatabaseInstance
import slick.jdbc.PostgresProfile

object Databases:
  type PostgresDatabase = PostgresProfile.backend.Database

  private lazy val instance = ConfigSource.default
    .at("database")
    .load[DatabaseInstance]
    .getOrElse(throw new Exception("Config error"))

  lazy val hikariDataSource: DataSource = new HikariDataSource(
    instance.toHikariConfig
  )

  lazy val default: PostgresDatabase = Database.forDataSource(
    hikariDataSource,
    Some(10)
  )
