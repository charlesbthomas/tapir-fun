package dev.parvus.db.models

import slick.jdbc.PostgresProfile.api._
import java.util.UUID
import java.time.Instant
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import dev.parvus.db.util.EntityUtils.*
import sttp.tapir.Schema

final case class UserPreferences(
    darkMode: Option[Boolean]
) derives Schema

final case class User(
    id: UUID,
    email: String,
    firstName: Option[String],
    middleName: Option[String],
    lastName: Option[String],
    createdAt: Instant,
    updatedAt: Instant,
    preferences: Option[UserPreferences]
) extends Entity
    derives Schema

class Users(tag: Tag) extends EntityTable[User](tag, "users") {
  def id = column[UUID]("id", O.PrimaryKey)
  def email = column[String]("email")
  def firstName = column[Option[String]]("first_name")
  def middleName = column[Option[String]]("middle_name")
  def lastName = column[Option[String]]("last_name")
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def preferences =
    column[Option[UserPreferences]]("preferences", O.SqlType("JSONB"))
  def * = (
    id,
    email,
    firstName,
    middleName,
    lastName,
    createdAt,
    updatedAt,
    preferences
  )
    .mapTo[User]

  given UserPreferencesColumnType: BaseColumnType[UserPreferences] =
    MappedColumnType.base[UserPreferences, String](
      up => up.asJson.noSpaces,
      column => decode[UserPreferences](column).right.get
    )
}

object Users extends EntityCrudQueries[User, Users]:
  def table = TableQuery[Users]
