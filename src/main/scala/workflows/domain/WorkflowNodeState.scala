package dev.parvus.workflows.domain

import io.circe.*

enum WorkflowNodeState derives Encoder, Decoder:
  case NotReady, Pending, Running, Completed
