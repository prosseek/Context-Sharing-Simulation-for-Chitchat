package smcho

import core.LabeledSummary
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseWithStrategyTest extends FunSuite  with BeforeAndAfterEach {

  var database: DatabaseWithStrategy = _

  override def beforeEach() {
    database = DatabaseWithStrategy()
    database.load("contextProcessor/resources/test/sample_contexts")
  }

//  test("getSummary test") {
//    assert(database.getSummary("summary1").getClass.getName == "smcho.Summary")
//    assert(database.getSummary("summary0") == null)
//  }
//
//  test("getContextMessageFromNames test") {
//    assert(database.getFromNames(List("summary1")).toString == "[0->0/0/0.0/summary1|b|46]")
//    //asse(database.getContextMessageFromName(List("summary1")).toString == "(0 => 0)[105]:summary1")
//  }
//
//  test("getHostAddressFromSummaryName test") {
//    assert(Database.getHostAddressFromSummaryName("id123") == 123)
//    intercept[java.lang.Exception] {
//      Database.getHostAddressFromSummaryName("id")
//    }
//    assert(Database.getHostAddressFromSummaryName("id123id") == 123)
//  }
//
//  test("addToHostHistory test ") {
//    Database.reset
//    Database.addToHistory(1, "A")
//    Database.addToHistory(1, "A")
//    Database.addToHistory(1, "B")
//    assert(Database.getHistory() == Map(1 -> Set("B", "A")))
//
//    Database.addToHistory(2, "A")
//    Database.addToHistory(2, "B")
//    assert(Database.getHistory(2) == Set[String]("B", "A"))
//  }
//
//  //
//
//  test("getClass test") {
//    val db = new Database("smcho.SimpleShareLogic")
//    val cl = db.getClass("smcho.SimpleShareLogic")
//    assert(cl.toString == "class smcho.SimpleShareLogic")
//    intercept[java.lang.Exception] {
//      val cl2 = db.getClass("smcho.SimpleShareLogic2")
//    }
//  }
//  test("loadObject test")
//  {
//    val db = new Database("smcho.SimpleShareLogic")
//    val cl = db.loadObject("smcho.SimpleShareLogic").asInstanceOf[smcho.ShareLogic]
//    cl.generateShareContext(10)
//    println(cl)
//  }
}
