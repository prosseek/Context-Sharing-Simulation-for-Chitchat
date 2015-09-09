package smcho

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import scala.collection.mutable.{Map => mm, Set => mSet}

/**
 * Created by smcho on 8/28/15.
 */
class StorageTest extends FunSuite with BeforeAndAfterEach {

  var database: DatabaseWithStrategy = _
  var storage = Storage()
  val contentString = "summary1|b|46:summary2|l|123"
  val idString = s"0->5/100/10.01/$contentString"
  var contextMessage: ContextMessage = _

  override def beforeEach() {
    database = DatabaseWithStrategy()
    //DatabaseWithStrategy.load("contextProcessor/resources/test/sample_contexts", storage)
    contextMessage = ContextMessage(0, 5, 100, 10.01, contentString)
  }

  test("test Add") {
    storage.add(0, contextMessage)

    val contentString = "summary1|b|46:summary2|l|123"
    val idString = s"0->1/100/10.01/$contentString"
    val contextMessage2 = ContextMessage(0, 1, 100, 10.01, contentString)

    storage.add(1, contextMessage2)
    
    val h = storage.getHistoryContextMessageMap()
    val expected = Map(0 -> Set(contextMessage), 1 -> Set(contextMessage2))
    assert(h.hashCode == expected.hashCode)

    val h2 = storage.getTupleMap()
    val expected2 = Map(
      1 -> Set((0,1,100,10.01,("summary1","b",46)), (0,1,100,10.01,("summary2","l",123))),
      0 -> Set((0,1,100,10.01,("summary1","b",46)), (0,1,100,10.01,("summary2","l",123))))
    //assert(h2.hashCode == expected2.hashCode)
  }

  test("test GetSummary") {
    // Map[String, Summary]
    storage.reset
    DatabaseWithStrategy.load("contextProcessor/resources/test/sample_contexts", storage, "")
    val h3 = storage.getSummaryMap()
    val expected = "Map(summary1 -> summary1|b|46, summary2 -> summary2|b|50)"
    assert(h3.toString == expected)

    // exists test
    assert(storage.exists("summary1"))
    assert(!storage.exists("Hello1"))
  }
}
