package dev.parvus.db.repositories

import dev.parvus.db.Databases.PostgresDatabase
import dev.parvus.db.models.Coffee
import dev.parvus.db.models.Coffees
import dev.parvus.util.await
import java.util.UUID

import scala.concurrent.ExecutionContext.Implicits.global

import slick.jdbc.PostgresProfile.api._
class CoffeeRepository(db: PostgresDatabase) extends CrudRepo[Coffee] {
  override def create(t: Coffee): Coffee =
    db.run(Coffees.create(t)).await

  override def read(id: UUID): Option[Coffee] =
    db.run(Coffees.getById(id)).await

  override def update(t: Coffee): Coffee =
    val query = for {
      _ <- Coffees.update(t)
      updated <- Coffees.getById(t.id)
    } yield updated.get
    db.run(query.transactionally).await

  override def delete(id: Int): Unit = ???

}
