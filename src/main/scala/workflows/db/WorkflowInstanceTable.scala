package dev.parvus.workflows.db

import dev.parvus.db.util.PostgresProfile.api._
import dev.parvus.db.util.PostgresProfile

import java.util.UUID
import dev.parvus.workflows.domain.WorkflowNode
import dev.parvus.workflows.domain.WorkflowNodeState
import dev.parvus.db.util.EntityUtils.EntityTable
import dev.parvus.db.util.EntityUtils.Entity
import java.time.Instant
import dev.parvus.db.util.ColumnMappers
import io.circe.*

final case class WorkflowNodeDocument(
    node: WorkflowNode,
    state: WorkflowNodeState
) derives Encoder,
      Decoder

final case class WorkflowInstanceRow(
    id: UUID,
    nodes: Map[Long, WorkflowNodeDocument],
    createdAt: Instant,
    updatedAt: Instant
) extends Entity

final class WorkflowInstanceTable(tag: Tag)
    extends EntityTable[WorkflowInstanceRow](tag, "workflow_instances") {
  def id = column[UUID]("id", O.PrimaryKey)
  def nodes =
    column[Map[Long, WorkflowNodeDocument]]("nodes", O.SqlType("JSONB"))
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (id, nodes, createdAt, updatedAt).mapTo[WorkflowInstanceRow]

  given BaseColumnType[WorkflowNodeDocument] =
    ColumnMappers.jsonb[WorkflowNodeDocument]
  given BaseColumnType[Map[Long, WorkflowNodeDocument]] =
    ColumnMappers.jsonb[Map[Long, WorkflowNodeDocument]]
}
