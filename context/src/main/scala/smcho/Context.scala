package smcho

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */
class Context (val host1: Int, val host2: Int, val message : String) {

  var size = -1;
  def setSize(size: Int) = this.size = size;
  def getSize() = this.size;
}

object Context {
  def create(host1: Int, host2: Int, message : String) =
  {
    new Context(host1 = host1, host2 = host2, message = message)
  }
}