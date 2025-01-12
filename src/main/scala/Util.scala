package dev.parvus

import scala.concurrent.Future
import ox.Ox
import ox.supervised
import ox.channels.*
import scala.concurrent.ExecutionContext
import scala.util.Success
import scala.util.Failure
import ox.get
import dev.parvus.Utils.awaitWithChannel

extension [T](inline f: Future[T]) inline def ! : T = awaitWithChannel(f)

object Utils:
  given ExecutionContext = scala.concurrent.ExecutionContext.global
  def awaitWithChannel[T](f: Future[T]): T = {
    supervised:
      val channel = Channel.rendezvous[T]
      f.onComplete {
        case Success(value) =>
          channel.send(value)
          channel.done()
        case Failure(exception) =>
          channel.error(exception)
      }
      channel.receive()
  }
