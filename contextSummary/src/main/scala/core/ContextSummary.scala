package core

import java.io.File

import util.compression.CompressorHelper
import util.conversion.ByteArrayTool

import scala.io.Source
import net.liftweb.json._

import grapevineType.BottomType.BottomType
import scala.collection.mutable.{Map => mm}

object ContextSummary {
  def loadJson(filePath:String) = {
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
abstract class ContextSummary {

  protected var jsonSize = 0
  protected var jsonCompressedSize = 0
  protected var jsonMap: Map[String, Any] = null

  def getJsonSize() = {
    jsonSize
  }
  def getJsonCompressedSize() = {
    jsonCompressedSize
  }
  def getJsonMap() = {
    jsonMap
  }
  /**
   * Returns the size of the summary
   *
   * @return
   */
  def getSize() : (Int, Int, Int);

  def getSerializedSize() : Int;

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
  def create(dict:Map[String, Any]);

  def load(filePath:String);
  def save(filePath:String);

  // def zip(): Array[Byte];
  def serialize(): Array[Byte];

  def toJsonString() : String
}
