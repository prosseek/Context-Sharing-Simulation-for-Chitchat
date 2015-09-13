package smcho

import scala.collection.mutable.{Map => mm}
import java.nio.file.{Files, Paths}

object ContextMessage {
  // <editor-fold desc="Constructors">

  def apply(nameTypesString:String) = {
    new ContextMessage(0, 0, 0.0, nameTypesString, 0)
  }
}

case class ContextMessage(var host1: Int,
                     var host2: Int,
                     var time: Double,
                     var nameTypesString: String,
                     var size:Int) {

  override def toString() : String = s"[${host1}->${host2}/${time}/${nameTypesString}/${size}]"
  def repr() = s"""{"host1":${host1}, "host2":${host2}, "time":${time}, "nameTypes":"${nameTypesString}", "size":${size}}"""
}
