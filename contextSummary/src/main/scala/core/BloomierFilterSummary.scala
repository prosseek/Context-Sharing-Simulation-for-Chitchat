package core

import bloomierFilter.ByteArrayBloomierFilter
import grapevineType.BottomType._
import grapevineType._
import util.conversion.{Joiner, Util}

import net.liftweb.json._

import scala.collection.mutable.{Map => MMap}
import scala.io.Source

object BloomierFilterSummary {
  def apply(t: Map[String, Any]) : BloomierFilterSummary =
    new BloomierFilterSummary(t, 0, 0)
  def apply(t: (Map[String, Any], Int, Int)) : BloomierFilterSummary  = new BloomierFilterSummary(t._1, t._2, t._3)
  def apply(filePath:String) : BloomierFilterSummary = BloomierFilterSummary(ContextSummary.loadJsonAll(filePath))
}

/**
 * Created by smcho on 8/16/14.
 */
case class BloomierFilterSummary(jsonMap: Map[String, Any],
                            jsonSize:Int,
                            jsonCompressedSize:Int) extends GrapevineSummary(jsonMap, jsonSize, jsonCompressedSize) {

  var instance: GrapevineType = _
  var byteArrayBloomierFilter: ByteArrayBloomierFilter = _
  var k:Int = _
  var q:Int = _
  var maxTry:Int = _
  var complete:Boolean = _
  var initM:Int = _

//  def setup(json:String, m:Int, k:Int, q:Int): Unit = {
//    setup(json, m, k, q, 20, 0, false)
//  }

//  def setup(jsonFilePath:String, m:Int, k:Int, q:Int, maxTry:Int, initialSeed:Int, complete:Boolean): Unit = {
//
//    val (x,y,z) = ContextSummary.loadJsonAll(jsonFilePath)
//
//    this.jsonMap = x; this.jsonSize = y; this.jsonCompressedSize = z
//
//    val jsonMap: Map[String, GrapevineType] = this.jsonMap.asInstanceOf[Map[String, GrapevineType]]
//    setup(jsonMap, m, k, q, maxTry, initialSeed, complete)
//  }

  def setup(m:Int, k:Int, q:Int, maxTry:Int = 20, initialSeed:Int = 0, complete:Boolean = false): Unit = {
    this.initM = m
    this.k = k
    this.q = q
    this.maxTry = maxTry
    this.complete = complete

    val baMap = grapevineToByteArrayMap(getMap(), Util.getByteSize(q))
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

    map.foreach { case (key, value) =>
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
  override def toString(): String = {
    s"""{"type":"b", "complete":${if (complete) 1 else 0}, "size":${getSerializedSize()}, "jsonSize":${getJsonSize()}, "jsonCompSize":${getJsonCompressedSize()}, "n":${getN()}, "m":${getM()}, "k":${getK()}, "q":${getQ()}}"""
  }
}
