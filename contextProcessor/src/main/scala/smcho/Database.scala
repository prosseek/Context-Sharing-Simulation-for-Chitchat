package smcho

import java.io.File

import scala.collection.mutable.{Map => mm}

import core.{LabeledSummary, ContextSummary}
import util.io.{File => uFile}

object Database {
  // todo:: the map should be ContextSummary not LabeledSummary
  val contexts = mm[String, LabeledSummary]();
  // todo:: make it simple and relative one
  val contextPath = "/Users/smcho/Desktop/code/ContextSharingSimulation/experiment/contexts/SimulationSimple/contexts"

  val files = new java.io.File(contextPath).listFiles.filter(_.getName.endsWith(".txt")) // context is in txt format

  for (file <- files) {
    val fileName = file.toString
    contexts(uFile.getBasename(fileName)) = uFile.fileToSummary(fileName)(0) // todo:: fileToSummary should return a summary
  }

  /**
   *  Host i sends a message c to j
   *  This transaction is stored in the database to be analyzed later
   *
   * @param c
   * @return true/false based on the added results
   */
  def add(c: ContextSummary) = {
    println(s"add $c");
    //contexts.put(c.getMessage(), c)
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContextMessage(id: Int) = {
    val c = ContextMessage.create(0, 0, "hello")
    c.setSize(432);
    c
  }

  def getContexts() = contexts
}