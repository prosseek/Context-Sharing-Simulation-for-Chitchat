package smcho

import core.LabeledSummary
import org.scalatest.FunSuite
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseTest extends FunSuite {
  test("getSummary test") {
    Database.loadContexts("contextProcessor/resources/test/sample_contexts")
    assert(Database.getSummary("summary1").get.getClass.getName == "smcho.Summary")
    assert(Database.getSummary("summary0") == None)
  }

  test("createContextToShare test") {
    // no host1/2 addresses are set
    assert(Database.getContextMessageToShare("summary1", "bf").toString == "(-1 => -1):summary1")
  }
}
