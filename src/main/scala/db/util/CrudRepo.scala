package dev.parvus.db.util

import java.util.UUID
import dev.parvus.db.util.EntityUtils.{Entity, EntityCrudQueries}
import dev.parvus.db.Databases.PostgresDatabase
import dev.parvus.db.util.EntityUtils.EntityTable
import dev.parvus.util.*
import scala.concurrent.ExecutionContext

import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile.api._

trait CrudRepo[E <: Entity, T <: EntityTable[E]] {
  protected def db: PostgresDatabase
  given queries: EntityCrudQueries[E, T]

  def create(entity: E): UUID = db.run(queries.create(entity)).await
  def read(id: UUID): Option[E] = db.run(queries.read(id)).await
  def update(e: E): E =
    given ExecutionContext = ExecutionContext.parasitic
    val q = for
      _ <- queries.update(e)
      nextState <- queries.read(e.id)
    yield nextState.get
    db.run(q.transactionally).await
  def delete(id: UUID): Unit = db.run(queries.delete(id)).await
}
