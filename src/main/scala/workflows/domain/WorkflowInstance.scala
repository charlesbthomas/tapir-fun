package dev.parvus.workflows.domain

trait WorkflowInstance[T <: NodeType]:
  def id: Long
  // def template: WorkflowTemplate
  def triggerDate: Option[java.time.Instant]
  def nodes: Seq[WorkflowNode[T]]
