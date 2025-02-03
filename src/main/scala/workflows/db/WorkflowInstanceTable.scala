package dev.parvus.workflows.db

import dev.parvus.db.util.PostgresProfile.api._
import dev.parvus.db.util.PostgresProfile

import java.util.UUID
import dev.parvus.workflows.domain.WorkflowNode
import dev.parvus.workflows.domain.WorkflowNodeState
import dev.parvus.workflows.domain.NodeType
import dev.parvus.db.util.EntityUtils.EntityTable
import dev.parvus.db.util.EntityUtils.Entity
import java.time.Instant
import dev.parvus.db.util.ColumnMappers
import io.circe.*

final case class WorkflowNodeDocument[T <: NodeType](
    node: WorkflowNode[T],
    state: WorkflowNodeState
) derives Encoder,
      Decoder

final case class WorkflowInstanceRow[T <: NodeType](
    id: UUID,
    nodes: Map[Long, WorkflowNodeDocument[T]],
    createdAt: Instant,
    updatedAt: Instant
) extends Entity

final class WorkflowInstanceTable[T <: NodeType: Encoder: Decoder](tag: Tag)
    extends EntityTable[WorkflowInstanceRow[T]](tag, "workflow_instances") {
  def id = column[UUID]("id", O.PrimaryKey)
  def nodes =
    column[Map[Long, WorkflowNodeDocument[T]]]("nodes", O.SqlType("JSONB"))
  def createdAt = column[Instant]("created_at")
  def updatedAt = column[Instant]("updated_at")
  def * = (id, nodes, createdAt, updatedAt).mapTo[WorkflowInstanceRow[T]]

  given BaseColumnType[WorkflowNodeDocument[T]] =
    ColumnMappers.jsonb[WorkflowNodeDocument[T]]

  given BaseColumnType[Map[Long, WorkflowNodeDocument[T]]] =
    ColumnMappers.jsonb[Map[Long, WorkflowNodeDocument[T]]]
}

object WorkflowInstanceTable {
  def query[T <: NodeType: Encoder: Decoder] =
    TableQuery(new WorkflowInstanceTable[T](_))
}
