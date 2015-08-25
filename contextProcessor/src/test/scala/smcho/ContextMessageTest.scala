package smcho

import org.scalatest.FunSuite

/**
 * Created by smcho on 8/12/15.
 */
class ContextMessageTest extends FunSuite {

  test("Create ContextMessage") {
    val id = "Hello"
    val d = ContextMessage(host1 = 0, host2 = 3, size = 100, time = 10.0, id=id)
    assert(d.getId() == id)
    assert(d.getSize() == 100)
    assert(d.getHost1() == 0)
    assert(d.getHost2() == 3)
    assert(d.getTime() == 10.0)
  }
  // Get/set size
  test("Simpler ContextMessage creating") {
    val id = "Hello"
    val d = new ContextMessage(id)
    assert(d.getId() == id)
    assert(d.getSize() == 0)
    assert(d.getHost1() == -1)
    assert(d.getHost2() == -1)
    assert(d.getTime() == 0.0)
  }
}
