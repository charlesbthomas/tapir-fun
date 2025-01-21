package dev.parvus.db.models

import dev.parvus.db.util.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*

final case class ProjectMembership(
    id: UUID,
    userId: UUID,
    projectId: UUID,
    role: String,
    createdAt: Instant,
    updatedAt: Instant
) extends Entity

class ProjectMemberships(tag: Tag)
    extends EntityTable[ProjectMembership](tag, "project_memberships") {
  def id = column[UUID]("id", O.PrimaryKey)
  def userId = column[UUID]("user_id")
  def projectId = column[UUID]("project_id")
  def role = column[String]("role")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")

  def * = (
    id,
    userId,
    projectId,
    role,
    createdAt,
    updatedAt
  ).mapTo[ProjectMembership]

  def uniqueUserIdProjectId = index(
    "project_memberships_unique_user_id_project_id",
    (userId, projectId),
    unique = true
  )
}

object ProjectMemberships
    extends EntityCrudQueries[ProjectMembership, ProjectMemberships]:
  def table = TableQuery[ProjectMemberships]
