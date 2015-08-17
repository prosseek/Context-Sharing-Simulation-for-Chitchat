package core

import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by smcho on 8/17/15.
 */
class TestContextSummary extends FunSuite with BeforeAndAfter {
  var l: LabeledSummary = _
  var b: BloomierFilterSummary = _
  before {
    l = new LabeledSummary
    b = new BloomierFilterSummary
  }

  test("setup 1") {
    val m = Map("A count" -> 10, "B count" -> 20, "C" -> "test")
    l.create(m)
    assert(l.get("A count") == 10)

    b.create(m, 10, 3, 16)
    assert(b.get("A count") == 10)

    println(l.get("X"))
    println(b.get("X"))
  }
}
