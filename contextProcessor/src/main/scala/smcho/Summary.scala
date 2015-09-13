package smcho

import java.io.File
import scala.collection.mutable.{Map => mm}
import net.liftweb.json.Serialization.{write => jsonWrite}

import util.io.{File => uFile}
import core.{BloomierFilterSummary, LabeledSummary, ContextSummary}

import java.io.FileInputStream
import java.util.Properties
import scala.collection.JavaConversions._

class Summary(var name:String, val filePath:String) {

  val confFilePath = Summary.getConfigurationFilePath(filePath)
  val conf = Summary.readProperty(confFilePath)
  val labeledSummary = LabeledSummary(filePath)
  val bloomierSummary = bloomier(labeledSummary)

  val sizeLabeled = labeledSummary.getSize()
  var sizeBloomier = bloomierSummary.getSize()
  var sizeJson = labeledSummary.getJsonSize()

  private def bloomier(labeledSummary: LabeledSummary) = {
    val bf = BloomierFilterSummary(labeledSummary)
    //val m : Int = math.ceil(labeledSummary.getKeys().length * 1.5).toInt
    bf.setup(labeledSummary.getMap(),
        m = conf("m").asInstanceOf[Int],
        k = conf("k").asInstanceOf[Int],
        q = conf("q").asInstanceOf[Int],
        complete = if (conf("complete").asInstanceOf[Int] == 1) true else false)
    bf
  }

  def size(summaryType: String) = {
    summaryType match {
      case "b" => sizeBloomier
      case "l" => sizeLabeled
      case "j" => labeledSummary.getJsonSize()
      case _ => throw new Exception(s"Format error ${summaryType}")
    }
  }
  def sizes = (sizeJson, sizeLabeled, sizeBloomier)
  def keys = labeledSummary.getKeys()

//  def copy(newSummaryType: String = "b") = {
//    new Summary(name=name, filePath=filePath, defaultSummaryType=newSummaryType)
//  }

  def repr = s"""{"name":"${name}", "sizes":[${sizes._1},${sizes._2},${sizes._3}], "filePath":"${filePath}"}"""
  override def toString() = s"${name}|[${sizes._1},${sizes._2},${sizes._3}]"
}

object Summary {

  def summariesToJsonString(summaries:mm[String, Summary]) = {
    var sb = new StringBuilder()
    sb ++= "[\n"
    summaries foreach {
      case (key, value) => sb ++= (s"""{"${key}":${value.repr},\n""")
    }
    sb.toString.dropRight(2) + "\n]"
  }

  def getConfigurationFilePath(filePath:String) = {
    val confFilePath = filePath.replace(".json", ".conf")
    if (new File(confFilePath).exists) confFilePath
    else throw new Exception(s"No configuration file ${confFilePath}")
  }

  def loadContext(directory: String, name: String, othername: String) = {
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory
    var filePath = absoluteDirectory + "/" + name + ".json"

    if (!(new File(filePath).exists())) {
      val otherFilePath = absoluteDirectory + "/" + othername + ".json"
      if (!new File(otherFilePath).exists)
        throw new Exception(s"Neither ${filePath} nor ${otherFilePath} exists")
      else
        filePath = otherFilePath
    }
    new Summary(name = name, filePath = filePath)
  }

  def loadContexts(directory: String) : mm[String, Summary] = loadContexts(directory, "b")

  def loadContexts(directory: String, summaryType:String) = {
    val summaries = mm[String, Summary]();
    // executed in one simulator, the "." is inside the one simulator directory, so there should be some changes
    // such as symbolic links should be added.
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory

    val files = new java.io.File(absoluteDirectory).listFiles.filter(_.getName.endsWith(".json")) // context is in txt format

    for (file <- files if !file.getName().replace(".json","").matches("default.*$")) {
      val fileName = file.toString
      val key = uFile.getBasename(fileName).replace(".json","")

      val groupId = 5
      summaries(key) = Summary.loadContext(directory = directory, name = key, othername = s"default${groupId}")
    }
    summaries
  }

  /**
   * g1c0 ... gNcX : N is the number of groups, X-1 is the number of hosts
   * @param directory
   * @param defaults
   * @param summaryType
   * @return
   */
  def loadContexts(directory: String, defaults:List[Int], summaryType:String = "b") : mm[String, Summary] = {
    val summaries = loadContexts(directory, summaryType)
    val absoluteDirectory = new File(".").getAbsoluteFile() + "/" + directory

    var sum = 0
    defaults.zipWithIndex foreach {
      case (count, index) => {
        for (i <- 0 until count) {
          val name = s"g${index+1}c${sum}"
          val defaultName = s"default${index+1}"
          sum += 1
          if (!summaries.contains(name)) {
            summaries(name) = loadContext(directory = directory, name = name, othername = defaultName)
          }
        }
      }
    }

    summaries
  }

  def readProperty(filePath:String) = {
    // http://stackoverflow.com/questions/9938098/how-to-check-to-see-if-a-string-is-a-decimal-number-in-scala
    def isAllDigits(x: String) = x forall Character.isDigit
    def convert(value:Any) = {
      val newValue = value.asInstanceOf[String]
      if (isAllDigits(newValue))
        newValue.toInt
      else
        newValue
    }

    val prop = new Properties()
    val input = new FileInputStream(filePath)
    val m = scala.collection.mutable.Map[String, Any]()
    prop.load(input)
    prop.keySet foreach { key =>
      val newKey = key.asInstanceOf[String]
      val value = convert(prop(newKey))
      m(newKey) = value
    }
    m.toMap
  }
}