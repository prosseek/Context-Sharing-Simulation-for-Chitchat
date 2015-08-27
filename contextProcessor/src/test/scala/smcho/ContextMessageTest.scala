package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/12/15.
 */
class ContextMessageTest extends FunSuite with BeforeAndAfterEach {

  var cm0: ContextMessage = _
  var cm0string: String = _
  val summaries = Summary.loadContexts("contextProcessor/resources/test/sample_contexts")
  val idString = "summary1:summary2|l|123"

  override def beforeEach() {
    cm0 = ContextMessage(host1 = 0, host2 = 5, size = 100, time = 10.01, id = idString)
    cm0string = ContextMessage.makeString(cm0)
  }

  test("Create ContextMessage") {
    val id = "Hello"
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, id=id)
    assert(d.getId() == id)
    assert(d.getSize() == 100)
    assert(d.getHost1() == 0)
    assert(d.getHost2() == 3)
    assert(d.getTime() == 10.0)
  }

  test("Simpler ContextMessage creating") {
    val id = "Hello"
    val d = ContextMessage(id)
    assert(d.getId() == id)
    assert(d.getSize() == 0)
    assert(d.getHost1() == 0)
    assert(d.getHost2() == 0)
    assert(d.getTime() == 0.0)
  }

  test("create from summaries") {
    val expected = "[0->3/100/10.0/summary1|b|46:summary2|b|50]"
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries=summaries)
    assert(d.toString() == expected)
  }

  // IdParser API test
  test("Test toString") {
    val id = idString
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, id=id)
    assert(d.toString() == s"[0->3/100/10.0/$id]")
  }
}
