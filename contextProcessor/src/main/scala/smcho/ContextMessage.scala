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
                     var nameTypes: String) {

  override def toString() : String = s"[${host1}->${host2}/${size}/${time}/${nameTypes}]"
  def repr() = s"""{"host1":${host1}, "host2":${host2}, "size":${size}, "time":${time}, "nameTypes":${nameTypes}}"""

  def parse() : Array[(String, String, Int)] = ??? // parse(this.nameTypes)
}

object ContextMessage {
  def summariesToContent(m:Iterable[Summary]) = ???
  def nameTypesToSummaries(i:AnyVal, j:AnyVal) = ???

  //type cmTuple = (Int, Int, Int, Double, (String, String, Int))

  // <editor-fold desc="Constructors">

  def apply(host1: Int, host2: Int, size:Int, time: Double, content: String) =
    new ContextMessage(host1, host2, size, time, content)

  def apply(host1: Int, host2: Int, size:Int, time: Double, nameTypes: Iterable[String], summaries: mm[String, Summary]) = {
    null
  //  new ContextMessage(host1, host2, size, time,
  //    summariesToContent(nameTypesToSummaries(nameTypes, summaries).asInstanceOf[Iterable[Summary]]))
  }

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: Iterable[Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToContent(summaries))

  def apply(host1: Int, host2: Int, size:Int, time: Double, summaries: mm[String, Summary]) =
    new ContextMessage(host1, host2, size, time, summariesToContent(summaries.values))

  def apply(content:String) = new ContextMessage(0, 0, 0, 0.0, content)

  def apply(summaries: Iterable[Summary]) =
    new ContextMessage(0, 0, 0, 0.0, summariesToContent(summaries))

  def apply(summaries: mm[String, Summary]): ContextMessage =
    apply(0, 0, 0, 0.0, summaries)

  def apply(nameTypes: Iterable[String], summaries: mm[String, Summary]):ContextMessage = {
    apply(0, 0, 0, 0.0, nameTypes, summaries)
  }
    // </editor-fold>
  // names, summaries) // summariesToId(namesToSummaries(names, summaries))
  //def getFromNames(names: Iterable[String], summaries: mm[String, Summary]) =
  //    summariesToId(namesToSummaries(names, summaries))
}
