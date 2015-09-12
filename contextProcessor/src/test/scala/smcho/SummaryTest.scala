package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/21/15.
 */
class SummaryTest extends FunSuite with BeforeAndAfterEach {
  val jsonFilePath = "contextProcessor/src/test/resources/unittest.json"
  val confFilePath = "contextProcessor/src/test/resources/unittest.conf"
  var t : Summary = _

  override def beforeEach(): Unit = {
    t = new Summary(name="hello", filePath=jsonFilePath, summaryType = "b")
  }

  test("test name") {
    assert(t.name == "hello")
    t.name = "oops"
    assert(t.name == "oops")
  }

  test("test filePath") {
    assert(t.filePath == jsonFilePath)
    assert(t.confFilePath == confFilePath)
  }

  test("test summaryType") {
    assert(t.summaryType == "b")
    t.summaryType = "l"
    assert(t.summaryType == "l")
  }

  test("test getConfigurationFilePath") {
    val res = Summary.getConfigurationFilePath(jsonFilePath)
    assert(res == confFilePath)

    intercept[Exception] {
      Summary.getConfigurationFilePath("hello.json")
    }
  }

  test("test conf") {
    assert(t.conf == Map("q" -> 24, "k" -> 3, "m" -> 0, "complete" -> 0))
  }

  test("test sizes") {
    val expected = (105,52,29)
    assert(t.sizes == expected)
    t.summaryType = "b"
    assert(t.size == expected._3)
    t.summaryType = "l"
    assert(t.size == expected._2)
    t.summaryType = "j"
    assert(t.size == expected._1)
  }

  test("toString test") {
    assert(t.toString() == "hello|b|29")
  }

  test("test repr") {
    val expected = """{"filePath":"contextProcessor/src/test/resources/unittest.json", "name":"hello", "summaryType":"b", "sizes":[105,52,29]}"""
    assert(t.repr == expected)
  }

  //    m = 0
  //    k = 3
  //    q = 24
  //    complete = 0
  test ("test read property") {
    val res = Summary.readProperty(confFilePath)
    assert(res == Map("q" -> 24, "k" -> 3, "m" -> 0, "complete" -> 0))
  }
  test ("test getConf") {
    assert(t.conf == Map("q" -> 24, "k" -> 3, "m" -> 0, "complete" -> 0))
  }

  test("test loadContext") {
    var s = Summary.loadContext(directory = "contextProcessor/src/test/resources/", name = "unittest", othername = "default", summaryType = "b")
    assert(s.toString() == "unittest|b|29")
    s = Summary.loadContext(directory = "contextProcessor/src/test/resources/", name = "a", othername = "unittest", summaryType = "b")
    assert(s.toString() == "a|b|29")
    assert(s.filePath.endsWith("unittest.json"))
  }

  test("test loadContexts") {
    val res = Summary.loadContexts(directory = "contextProcessor/src/test/resources/")
    assert(res.size == 2) // there is only one context in the directory

    val res2 = Summary.loadContexts(directory = "contextProcessor/src/test/resources/", List(5,5,5))
    assert(res2.size == (5+5+5+2-1)) // default (5+5+5), +2 existing -1 (g1c0)
  }
}
