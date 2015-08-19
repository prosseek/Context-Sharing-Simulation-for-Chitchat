package smcho

import java.io.File

import scala.collection.mutable.{Map => mm}

import core.{LabeledSummary, BloomierFilterSummary, ContextSummary}
import util.io.{File => uFile}

class Summary(val labeledSummary: LabeledSummary) {
  private val sizeLabeled = labeledSummary.getSerializedSize()
  private var sizeBloomier = 0


  def getSizeBloomier() = {
    if (sizeBloomier <= 0) {
      val bf = new BloomierFilterSummary
      val m : Int = math.ceil(labeledSummary.getKeys().length * 1.5).toInt
      bf.create(labeledSummary.getMap(), m = m, k = 3, q = 8 * 2)
      sizeBloomier = bf.getSerializedSize()
    }
    sizeBloomier
  }
  def getSizeLabeled() = sizeLabeled
  def getKeys() = labeledSummary.getKeys()
}

object Database {
  // todo:: the map should be ContextSummary not LabeledSummary
  val contexts = mm[String, Summary]();
  // todo:: make it simple and relative one
  // val contextPath = "experiment/simulation/simpleSimulation"
  // loadContexts(contextPath)

  def loadContexts(directory: String) = {

    // executed in one simulator, the "." is inside the one simulator directory, so there should be some changes
    // such as symbolic links should be added.
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory

    val files = new java.io.File(absoluteDirectory).listFiles.filter(_.getName.endsWith(".txt")) // context is in txt format

    for (file <- files) {
      val fileName = file.toString
      contexts(uFile.getBasename(fileName)) = new Summary(uFile.fileToSummary(fileName)(0)) // todo:: fileToSummary should return a summary
    }
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