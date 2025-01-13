package dev.parvus.db

// Use H2Profile to connect to an H2 database
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api.*

import javax.sql.DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import pureconfig.ConfigSource
import dev.parvus.DatabaseInstance

object Databases:
  private lazy val instance = ConfigSource.default
    .at("database")
    .load[DatabaseInstance]
    .getOrElse(throw new Exception("Config error"))

  private lazy val hikariDataSource = new HikariDataSource(
    instance.toHikariConfig
  )

  lazy val default = Database.forDataSource(
    hikariDataSource,
    Some(10)
  )
