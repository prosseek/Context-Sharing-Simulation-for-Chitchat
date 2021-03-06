package util.measure

import java.awt.Label

import core.{LabeledSummary, BloomierFilterSummary, GrapevineSummary, CompleteSummary}
import util.io.File
import util.compression._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
 * Created by smcho on 8/21/14.
 */
object Summary {
  def getSizeNormalDetail(summary:GrapevineSummary, byteWidth:Int) = {
    val bfs = BloomierFilterSummary(summary.getMap)
    bfs.setup(m = -1, k = 3, q = byteWidth*8, complete=false)
    val x = bfs.getDetailedSize()
    bfs.setup(m = -1, k = 3, q = byteWidth*8, complete=true)
    val y = bfs.getDetailedSize()
    (x, y)
  }

  def getSize(summary:GrapevineSummary, byteWidth:Int, complete:Boolean) = {
    val bfs = BloomierFilterSummary(summary.getMap)
    bfs.setup(m = -1, k = 3, q = byteWidth*8, complete=complete)
    bfs.getSizes()
//    val serial = bfs.serialize()
//    val compressed = CompressorHelper.compress(serial)
//    (bfs.getSize, serial.size, compressed.size)
  }

  def getSizeNormal(summary:GrapevineSummary, byteWidth:Int) = {
    getSize(summary, byteWidth, complete=false)
  }
  def getSizeComplete(summary:GrapevineSummary, byteWidth:Int) = {
    getSize(summary, byteWidth, complete=true)
  }

  def getSizeAverage(summary:GrapevineSummary, byteWidth:Int, complete:Boolean, count:Int) = {
    val bfs = BloomierFilterSummary(summary.getMap)
    val sizes = ArrayBuffer[Int]()
    (0 until count).foreach { c =>
      bfs.setup(m = -1, k = 3, q = byteWidth * 8, initialSeed = c, complete = complete)
      sizes += bfs.getSizes._1
    }
    sizes.sum.toFloat / sizes.size
  }

  def getSizeNormalAverage(summary:GrapevineSummary, byteWidth:Int, count:Int=10) = {
    getSizeAverage(summary = summary, byteWidth = byteWidth, complete = false, count = count)
  }

  def getSizeCompleteAverage(summary:GrapevineSummary, byteWidth:Int, count:Int=10) = {
    getSizeAverage(summary = summary, byteWidth = byteWidth, complete = true, count = count)
  }

  def getSizes(summaryPath:String, start:Int = 1, stop:Int = 10) :Array[Array[AnyVal]] = {
    val labeledSummary = LabeledSummary(summaryPath)
    val completeSummary = CompleteSummary(summaryPath)
    //completeSummary.setDataStructure(summary)

    val listCount = (start to stop).toList
    var listBFNormal = ListBuffer[Float]()
    var listBFComplte = ListBuffer[Float]()

    var listBFSerialNormal = ListBuffer[Float]()
    var listBFSerialComplte = ListBuffer[Float]()

    var listBFZipSerialNormal = ListBuffer[Float]()
    var listBFZipSerialComplte = ListBuffer[Float]()

    val labeledSize = labeledSummary.getSizes()
    listCount.foreach { i =>
      val sizeNormal = getSizeNormal(labeledSummary, i)
      val sizeComplete = getSizeComplete(labeledSummary, i)

      listBFNormal += sizeNormal._1 //getSizeNormalAverage(summary, i)
      listBFComplte += sizeComplete._1 //getSizeCompleteAverage(summary, i)

      listBFSerialNormal += sizeNormal._2 //getSizeNormalAverage(summary, i)
      listBFSerialComplte += sizeComplete._2 //getSizeCompleteAverage(summary, i)

      listBFZipSerialNormal += sizeNormal._3 //getSizeNormalAverage(summary, i)
      listBFZipSerialComplte += sizeComplete._3 //getSizeCompleteAverage(summary, i)
    }
//    listCount.foreach { i =>
//      println(s"${summaryPath}/${getSizeNormalDetail(summary, i)}")
//    }
    val totalSize = stop - start + 1
    val listLabeled = List.fill(totalSize)(labeledSummary.getSizes()._1)
    val listSerialLabeled = List.fill(totalSize)(labeledSummary.getSizes()._2)
    val listZipSerialLabeled = List.fill(totalSize)(labeledSummary.getSizes()._3)

    val listComplete = List.fill(totalSize)(completeSummary.getSizes()._1)
    val listSerialComplete = List.fill(totalSize)(completeSummary.getSizes()._2)
    val listZipSerialComplete = List.fill(totalSize)(completeSummary.getSizes()._3)

    Array[Array[AnyVal]](
      listCount.toArray,

      listLabeled.toArray,
      listSerialLabeled.toArray,
      listZipSerialLabeled.toArray,

      listBFNormal.toArray,
      listBFSerialNormal.toArray,
      listBFZipSerialNormal.toArray,

      listBFComplte.toArray,
      listBFSerialComplte.toArray,
      listBFZipSerialComplte.toArray,

      listComplete.toArray,
      listSerialComplete.toArray,
      listZipSerialComplete.toArray
    )
  }
//
//  def getSizesSerialized(summaryPath:String, start:Int = 1, stop:Int = 10) :Array[Array[AnyVal]] = {
//    val summaries = File.fileToSummary(summaryPath)
//    val summary = summaries(0)
//
//    val listCount = (start to stop).toList
//    var listBFNormal = ListBuffer[Float]()
//    var listBFComplte = ListBuffer[Float]()
//
//    val labeledSize = summary.getSize()
//    listCount.foreach { i =>
//      listBFNormal += getSizeNormal(summary, i) //getSizeNormalAverage(summary, i)
//      listBFComplte += getSizeComplete(summary, i) //getSizeCompleteAverage(summary, i)
//    }
//    //    listCount.foreach { i =>
//    //      println(s"${summaryPath}/${getSizeNormalDetail(summary, i)}")
//    //    }
//    val totalSize = stop - start + 1
//    val listLabeled = List.fill(totalSize)(summary.getSize())
//    val listComplete = List.fill(totalSize)(summary.getCompleteSize())
//
//    Array[Array[AnyVal]](
//      listCount.toArray,
//      listBFNormal.toArray,
//      listLabeled.toArray,
//      listBFComplte.toArray,
//      listComplete.toArray)
//  }
}
