package core

import java.io.{PrintWriter, File}

import util.compression.CompressorHelper
import util.conversion.ByteArrayTool
import util.io.File

import scala.io.Source
import net.liftweb.json._

import grapevineType.BottomType.BottomType
import scala.collection.mutable.{Map => mm}

object ContextSummary {

  def loadJson(filePath:String) = {
    val (x,y,z) = loadJsonAll(filePath)
    x
  }

  def loadJsonAll(filePath:String) = {
    implicit val formats = DefaultFormats
    def convertJsonMap(m:Map[String, Any]) = {
      def toInt(value:Any) = {
        if (value.isInstanceOf[scala.math.BigInt]) {
          value.asInstanceOf[scala.math.BigInt].toInt
        }
        else
          value
      }
      val newMap = mm[String, Any]()

      m foreach {
        case (key, value) =>
          newMap(key) = value
          if (value.isInstanceOf[scala.math.BigInt]) {
            newMap(key) = value.asInstanceOf[scala.math.BigInt].toInt
          }
          if (value.isInstanceOf[List[_]]) {
            val oldList = value.asInstanceOf[List[_]]

            if (oldList.size == 2) {
              val res = (toInt(oldList(0)), toInt(oldList(1)))
              newMap(key) = res
            }
            if (oldList.size == 3) {
              val res = (toInt(oldList(0)), toInt(oldList(1)), toInt(oldList(2)))
              newMap(key) = res
            }
            if (oldList.size == 4) {
              val res = (toInt(oldList(0)), toInt(oldList(1)), toInt(oldList(2)), toInt(oldList(3)))
              newMap(key) = res
            }
          }
      }
      newMap.toMap
    }

    // File size
    val jsonSize = new File(filePath).length.toInt
    val source = Source.fromFile(filePath).mkString("")
    val ba = ByteArrayTool.stringToByteArray(source)
    val z = CompressorHelper.compress(ba)
    val jsonCompressedSize = z.length
    val res = parse(source)
    val json = res.values.asInstanceOf[Map[String, Any]]
    val map = convertJsonMap(json)
    (map, jsonSize, jsonCompressedSize)
  }
}

/**
 * The summary contains a key -> value set.
 * The key is a string, and value is gv type
 */
abstract class ContextSummary(jsonMap: Map[String, Any], jsonSize:Int, jsonCompressedSize:Int) {

  protected var _jsonMap = jsonMap
  protected var _jsonSize =  jsonSize
  protected var _jsonCompressedSize = jsonCompressedSize

  def getJsonSize() = {
    this._jsonSize
  }
  def getJsonCompressedSize() = {
    this._jsonCompressedSize
  }
  def getJsonMap() = {
    this._jsonMap
  }
  /**
   * Returns the size of the summary
   *
   * @return
   */
  def getSize() : (Int, Int, Int)
  def getSerializedSize() : Int

  /**
   * Returns the value from the input key
   * The returned value can be null, so Option type is used.
   *
   * @param key
   * @return
   */
  def get(key:String): Any

  def check(key:String): BottomType

  /**
   * create a context summary from dictionary.
   * From the dict (Map of String -> Object), it generates a summary
   * wholeDict is used for complete dictionary
   *
   *
   * @param dict
   */
  def setup(dict:Map[String, Any])

  def load(filePath: String): Unit = {
    val (x,y,z) = ContextSummary.loadJsonAll(filePath)
    this._jsonMap = x
    this._jsonSize = y
    this._jsonCompressedSize = z
  }

  def save(filePath: String): Unit = {
    val f = new PrintWriter(new File(filePath))
    (f.write(toString()))
    f.close()
  }

  // def zip(): Array[Byte];
  def serialize(): Array[Byte]

  override def toString() : String = {

    def tupleToString(item:Any) = {
      if (item.isInstanceOf[Tuple2[_,_]]) {
        val t = item.asInstanceOf[Tuple2[_,_]]
        s"[${t._1}, ${t._2}]"
      }
      else if (item.isInstanceOf[Tuple3[_,_,_]]) {
        val t = item.asInstanceOf[Tuple3[_,_,_]]
        s"[${t._1}, ${t._2}, ${t._3}]"
      }
      else if (item.isInstanceOf[Tuple4[_,_,_,_]]) {
        val t = item.asInstanceOf[Tuple4[_,_,_,_]]
        s"[${t._1}, ${t._2}, ${t._3}, ${t._4}]"
      }
      else s"${item}"
    }

    val res = new StringBuilder
    res ++= "{\\n"
    this.jsonMap foreach {
      case (key, value) => {
        res ++= s"${key}:"
        if (value.isInstanceOf[Tuple2[_,_]] || value.isInstanceOf[Tuple3[_,_,_]] || value.isInstanceOf[Tuple4[_,_,_,_]]) {
          res ++= tupleToString(value)
        }
        else {
          res ++= s"${value}"
        }
        res ++= ",\\n"
      }
    }
    val leng = res.length
    res.toString.substring(0, leng-2) + "\\n}\\n"
  }
}
