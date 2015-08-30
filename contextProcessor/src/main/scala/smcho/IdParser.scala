package smcho

import scala.collection.mutable.{Map => mm}

/**
 * Created by smcho on 8/26/15.
 */
trait IdParser extends ContentParser {

  // <editor-fold desc="API">

  /**
   * for example:
   * id = "0/3/400/10.01/summary1:summary2|l|123
   * it returns a sequence
   *
   *
   * @param id
   * @param summaryPool
   * @return
   */
  def idToSummaries(id:String, summaryPool: mm[String, Summary]) = {
    val (host1, host2, size, time, contents) = this.parse(id)
    // ignore all the others, make summaries only from id

    // in stringsToSummaries, the type of summary is by detecting the last character of the name,
    // so the summaryType should be added to (re)use the function.
    val names = contents map { case (name, summaryType, size) => name + summaryType}
    namesToSummaries(names, summaryPool)
  }

  def namesToSummaries(names: Iterable[String], summaryPool: mm[String, Summary]) = {
    val namePattern = """([a-zA-Z]+\d+)([a-z])?""".r
    names flatMap { name =>
      name match {
        case namePattern(summaryName, summaryType) if (summaryPool.contains(summaryName)) => {
            var sType = summaryType
            if (sType == null || !(sType == "b" || sType == "l")) sType = "b"
            Some(summaryPool.get(summaryName).get.copy(sType))
        }
        case _ => None
      }
    }
  }

  def summariesToContent(summaries: Iterable[Summary]) : String = {
    def makeSummaryName(s : Summary) = {
      s.getName + "|" + s.getSummaryType() +  "|" + s.getSize()
    }
    ("" /: summaries) { (acc, summary) => acc + (if (acc != "") ":" else "") + makeSummaryName(summary) }
  }

  def summariesToContent(summaries: mm[String, Summary]) : String = summariesToContent(summaries.values)


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
    val size = items(1).toInt
    val time = items(2).toDouble
    val content = items(3).replace("]", "")

    val (host1, host2) = hosts match {
      case hostsPattern(host1, host2) => (host1.toInt, host2.toInt)
      case _ => throw new Exception(s"No match for hosts $hostsPattern")
    }

    val contents = parseContent(content)

    (host1, host2, size, time, contents)
  }

  // </editor-fold>
  def getTotalTime(summaries: Iterable[Summary]) = {
    (0 /: summaries) { (acc, v) => acc + v.getSize()}
  }
}
