package dev.parvus.util

import de.mkammerer.argon2.{Argon2, Argon2Factory}

trait PasswordHasher:
  def hashPassword(password: String): String
  def checkPassword(password: String, hashedPassword: String): Boolean

object Argon2Hasher extends PasswordHasher {
  private val argon2: Argon2 = Argon2Factory.create()

  def hashPassword(password: String): String = {
    val iterations = 3
    val memory = 65536 // 64MB
    val parallelism = 1
    argon2.hash(iterations, memory, parallelism, password.toCharArray)
  }

  def checkPassword(password: String, hashed: String): Boolean = {
    argon2.verify(hashed, password.toCharArray)
  }
}
