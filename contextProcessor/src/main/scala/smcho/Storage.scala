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
    val contents = contextMessage.parse()
    contents foreach { case (name, summaryType, size) => addToTupleMap(host,
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

  def exists(name: String) = {
    summaryMap.keySet(name)
  }

  def print() = {
    // print
    //summaryMap = mm[String, Summary]()
    println("---------------------------------------")
    println("summaryMap print")
    summaryMap.keys.toList.sorted foreach { key =>
      println (s"KEY $key: VALUE:${summaryMap.get(key).get.toString()}")
    }

    // tupleMap = mm[Int, mSet[ContextMessage.cmTuple]]()
    println("tupleMap (what tuples each node has) print")
    tupleMap.keys.toList.sorted foreach { key =>
      println (s"KEY $key: VALUE:${tupleMap.get(key).get.toString()}")
    }
    // contextMessageMap = mm[Int, mSet[ContextMessage]]()
    println("---------------------------------------")
  }
}
