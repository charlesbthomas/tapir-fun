package dev.parvus.workflows

import dev.parvus.workflows.domain.*

trait WorkflowStorageProvider:
  def getStorage(instanceId: Long): WorkflowStorage[? <: NodeType]
