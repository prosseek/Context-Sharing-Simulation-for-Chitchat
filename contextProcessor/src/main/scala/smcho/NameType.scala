package smcho

import scala.collection.mutable.{Map => MM}

object NameType {

  val cache = MM[String, Summary]()

  def getSummaryFromSummaries(name:String, summaries: MM[String, Summary]) = {

    if (NameType.cache.contains(name)) { // if in cache
      NameType.cache(name)
    }
    else {
      val res = summaries.getOrElse(name, null)
      if (res == null) throw new Exception(s"No summary found for ${name}")
      NameType.cache(name) = res
      res
    }
  }

  def split(name: String) = {
    (name.dropRight(1), name.takeRight(1))
  }

  def size(name:String, summaries:MM[String, Summary]) = {
    val (summaryName, groupId, hostId, summaryType) = NameParser.getParams(name).get
    val summary = getSummaryFromSummaries(summaryName, summaries)
    summary.size(summaryType)
  }
}

/**
 * Created by smcho on 9/12/15.
 */
case class NameType(val name:String, val summaries:MM[String, Summary]) {
  if (!NameParser.isValidName(name)) {
    throw new Exception(s"Wrong nametype ${name}")
  }

  val (summaryName, groupId, hostId, summaryType) = NameParser.getParams(name).get
  val summary = NameType.getSummaryFromSummaries(summaryName, summaries)
  val keys = summary.labeledSummary.getKeys()

  def get(key:String) = {
    summaryType match {
      case "j" => {
        if (keys.contains(key)) {
          summary.labeledSummary.jsonMap(key)
        }
        else
          null
      }
      case "l" => summary.labeledSummary.get(key)
      case "b" => summary.bloomierSummary.get(key)
    }
  }

  def size() = summary.size(summaryType)
}
