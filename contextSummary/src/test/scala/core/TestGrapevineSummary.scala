package core

import grapevineType.BottomType._
import org.scalatest._

object TG {
  def apply(t: (Map[String, Any], Int, Int)) : TG  = new TG(t._1, t._2, t._3)
  def apply(filePath:String) : TG = TG(ContextSummary.loadJsonAll(filePath))
}

case class TG(var m: Map[String, Any], var j:Int = 0, var jc:Int = 0) extends GrapevineSummary(m, j, jc) {
  override def getSize(): (Int, Int, Int) = ???
  override def getSerializedSize(): Int = ???
  override def get(key: String): Any = ???
  override def serialize(): Array[Byte] = ???
  override def check(key: String): BottomType = ???
}

/**
 * Created by smcho on 8/10/14.
 */

class TestGrapevineSummary extends FunSuite with BeforeAndAfter  {
  var t: GrapevineSummary = _
  val m = Map[String, Any]("number of apples"->10, "age of kids"->4, "speed of a car"->14, "latitude"->(32, 22, 44, 33), "date"->(2014,10,1), "time"->(11,11))

  before {
    t = TG(m)
  }

  test ("Test create from key") {
//    "age" -> classOf[AgeType],
//    "speed" -> classOf[SpeedType],
//    "number" -> classOf[ByteType],
//    "latitude" -> classOf[LatitudeType],
//    "longitude" -> classOf[LongitudeType],
//    "date" -> classOf[DateType],
//    "time" -> classOf[TimeType]

      t.setup(m)

    assert(t.getValue("number of apples").get == m("number of apples"))
    assert(t.getValue("age of kids").get == m("age of kids"))
    assert(t.getValue("speed of a car").get == m("speed of a car"))
    assert(t.getValue("latitude").get == m("latitude"))
    assert(t.getValue("date").get == m("date"))
    assert(t.getValue("time").get == m("time"))
    assert(t.getValue("latitude of the world").isEmpty)
  }

  test ("Test create from value") {
    val m = Map[String, Any]("A count"->10, "B_f"->20.5, "C"->"Hello")
    t.setup(m)

    assert(t.getValue("A count").get == m("A count"))
    assert(t.getValue("B_f").get == m("B_f"))
    assert(t.getValue("C").get == m("C"))
    assert(t.getValue("D").isEmpty)
  }
}
