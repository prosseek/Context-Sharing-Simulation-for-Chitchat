package smcho

import scala.collection.mutable.{Set => mSet, Map => mm}

/**
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {
  override def get(host: Int, storage: Storage): String = {
    // SimpleShareLogic blindingly aggregates all the available contexts and share
    //val contexts = getHistory(host)
    ""
  }
}
