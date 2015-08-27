package smcho

import scala.collection.mutable.{Map => mm}
import java.nio.file.{Files, Paths}

/** ContextMessage is a class to transform between a set of contexts to Message (Simulator)
  *
  * summary = ([a-zA-Z]+\d+)([b|l])([\d]+)
  *         =>  name         type   size
  * id = summary(:summary)+
  *
  * @constructor create a database with owner id
  */

class ContextMessage(var host1: Int,
                          var host2: Int,
                          var size:Int,
                          var time: Double,
                          var id: String) {

  // <editor-fold desc="Get/Set">

  def setHost1(host: Int) = this.host1 = host
  def setHost2(host: Int) = this.host2 = host
  def setSize(size: Int) = this.size = size
  def setTime(time: Double) = this.time = time
  def setId(id:String) = this.id = id

  def getHost1() = this.host1
  def getHost2() = this.host2
  def getSize() = this.size
  def getTime() = this.time
  def getId() = this.id

  // </editor-fold>

  override def toString() : String = ContextMessage.makeString(this)
}

object ContextMessage extends IdParser  {
  // <editor-fold desc="Constructor">
  def apply(host1: Int, host2: Int, size:Int, time: Double, id: String) = new ContextMessage(host1, host2, size, time, id)
  def apply(id:String) = new ContextMessage(0, 0, 0, 0.0, id)
  def apply(summaries: Iterable[Summary]) = new ContextMessage(0, 0, 0, 0.0, summariesToId(summaries))
  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: Iterable[Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToId(summaries))

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: mm[String, Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToId(summaries.values))

  // </editor-fold>
}
