package dev.parvus.workflows.domain

sealed trait WorkflowNodeState
object WorkflowNodeState:
  case object NotReady extends WorkflowNodeState
  case object Pending extends WorkflowNodeState
  case object Running extends WorkflowNodeState
  case object Completed extends WorkflowNodeState
