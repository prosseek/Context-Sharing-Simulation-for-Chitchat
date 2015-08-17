package smcho

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */

class Context (val fromAddress: Int, val toAddress: Int, val message : String) {
  var size = -1;
  def setSize(size: Int) = this.size = size;
  def getSize() = this.size;
  def getMessage() = this.message
  def getFromAddress() = this.fromAddress
  def getToAddress() = this.toAddress
  override def toString() = s"($fromAddress => $toAddress):$message"
}

object Context {
  def create(host1: Int, host2: Int, message : String) =
  {
    new Context(fromAddress = host1, toAddress = host2, message = message)
  }
}