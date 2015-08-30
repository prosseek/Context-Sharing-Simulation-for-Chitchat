package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by smcho on 8/26/15.
 */
class IdParserTest extends FunSuite with BeforeAndAfterEach {
  class A extends IdParser
  var t: A = _
  var cm0: ContextMessage = _
  val summaries = Summary.loadContexts("contextProcessor/resources/test/sample_contexts")
  val contentString = "summary1|b|46:summary2|l|107"
  val cm0string = s"0->5/100/10.01/$contentString"

  override def beforeEach() {
    t = new A()
  }

  test("test idToSummaries") {
    // summary1 and summary2 is selected
    val s = t.idToSummaries(cm0string, summaries)
    //println(s.toString)
    assert(s.toString == "ArrayBuffer(summary1|b|46, summary2|l|107)")//(s.length == 2) // there are two summaries
  }

  test("test SummariesToId") {
    assert(t.summariesToContent(summaries) == "summary1|b|46:summary2|b|50")
    //assert(t.summariesToId(summaries, "l") == "summary1|l|105:summary2|l|107")
  }

  test("test parse") {
    assert(t.parse(cm0string).toString.startsWith("(0,5,100,10.01,[Lscala.Tuple3;") == true)
  }

  test("test totalSize") {
    assert(t.getTotalTime(summaries.values) == 96)
    //val contentString = "summary1|b|46:summary2|l|123"
    val s = t.namesToSummaries(List("summary1b", "summary2l"), summaries)
    // s foreach {v => println(v.getSize())}
    assert(t.getTotalTime(s) == 46 + 107)
  }
}
