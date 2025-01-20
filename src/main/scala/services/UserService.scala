package dev.parvus.services

import dev.parvus.db.models.Organization
import dev.parvus.db.models.User
import dev.parvus.db.models.Users
import dev.parvus.db.models.UsersTableQuery

import dev.parvus.db.repositories.*
import dev.parvus.db.Databases.PostgresDatabase
import sttp.tapir.Schema
import sttp.tapir.json.circe.*
import io.circe.generic.auto.*
import dev.parvus.db.util.PostgresProfile.api.*
import dev.parvus.db.models.Organizations
import scala.concurrent.ExecutionContext
import dev.parvus.db.util.*

given ExecutionContext = ExecutionContext.parasitic

case class RegisterUserInput(
    firstName: String,
    middleName: Option[String],
    lastName: String,
    email: String,
    password: Option[String]
) derives Schema
case class RegisterUserOutput(user: User, organization: Option[Organization])
    derives Schema

case class LoginInput(email: String, password: String)
case class UserSession(user: User, token: String)

trait UserService:
  def register(input: RegisterUserInput): RegisterUserOutput
  def login(input: LoginInput): UserSession

class UserServiceImpl(
    private val repo: UserRepo,
    private val organizationRepo: OrganizationRepo,
    private val db: PostgresDatabase
) extends UserService:
  given PostgresDatabase = db

  def register(input: RegisterUserInput): RegisterUserOutput =

    val user = User(
      id = java.util.UUID.randomUUID(),
      email = input.email,
      firstName = Some(input.firstName),
      middleName = input.middleName,
      lastName = Some(input.lastName),
      createdAt = java.time.Instant.now(),
      updatedAt = java.time.Instant.now(),
      preferences = None
    )
    val organization = Organization(
      id = java.util.UUID.randomUUID(),
      name = s"${user.firstName}'s Organization",
      createdAt = java.time.Instant.now(),
      updatedAt = java.time.Instant.now()
    )

    // val q = (
    //   for
    //     createdUserId <- Users.create(user)
    //     createdOrgId <- Organizations.create(organization)
    //   yield ((createdUserId, createdOrgId))
    // ).transactionally

    val createdUserId = repo.create(user)
    val createdOrgId = organizationRepo.create(
      Organization(
        id = java.util.UUID.randomUUID(),
        name = s"${user.firstName}'s Organization",
        createdAt = java.time.Instant.now(),
        updatedAt = java.time.Instant.now()
      )
    )

    RegisterUserOutput(
      user.copy(id = createdUserId),
      Some(organization.copy(id = createdOrgId))
    )
  end register

  def login(input: LoginInput): UserSession = ???
