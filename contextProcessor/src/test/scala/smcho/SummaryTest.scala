package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/21/15.
 */
class SummaryTest extends FunSuite with BeforeAndAfterEach {

  override def beforeEach() {
  }

  test("testContextSummary") {
    val summaries = Summary.loadContexts("contextProcessor/resources/test/sample_contexts")
    summaries foreach {
      case (string, context)  =>
        string match {
          case "summary1" =>
            assert(context.getSize("l") == 105)
            assert(context.getSize("b") == 46)
            assert(context.getSummaryType() == "b")
            assert(context.getKeys().size == 9)
            assert(context.getName() == "summary1")
            assert(context.makeString() == "summary1|b|46")
            assert(context.makeString("l") == "summary1|l|105")
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case "summary2" =>
            assert(context.getSize("l") == 107)
            assert(context.getSize("b") == 50)
            assert(context.getSummaryType() == "b")
            assert(context.getKeys().size == 8)
            assert(context.getName() == "summary2")
            assert(context.makeString() == "summary2|b|50")
            assert(context.makeString("l") == "summary2|l|107")
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case _ => println(s"${string} <- unkonwn test name")
        }
    }
  }

  test("testLoad loadContext") {

    intercept[Exception] {
      Summary.loadContext("contextProcessor/resources/test/sample_contexts", "hello7.txt", "default.txt")
    }
    val res2 = Summary.loadContext("contextProcessor/resources/test/sample_contexts", "default.txt", "default.txt")
    // Even the name is different, the contextSummary content should be the same.
    //assert(res1.contextSummary.hashCode == res2.contextSummary.hashCode)
    assert(res2.contextSummary.getSize() == (69,81,81))
  }

  test("test summary toJsonString") {

  }
}
