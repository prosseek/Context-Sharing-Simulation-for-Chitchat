package smcho

import scala.collection.mutable.{Map => mm}

/**
 * Created by smcho on 8/26/15.
 */
trait IdParser {

  // <editor-fold desc="API">

  def idToSummaries(id:String, summaries: mm[String, Summary]) : Seq[Summary] = {
    val (host1, host2, size, time, ids) = this.parse(id)
    ids map { case (name, summaryType, size) =>
      summaries.get(name) match {
        case Some(p) => p
        case None => throw new Exception(s"Not found matching summary $name")
      }
    }
  }

  def summariesToId(summaries: mm[String, Summary]) : String = summariesToId(summaries, "b")
  def summariesToId(summaries: mm[String, Summary], summaryType: String) : String = {
    summariesToId(summaries.values, summaryType)
  }
  def summariesToId(summaries: Iterable[Summary]) : String = summariesToId(summaries, "b")
  def summariesToId(summaries: Iterable[Summary], summaryType: String) : String = {
    def makeSummaryName(s : Summary) = {
      s.getName + "|" + summaryType +  "|" + s.getSize(summaryType)
    }
    ("" /: summaries) { (acc, summary) => acc + (if (acc != "") ":" else "") + makeSummaryName(summary) }
  }

  def makeString(cm: ContextMessage) = {
    s"[${cm.host1}->${cm.host2}/${cm.size}/${cm.time}/${cm.id}]"
  }

  // </editor-fold>

  // <editor-fold desc="Internal methods">

  /**
   * host1
   *    host2
   *       total_size
   *           time
   * [0->5/123/10.01/hello0:hello1|l|123]
   *                 id0    id1 (name l/b size)
   * @param input
   * @return
   */
  def parse(input:String) = {
    val items = input.split("/")
    val hosts = items(0).replace("[","")

    val hostsPattern = """(\d+)->(\d+)""".r
    val summaryPattern = """([a-zA-Z0-9]+)(\|([b|l])\|([\d]+))?""".r
    val size = items(1).toInt
    val time = items(2).toDouble
    val id = items(3).replace("]", "")

    val (host1, host2) = hosts match {
      case hostsPattern(host1, host2) => (host1.toInt, host2.toInt)
      case _ => throw new Exception(s"No match for hosts $hostsPattern")
    }

    val ids = id.split(":") map (v =>
      v match {
        case summaryPattern(name, option, summaryType, size) => (
          name,
          if (summaryType == null) "n" else summaryType,
          if (size == null) 0 else size.toInt)
        case _ => throw new Exception(s"No match for summary $summaryPattern")
      })

    (host1, host2, size, time, ids)
  }

  // </editor-fold>

}
