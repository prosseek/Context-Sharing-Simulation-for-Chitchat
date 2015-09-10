package smcho

import org.scalatest.FunSuite

/**
 * Created by smcho on 8/27/15.
 */

class T extends ContentParser

class ContentParserTest extends FunSuite {

  var cm0: ContextMessage = _
  val summaries = Summary.loadContexts("contextProcessor/resources/test/sample_contexts")
  val contentString = "summary1|b|46:summary2|l|107"
  val t = new T

  test("test ParseContent") {
    val idString = "summary1|b|33:summary2|l|123"
    val res = t.parse(idString)
    res foreach { v =>
      v._1 match {
        case "summary1" => assert(v == ("summary1","b",33))
        case "summary2" => assert(v == ("summary2","l",123))
        case _ => None
      }
    }
  }

  test("test ParseContent wrong format") {
    val t = new T
    // summary1 is in the wrong format (missing type and size), so it will be ignored
    val idString = "summary1:summary2|b|33"
    val res = t.parse(idString)
    assert(res.length == 1)
  }

  test("test idToSummaries") {
    // summary1 and summary2 is selected
    val s = t.contentToSummaries(contentString, summaries)
    // println(s.toString)
    assert(s.toString == "ArrayBuffer(summary1|b|46, summary2|l|107)")//(s.length == 2) // there are two summaries
  }

  test("test SummariesToId") {
    assert(t.summariesToContent(summaries) == "summary1|b|46:summary2|b|50")
    //assert(t.summariesToId(summaries, "l") == "summary1|l|105:summary2|l|107")
  }

  test("test one summary parse") {
    val oneSummaryConent = "summary0|b|30"
    val p = t.parse(oneSummaryConent)
    assert(p.length == 1)
    assert(p(0) == ("summary0", "b", 30))
  }

  test("test totalSize") {
    assert(t.getTotalTime(summaries.values) == 96)
    //val contentString = "summary1|b|46:summary2|l|123"
    val s = t.namesToSummaries(List("summary1b", "summary2l"), summaries)
    // s foreach {v => println(v.getSize())}
    assert(t.getTotalTime(s) == 46 + 107)
  }

}


