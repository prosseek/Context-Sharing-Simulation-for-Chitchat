package smcho

import scala.collection.mutable.{Map => MM}

/**
 * Created by smcho on 9/12/15.
 */
case class NameType(val nameType:String, val summaries:MM[String, Summary]) {
  if (!NameParser.isValidName(nameType)) {
    throw new Exception(s"Wrong nametype ${nameType}")
  }

  val (name, groupId, hostId, summaryType) = NameParser.getParams(nameType).get
  val summary = getSummaryFromSummaries(name, summaries)
  val keys = summary.labeledSummary.getKeys()

  def getSummaryFromSummaries(name:String, summaries: MM[String, Summary]) = {
    val res = summaries.getOrElse(name, null)
    if (res == null) throw new Exception(s"No summary found for ${name}")
    res
  }

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
