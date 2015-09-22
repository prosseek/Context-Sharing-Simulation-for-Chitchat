package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/28/15.
 */
class SimpleShareLogicTest extends FunSuite with BeforeAndAfterEach {

  val directory = "contextProcessor/src/test/resources"
  val storage = Storage(directory, "1:0:1")
  var s:SimpleShareLogic = _

  override def beforeEach() {
    s = SimpleShareLogic()
    s.setStorage(storage)
  }

  test("testGet first case") {
    val host = 0
    val limit = 1000

    // "l" is initial summary type that is shared
    assert(s.get(host, limit, "l") == "g1c0l")
  }

  test("test findContextTuple") {
    val host = 0
    val limit = 1000
    storage.add(host, ContextMessage("g1c0l:g3c1b"))
    val contextTuples = s.getContextTuples(host)
    assert(s.findContextTuple(host, contextTuples) == (0,0,0.0,"g1c0l",52))
    assert(s.findContextTuple("g1c0", contextTuples) == (0,0,0.0,"g1c0l",52))
    assert(s.findContextTuple(1, contextTuples) == (0,0,0.0,"g3c1b",29))
    assert(s.findContextTuple("g3c1", contextTuples) == (0,0,0.0,"g3c1b",29))
  }
}
