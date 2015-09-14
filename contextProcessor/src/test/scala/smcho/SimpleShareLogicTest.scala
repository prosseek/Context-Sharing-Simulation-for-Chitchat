package smcho

import org.scalatest.FunSuite

/**
 * Created by smcho on 8/28/15.
 */
class SimpleShareLogicTest extends FunSuite {

  test("testGet") {
    val directory = "contextProcessor/src/test/resources"
    val storage = Storage(directory)
    val s = SimpleShareLogic()
    val host = 0
    val limit = 1000

    storage.add(host, ContextMessage("g1c0l:g3c1b"))
    assert(s.get(host, limit, storage) == "g3c1b:g1c0l")
  }
}
