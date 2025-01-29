package dev.parvus.services

import dev.parvus.db.models.User
import dev.parvus.db.models.Users
import dev.parvus.db.models.UsersTableQuery

import dev.parvus.db.repositories.*
import dev.parvus.db.Databases.PostgresDatabase
import sttp.tapir.Schema
import sttp.tapir.json.circe.*
import io.circe.generic.auto.*
import dev.parvus.db.util.PostgresProfile.api.*
import scala.concurrent.ExecutionContext
import dev.parvus.db.util.*
import dev.parvus.util.*
import dev.parvus.db.models.Project
import dev.parvus.db.models.Projects
import dev.parvus.util.Argon2Hasher

given ExecutionContext = ExecutionContext.parasitic

case class RegisterUserOptions(
    existingProjectId: Option[String],
    projectName: Option[String]
) derives Schema

case class RegisterUserInput(
    firstName: String,
    middleName: Option[String],
    lastName: String,
    email: String,
    password: Option[String],
    options: Option[RegisterUserOptions]
) derives Schema

case class RegisterUserOutput(
    user: User,
    project: Project
) derives Schema

case class LoginInput(email: String, password: String)
case class UserSession(user: User, token: String)

trait UserService:
  def register(input: RegisterUserInput): RegisterUserOutput
  def login(input: LoginInput): UserSession

class UserServiceImpl(
    private val repo: UserRepo,
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
      preferences = None,
      password = input.password.map(Argon2Hasher.hashPassword)
    )

    val orgName = user.firstName
      .map(n => s"${n}'s Organization")
      .getOrElse("Default Organization")

    val project = Project(
      id = java.util.UUID.randomUUID(),
      name = "Default Project",
      description = None,
      settings = None,
      createdAt = java.time.Instant.now(),
      updatedAt = java.time.Instant.now()
    )

    val (createdUserId, createdProjectId) = transaction.run {
      for
        createdUserId <- Users.create(user)
        createdProjectId <- Projects.create(project)
      yield ((createdUserId, createdProjectId))
    }

    RegisterUserOutput(
      user.copy(id = createdUserId),
      project.copy(id = createdProjectId)
    )
  end register

  def login(input: LoginInput): UserSession =
    val user = transaction.run {
      Users.table
        .filter(_.email === input.email)
        .result
        .headOption
    }

    user match
      case Some(u)
          if u.password.isDefined &&
            Argon2Hasher.checkPassword(
              input.password,
              u.password.get
            ) =>
        UserSession(u, java.util.UUID.randomUUID().toString)
      case _ => throw new Exception("Invalid credentials")
  end login
