package dev.parvus.workflows.domain

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.generic.semiauto.*

trait NodeType

case class WorkflowNode[T <: NodeType](
    id: Long,
    parent: Long,
    node: T
) derives Encoder,
      Decoder
