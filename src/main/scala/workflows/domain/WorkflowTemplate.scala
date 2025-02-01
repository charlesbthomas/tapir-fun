package dev.parvus.workflows.domain

trait WorkflowTemplate:
  def id: Long
  def name: String
  def description: Option[String]
  def nodes: List[WorkflowNode]
  def startNode: Long
  // def trigger: WorkflowTrigger
