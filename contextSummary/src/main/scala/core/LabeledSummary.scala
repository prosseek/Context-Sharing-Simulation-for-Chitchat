package core

import grapevineType.BottomType._
import grapevineType.GrapevineType
import util.conversion.Util
import util.conversion.ByteArrayTool._
import util.compression.CompressorHelper._
/**
 * Created by smcho on 8/10/14.
 */

object LabeledSummary {
  def apply(t: Map[String, Any]) : LabeledSummary =
    new LabeledSummary(t, 0, 0)
  def apply(t: (Map[String, Any], Int, Int)) : LabeledSummary  = new LabeledSummary(t._1, t._2, t._3)
  def apply(filePath:String) : GrapevineSummary = LabeledSummary(ContextSummary.loadJsonAll(filePath))
}

case class LabeledSummary(jsonMap: Map[String, Any],
                     jsonSize:Int,
                     jsonCompressedSize:Int) extends GrapevineSummary(jsonMap, jsonSize, jsonCompressedSize) {

  def getKeys(): List[String] = {
    getMap().keySet.toList
  }

  // this is the size in bytes
  def getTheorySize(): Int = {
    (0 /: map) { (acc, value) => acc + value._2.getSize } + // sum value size
    (0 /: map.keys) {(acc, value) => acc + value.length}     // sum of keys
    // dataStructure.size     // 1 byte is used for identifying the type
  }

  override def getSize() = {
    val serial = serialize()
    val compressed = compress(serial)

    (getTheorySize(), serial.length, compressed.length)
  }

  override def getSerializedSize() = getTheorySize()

//  def getCompleteSize(): Int = {
//    def log2(x : Double) = {
//      math.log10(x)/math.log10(2.0)
//    }
//    val size1 = (0 /: dataStructure) { (acc, value) => acc + value._2.getSize }
//    val size2 = Util.getByteSizeFromSize(math.ceil(dataStructure.size * log2(dataStructure.size.toDouble)).toInt)
//    size1 + size2
//  }

  override def get(key: String): Any = {
    val r = getValue(key)
    if (r.nonEmpty)
    //MMap[String, Tuple2[GrapevineType, Object]]()
      r.get
    else
      None
      //throw new RuntimeException(s"No matching value for key ${key}")
  }

  override def check(key: String): BottomType = {
    if (getValue(key).isEmpty) Bottom // this is structural check to return Buttom
    else NoError
  }

  override def serialize(): Array[Byte] = {
    var ab = Array[Byte]()
    // get the contents

    // KEY_STRING + 0 + SIZE_OF_BYTES + VALUE_AS_BYTE_ARRAY
    map.foreach { case (key, value) =>
      val byteArrayValue = value.toByteArray()
      val id = value.getId()
      ab ++= (stringToByteArray(key) ++ Array[Byte](id.toByte) ++ byteArrayValue)
    }
    ab
  }

  override def toString(): String = {
    s"""{"type":"l", "size":${getSerializedSize()}, "jsonSize":${getJsonSize()}, "jsonCompSize":${getJsonCompressedSize()}"""

  }
}