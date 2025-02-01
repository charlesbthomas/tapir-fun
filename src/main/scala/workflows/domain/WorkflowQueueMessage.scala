package dev.parvus.workflows.domain

trait WorkflowQueueMessage[T]:
  def message: T
  def ack: Unit
  def nack: Unit
