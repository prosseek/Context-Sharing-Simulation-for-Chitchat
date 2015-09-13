package smcho

import net.liftweb.json.DefaultFormats

import scala.collection.mutable.{Map => mm, Set => mSet}
import net.liftweb.json.Serialization.{write => jsonWrite}

/**
 * Created by smcho on 8/28/15.
 */

object Storage {
  def storageToJSonString(storage: Storage) = {

    // 1. summaryMap
    //   class Summary(val contextSummary: ContextSummary, var name:String, var summaryType: String) {
    //   private var summaryMap = mm[String, Summary]()
    storage.summaryMap.keys.toList.sorted foreach { key =>
      implicit val formats = DefaultFormats

      // 1. summaryMap gets all the summary read from directory or created in between
      // {"contextSummary":{},"name":"is0","summaryType":"b"}
      val contextSummaryString = jsonWrite(storage.summaryMap.get(key).get)

      storage.summaryMap.get(key) foreach {key =>
        println(key)
      }

      // println (s"KEY $key: VALUE:${summaryMap.get(key).get.toString()}")
    }
  }
}

case class Storage() {
  private var _summaryMap = mm[String, Summary]()
  //private var _tupleMap = mm[Int, mSet[ContextMessage.cmTuple]]()
  private var _contextMessageMap = mm[Int, mSet[ContextMessage]]()

  def summaryMap = _summaryMap
  //def tupleMap = _tupleMap
  def contextMessageMap = _contextMessageMap

  def reset() = {
    _summaryMap = mm[String, Summary]()
    //_tupleMap = mm[Int, mSet[ContextMessage.cmTuple]]()
    _contextMessageMap = mm[Int, mSet[ContextMessage]]()
  }

  def add(host:Int, contextMessage: ContextMessage) = {
    if (!contextMessageMap.contains(host)) {
      contextMessageMap.put(host, mSet[ContextMessage]())
    }
    contextMessageMap.get(host).get += contextMessage
    // val contents = parseContent(contextMessage.getContent()) // a list of 3 tuples (name, type, size)
    val contents = contextMessage.parse()
//    contents foreach { case (name, summaryType, size) => addToTupleMap(host,
//        (contextMessage.host1,
//         contextMessage.host2,
//         contextMessage.size,
//         contextMessage.time, (name, summaryType, size)))
//    }
  }

  def addToContextMessageMap(host:Int, context:ContextMessage) = add(host, context)

  def addToSummaryMap(name:String, summary: Summary) = {
    summaryMap.put(name, summary)
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
//    println("tupleMap (what tuples each node has) print")
//    tupleMap.keys.toList.sorted foreach { key =>
//      println (s"KEY $key: VALUE:${tupleMap.get(key).get.toString()}")
//    }
    // contextMessageMap = mm[Int, mSet[ContextMessage]]()
    println("---------------------------------------")
  }
}
