package smcho

/**
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {

  override def get(host: Int, storage: Storage): Set[Summary] = {
    // SimpleShareLogic blindingly aggregates all the available contexts and share
    //val contexts = getHistory(host)

    // 1. get the whole tuple that it contains
    val tuples = storage.getTuple(host)
    // maybe I can do some analysis based on the information
    val res = tuples flatMap  { t =>
      t match {
        case (host1, host2, totalSize, time, (name, summaryType, size)) => {
          Some(storage.getSummary(name))
        }
        case _ => None
      }
    }
    res.toSet
  }
}
