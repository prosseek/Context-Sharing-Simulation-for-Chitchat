package core

import bloomierFilter.ByteArrayBloomierFilter
import grapevineType.BottomType._
import grapevineType._
import util.conversion.{Joiner, Util}

import net.liftweb.json._

import scala.collection.mutable.{Map => MMap}
import scala.io.Source

/**
 * Created by smcho on 8/16/14.
 */
class BloomierFilterSummary extends GrapevineSummary {
  var instance: GrapevineType = _
  var byteArrayBloomierFilter: ByteArrayBloomierFilter = _
  var k:Int = 3
  var q:Int = 4*8
  var maxTry:Int = 20
  var complete:Boolean = false
  var initM:Int = -1

//  def createFromGrapevineMap(map: Map[String, Any], m:Int, k:Int, q:Int, maxTry:Int = 20, complete:Boolean = false): Unit = {
//  }

  def create(json:String, m:Int, k:Int, q:Int): Unit = {
    create(json, m, k, q, 20, 0, false)
  }

  def create(jsonFilePath:String, m:Int, k:Int, q:Int, maxTry:Int, initialSeed:Int, complete:Boolean): Unit = {

    val (x,y,z) = ContextSummary.loadJson(jsonFilePath)

    this.jsonMap = x; this.jsonSize = y; this.jsonCompressedSize = z

    val jsonMap: Map[String, GrapevineType] = this.jsonMap.asInstanceOf[Map[String, GrapevineType]]
    create(jsonMap, m, k, q, maxTry, initialSeed, complete)
  }

  def create(map: Map[String, Any], m:Int, k:Int, q:Int, maxTry:Int = 20, initialSeed:Int = 0, complete:Boolean = false): Unit = {
    this.initM = m
    this.k = k
    this.q = q
    this.maxTry = maxTry
    this.complete = complete

    super.create(map) // any map to grapevineDataTypeMap
    val baMap = grapevineToByteArrayMap(super.getMap, Util.getByteSize(q))
    //println(!complete)
    byteArrayBloomierFilter = new ByteArrayBloomierFilter(map = baMap, initialM = m, k = k, q = q, initialSeed = initialSeed, maxTry = maxTry, complete = complete)
  }

  /**
   * Returns the size of the summary
   *
   * @return
   */
  override def getSize(): (Int, Int, Int) = {
    val s = byteArrayBloomierFilter.serialize()
    val z = util.compression.CompressorHelper.compress(s)
    (byteArrayBloomierFilter.getSize(), s.size, z.size)
  }

  /**
   *
   * @return
   */
  def getDetailedSize() = {
    byteArrayBloomierFilter.getDetailedSize()
  }

  def getM() = {
    byteArrayBloomierFilter.getM()
  }

  def getN() = {
    byteArrayBloomierFilter.getN()
  }

  /**
   *
   * @return
   */
  def getK() = {
    k
  }

  def getQ() = {
    q
  }
  /**
   * Returns the value from the input key
   * The returned value can be null, so Option type is used.
   *
   * WARNING: get should be sent after the check method
   *
   * @param key
   * @return
   *
   */
  override def get(key: String): Any = {
    // [2014/08/21] bug
    // It should call the method, not the value
    if (check(key) == NoError) instance.get()
    else None
  }

  def _get(key: String): Any = {
    instance.get()
  }

  override def check(key:String): BottomType = {
    check(key, useRelation = false)
  }

  /**
   *
   * @param key
   * @param useRelation
   * @return
   */
  def check(key: String, useRelation:Boolean = false): BottomType = {
    val j = new Joiner
    val value = j.join(byteArrayBloomierFilter, key) // Option[Array[Byte]]

    if (value.isEmpty) Bottom
    else {
      // get the data
      val t = GrapevineType.getTypeFromKey(key)
      var assumedType: Class[_] = if (t.isDefined) t.get else classOf[ByteType]
      instance = assumedType.newInstance.asInstanceOf[GrapevineType]
      val res = instance.fromByteArray(value.get)
      res
//      if (useRelation) {
//        val r = new Relation(this)
//        r.check(key)
//      } else
//        // if we don't use relation, we just return the OK/Bottom status
//        res
    }
  }

  /**
   *
   * @param filePath
   */
  override def load(filePath:String) :Unit = {
    super.load(filePath) // fill in the dataStructure
    val baMap = grapevineToByteArrayMap(super.getMap, Util.getByteSize(q))
    byteArrayBloomierFilter = new ByteArrayBloomierFilter(map = baMap, initialM = this.initM, k = k, q = q, initialSeed = 0, maxTry = maxTry, complete = complete)
  }

  /* NOT FINISHED */
  override def serialize(): Array[Byte] = {
    var ab = Array[Byte]()
    // get the contents

    dataStructure.foreach { case (key, value) =>
    }
    return ab
  }

  /**
   * todo:: the serialized size is not correct as it is missing the header size.
   * @return
   */
  override def getSerializedSize(): Int = getSize()._1

  /**
   *
   * @return
   */
  override def toJsonString(): String = {
    s"""{"type":"b", "size":${getSerializedSize()}, "n":${getN()}, "m":${getM()}, "k":${getK()}, "q":${getQ()}}"""
  }
}
