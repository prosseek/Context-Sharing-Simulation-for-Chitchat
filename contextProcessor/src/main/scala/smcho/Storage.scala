package smcho

import net.liftweb.json.DefaultFormats

import scala.collection.mutable.{Map => mm, Set => mSet}
import net.liftweb.json.Serialization.{write => jsonWrite}

/**
 * Created by smcho on 8/28/15.
 */

object Storage {
  var storage: Storage = null
  var summaries: Iterable[Summary] = _
  var summariesMap: mm[String, Summary] = _
  val hostToContextMessagesMap = mm[Int, mSet[ContextMessage]]()

  def apply(directory:String) = {
    if (storage == null) storage = new Storage(directory)
    storage
  }
}

class Storage(directory:String) {
  Storage.summariesMap = Summary.loadContexts(directory)
  Storage.summaries = Storage.summariesMap.values
  ContextMessage.summariesMap = Storage.summariesMap

  def summaries = Storage.summaries
  def summariesMap = Storage.summariesMap

  override def toString = {
    s"""summary has ${Storage.summaries.size} items, map has ${Storage.summaries.size} items"""
  }

  def repr() = {

    def getJsonFromSummaries() = {
      val sb = (new StringBuilder() /: Storage.summaries ) { (acc, value) =>
        acc ++= value.repr() + ","
      }
      sb.toString.dropRight(1)
    }

    def getJsonFromMaps() = {

      def dictToJson(m:Tuple2[Int, mSet[ContextMessage]]) = {
        def setToJson(set:mSet[ContextMessage]) = {
          val sb = (new StringBuilder() /: set ) { (acc, value) =>
            acc ++= value.repr + ","
          }
          "[" ++ sb.toString.dropRight(1) ++ "]"
        }
        s""""${m._1}":${setToJson(m._2)}"""
      }

      val sb = (new StringBuilder() /: Storage.hostToContextMessagesMap ) { (acc, value) =>
        acc ++= dictToJson(value) + ","
      }
      sb.toString.dropRight(1)
    }

    s"""{"summaries":[${getJsonFromSummaries()}],\n"hostTocontextMessagesMap":{${getJsonFromMaps()}}}"""
  }

  def add(host:Int, c:ContextMessage) = {
    if (!Storage.hostToContextMessagesMap.contains(host)) {
      Storage.hostToContextMessagesMap(host) = mSet[ContextMessage]()
    }
    val set = Storage.hostToContextMessagesMap(host)
    set += c
    Storage.hostToContextMessagesMap(host) = set
  }

  def get(host:Int) = {
    Storage.hostToContextMessagesMap.getOrElse(host, mSet[ContextMessage]())
  }
}
