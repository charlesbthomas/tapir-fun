package dev.parvus.workflows

import dev.parvus.workflows.domain.WorkflowNode
import io.circe.*

case class TestNode(
    id: Long,
    name: String,
    parent: Long
) extends WorkflowNode
    derives Encoder,
      Decoder:
  override def description: Option[String] = None
  override def nodeType: String = TestNode.NodeType

object TestNode:
  val NodeType = "TestNode"
  WorkflowNode.registerDecoder(NodeType, summon[Decoder[TestNode]])
