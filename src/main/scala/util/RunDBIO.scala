package dev.parvus.util

import slick.jdbc.PostgresProfile.api.*
import dev.parvus.db.Databases.PostgresDatabase

extension [T](inline dbio: DBIO[T])
  inline def run(using db: PostgresDatabase): T = db.run(dbio).await
