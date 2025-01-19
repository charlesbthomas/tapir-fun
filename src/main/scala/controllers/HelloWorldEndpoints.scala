package dev.parvus.controllers

import sttp.tapir.json.circe.*
import io.circe.generic.auto.*
import sttp.tapir.Schema
import sttp.tapir.*
import dev.parvus.controllers.util.HasEndpoints

case class HelloRequest(name: String) derives Schema

object HelloRequest:
  def input = jsonBody[HelloRequest]

class HelloWorldController extends HasEndpoints:
  private val e1 = endpoint.get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)
    .handleSuccess(name => s"Hello, $name!")

  private val e2 = endpoint.post
    .in("hello")
    .in(HelloRequest.input)
    .out(stringBody)
    .handleSuccess(req => s"Hello, ${req.name}!")

  override val endpoints = List(e1, e2)
