package smcho

import scala.collection.mutable.{Map => mm}

object SummaryFront {
  def create(nameTypes: Iterable[String], summaries: mm[String, Summary]) = {

  }
}

/**
 * Created by smcho on 9/12/15.
 */
case class SummaryFront(var name:String, var summaryType:String, var summary:Summary) {

}
