package smcho

import scala.collection.mutable.{Map => mm, Set => mSet}

/**
 * Created by smcho on 8/28/15.
 */
case class Storage() {
  private var summaryMap = mm[String, Summary]()
  private var tupleMap = mm[Int, mSet[ContextMessage.cmTuple]]()
  private var contextMessageMap = mm[Int, mSet[ContextMessage]]()

  def getTupleMap() = tupleMap
  def getHistoryContextMessageMap() = contextMessageMap
  def getSummaryMap() = summaryMap

  def reset() = {
    summaryMap = mm[String, Summary]()
    tupleMap = mm[Int, mSet[ContextMessage.cmTuple]]()
    contextMessageMap = mm[Int, mSet[ContextMessage]]()
  }

  def add(host:Int, contextMessage: ContextMessage) = {
    if (!contextMessageMap.contains(host)) {
      contextMessageMap.put(host, mSet[ContextMessage]())
    }
    contextMessageMap.get(host).get += contextMessage
    // val contents = parseContent(contextMessage.getContent()) // a list of 3 tuples (name, type, size)
    contextMessage.parseContent map {
      case (name, summaryType, size) => addToTupleMap(host,
        (contextMessage.getHost1(),
         contextMessage.getHost2(),
         contextMessage.getSize(),
         contextMessage.getTime(), (name, summaryType, size)))
    }
  }

  def addToContextMessageMap(host:Int, context:ContextMessage) = add(host, context)

  def addToSummaryMap(name:String, summary: Summary) = {
    summaryMap.put(name, summary)
  }

  def addToTupleMap(host: Int, content: ContextMessage.cmTuple) = {
    if (!tupleMap.contains(host)) {
      tupleMap.put(host,  mSet[ContextMessage.cmTuple]())
    }
    tupleMap.get(host).get += content
  }

  // <editor-fold desc="GET/SET">

  def getTuple(id: Int) = {
    tupleMap.get(id) match {
      case Some(p) => p
      case None => mSet[ContextMessage.cmTuple]()
    }
  }

  def getSummary(name: String) : Summary = {
    summaryMap.get(name) match {
      case Some(p) => p
      case None => null
    }
  }

  def print() = {}
}
