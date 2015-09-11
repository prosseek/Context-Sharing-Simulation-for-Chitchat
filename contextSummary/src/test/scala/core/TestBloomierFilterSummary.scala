package core

import java.io.File

import grapevineType.{GrapevineType, BottomType}
import grapevineType.BottomType._
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * Created by smcho on 8/16/14.
 */
class TestBloomierFilterSummary extends FunSuite with BeforeAndAfter {
  var message = "hello, world?"
  var t: BloomierFilterSummary = _
  var map1: Map[String, Any] = Map("a_f" -> 10.0, "message" -> "hi", "c count" -> 20, "d count" -> 30)

  def test(width:Int, map:Map[String, Any]) = {
    t.setup(m = 8, k = 3, q = 8*width)
    if (t.check("latitude") == NoError)
      assert(t.get("latitude") == (10,10,10,10))
    if (t.check("time") == NoError)
      assert(t.get("time") == (11,11))
    if (t.check("date") == NoError)
      assert(t.get("date") == (2014,10, 1))

    println(s"width = ${width}, ${t.getSizes}, ${t.getSize}")
  }

  def getMap(message:String) = {
    var map: Map[String, Any] = Map("latitude" ->(10, 10, 10, 10),
      "message" -> message, // , world?",
      "time" ->(11, 11),
      "date" ->(2014, 10, 1))
    map
  }

  before {
    t = BloomierFilterSummary(getMap("Hello, world"))
  }
  test ("Size test") {
    // map1 size
    t.setup(m = 8, k = 3, q = 8*4)
    val expectedSize = t.getM()/8 + 4 * 4
    println(t.getSizes())
    assert(t.getSizes()._1 == expectedSize)
  }
  test ("Simple") {
    t.setup(m = 6, k = 3, q = 8*4)
    if (t.check("a_f") == NoError) {
      assert(t.get("a_f") == 10.0)
    }
  }

  test ("test size from Map") {
    message = "Hello, world"
    test(1, getMap(message))
    test(2, getMap(message))
    test(3, getMap(message))
    test(4, getMap(message))
    test(5, getMap(message))
    test(6, getMap(message))
    test(7, getMap(message))
    test(8, getMap(message))

    val ls = LabeledSummary(getMap("Hello, world"))
    ls.setup(dict = getMap(message))
    println(s"Labeled - ${ls.getSizes}")
  }

  test ("test size from Json") {
    val (map, jsonSize, jsonCompressedSize) = ContextSummary.loadJsonAll("contextSummary/src/test/scala/resource/unittest.json")
    test(1, map)
    test(2, map)
    test(3, map)
    test(4, map)
    test(5, map)
    test(6, map)
    test(7, map)
    test(8, map)

    println(s"json - ${jsonSize}-${jsonCompressedSize}")
  }

  test ("load") {
    val ls = t
    ls.load("experiment/data/sample_context.txt")
    assert(ls.check("abc") == BottomType.NoError)
    assert(ls.get("abc") == "Hello, world")
    assert(ls.check("recommendation") == BottomType.NoError)
    assert(ls.get("recommendation") == "Chef")
    assert(ls.check("level of recommendation") == BottomType.NoError)
    assert(ls.get("level of recommendation") == 5)
    assert(ls.check("level of recommendations") != BottomType.NoError)
  }
  test ("store") {
    val filePath = "experiment/data/save_test_context.txt"
    val ls = t
    val file = new java.io.File(filePath)
    if (file.exists()) {
      file.delete()
    }
    val m = Map[String, Any]("a" -> "helloa", "level of a" -> 3)
    ls.setup(m)
    ls.save(filePath)

    ls.load(filePath)
    assert(ls.check("a") == BottomType.NoError)
    assert(ls.get("a") == m("a"))
    assert(ls.check("level of a") == BottomType.NoError)
    assert(ls.get("level of a") == m("level of a"))
  }

  test("test json") {
    t.setup(m = 8, k = 3, q = 8*4)
    val expectedSize = t.getM()/8 + 4 * 4
//    assert(t.toJsonString() == """{"type":"b", "complete":0, "size":17, "jsonSize":0, "jsonCompSize":0, "n":4, "m":8, "k":3, "q":32}""")
  }

  test("test json read") {
//    t.setup("contextSummary/src/test/scala/resource/simple.json", m = 8, k = 3, q = 8 * 4)
//    assert(t.getSize() == (21,23,23))
//
//    assert(t.getJsonSize() == 55)
//    assert(t.getJsonCompressedSize() == 47)
  }

  test("test json read2") {
//    t.setup("contextSummary/src/test/scala/resource/simple2.json", m = 8, k = 3, q = 8 * 4)
//    assert(t.getSize() == (67,68,68))
//    assert(t.getJsonSize() == 232)
//    assert(t.getJsonCompressedSize() == 168)
  }
}
