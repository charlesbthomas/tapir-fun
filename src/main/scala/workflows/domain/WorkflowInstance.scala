package dev.parvus.workflows.domain

trait WorkflowInstance:
  def id: Long
  def template: WorkflowTemplate
  def triggerDate: Option[java.time.Instant]
  def nodes: Seq[WorkflowNode]
