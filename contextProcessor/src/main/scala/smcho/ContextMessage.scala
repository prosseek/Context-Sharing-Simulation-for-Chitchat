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
                          var content: String) {

  // <editor-fold desc="Get/Set">

  def setHost1(host: Int) = this.host1 = host
  def setHost2(host: Int) = this.host2 = host
  def setSize(size: Int) = this.size = size
  def setTime(time: Double) = this.time = time
  def setId(id:String) = this.content = id

  def getHost1() = this.host1
  def getHost2() = this.host2
  def getSize() = this.size
  def getTime() = this.time
  def getContent() = this.content

  // </editor-fold>

  override def toString() : String = makeString()
  def makeString() =  s"[${host1}->${host2}/${size}/${time}/${content}]"
}

object ContextMessage extends IdParser  {

  // <editor-fold desc="Constructors">

  def apply(host1: Int, host2: Int, size:Int, time: Double, content: String) =
    new ContextMessage(host1, host2, size, time, content)

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: Iterable[String], summaryPool: mm[String, Summary]) = {
    new ContextMessage(host1, host2, size, time,
      summariesToId(namesToSummaries(summaries, summaryPool).asInstanceOf[Iterable[Summary]]))
  }

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: Iterable[Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToId(summaries))

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: mm[String, Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToId(summaries.values))

  def apply(content:String) = new ContextMessage(0, 0, 0, 0.0, content)

  def apply(summaries: Iterable[Summary]) =
    new ContextMessage(0, 0, 0, 0.0, summariesToId(summaries))

  def apply(summaries: mm[String, Summary]): ContextMessage =
    apply(0, 0, 0, 0.0, summaries)

  def apply(summaries: Iterable[String], summaryPool: mm[String, Summary]):ContextMessage = {
    apply(0, 0, 0, 0.0, summaries, summaryPool)
  }
    // </editor-fold>
}
