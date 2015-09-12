package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/12/15.
 */
class ContextMessageTest extends FunSuite with BeforeAndAfterEach {

  val jsonDir = "contextProcessor/src/test/resources"
  var idstring: String = _
  val summaries = Summary.loadContexts(jsonDir)

  override def beforeEach(): Unit = {
    idstring = "unittest|b|29:g1c0|l|52"
  }

  test("Create ContextMessage") {
    val id = "Hello"
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, content=id)
    assert(d.content == id)
    assert(d.size == 100)
    assert(d.host1 == 0)
    assert(d.host2 == 3)
    assert(d.time == 10.0)
  }

  test("Simpler ContextMessage creating") {
    val id = "Hello"
    val d = ContextMessage(id)
    assert(d.content == id)
    assert(d.size == 0)
    assert(d.host1 == 0)
    assert(d.host2 == 0)
    assert(d.time == 0.0)
  }

  test("create from summaries (Summary)") {
    val expected = "[0->3/100/10.0/unittest|b|29:g1c0|b|29]"
    val expected2 = "[0->0/0/0.0/unittest|b|29:g1c0|b|29]"
    var d: ContextMessage = null

    // 1. map
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries = summaries)
    assert(d.toString() == expected)
    //println(d.toString())
    d = ContextMessage(summaries = summaries)
    //println(d.toString())
    assert(d.toString() == expected2)

    // 2. Iterable[Summary]
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, summaries = summaries.values)
    assert(d.toString() == expected)

    d = ContextMessage(summaries = summaries.values)
    assert(d.toString() == expected2)
  }

  test("create from summaries (String)") {
    val expected = "[0->3/100/10.0/unittest|b|29:g1c0|b|29]"
    val expected2 = "[0->0/0/0.0/unittest|b|29:g1c0|l|52]"
    var d: ContextMessage = null

    // 3. Iterable[String]
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, nameTypes=List("unittestb", "g1c0b"), summaries)
    assert(d.toString() == expected)
    //println(d.toString())

    d = ContextMessage(nameTypes=List("unittestb", "g1c0l"), summaries)
    assert(d.toString() == expected2)
    //println(d.toString())

    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, nameTypes=List("unittestl", "g1c0l"), summaries)
    val expected3 = "[0->3/100/10.0/unittest|l|52:g1c0|l|52]"
    //println(d.toString)
    assert(d.toString() == expected3)
  }

  test("create from summaries (String), but missing") {
    var d: ContextMessage = null
    val expected = "[0->3/100/10.0/unittest|b|29:g1c0|b|29]"
    // 3. Iterable[String]
    d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, nameTypes=List("unittestb", "unittest", "g1c0b"), summaries)
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
