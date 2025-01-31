package dev.parvus.workflows

import java.sql.Date
import ox.flow.Flow
import scala.util.Try
import scala.util.Success
import scala.util.Failure

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
case object ToDo extends WorkflowNodeState
case object Pending extends WorkflowNodeState
case object Running extends WorkflowNodeState
case object Completed extends WorkflowNodeState

trait WorkflowInstance:
  def id: Long
  def template: WorkflowTemplate
  def triggerDate: Option[java.time.Instant]
  def nodes: Seq[WorkflowNode]

trait WorkflowNodeVisitor:
  def workflowInstanceId: Long
  def nodeId: Long

trait WorkflowQueueMessage[T]:
  def message: T
  def ack: Unit
  def nack: Unit

trait WorkflowQueue:
  def enqueue(workflow: WorkflowInstance): Unit
  def stream: Flow[WorkflowQueueMessage[WorkflowNodeVisitor]]

trait WorkflowStorage:
  def fetchInstance(instanceId: Long): Option[WorkflowInstance]
  def markNodeAsCompleted(visitor: WorkflowNodeVisitor): Unit

class WorkflowExecutor(queue: WorkflowQueue, storage: WorkflowStorage):
  queue.stream
    .mapPar(10)(message =>
      Try {
        val visitor = message.message
        val instance = storage.fetchInstance(visitor.workflowInstanceId).get
        val node = instance.nodes.find(_.id == visitor.nodeId).get
        node.run()
        storage.markNodeAsCompleted(visitor)
        message.ack
      }.recover { case e =>
        message.nack
      }
    )
    .runDrain()
