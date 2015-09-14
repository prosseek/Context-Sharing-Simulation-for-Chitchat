package smcho

import scala.collection.mutable.{Map => mm}
import java.nio.file.{Files, Paths}

object ContextMessage {
  var summariesMap: scala.collection.mutable.Map[String, Summary] = _
  // <editor-fold desc="Constructors">

  def apply(nameTypesString:String) = {
    val size = NameTypes.size(nameTypesString, summariesMap)
    new ContextMessage(0, 0, 0.0, nameTypesString, size)
  }

  def apply(host1: Int, host2: Int, time: Double, nameTypesString: String) = {
    val size = NameTypes.size(nameTypesString, summariesMap)
    new ContextMessage(host1, host2, time, nameTypesString, size)
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
