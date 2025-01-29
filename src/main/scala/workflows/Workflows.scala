package dev.parvus.workflows

import java.sql.Date

sealed trait TriggerType
case object APITrigger extends TriggerType
case object ScheduleTrigger extends TriggerType

trait WorkflowTrigger:
  def triggerType: TriggerType

trait WorkflowTemplate:
  def id: Long
  def name: String
  def description: Option[String]
  def nodes: List[WorkflowNode]
  def startNode: Long
  def trigger: WorkflowTrigger

trait WorkflowNode:
  def id: Long
  def name: String
  def description: Option[String]
  def run(): Unit
  def parents: Set[Long]

sealed trait WorkflowNodeState
case object NotStarted extends WorkflowNodeState
case object Running extends WorkflowNodeState
case object Completed extends WorkflowNodeState
case object Failed extends WorkflowNodeState

case class NodeExecutionState(
    state: WorkflowNodeState,
    lastSeen: Date
)

trait WorkflowInstance:
  def id: Long
  def template: WorkflowTemplate
  def triggerDate: Option[java.time.Instant]
  def nodes: Map[Long, NodeExecutionState]

trait WorkflowQueue:
  def dequeue: Option[WorkflowInstance]

trait WorkflowExecutor:
  def queue: WorkflowQueue
