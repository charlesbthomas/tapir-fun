package dev.parvus.controllers

import sttp.tapir.Endpoint
import sttp.tapir.server.ServerEndpoint
import sttp.shared.Identity

trait HasEndpoints:
  def endpoints: List[ServerEndpoint[Any, Identity]]
