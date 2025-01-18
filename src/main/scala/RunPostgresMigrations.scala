package dev.parvus

import dev.parvus.db.Databases

import com.softwaremill.macwire._
import dev.parvus.db.Migration
import javax.sql.DataSource

object RunPostgresMigrations extends App:
  val dataSource: DataSource = Databases.hikariDataSource
  val migrator = wire[Migration]
  migrator.migrate
