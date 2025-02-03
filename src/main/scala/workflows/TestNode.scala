package dev.parvus.workflows

import dev.parvus.workflows.domain.WorkflowNode
import dev.parvus.workflows.domain.NodeType
import io.circe.*
import dev.parvus.workflows.domain.WorkflowNodeProcessor

sealed trait TestWorkflowNode extends NodeType derives Encoder, Decoder

case class TestNode(
    data: String
) extends TestWorkflowNode

object TestNode:
  given WorkflowNodeProcessor[TestNode] with
    def process(node: WorkflowNode[TestNode]): Unit = println(
      node.node.data
    )

case class TestNode2(
    data2: String
) extends TestWorkflowNode
