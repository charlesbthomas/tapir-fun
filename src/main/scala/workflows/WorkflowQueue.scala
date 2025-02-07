package dev.parvus.workflows

import dev.parvus.workflows.domain.*
import ox.flow.Flow

trait WorkflowQueue:
  def pub(visitors: Seq[WorkflowNodeVisitor]): Unit
  def pub(visitor: WorkflowNodeVisitor): Unit = pub(Seq(visitor))
  def cons: Flow[WorkflowQueueMessage[WorkflowNodeVisitor]]
