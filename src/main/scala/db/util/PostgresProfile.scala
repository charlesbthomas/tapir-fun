package dev.parvus.db.util

import com.github.tminglei.slickpg._
import io.circe.Json

trait PostgresProfile
    extends ExPostgresProfile
    with PgArraySupport
    with PgDate2Support
    with PgRangeSupport
    with PgHStoreSupport
    with PgCirceJsonSupport
    with PgSearchSupport
    with PgNetSupport
    with PgLTreeSupport {

  def pgjson = "jsonb"

  override protected def computeCapabilities: Set[slick.basic.Capability] =
    super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api = MyAPI

  object MyAPI
      extends ExtPostgresAPI
      with ArrayImplicits
      with Date2DateTimeImplicitsDuration
      with JsonImplicits
      with NetImplicits
      with LTreeImplicits
      with RangeImplicits
      with HStoreImplicits
      with SearchImplicits
      with SearchAssistants {
    implicit val strListTypeMapper: DriverJdbcType[List[String]] =
      new SimpleArrayJdbcType[String]("text").to(_.toList)

    implicit val JsonArrayTypeMapper: DriverJdbcType[List[Json]] =
      new AdvancedArrayJdbcType[Json](
        pgjson,
        (s) =>
          utils.SimpleArrayUtils
            .fromString[Json](it => Json.fromString(it))(s)
            .orNull,
        (v) => utils.SimpleArrayUtils.mkString[Json](json => json.toString())(v)
      ).to(_.toList)
  }
}

object PostgresProfile extends PostgresProfile
