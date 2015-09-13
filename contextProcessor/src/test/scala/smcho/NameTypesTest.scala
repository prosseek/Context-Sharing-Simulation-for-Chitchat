package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 9/12/15.
 */
class NameTypesTest extends FunSuite with BeforeAndAfterEach {
  var summaries = Summary.loadContexts(directory = "contextProcessor/src/test/resources/")

  override def beforeEach() {

  }

  test("test splitNameTypes") {
    val input = "g3c14l"
    val expected = ("g3c14", "l")
    assert(NameTypes.split(input) == expected)
  }

  test("test nameSort") {
    val input = "z:z:y:x:p:a:c:d:c"
    val expected = "a:c:d:p:x:y:z"
    assert(NameTypes.nameSort(input) == expected)
  }

  test("test set/get") {

  }
}
