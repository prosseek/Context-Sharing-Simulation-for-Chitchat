package smcho

import scala.collection.mutable.{Map => mm}

object Database {

  val contexts = mm[String, ContextMessage]();

  /**
   *  Host i sends a message c to j
   *  This transaction is stored in the database to be analyzed later
   *
   * @param c
   * @return true/false based on the added results
   */
  def add(c: ContextMessage) = {
    println(s"add $c");
    contexts.put(c.getMessage(), c)
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContext(id: Int) = {
    val c = ContextMessage.create(0, 0, "hello")
    c.setSize(432);
    c
  }

  /**
   *
   * @param id
   * @param context
   */
  def processMessage(id: Int, context: ContextMessage): Unit = {

  }
}