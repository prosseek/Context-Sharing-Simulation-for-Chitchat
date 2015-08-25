package smcho

import scala.collection.mutable.{Set => mSet, Map => mm}

/**
 * Created by smcho on 8/22/15.
 */
trait ShareLogic {
  private var summaries : mm[String, Summary] = _
  private var history = mm[Int, mSet[String]]()

  def getSummaries() = summaries
  def getHistory() = history

  def generateShareContext(host: Int)
}
