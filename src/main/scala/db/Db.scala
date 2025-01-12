package dev.parvus.db

// Use H2Profile to connect to an H2 database
import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.H2Profile.api.*

object Db:
  val instance = ???
