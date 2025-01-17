package dev.parvus.util

import scala.concurrent.Future
import ox.Ox
import ox.supervised
import ox.channels.*
import ox.fork
import ox.get
import scala.util.Success
import scala.util.Failure
import ox.get
import scala.concurrent.ExecutionContext

extension [T](inline f: Future[T]) inline def await: T = awaitWithChannel(f)

extension [T](inline f: Future[Option[T]])
  inline def awaitOpt: Option[T] = awaitWithChannel(f)

private def awaitWithChannel[T](f: => Future[T]): T = supervised {
  given ExecutionContext = scala.concurrent.ExecutionContext.global
  val channel = Channel.rendezvous[T]
  f.onComplete {
    case Success(value) =>
      channel.send(value)
      channel.done()
    case Failure(exception) =>
      channel.error(exception)
      channel.done()
  }
  channel.receive()
}
