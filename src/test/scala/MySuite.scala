package dev.parvus

import dev.parvus.workflows.TestNode
import io.circe.Json
import io.circe.*
import io.circe.parser.*
import io.circe.syntax.*
import dev.parvus.workflows.domain.WorkflowNode

class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }

  test("workflow node json serde?") {
    val node = TestNode(1, "test", 0)
    println(node.toJson)
  }
}
