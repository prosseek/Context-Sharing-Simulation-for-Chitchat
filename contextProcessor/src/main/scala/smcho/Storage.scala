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
  val hostTocontextMessagesMap = mm[Int, mSet[ContextMessage]]()

  def apply(directory:String) = {
    if (storage == null) storage = new Storage(directory)
    storage
  }
}

class Storage(directory:String) {
  Storage.summariesMap = Summary.loadContexts(directory)
  Storage.summaries = Storage.summariesMap.values


  override def toString = {
    repr()
  }

  def repr() = {
    val summariesInJson = "HI"
    s"""{"summaries":"${summariesInJson}"}"""
  }

  def add(host:Int, c:ContextMessage) = {
    if (!Storage.hostTocontextMessagesMap.contains(host)) {
      Storage.hostTocontextMessagesMap(host) = mSet[ContextMessage]()
    }
    val set = Storage.hostTocontextMessagesMap(host)
    set += c
    Storage.hostTocontextMessagesMap(host) = set
  }
}
