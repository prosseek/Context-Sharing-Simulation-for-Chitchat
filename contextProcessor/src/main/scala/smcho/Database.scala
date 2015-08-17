package smcho

<<<<<<< HEAD
import scala.collection.mutable.{Map => mm}

object Database {

  val contexts = mm[String, Context]();

=======
object Database {
>>>>>>> 0407f12c3c956d2c55fa339c3b7af50d0e9ace39
  /**
   *  Host i sends a message c to j
   *  This transaction is stored in the database to be analyzed later
   *
   * @param c
   * @return true/false based on the added results
   */
  def add(c: Context) = {
    println(s"add $c");
    contexts.put(c.getMessage(), c)
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