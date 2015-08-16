package smcho

object Database {
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
    println(s"add $i * $j");
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContext(id: Int) = {
    val c = Context.create(0, 0, "hello")
    c.setSize(432);
    c
  }

  /**
   *
   * @param id
   * @param context
   */
  def processMessage(id: Int, context: Context): Unit = {

  }
}