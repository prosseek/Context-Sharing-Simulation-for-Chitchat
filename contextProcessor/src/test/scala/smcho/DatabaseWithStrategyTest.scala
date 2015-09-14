package smcho

import core.LabeledSummary
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseWithStrategyTest extends FunSuite  with BeforeAndAfterEach {

  var database: DatabaseWithStrategy = _
  val strategy = "smcho.SimpleShareLogic"

  override def beforeEach() {
    database = DatabaseWithStrategy(strategy, "contextProcessor/src/test/resources")
  }

  test("test construction") {
    assert(database.storage.repr.startsWith("""{"summaries":[{"name":"g1c0""""))
    assert(database.strategy == strategy)
    assert(database.storage.summariesMap == ContextMessage.summariesMap)
  }

  // add received ContextMessage to host
  test ("test add") {
    // add(host: Int, contextMessage: ContextMessage)
    database.add(0, ContextMessage("g1c0l:g3c1b"))
    assert(database.storage.get(0).toString == "Set([0->0/0.0/g1c0l:g3c1b/81])")
    assert(database.storage.get(3).toString == "Set()")
  }
  test ("test getSize") {
    //getSize(nameTypesString: String) : Int
    assert(database.getSize("g1c0l:g3c1b") == 81)
  }

}
