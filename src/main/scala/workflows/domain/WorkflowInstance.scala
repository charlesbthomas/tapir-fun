package dev.parvus.workflows.domain

/** A running instance of a workflow. Holds the state of the workflow and the
  * nodes.
  *
  * @tparam T
  *   The type of the nodes in the workflow. This is unique per domain using the
  *   workflow system.
  */
trait WorkflowInstance[T <: NodeType]:
  def id: Long
  // def template: WorkflowTemplate
  def triggerDate: Option[java.time.Instant]
  def nodes: Seq[WorkflowNode[T]]
