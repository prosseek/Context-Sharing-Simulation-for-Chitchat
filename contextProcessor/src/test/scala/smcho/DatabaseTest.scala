package smcho

import core.LabeledSummary
import org.scalatest.FunSuite
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseTest extends FunSuite {
  test("Create test") {
    Database.loadContexts("contextProcessor/resources/test/sample_contexts")
    Database.getContexts() foreach {
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
          case _ => println(s"${string} <- ???")
        }
    }
  }
//  test("Create test2") {
//    val d = new Database(2)
//    assert(d.getOwner() == 2)
//  }
}
