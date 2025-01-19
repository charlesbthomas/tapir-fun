package dev.parvus.db.models

import dev.parvus.db.util.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*

final case class UserOrganization(
    userId: UUID,
    organizationId: UUID,
    role: String,
    createdAt: Instant,
    updatedAt: Instant
)

class UserOrganizations(tag: Tag)
    extends Table[UserOrganization](tag, "user_organizations") {
  def userId = column[UUID]("user_id")
  def organizationId = column[UUID]("organization_id")
  def role = column[String]("role")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (
    userId,
    organizationId,
    role,
    createdAt,
    updatedAt
  )
    .mapTo[UserOrganization]
  def pk = primaryKey("user_organizations_pk", (userId, organizationId))
}

object UserOrganizations:
  def table = TableQuery[UserOrganizations]
