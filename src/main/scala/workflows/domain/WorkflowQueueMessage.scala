package dev.parvus.workflows.domain

trait WorkflowQueueMessage[T]:
  def message: T
  def ack: Unit
  def nack: Unit

object WorkflowQueueMessage:
  def apply[T](
      message: T,
      ack: => Unit,
      nack: => Unit
  ): WorkflowQueueMessage[T] =
    new WorkflowQueueMessage[T]:
      def message: T = message
      def ack: Unit = ack
      def nack: Unit = nack
