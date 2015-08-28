package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/26/15.
 */
class IdParserTest extends FunSuite with BeforeAndAfterEach {
  class A extends IdParser
  var t: A = _
  var cm0: ContextMessage = _
  val summaries = Summary.loadContexts("contextProcessor/resources/test/sample_contexts")
  val idString = "summary1:summary2|l|123"
  val cm0string = s"0->5/100/10.01/$idString"

  override def beforeEach() {
    t = new A()
  }

  test("test idToSummaries") {
    // summary1 and summary2 is selected
    val s = t.idToSummaries(cm0string, summaries)
    println(s)//(s.length == 2) // there are two summaries
  }

  test("test SummariesToId") {
    assert(t.summariesToId(summaries) == "summary1|b|46:summary2|b|50")
    //assert(t.summariesToId(summaries, "l") == "summary1|l|105:summary2|l|107")
  }

  test("test parse") {
    assert(t.parse(cm0string).toString.startsWith("(0,5,100,10.01,[Lscala.Tuple3;") == true)
  }
}
