package smcho

import java.io.File
import scala.collection.mutable.{Map => mm}

import util.io.{File => uFile}
import core.{BloomierFilterSummary, LabeledSummary, ContextSummary}

class Summary(val contextSummary: ContextSummary) {
  private val labeledSummary = contextSummary.asInstanceOf[LabeledSummary]
  private val bloomierSummary = getBloomier(labeledSummary)
  private val sizeLabeled = labeledSummary.getSerializedSize()
  private var sizeBloomier = bloomierSummary.getSerializedSize()

  private def getBloomier(labeledSummary: LabeledSummary) = {
    val bf = new BloomierFilterSummary
    val m : Int = math.ceil(labeledSummary.getKeys().length * 1.5).toInt
    bf.create(labeledSummary.getMap(), m = m, k = 3, q = 8 * 2)
    bf
  }

  def getSizeLabeled() = sizeLabeled
  def getSizeBloomier() = sizeBloomier
  def getKeys() = labeledSummary.getKeys()
}

object Summary {
  def loadContexts(directory: String) = {
    val summaries = mm[String, Summary]();
    // executed in one simulator, the "." is inside the one simulator directory, so there should be some changes
    // such as symbolic links should be added.
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory

    val files = new java.io.File(absoluteDirectory).listFiles.filter(_.getName.endsWith(".txt")) // context is in txt format

    for (file <- files) {
      val fileName = file.toString
      // todo:: fileToSummary should return a summary not a list of summaries (remove (0))
      summaries(uFile.getBasename(fileName)) = new Summary(uFile.fileToSummary(fileName)(0))
    }
    summaries
  }
}