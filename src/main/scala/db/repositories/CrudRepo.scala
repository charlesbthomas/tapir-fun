package dev.parvus.db.repositories

import java.util.UUID

trait CrudRepo[T] {
  def create(t: T): T
  def read(id: UUID): Option[T]
  def update(t: T): T
  def delete(id: Int): Unit
}
