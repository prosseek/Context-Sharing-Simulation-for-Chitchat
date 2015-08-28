package smcho

import java.io.File
import scala.collection.mutable.{Map => mm}

import util.io.{File => uFile}
import core.{BloomierFilterSummary, LabeledSummary, ContextSummary}

class Summary(val contextSummary: ContextSummary, var name:String, var summaryType: String) {

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

  def setName(name:String) = this.name = name
  def setSummaryType(sType: String) = this.summaryType = sType

  def getName() = this.name
  def getSummaryType() = this.summaryType
  def getSize(summaryType: String = this.summaryType) = if (summaryType == "b") sizeBloomier else sizeLabeled
  def getKeys() = labeledSummary.getKeys()
  def makeString(summaryType: String = this.summaryType) = {
    s"${name}|${summaryType}|${getSize(summaryType)}"
  }

  def copy(newSummaryType: String = "b") = {
    new Summary(contextSummary, name, summaryType = newSummaryType)
  }

  override def toString() = makeString()
}

object Summary {

  def apply(contextSummary: ContextSummary, name:String, summaryType: String) : Summary = new Summary(contextSummary, name, summaryType)
  def apply(contextSummary: ContextSummary, name:String) : Summary = new Summary(contextSummary, name, "b")
  def apply(contextSummary: ContextSummary) : Summary = new Summary(contextSummary, "", "b")

  def loadContexts(directory: String) = {
    val summaries = mm[String, Summary]();
    // executed in one simulator, the "." is inside the one simulator directory, so there should be some changes
    // such as symbolic links should be added.
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory

    val files = new java.io.File(absoluteDirectory).listFiles.filter(_.getName.endsWith(".txt")) // context is in txt format

    for (file <- files) {
      val fileName = file.toString
      val key = uFile.getBasename(fileName)
      // todo:: fileToSummary should return a summary not a list of summaries (remove (0))
      summaries(key) = Summary(contextSummary = uFile.fileToSummary(fileName)(0), name = key)
    }
    summaries
  }
}