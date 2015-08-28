package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/28/15.
 */
class StorageTest extends FunSuite with BeforeAndAfterEach {

  var database: DatabaseWithStrategy = _
  var storage = Storage()
  val contentString = "summary1|b|46:summary2|l|123"
  val cm0string = s"0->5/100/10.01/$contentString"

  override def beforeEach() {
    database = DatabaseWithStrategy()
    DatabaseWithStrategy.load("contextProcessor/resources/test/sample_contexts", storage)
  }

  test("testAdd") {
    val contextMessage = ContextMessage(0,5,100, 10.01, contentString)
    storage.add(0, contextMessage)
    val hcm = storage.getHistoryContextMessage()
    val expected = "Map(2 -> Set([0->0/0/0.0/summary2|b|50]), 1 -> Set([0->0/0/0.0/summary1|b|46]), 0 -> Set([0->5/100/10.01/summary1|b|46:summary2|l|123]))"
    assert(hcm.toString == expected)

    val a = storage.getHistory()
    //println(a)
    val expected2 = "Map(2 -> Set((summary2,b,50)), 1 -> Set((summary1,b,46)), 0 -> Set((summary1,b,46), (summary2,l,123)))"

  }

  test("testGetSummary") {

  }

  test("testAddToHistory") {

  }



}
