package dev.parvus.workflows

import scala.util.Try
import ox.flow.Flow
import dev.parvus.workflows.domain.*

trait WorkflowQueue:
  def pub(visitors: Seq[WorkflowNodeVisitor]): Unit
  def pub(visitor: WorkflowNodeVisitor): Unit = pub(Seq(visitor))
  def cons: Flow[WorkflowQueueMessage[WorkflowNodeVisitor]]

trait WorkflowStorage:
  def fetchInstance(instanceId: Long): Option[WorkflowInstance]
  def changeNodeStatus(
      node: WorkflowNodeVisitor,
      state: WorkflowNodeState
  ): Unit = changeNodeStatus(Seq(node), state)
  def changeNodeStatus(
      nodes: Seq[WorkflowNodeVisitor],
      state: WorkflowNodeState
  ): Unit

trait WorkflowExecutor:
  def queue: WorkflowQueue
  def storage: WorkflowStorage
  def run(): Unit

case class BasicWorkflowExecutor(
    val queue: WorkflowQueue,
    val storage: WorkflowStorage
) extends WorkflowExecutor:
  override def run(): Unit = queue.cons
    .tap(m => storage.changeNodeStatus(m.message, WorkflowNodeState.Running))
    .mapPar(10)(message =>
      Try {
        val visitor = message.message

        val instance =
          storage
            .fetchInstance(visitor.workflowInstanceId)
            .get // TODO: don't use get here... I'm lazy

        val node = instance.nodes.find(_.id == visitor.nodeId).get

        node.run() // TODO: typeclass this

        storage.changeNodeStatus(visitor, WorkflowNodeState.Completed)

        message.ack

        val children = instance.nodes.filter(_.parent == node.id)
        val newVisitors =
          children.map(c =>
            WorkflowNodeVisitor(visitor.workflowInstanceId, c.id)
          )

        storage.changeNodeStatus(newVisitors, WorkflowNodeState.Pending)
        queue.pub(newVisitors)

      }.recover { case e =>
        message.nack
      }
    )
    .runDrain()
