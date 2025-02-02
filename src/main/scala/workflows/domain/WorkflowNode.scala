package dev.parvus.workflows.domain

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.generic.semiauto.*

trait WorkflowNode:
  def id: Long
  def name: String
  def description: Option[String]
  def parent: Long
  def nodeType: String
  def toJson: Json = this.asJson

object WorkflowNode:
  given Encoder[WorkflowNode] = Encoder.instance { node =>
    Json.obj(
      "nodeType" -> Json.fromString(node.nodeType),
      "data" -> node.asJson
    )
  }

  given Decoder[WorkflowNode] = Decoder.instance { cursor =>
    cursor.downField("nodeType").as[String].flatMap { nodeType =>
      summonDecoder(nodeType) match
        case Some(decoder) => cursor.downField("data").as(decoder)
        case None =>
          Left(DecodingFailure(s"Unknown nodeType: $nodeType", cursor.history))
    }
  }

  private var registeredDecoders: Map[String, Decoder[? <: WorkflowNode]] =
    Map()

  def registerDecoder[T <: WorkflowNode](
      nodeType: String,
      decoder: Decoder[T]
  ): Unit =
    registeredDecoders += (nodeType -> decoder)

  private def summonDecoder(
      nodeType: String
  ): Option[Decoder[? <: WorkflowNode]] =
    registeredDecoders.get(nodeType).asInstanceOf[Option[Decoder[WorkflowNode]]]
