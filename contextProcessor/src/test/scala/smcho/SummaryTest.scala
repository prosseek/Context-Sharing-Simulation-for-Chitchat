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
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case "summary2" =>
            assert(context.getSize("l") == 107)
            assert(context.getSize("b") == 50)
            assert(context.getSummaryType() == "b")
            assert(context.getKeys().size == 8)
            assert(context.getName() == "summary2")
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case _ => println(s"${string} <- unkonwn test name")
        }
    }
  }
}
