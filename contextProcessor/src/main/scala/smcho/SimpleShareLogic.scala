package smcho

/**
 * Created by smcho on 8/24/15.
 */
class SimpleShareLogic extends ShareLogic {
  override def generateShareContext(host: Int): Unit = {
    // SimpleShareLogic blindingly aggregates all the available contexts and share
    //val contexts = getHistory(host)
    println("Hello, world")
  }
}
