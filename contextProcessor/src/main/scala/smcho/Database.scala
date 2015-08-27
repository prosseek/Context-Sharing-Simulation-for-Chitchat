package smcho

/**
 * Created by smcho on 8/26/15.
 */
trait Database {
  // Given a directory, load all the contexts into database
  def load(directory: String)
  // returns ContextMassage to share for host
  def get(host: Int)
  // add received ContextMessage to host
  def add(host: Int, contextMessage: ContextMessage)

  def reset()

  def getContextMessageFromAddress(id: Int): ContextMessage

  def getContextMessageFromName(contextName: Iterable[String]): ContextMessage
}
