package smcho

import scala.collection.mutable.{Map => MM}

object NameType {

  def isValidName(nameType:String) = {
    getParams(nameType) match {
      case Some((name, groupId, hostId, summaryType)) => true
      case None => false
    }
  }

  def getParams(nameType:String) = {
    val p = """^(g(\d+)c(\d+))([blj])""".r
    nameType match {
      case p(name, groupId, hostId, summaryType) => Some(name, groupId.toInt, hostId.toInt, summaryType)
      case _ => None
    }
  }

  def getGroupId(nameType:String) = {
    getParams(nameType) match {
      case Some((name, groupId, hostId, summaryType)) => groupId
      case None => -1
    }
  }

  def getHostId(nameType:String) = {
    getParams(nameType) match {
      case Some((name, groupId, hostId, summaryType)) => hostId
      case None => -1
    }
  }

  def getSummaryType(nameType:String) = {
    getParams(nameType) match {
      case Some((name, groupId, hostId, summaryType)) => summaryType
      case None => ""
    }
  }

  def getName(nameType:String) = {
    getParams(nameType) match {
      case Some((name, groupId, hostId, summaryType)) => name
      case None => ""
    }
  }

}
/**
 * Created by smcho on 9/12/15.
 */
case class NameType(val nameType:String, val summaries:MM[String, Summary]) {
  if (!NameType.isValidName(nameType)) {
    throw new Exception(s"Wrong nametype ${nameType}")
  }

  val (name, groupId, hostId, summaryType) = NameType.getParams(nameType).get
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

  def size() = {
    summaryType match {
      case "j" => summary.labeledSummary.getJsonSize()
      case "l" => summary.labeledSummary.getSize()
      case "b" => summary.bloomierSummary.getSize()
    }
  }
}
