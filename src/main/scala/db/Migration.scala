package dev.parvus.db

import javax.sql.DataSource
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.Flyway
import pureconfig.ConfigReader
import org.flywaydb.core.api.Location
import scala.jdk.CollectionConverters.*
import com.typesafe.scalalogging.LazyLogging
import pureconfig.ConfigSource

class Migration(dataSource: DataSource) extends LazyLogging {
  def migrate: Int = {
    logger.info("Running migrations from locations.")
    val count = unsafeMigrate
    logger.info(s"Executed $count migrations")
    count
  }

  private def unsafeMigrate: Int = {
    val m: FluentConfiguration = Flyway.configure
      .dataSource(dataSource)
      .group(true)
      .outOfOrder(false)
      .table("schema_version")
      .locations(
        List("classpath:db/migration")
          .map(new Location(_))
          .toList*
      )
      .baselineOnMigrate(true)

    logValidationErrorsIfAny(m)
    m.load().migrate().migrationsExecuted
  }

  private def logValidationErrorsIfAny(m: FluentConfiguration): Unit = {
    val validated = m
      .ignoreMigrationPatterns("*:pending")
      .load()
      .validateWithResult()

    if (!validated.validationSuccessful)
      for (error <- validated.invalidMigrations.asScala)
        logger.warn(s"""
          |Failed validation:
          |  - version: ${error.version}
          |  - path: ${error.filepath}
          |  - description: ${error.description}
          |  - errorCode: ${error.errorDetails.errorCode}
          |  - errorMessage: ${error.errorDetails.errorMessage}
        """.stripMargin.strip)
  }
}
