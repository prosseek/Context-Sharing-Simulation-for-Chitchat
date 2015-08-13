package smcho

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */
object Database  {
  var database: Database = null;
  def get() = {
    if (database == null) {
      database = new Database()
    }
    database
  }
}

class Database {
  /**
   *  Host i sends a message c to j
   * @param i
   * @param j
   * @param c
   */
  def add(i: Int, j:Int, c: Context) = {
    println(s"$i sends a context to $j");
  }

  def getContext(id: Int) = {
    Context.create()
  }
}