package dev.parvus.db.repositories

import dev.parvus.db.Databases.PostgresDatabase
import dev.parvus.db.util.CrudRepo
import dev.parvus.db.util.EntityUtils.EntityCrudQueries
import dev.parvus.db.models.*
import com.softwaremill.macwire.*

class UserRepo(protected val db: PostgresDatabase)
    extends CrudRepo[User, Users] {
  given queries: EntityCrudQueries[User, Users] = Users
}

class OrganizationRepo(protected val db: PostgresDatabase)
    extends CrudRepo[Organization, Organizations] {
  given queries: EntityCrudQueries[Organization, Organizations] = Organizations
}

class SessionRepo(protected val db: PostgresDatabase)
    extends CrudRepo[Session, Sessions] {
  given queries: EntityCrudQueries[Session, Sessions] = Sessions
}

class ProjectRepo(protected val db: PostgresDatabase)
    extends CrudRepo[Project, Projects] {
  given queries: EntityCrudQueries[Project, Projects] = Projects
}

trait Repositories:
  def db: PostgresDatabase

  lazy val userRepo = wire[UserRepo]
  lazy val organizationRepo = wire[OrganizationRepo]
  lazy val sessionRepo = wire[SessionRepo]
  lazy val projectRepo = wire[ProjectRepo]
