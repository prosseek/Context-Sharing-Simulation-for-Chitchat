package smcho

import org.scalatest.FunSuite
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseTest extends FunSuite {
  test("Create test") {
    val d = new Database(1)
    assert(d.getOwner() == 1)
  }
  test("Create test2") {
    val d = new Database(2)
    assert(d.getOwner() == 2)
  }
}
