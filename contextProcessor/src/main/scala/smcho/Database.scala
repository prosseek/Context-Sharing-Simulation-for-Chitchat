package smcho

import scala.collection.mutable.{Map => mm, Set => mSet}

class Database(val strategy: String) {
  val shareLogic: ShareLogic = loadObject(strategy).asInstanceOf[ShareLogic]

  def getClass (className: String) = {
    var c: Class[_] = null
    try {
      c = Class.forName(className)
    }
    catch {
      case e: ClassNotFoundException => {
        throw new Exception("Couldn't find class '" + className + "'" + "\n" + e.getMessage, e)
      }
    }
    c
  }

  def loadObject (className: String) = {
    var o: Any = null

    try {
      val objClass = getClass(className)
      o = objClass.newInstance
    }
    catch {
      case e: SecurityException => {
        e.printStackTrace
        throw new Exception("Fatal exception " + e, e)
      }
    }
    o
  }
}

object Database {

  private val shareLogic = new SimpleShareLogic()
  private var summaries = shareLogic.getSummaries()
  private var history = shareLogic.getHistory()

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