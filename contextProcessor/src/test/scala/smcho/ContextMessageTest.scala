package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/12/15.
 */
class ContextMessageTest extends FunSuite with BeforeAndAfterEach {

  var idstring: String = _
  val summaries = null // Summary.loadContexts("contextProcessor/resources/test/sample_contexts")

  override def beforeEach(): Unit = {
    idstring = "summary1|b|46:summary2|l|123"
  }

  test("Create ContextMessage") {
    val id = "Hello"
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, content=id)
    assert(d.getContent() == id)
    assert(d.getSize() == 100)
    assert(d.getHost1() == 0)
    assert(d.getHost2() == 3)
    assert(d.getTime() == 10.0)
  }

  test("Simpler ContextMessage creating") {
    val id = "Hello"
    val d = ContextMessage(id)
    assert(d.getContent() == id)
    assert(d.getSize() == 0)
    assert(d.getHost1() == 0)
    assert(d.getHost2() == 0)
    assert(d.getTime() == 0.0)
  }

  test("create from summaries (Summary)") {
    val expected = "[0->3/100/10.0/summary1|b|46:summary2|b|50]"
    val expected2 = "[0->0/0/0.0/summary1|b|46:summary2|b|50]"
    var d: ContextMessage = null

    // 1. map
//    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries = summaries)
//    assert(d.toString() == expected)
//
//    d = ContextMessage(summaries = summaries)
//    assert(d.toString() == expected2)
//
//    // 2. Iterable[Summary]
//    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries = summaries.values)
//    assert(d.toString() == expected)
//
//    d = ContextMessage(summaries = summaries.values)
//    assert(d.toString() == expected2)
  }

  test("create from summaries (String)") {
    val expected = "[0->3/100/10.0/summary1|b|46:summary2|b|50]"
    val expected2 = "[0->0/0/0.0/summary1|b|46:summary2|b|50]"
    var d: ContextMessage = null

    // 3. Iterable[String]
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries=List("summary1", "summary2"), summaries)
    assert(d.toString() == expected)

    d = ContextMessage(summaries=List("summary1", "summary2"), summaries)
    assert(d.toString() == expected2)

    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries=List("summary1l", "summary2l"), summaries)
    val expected3 = "[0->3/100/10.0/summary1|l|105:summary2|l|107]"
    //println(d.toString)
    assert(d.toString() == expected3)

    d = ContextMessage(summaries=List("summary1l", "summary2l"), summaries)
    val expected4 = "[0->0/0/0.0/summary1|l|105:summary2|l|107]"
    assert(d.toString() == expected4)

    // The original data should be the same
    d = ContextMessage(summaries=List("summary1", "summary2"), summaries)
    assert(d.toString() == expected2)
  }

  test("create from summaries (String), but missing") {
    val expected = "[0->3/100/10.0/summary2|b|50]"
    var d: ContextMessage = null

    // 3. Iterable[String]
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries=List("summary11", "summary2"), summaries)
    //println(d.toString())
    assert(d.toString() == expected)
  }

  // IdParser API test
  test("Test toString") {
    var id = "summary1|b|46:summary2|b|50"
    var d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, content=id)
    assert(d.toString() == s"[0->3/100/10.0/$id]")

    id = "summary1:summary2|b|50"
    d = ContextMessage(content=id)
    assert(d.toString() == s"[0->0/0/0.0/$id]")
  }
}
