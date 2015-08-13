package smcho

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */
class Context (val id: Int) {

  def this() = this(-1)

  var size = 0;
  def getId() = this.id
  def setSize(size: Int) = this.size = size;
  def getSize() = this.size;
}

object Context {
  def create() = {
    new Context()
  }
}