package smcho

import scala.collection.mutable.{Map => mm, Set => mSet}

object Database {

  private var summaries : mm[String, Summary] = _
  private var history = mm[Int, mSet[String]]()

  def getSummary(name: String) = {
    summaries.get(name)
  }

  /**
   * "abc434" --> 434 is the id number
   *
   * @param summaryName
   * @return
   */
  def getHostAddressFromSummaryName(summaryName: String): Int = {
    val idPattern = """[^\d]+(\d+)""".r
    val m = idPattern.findFirstMatchIn(summaryName)
    m match {
      case Some(s) => Integer.valueOf(s.group(1))
      case None => throw new Exception(s"not correct summary name $summaryName")
    }
  }

  def reset() = {
    summaries = mm[String, Summary]()
    history = mm[Int, mSet[String]]()
  }

  def loadContexts(directory: String) = {
    summaries = Summary.loadContexts(directory)

    // from the loaded contexts, setup the initial contexts into the history
    summaries foreach {
      case (summaryName, summary) => {
        val host = getHostAddressFromSummaryName(summaryName)
        val id = summaryName // summaryToContextMessageMap creates a ContextMessage which needs an id
        val contextMessage = Database.summaryToContextMessageMap(summary, id)
        Database.addToHistory(host, summaryName)
      }
    }
    summaries
  }

  def addToHistory(host: Int, summaryName: String) = {
    if (!history.contains(host)) {
      history.put(host, mSet[String]())
    }
    history.get(host).get += summaryName
  }

  def getHistory() = {
    history
  }

  def getHistory(id: Int) = {
    history.get(id) match {
      case Some(p) => p
      case None => mSet[String]()
    }
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContextMessageFromAddress(id: Int, summaryType: String): ContextMessage = {
    // This is totally arbitrary
    // todo:: update this, this is very wrong
    val contextName = "is" + id
    Database.getContextMessageFromName(contextName, summaryType)
  }

  def getContextMessageFromName(contextName: String, summaryType: String) = {
    val summary = Database.getSummary(contextName)
    if (summary == None) {
      throw new NoContextException(contextName)
    }
    // each summary has bf or lf ContextSummary information, get one of them based on the summaryType
    val contextMessages = summaryToContextMessageMap(summary.get, contextName)
    contextMessages(summaryType)
  }

  /**
   * The summary itself does not contain its name
   * @param summary
   * @param id
   */
  def summaryToContextMessageMap(summary: Summary, id: String) = {
    val bf = new ContextMessage(id)
    bf.setSize(summary.getSizeBloomier())
    val lb = new ContextMessage(id)
    lb.setSize(summary.getSizeLabeled())

    val map = Map("bf" -> bf, "lb" -> lb)
    map
  }

  def getSummaries() = summaries
}