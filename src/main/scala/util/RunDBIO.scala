package dev.parvus.util

import slick.jdbc.PostgresProfile.api.*
import dev.parvus.db.Databases.PostgresDatabase
import scala.util.boundary

extension [R](inline dbio: DBIOAction[R, ?, ?])
  inline def run(using db: PostgresDatabase): R = db.run(dbio).await

object transaction:
  inline def run[T](inline txn: => DBIOAction[T, ?, ?])(using
      PostgresDatabase
  ): T =
    txn.transactionally.run

  def apply[E <: Effect, R, S <: NoStream](
      txn: => DBIOAction[R, S, E]
  ): DBIOAction[R, S, E & Effect.Transactional] =
    txn.transactionally
end transaction
