package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import scala.collection.mutable.{Map => mm, Set => mSet}

/**
 * Created by smcho on 8/28/15.
 */
class StorageTest extends FunSuite with BeforeAndAfterEach {
  val directory = "contextProcessor/src/test/resources"

  test("test add") {
    val s = Storage(directory)
    s.add(1, ContextMessage("g1c0l:g3c1b"))
    assert(s.get(1).size == 1)
    s.add(1, ContextMessage("g1c0l:g3c1b"))
    assert(s.get(1).size == 1)
    s.add(1, ContextMessage("g1c0l"))
    assert(s.get(1).size == 2)
    assert(s.get(2).size == 0)
  }

  test("test json") {
    val s = Storage(directory)
    s.add(1, ContextMessage("g1c0l:g3c1b"))
    s.add(1, ContextMessage("g3c1b"))
    s.add(2, ContextMessage("g1c0l:g3c1b"))
    s.add(2, ContextMessage("g3c1b"))

    val expected = """{"summaries":[{"name":"g1c0", "sizes":[105,52,29], "fileName":"g1c0.json"},{"name":"g3c1", "sizes":[105,52,29], "fileName":"g3c1.json"}],"""
    assert(s.repr.startsWith(expected))
  }
}
