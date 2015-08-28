package smcho

import scala.collection.mutable.{Set => mSet, Map => mm}

/**
 * Created by smcho on 8/22/15.
 */
trait ShareLogic {

//
//  def getSummaries() = summaries
//  def getHistory() = history


  def get(host: Int, summaries: mm[String, Summary], history:  mm[Int, mSet[String]]) : String
}
