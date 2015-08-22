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
          case "summary1.txt" =>
            assert(context.getSizeLabeled() == 105)
            assert(context.getSizeBloomier() == 46)
            assert(context.getKeys().size == 9)
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case "summary2.txt" =>
            assert(context.getSizeLabeled() == 107)
            assert(context.getSizeBloomier() == 50)
            assert(context.getKeys().size == 8)
          // experiment/contexts/SimulationSimple/contexts/summary1.txt
          case _ => println(s"${string} <- unkonwn test name")
        }
    }
  }
}
