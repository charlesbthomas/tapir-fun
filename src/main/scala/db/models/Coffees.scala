package dev.parvus.db.models

import slick.jdbc.PostgresProfile.api._
import java.util.UUID

final case class Coffee(id: UUID, name: String, price: Double)

private class Coffees(tag: Tag) extends Table[Coffee](tag, "COFFEES") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def price = column[Double]("price")
  def * = (id, name, price).mapTo[Coffee]
}

object Coffees:

  private val coffees = TableQuery[Coffees]

  def getById(id: UUID): DBIO[Option[Coffee]] =
    coffees.filter(_.id === id).result.headOption

  def create(coffee: Coffee): DBIO[Coffee] =
    coffees
      .returning(coffees.map(_.id))
      .into((c, id) => c.copy(id = id)) += coffee

  def update(coffee: Coffee): DBIO[Int] = {
    coffees.filter(_.id === coffee.id).update(coffee)
  }

  def delete(id: UUID): DBIO[Int] = coffees.filter(_.id === id).delete
