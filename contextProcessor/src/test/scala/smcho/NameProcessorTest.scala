package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
 * Created by smcho on 8/24/15.
 */
class NameProcessorTest extends FunSuite with BeforeAndAfterEach {
  val input = "is0:is1(1->2[100]):is3(3->1[434])"

  override def beforeEach() {
  }

  test ("separator test") {
    assert(
      NameProcessor(input).toSeq.toList.toString ==
        "List(Context(is0,-1,-1,0), Context(is1,1,2,100), Context(is3,3,1,434))")
  }

  test ("stringToContext test") {
    val input = "is1"
    assert(NameProcessor.stringToContext(input) == Context("is1",-1,-1,0))
    val input2 = "is132(3->4[100])"
    assert(NameProcessor.stringToContext(input2) == Context("is132", 3, 4,100))
  }

  test("getTotalSize test") {
    assert(NameProcessor(input).getTotalSize() == (0 + 100 + 434))
  }
}
