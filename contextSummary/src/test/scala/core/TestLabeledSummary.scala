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
    assert(t.getSize()._1 == keySize + valueSize)
  }
  test ("load") {
    val ls = LabeledSummary("experiment/data/sample_context.txt")

    assert(ls.check("abc") == BottomType.NoError)
    assert(ls.get("abc") == "Hello, world")
    assert(ls.check("recommendation") == BottomType.NoError)
    assert(ls.get("recommendation") == "Chef")
    assert(ls.check("level of recommendation") == BottomType.NoError)
    assert(ls.get("level of recommendation") == 5)
    assert(ls.check("level of recommendations") != BottomType.NoError)
  }
  test ("store") {
//    val ls = new LabeledSummary("experiment/data/save_test_context.txt")
//    val file = new java.io.File(filePath)
//    if (file.exists()) {
//      file.delete()
//    }
//    val m = Map[String, Any]("a" -> "x", "level of a" -> 3)
//    ls.setup(m)
//    ls.save(filePath)
//
//    ls.load(filePath)
//    assert(ls.check("a") == BottomType.NoError)
//    assert(ls.get("a") == "x")
//    assert(ls.check("level of a") == BottomType.NoError)
//    assert(ls.get("level of a") == 3)
  }
  test("serialize") {
    val m = Map("A count" -> 10, "B count"->20, "C"->"test")
    t.setup(m)
    val s = t.serialize()
    println(s)
  }
}
