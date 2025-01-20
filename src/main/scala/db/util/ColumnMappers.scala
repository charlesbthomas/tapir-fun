package dev.parvus.db.util

import dev.parvus.db.util.PostgresProfile.api._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import scala.reflect.ClassTag

given BaseColumnType[Json] = PostgresProfile.MyAPI.circeJsonTypeMapper

object ColumnMappers:
  def jsonb[T: Encoder: Decoder: ClassTag]: BaseColumnType[T] =
    MappedColumnType.base[T, Json](
      up => up.asJson,
      column => column.as[T].toOption.get
    )
