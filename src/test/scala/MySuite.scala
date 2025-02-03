package dev.parvus

import dev.parvus.workflows.TestNode
import io.circe.Json
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import dev.parvus.workflows.domain.WorkflowNode
import dev.parvus.workflows.TestWorkflowNode

class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }

  test("workflow node json serde?") {
    val node = TestNode("hello")

    val workflowNode: WorkflowNode[TestWorkflowNode] = WorkflowNode(1, 0, node)

    val json = workflowNode.asJson

    println(json)

    val parsed = json.as[WorkflowNode[TestWorkflowNode]].toOption.get
  }
}
