package smcho

import scala.collection.mutable.{Map => mm, Set => mSet, ListBuffer}

object DatabaseWithStrategy {
  def apply(strategy: String) = new DatabaseWithStrategy(strategy)
  def apply() = new DatabaseWithStrategy("smcho.SimpleShareLogic")
}

class DatabaseWithStrategy(val strategy: String) extends Database with LoadClass {
  private var summaries = mm[String, Summary]()
  private var history = mm[Int, mSet[String]]()
  private var historyContextMessage = mm[Int, mSet[ContextMessage]]()

  val shareLogic: ShareLogic = loadObject(strategy).asInstanceOf[ShareLogic]

  // <editor-fold desc="API">

  // returns ContextMassage to share for host
  override def get(host: Int): String = {
    shareLogic.get(host, summaries, history)
  }

  // Given a directory, load all the contexts into database
  override def load(directory: String) = {
    summaries = Summary.loadContexts(directory)

    // from the loaded contexts, setup the initial contexts into the history
    summaries foreach {
      case (summaryName, summary) => {
        val host = getHostAddressFromSummaryName(summaryName)
        val contextMessage = ContextMessage(summary.toString())
        addToHistory(host, contextMessage)
      }
    }
  }

  // add received ContextMessage to host
  override def add(host: Int, contextMessage: ContextMessage) = {
    if (!historyContextMessage.contains(host)) {
      historyContextMessage.put(host, mSet[ContextMessage]())
    }
    historyContextMessage.get(host).get += contextMessage
    val content = contextMessage.getContent()
  }

  override def reset() = {
    summaries = mm[String, Summary]()
    history = mm[Int, mSet[String]]()
  }

//  override def getContextMessageFromAddress(id: Int): ContextMessage = {
//    //    val contextName = "is" + id
//    //    getContextMessageFromName(contextName, summaryType)
//    null
//  }

  override def getFromNames(names: Iterable[String]) = {
    val tempSummaries = (ListBuffer[Summary]() /: names) {
      (acc, name) => if (summaries.contains(name)) acc += summaries.get(name).get else acc
    }
    //ContextMessage(tempSummaries)
    ""
  }

  // </editor-fold>

  def addToHistory(host: Int, contextMessage: ContextMessage) = {
//    if (!history.contains(host)) {
//      history.put(host, mSet[String]())
//    }
//    history.get(host).get += summaryName
  }

  // <editor-fold desc="GET/SET">

  def getSummaries() = summaries

  def getHistory() = history

  def getHistory(id: Int) = {
    history.get(id) match {
      case Some(p) => p
      case None => mSet[String]()
    }
  }

  def getSummary(name: String) = {
    summaries.get(name) match {
      case Some(p) => p
      case None => null
    }
  }

  // </editor-fold>

  /**
   * "abc434" --> 434 is the id number
   *
   * @param summaryName
   * @return
   */
  private def getHostAddressFromSummaryName(summaryName: String): Int = {
    val idPattern = """[^\d]+(\d+)""".r
    val m = idPattern.findFirstMatchIn(summaryName)
    m match {
      case Some(s) => Integer.valueOf(s.group(1))
      case None => throw new Exception(s"not correct summary name $summaryName")
    }
  }
}