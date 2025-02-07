package dev.parvus.workflows

import scala.util.Try
import ox.flow.Flow
import dev.parvus.workflows.domain.*

trait WorkflowExecutor:
  def queue: WorkflowQueue
  def storageProvider: WorkflowStorageProvider
  def run(): Unit

case class BasicWorkflowExecutor(
    val queue: WorkflowQueue,
    val storageProvider: WorkflowStorageProvider
) extends WorkflowExecutor:

  override def run(): Unit = queue.cons
    .tap(m => {
      storageProvider
        .getStorage(m.message.workflowInstanceId)
        .changeNodeStatus(m.message, WorkflowNodeState.Running)
    })
    .mapPar(10)(message =>
      Try {
        val visitor = message.message
        val storage = storageProvider.getStorage(visitor.workflowInstanceId)

        val instance =
          storage
            .fetchInstance(visitor.workflowInstanceId)
            .get // TODO: don't use get here... I'm lazy

        val node = instance.nodes.find(_.id == visitor.nodeId).get

        // node.run() // TODO: typeclass this

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
