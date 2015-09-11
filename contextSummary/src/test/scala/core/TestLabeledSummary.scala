package core

import grapevineType.BottomType
import org.scalatest._

/**
 * Created by smcho on 8/10/14.
 */
class TestLabeledSummary extends FunSuite with BeforeAndAfter {
  var t: LabeledSummary = _
  val m = Map("A count" -> 10, "B count"->20, "C"->"test")
  before {
    t = LabeledSummary(m)
  }
  test ("Get size test") {
    val totalItem = m.size
    val keySize = (0 /: m.keys) {(acc, value) => acc + value.size}
    val valueSize = 2 + 2 + ("test".size + 1)
    assert(t.getSizes()._1 == keySize + valueSize)
  }


}
