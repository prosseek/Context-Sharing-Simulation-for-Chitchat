package smcho

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */
class Database (val owner: Int) {
  def getOwner() = this.owner
}
