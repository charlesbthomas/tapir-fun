package dev.parvus

import sttp.tapir._
import sttp.tapir.server.netty.sync.NettySyncServer
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import sttp.tapir.generic.auto.*
import io.circe.generic.auto.*
import sttp.tapir.json.circe.*

case class HelloRequest(name: String)
object HelloRequest:
  def input = jsonBody[HelloRequest]

@main def hello: Unit =
  val e1 = endpoint.get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)
    .handleSuccess(name => s"Hello, $name!")

  val e2 = endpoint.post
    .in("hello")
    .in(HelloRequest.input)
    .out(stringBody)
    .handleSuccess(req => s"Hello, ${req.name}!")

  val appEndpoints = List(e1, e2)

  val swaggerEndpoints =
    SwaggerInterpreter().fromServerEndpoints(appEndpoints, "My App", "1.0")

  val allEndpoints = swaggerEndpoints ++ appEndpoints

  allEndpoints.foreach(it => println(it.show))

  NettySyncServer()
    .addEndpoints(allEndpoints)
    .port(8080)
    .startAndWait()
