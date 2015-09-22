package smcho

import scala.collection.mutable.{Set => mSet}

object SimpleShareLogic {
  def apply() = new SimpleShareLogic()
}

/**
 * This module is dynamically loaded
 * You must invoke setStorage to store the storage field after the instatiation
 *
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {
  var storage:Storage = _
  // ex) 3:4:5 to indicate group1 has 3 elements, group 2 has 4 elements, and group 3 has 5 elemtns
  var hostSizes:String = _

  def setStorage(storage:Storage) = {
    this.storage = storage
    this.hostSizes = storage.hostSizes
  }

  def getContextTuples(host:Int) = {
    storage.getTuples(host)
  }

  def findContextTuple(host:Int, m: mSet[(Int, Int, Double, String, Int)]) : (Int, Int, Double, String, Int) = {
    val nameTypeString = NameType.hostIdToContextName(host, hostSizes)
    findContextTuple(nameTypeString, m)
  }

  def findContextTuple(name:String, mset: mSet[(Int, Int, Double, String, Int)]) : (Int, Int, Double, String, Int) = {
    mset foreach {
      case (from, to, time, nameType, size) => if (nameType.startsWith(name)) return (from, to, time, nameType, size)
    }
    null
  }

  def getContextNamesToSendAll(host: Int, limit:Int, initialSummaryType:String) = {
    val setOfContexts = getContextTuples(host)

    val sb = new StringBuilder()
    // maybe I can do some analysis based on the information
    setOfContexts foreach {
      case (host1, host2, time, nameType, size) =>
        sb.append(nameType + ":")
    }
    val total = sb.toString.dropRight(1)
    NameTypes.nameSort(total)
  }

  def getContextNamesToSend(host: Int, limit:Int) = {


//    val setOfContexts = storage.getTuples(host)
//
//    val sb = new StringBuilder()
//
//    // 1. get host context
//    sb.append(getHostName())
//
//    val nameTypeString = getHostName()
//    val cm = ContextMessage(nameTypeString)
//    var newLimit = limit - cm.size
//
//    // 2. Sort the available contexts based on
//    //    2.1 contexts with similar taste
//    //    2.2 contexts that are received most recently
//    val similarContexts = getSimilarContexts(dropContexts(setOfContexts, nameTypeString))
//    val sizeSortedContexts = getSizeSortedContexts(setOfContexts, similarContexts)
//
//
//
//    // maybe I can do some analysis based on the information
//    setOfContexts foreach {
//      case (host1, host2, time, name, size) =>
//        sb.append(name + ":")
//    }
//    val total = sb.toString.dropRight(1)
//    NameTypes.nameSort(total)
    ""
  }

  /**
   * We need initialSummaryType as we need to decide what is the summary type for the first time
   * in exchanging summaries
   *
   * @param host
   * @param limit
   * @param initialSummaryType
   * @return
   */
  override def get(host: Int, limit:Int, initialSummaryType:String): String = {


    // SimpleShareLogic blindingly aggregates all the available contexts and share
    // 1. get the whole tuple that it contains
    val setOfContexts = getContextTuples(host)

    // For the first context sharing
    if (setOfContexts.size == 0) {
      val nameTypeString = NameType.hostIdToContextName(host, hostSizes) + initialSummaryType
      val cm = ContextMessage(nameTypeString)
      storage.add(host, cm)
      nameTypeString
    } else {
      getContextNamesToSend(host, limit)
    }
  }
}
