package smcho

object SimpleShareLogic {
  def apply() = new SimpleShareLogic()
}

/**
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {

  override def get(host: Int, limit:Int, storage: Storage): String = {

    // SimpleShareLogic blindingly aggregates all the available contexts and share
    // 1. get the whole tuple that it contains
    val setOfContexts = storage.getTuples(host)

    val sb = new StringBuilder()
    // maybe I can do some analysis based on the information
    setOfContexts foreach {
      case (host1, host2, time, name, size) =>
        sb.append(name + ":")
    }
    sb.toString.dropRight(1)
    //res.toSet
  }
}
