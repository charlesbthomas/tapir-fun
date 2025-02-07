package dev.parvus.workflows

import dev.parvus.workflows.domain.*

trait WorkflowStorage[T <: NodeType]:
  def fetchInstance(instanceId: Long): Option[WorkflowInstance[T]]
  def changeNodeStatus(
      node: WorkflowNodeVisitor,
      state: WorkflowNodeState
  ): Unit = changeNodeStatus(Seq(node), state)
  def changeNodeStatus(
      nodes: Seq[WorkflowNodeVisitor],
      state: WorkflowNodeState
  ): Unit
