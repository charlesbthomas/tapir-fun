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
import ox.supervised

given ExecutionContext = scala.concurrent.ExecutionContext.global

object Main:
  val conf = ConfigSource.default
    .load[AppConfig]
    .getOrElse(throw new Exception("Config error"))

  val db = Databases.default

  lazy val helloWorldEndpoints = wire[HelloWorldController]

  @main def start: Unit =
    val appEndpoints: List[ServerEndpoint[Any, Identity]] =
      helloWorldEndpoints.endpoints

    val swagger = SwaggerInterpreter()
      .fromServerEndpoints(appEndpoints, "Parvus", "1.0")

    NettySyncServer()
      .addEndpoints(appEndpoints ++ swagger)
      .port(conf.port)
      .startAndWait()
