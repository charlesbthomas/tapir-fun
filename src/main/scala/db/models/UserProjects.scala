package dev.parvus.db.models

import dev.parvus.db.util.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*

final case class UserProject(
    userId: UUID,
    projectId: UUID,
    role: String,
    createdAt: Instant,
    updatedAt: Instant
)

class UserProjects(tag: Tag) extends Table[UserProject](tag, "user_projects") {
  def userId = column[UUID]("user_id")
  def projectId = column[UUID]("project_id")
  def role = column[String]("role")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (
    userId,
    projectId,
    role,
    createdAt,
    updatedAt
  )
    .mapTo[UserProject]
  def pk = primaryKey("user_projects_pk", (userId, projectId))
}

object UserProjects:
  def table = TableQuery[UserProjects]
