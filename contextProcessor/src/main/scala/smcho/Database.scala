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
   *  This transaction is stored in the database to be analyzed later
   *
   * @param i
   * @param j
   * @param c
   * @return true/false based on the added results
   */
  def add(i: Int, j:Int, c: Context) = {
    println(s"$i sends a context to $j");
    true;
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContext(id: Int) = {
    Context.create()
  }
}