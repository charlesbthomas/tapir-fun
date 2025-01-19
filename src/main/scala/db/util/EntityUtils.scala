package dev.parvus.db.util

import java.util.UUID
import java.time.Instant

import slick.jdbc.PostgresProfile.api._

object EntityUtils:
  trait Entity:
    def id: UUID
    def createdAt: Instant
    def updatedAt: Instant

  abstract class EntityTable[E <: Entity](tag: Tag, tablename: String)
      extends Table[E](tag, tablename):
    def id: Rep[UUID]
    def createdAt: Rep[Instant]
    def updatedAt: Rep[Instant]

  trait EntityCrudQueries[E <: Entity, T <: EntityTable[E]]:
    // This can't be defaulted because of the way the TableQuery
    // macro works. The constructor of the table row class
    // can only take a tag.
    def table: TableQuery[T]
    def create(it: E): DBIO[UUID] = table.returning(table.map(_.id)) += it
    def read(id: UUID): DBIO[Option[E]] =
      table.filter(_.id === id).result.headOption
    def update(it: E): DBIO[Int] = table.filter(_.id === it.id).update(it)
    def delete(id: UUID): DBIO[Int] = table.filter(_.id === id).delete
