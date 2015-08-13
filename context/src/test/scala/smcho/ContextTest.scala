package smcho

import org.scalatest.FunSuite
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class ContextTest extends FunSuite {

  def fixture = {
    new {
      val context = new Context(0);
    }
  }
  test("Create context") {
    val d = new Context(1)
    assert(d.getId() == 1)
  }
  test("Create context 2") {
    val d = new Context(2)
    assert(d.getId() == 2)
  }

  // Get/set size
  test("Set and get test") {
    val f = fixture
    f.context.setSize(10)
    assert(f.context.getSize() == 10)
  }
}
