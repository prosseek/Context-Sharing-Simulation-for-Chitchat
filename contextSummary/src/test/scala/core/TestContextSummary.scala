package core

import grapevineType.BottomType.BottomType
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by smcho on 8/17/15.
 */

object C {
  def apply(t: (Map[String, Any], Int, Int)) : C  = new C(t._1, t._2, t._3)
  def apply(filePath:String) : C = C(ContextSummary.loadJsonAll(filePath))
}

case class C(var m: Map[String, Any], var j:Int = 0, var jc:Int = 0) extends ContextSummary(m, j, jc) {
  override def getSize(): (Int, Int, Int) = ???
  override def getSerializedSize(): Int = ???
  override def get(key: String): Any = ???
  override def setup(dict: Map[String, Any]): Unit = ???
  override def serialize(): Array[Byte] = ???
  override def check(key: String): BottomType = ???
}

class TestContextSummary extends FunSuite with BeforeAndAfter {
  var c: C = _
  before {
    val filePath = "contextSummary/src/test/scala/resource/unittest.json"
    c = C(filePath)
  }

  test("test save") {
    c.save("contextSummary/src/test/scala/resource/unittest2.json")
  }
}
