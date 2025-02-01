package dev.parvus.workflows

import dev.parvus.workflows.domain.*
import ox.flow.Flow
import ox.scheduling.repeat
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.RedisClient
import com.typesafe.scalalogging.LazyLogging
import pureconfig.ConfigSource
import pureconfig.ConfigReader
import io.lettuce.core.XReadArgs
import ox.scheduling.RepeatConfig
import io.lettuce.core.Consumer
import scala.jdk.CollectionConverters.*
import io.circe._
import io.circe.parser._
import io.circe.syntax._

case class RedisWorkflowNodeVisitor(workflowInstanceId: Long, nodeId: Long)
    extends WorkflowNodeVisitor derives Encoder, Decoder

final class RedisWorkflowQueue(redis: RedisClient)
    extends WorkflowQueue
    with LazyLogging:

  lazy val commands = redis.connect.sync

  final override def pub(visitors: Seq[WorkflowNodeVisitor]): Unit = {
    visitors.foreach { visitor =>
      commands.xadd(
        RedisWorkflowQueue.defaultQueueName,
        visitor
      )
    }
  }

  final override def cons: Flow[WorkflowQueueMessage[WorkflowNodeVisitor]] = {
    val groupConf =
      XReadArgs.StreamOffset.from(RedisWorkflowQueue.defaultQueueName, "0-0")

    try commands.xgroupCreate(groupConf, "consumer-group")
    catch case e: Exception => logger.info("Consumer group already exists")

    Flow.usingEmit(emit => {
      repeat(RepeatConfig.immediateForever()) {
        val messages = commands
          .xreadgroup(
            Consumer.from("consumer-group", "consumer-1"),
            XReadArgs.StreamOffset.lastConsumed(
              RedisWorkflowQueue.defaultQueueName
            )
          )
          .asScala

        if !messages.isEmpty then
          messages.foreach { message =>
            val json = message.getBody().asScala.asJson
            val visitor = json.as[RedisWorkflowNodeVisitor].getOrElse {
              logger.error("Failed to parse visitor")
              throw new Exception("Failed to parse visitor")
            }
            def ack = commands.xack(
              RedisWorkflowQueue.defaultQueueName,
              "consumer-group",
              message.getId
            )
            def nack = throw new UnsupportedOperationException(
              "Nack not supported by Redis"
            )
            emit(WorkflowQueueMessage(visitor, ack, nack))
          }
      }
    })
  }
end RedisWorkflowQueue

object RedisWorkflowQueue:
  case class RedisWorkflowQueueConfig(queueName: Option[String])
      derives ConfigReader
  val config = ConfigSource.default.loadOrThrow[RedisWorkflowQueueConfig]
  lazy val defaultQueueName = config.queueName.getOrElse("workflow-queue")

end RedisWorkflowQueue
