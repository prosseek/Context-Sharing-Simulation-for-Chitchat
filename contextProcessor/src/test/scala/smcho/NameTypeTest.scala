package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 9/12/15.
 */
class NameTypeTest extends FunSuite with BeforeAndAfterEach {

  var nameType:NameType = _
  var summaries = Summary.loadContexts(directory = "contextProcessor/src/test/resources/")

  override def beforeEach() {

  }

  test("test GetGroupId") {
    var input = "g3c0l"

    var expected = 3
    assert(NameType.getGroupId(input) == expected)
    expected = 0
    assert(NameType.getHostId(input) == expected)
    var stringExpected = "l"
    assert(NameType.getSummaryType(input) == stringExpected)
    stringExpected = "g3c0"
    assert(NameType.getName(input) == stringExpected)

    input = "xg33c0p"

    expected = -1
    assert(NameType.getGroupId(input) == expected)
    expected = -1
    assert(NameType.getHostId(input) == expected)
    stringExpected = ""
    assert(NameType.getSummaryType(input) == stringExpected)
    stringExpected = ""
    assert(NameType.getName(input) == stringExpected)
  }

  test("test constructor") {
    val n = NameType("g1c0l", null)
    assert(n.name == "g1c0")
    assert(n.groupId == 1)
    assert(n.hostId == 0)
    assert(n.summaryType == "l")
  }

  test("test constructor 2") {
    assert(nameType.summary.toString() == "g1c0|[105,52,29]")
  }

  test("test get") {
    nameType = NameType("g1c0l", summaries)
    assert(nameType.get("h") == null)
    assert(nameType.get("latitude") == (10, 10, 10, 10))

    nameType = NameType("g1c0b", summaries)
    assert(nameType.get("h") == null)
    assert(nameType.get("latitude") == (10, 10, 10, 10))

    nameType = NameType("g1c0j", summaries)
    assert(nameType.get("h") == null)
    assert(nameType.get("latitude") == (10, 10, 10, 10))
  }
}
