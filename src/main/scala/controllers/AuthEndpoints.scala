package dev.parvus.controllers

import sttp.tapir.json.circe.*
import io.circe.generic.auto.*
import sttp.tapir.Schema
import sttp.tapir.*
import dev.parvus.controllers.util.HasEndpoints
import dev.parvus.services.*

class AuthEndpoints(userService: UserService) extends HasEndpoints:
  private val register = endpoint.post
    .in("auth" / "register")
    .in(jsonBody[RegisterUserInput])
    .out(jsonBody[RegisterUserOutput])
    .handleSuccess(userService.register)

  override val endpoints = List(register)
