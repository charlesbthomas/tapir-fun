package dev.parvus

import sttp.tapir._
import sttp.tapir.server.netty.sync.NettySyncServer
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import sttp.tapir.generic.auto.*
import io.circe.generic.auto.*
import sttp.tapir.json.circe.*
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import pureconfig.ConfigSource
import pureconfig._
import com.softwaremill.macwire._
import dev.parvus.db.Databases

import dev.parvus.controllers.*
import sttp.tapir.server.ServerEndpoint
import sttp.shared.Identity
import dev.parvus.db.Databases.PostgresDatabase
import dev.parvus.db.repositories.Repositories
import dev.parvus.services.UserServiceImpl

given ExecutionContext = scala.concurrent.ExecutionContext.global

trait DatabaseDeps:
  def db: PostgresDatabase

object Main extends App with DatabaseDeps with Repositories:
  val conf = ConfigSource.default
    .load[AppConfig]
    .getOrElse(throw new Exception("Config error"))

  // Database Layers
  override val db: PostgresDatabase = Databases.default

  // Service Layers
  lazy val userService = wire[UserServiceImpl]

  // Endpoint Layers
  lazy val helloWorldEndpoints = wire[HelloWorldController]
  lazy val authEndpoints = wire[AuthEndpoints]

  val appEndpoints: List[ServerEndpoint[Any, Identity]] =
    helloWorldEndpoints.endpoints ++ authEndpoints.endpoints

  val swagger = SwaggerInterpreter()
    .fromServerEndpoints(appEndpoints, "Parvus", "1.0")

  NettySyncServer()
    .addEndpoints(appEndpoints ++ swagger)
    .port(conf.port)
    .startAndWait()
