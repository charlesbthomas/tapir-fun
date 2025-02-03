import dev.parvus.workflows.db.WorkflowInstanceTable
import dev.parvus.workflows.*

class FOo {
  val table = WorkflowInstanceTable.query[TestWorkflowNode]

}
