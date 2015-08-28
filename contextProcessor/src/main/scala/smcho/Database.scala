package smcho

/**
 * Database only cares about generating ContextMessage content
 *
 * Created by smcho on 8/26/15.
 */
trait Database {
  // Given a directory, load all the contexts into database
  def load(directory: String)
  // returns Content content (string) to share for host
  def get(host: Int) : String
  // add received ContextMessage to host
  def add(host: Int, contextMessage: ContextMessage)
  // make initial state
  def reset()
}
