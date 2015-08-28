package smcho

import scala.collection.mutable.{Map => mm, Set => mSet}

/**
 * Created by smcho on 8/28/15.
 */
case class Storage() {
  private var summaries = mm[String, Summary]()
  private var history = mm[Int, mSet[Tuple3[String, String, Int]]]()
  private var historyContextMessage = mm[Int, mSet[ContextMessage]]()

  def getHistory() = history
  def getHistoryContextMessage() = historyContextMessage
  def getSummaries() = summaries

  def reset() = {
    summaries = mm[String, Summary]()
    history = mm[Int, mSet[Tuple3[String, String, Int]]]()
    historyContextMessage = mm[Int, mSet[ContextMessage]]()
  }

  def add(host:Int, contextMessage: ContextMessage) = {
    if (!historyContextMessage.contains(host)) {
      historyContextMessage.put(host, mSet[ContextMessage]())
    }
    historyContextMessage.get(host).get += contextMessage
    // val contents = parseContent(contextMessage.getContent()) // a list of 3 tuples (name, type, size)
    contextMessage.parseContent map {
      case (name, summaryType, size) => addToHistory(host, (name, summaryType, size))
    }
  }

  def addToHistory(host: Int, content: Tuple3[String, String, Int]) = {
    if (!history.contains(host)) {
      history.put(host, mSet[Tuple3[String, String, Int]]())
    }
    history.get(host).get += content
  }

  // <editor-fold desc="GET/SET">

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
}
