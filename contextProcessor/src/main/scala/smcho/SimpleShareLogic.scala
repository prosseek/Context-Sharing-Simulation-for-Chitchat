package smcho

import scala.collection.mutable.{Set => mSet, Map => mm}

/**
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {
  override def get(host: Int, summaries: mm[String, Summary], history:  mm[Int, mSet[String]]): String = {
    // SimpleShareLogic blindingly aggregates all the available contexts and share
    //val contexts = getHistory(host)
    ""
  }
}
