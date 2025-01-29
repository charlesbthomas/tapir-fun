package dev.parvus.db.models

import dev.parvus.db.util.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*
import dev.parvus.db.util.ColumnMappers
import sttp.tapir.Schema

final case class ProjectSettings(
    features: Map[String, Boolean]
) derives Schema

final case class Project(
    id: UUID,
    // organizationId: UUID,
    name: String,
    description: Option[String],
    settings: Option[ProjectSettings],
    createdAt: Instant,
    updatedAt: Instant
) extends Entity
    derives Schema

class Projects(tag: Tag) extends EntityTable[Project](tag, "projects") {
  def id = column[UUID]("id", O.PrimaryKey)
  // def organizationId = column[UUID]("organization_id")
  def name = column[String]("name")
  def description = column[Option[String]]("description")
  def settings = column[Option[ProjectSettings]]("settings")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (
    id,
    // organizationId,
    name,
    description,
    settings,
    createdAt,
    updatedAt
  ).mapTo[Project]

  given BaseColumnType[ProjectSettings] = ColumnMappers.jsonb[ProjectSettings]
}

object Projects extends EntityCrudQueries[Project, Projects]:
  def table = TableQuery[Projects]
