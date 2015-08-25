package smcho

import core.LabeledSummary
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseTest extends FunSuite  with BeforeAndAfterEach {

  override def beforeEach() {

  }

  test("getSummary test") {
    Database.loadContexts("contextProcessor/resources/test/sample_contexts")
    assert(Database.getSummary("summary1").get.getClass.getName == "smcho.Summary")
    assert(Database.getSummary("summary0") == None)
  }

  test("getContextMessageFromName test") {
    // no host1/2 addresses are set
    assert(Database.getContextMessageFromName("summary1", "bf").toString == "(-1 => -1)[46]:summary1")
    assert(Database.getContextMessageFromName("summary1", "lb").toString == "(-1 => -1)[105]:summary1")
  }

  test("getHostAddressFromSummaryName test") {
    assert(Database.getHostAddressFromSummaryName("id123") == 123)
    intercept[java.lang.Exception] {
      Database.getHostAddressFromSummaryName("id")
    }
    assert(Database.getHostAddressFromSummaryName("id123id") == 123)
  }

  test("addToHostHistory test ") {
    Database.reset
    Database.addToHistory(1, "A")
    Database.addToHistory(1, "A")
    Database.addToHistory(1, "B")
    assert(Database.getHistory() == Map(1 -> Set("B", "A")))

    Database.addToHistory(2, "A")
    Database.addToHistory(2, "B")
    assert(Database.getHistory(2) == Set[String]("B", "A"))
  }

  //

  test("getClass test") {
    val db = new Database("smcho.SimpleShareLogic")
    val cl = db.getClass("smcho.SimpleShareLogic")
    assert(cl.toString == "class smcho.SimpleShareLogic")
    intercept[java.lang.Exception] {
      val cl2 = db.getClass("smcho.SimpleShareLogic2")
    }
  }
  test("loadObject test")
  {
    val db = new Database("smcho.SimpleShareLogic")
    val cl = db.loadObject("smcho.SimpleShareLogic").asInstanceOf[smcho.ShareLogic]
    cl.generateShareContext(10)
    println(cl)
  }
}
