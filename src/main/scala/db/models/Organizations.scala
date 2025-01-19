package dev.parvus.db.models

import slick.jdbc.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*

final case class Organization(
    id: UUID,
    name: String,
    createdAt: Instant,
    updatedAt: Instant
) extends Entity

class Organizations(tag: Tag)
    extends EntityTable[Organization](tag, "organizations") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (
    id,
    name,
    createdAt,
    updatedAt
  )
    .mapTo[Organization]
}

object Organizations extends EntityCrudQueries[Organization, Organizations]:
  def table = TableQuery[Organizations]
