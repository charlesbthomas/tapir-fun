package dev.parvus.db.util

import java.util.UUID
import dev.parvus.db.util.EntityUtils.{Entity, EntityCrudQueries}
import dev.parvus.db.Databases.PostgresDatabase
import dev.parvus.db.util.EntityUtils.EntityTable
import dev.parvus.util.*
import scala.concurrent.ExecutionContext

import slick.jdbc.PostgresProfile.api._

trait CrudRepo[E <: Entity, T <: EntityTable[E]] {
  protected given db: PostgresDatabase
  protected given queries: EntityCrudQueries[E, T]

  def create(entity: E): UUID = queries.create(entity).run
  def read(id: UUID): Option[E] = queries.read(id).run
  def update(e: E): E =
    given ExecutionContext = ExecutionContext.parasitic
    (
      for
        _ <- queries.update(e)
        nextState <- queries.read(e.id)
      yield nextState.get
    ).transactionally.run
  def delete(id: UUID): Unit = queries.delete(id).run
}
