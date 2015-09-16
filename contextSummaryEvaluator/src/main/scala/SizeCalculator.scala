package smcho

import java.io.{PrintWriter, File}

import core.{CompleteSummary, BloomierFilterSummary, LabeledSummary}

/**
 * Created by smcho on 9/16/15.
 */
class SizeCalculator(val filePath:String) {

  def getCompletSize() = {
    val cl = CompleteSummary(filePath)
    cl.getSize()
  }

  def getJsonSize() = {
    val lb = LabeledSummary(filePath)
    val sizes = lb.getSizes()
    Array(lb.getJsonSize(), lb.getJsonCompressedSize())
  }

  def getLabeledSize() = {
    val lb = LabeledSummary(filePath)
    val sizes = lb.getSizes()
    Array(sizes._2, sizes._3)
  }

  def getBfSize(width:Int, complete:Boolean) = {
    val bf = BloomierFilterSummary(filePath)
    bf.setup(m = 0, k = 3, q = 8*width, complete = complete)
    bf.getSize()
  }

  def getResultInJson(jsonFilePath:String) = {

    def iterableToJsonList(i:Iterable[Int]) = {
      val sb = ("[" /: i) { (acc, value) =>
        acc + value.toString + ","
      }
      sb.dropRight(1) + "]"
    }

    val complete = getCompletSize()
    val label = getLabeledSize()
    val json = getJsonSize()

    val bf = new Array[Int](10)
    val bf_complete = new Array[Int](10) // ignore index 0, make it start from 1
    1.until(11) foreach {i =>
      bf_complete(i-1) = getBfSize(i, true)
    }
    1.until(11) foreach {i =>
      bf(i-1) = getBfSize(i, false)
    }

    val jsonString = s"""{"c":${complete}, "l":${iterableToJsonList(label)}, "j":${iterableToJsonList(json)}, "fbf":${iterableToJsonList(bf)}, "cbf":${iterableToJsonList(bf_complete)}}"""
    val pw = new PrintWriter(new File(jsonFilePath))
    pw.write(jsonString)
    pw.close()
  }
}
