package dev.parvus.workflows.domain

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.generic.semiauto.*

trait Node:
  self: NodeType & NodeRunner[NodeType] =>

trait NodeType

trait NodeRunner[N <: NodeType]:
  extension (node: N) def run: Unit

case class WorkflowNode[T <: NodeType](
    id: Long,
    parent: Long,
    node: T
) derives Encoder,
      Decoder

trait WorkflowNodeProcessor[T <: NodeType]:
  def process(node: WorkflowNode[T]): Unit
