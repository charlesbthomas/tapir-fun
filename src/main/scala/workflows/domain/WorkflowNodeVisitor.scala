package dev.parvus.workflows.domain

trait WorkflowNodeVisitor:
  def workflowInstanceId: Long
  def nodeId: Long

case class WorkflowNodeVisitorImpl(workflowInstanceId: Long, nodeId: Long)
    extends WorkflowNodeVisitor

object WorkflowNodeVisitor:
  def apply(workflowInstanceId: Long, nodeId: Long): WorkflowNodeVisitor =
    WorkflowNodeVisitorImpl(workflowInstanceId, nodeId)
