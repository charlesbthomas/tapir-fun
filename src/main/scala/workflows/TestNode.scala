package dev.parvus.workflows

import dev.parvus.workflows.domain.WorkflowNode
import dev.parvus.workflows.domain.NodeType
import io.circe.*

sealed trait TestWorkflowNode extends NodeType derives Encoder, Decoder

case class TestNode(
    data: String
) extends TestWorkflowNode

case class TestNode2(
    data2: String
) extends TestWorkflowNode
