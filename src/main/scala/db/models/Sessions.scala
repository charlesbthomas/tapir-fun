package dev.parvus.db.models

import dev.parvus.db.util.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*

final case class Session(
    id: UUID,
    userId: UUID,
    token: String,
    expiresAt: Instant,
    createdAt: Instant,
    updatedAt: Instant
) extends Entity

class Sessions(tag: Tag) extends EntityTable[Session](tag, "sessions") {
  def id = column[UUID]("id", O.PrimaryKey)
  def userId = column[UUID]("user_id")
  def token = column[String]("session_token")
  def expiresAt = column[Instant]("expires_at")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (
    id,
    userId,
    token,
    expiresAt,
    createdAt,
    updatedAt
  )
    .mapTo[Session]
}

object Sessions extends EntityCrudQueries[Session, Sessions]:
  def table = TableQuery[Sessions]
