package smcho

/**
 * Database only cares about generating ContextMessage content
 *
 * Created by smcho on 8/26/15.
 */
trait Database {
  // Given a directory, load all the contexts into database
  // hostSize is a string separated by ","
  // 3,4,5,6 <- 4 groups each member count of 3/4/5/6
  def load(directory: String, hostSize:String)
  // returns Content content (string) to share for host
  def get(host: Int) : ContextMessage
  // add received ContextMessage to host
  def add(host: Int, contextMessage: ContextMessage)
  // make initial state
  def reset()
  //
  def getStorage() : Storage
}
