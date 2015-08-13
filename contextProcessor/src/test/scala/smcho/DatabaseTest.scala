package smcho

import org.scalatest.FunSuite
import org.scalatest.Assertions._

/**
 * Created by smcho on 8/12/15.
 */
class DatabaseTest extends FunSuite {
  test("Create singleton test") {
    val d1 = Database.get()
    val d2 = Database.get()
    assert(d1 == d2)
  }
//  test("Create test2") {
//    val d = new Database(2)
//    assert(d.getOwner() == 2)
//  }
}
