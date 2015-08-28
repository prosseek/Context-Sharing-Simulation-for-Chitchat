package smcho

import org.scalatest.FunSuite

/**
 * Created by smcho on 8/27/15.
 */

class T extends ContentParser

class ContentParserTest extends FunSuite {

  test("testParseContent") {
    val t = new T
    val idString = "summary1|b|33:summary2|l|123"
    val res = t.parseContent(idString)
    res foreach { v =>
      v._1 match {
        case "summary1" => assert(v == ("summary1","b",33))
        case "summary2" => assert(v == ("summary2","l",123))
        case _ => None
      }
    }
  }

  test("test ParseContent wrong format") {
    val t = new T
    val idString = "summary1"
    intercept[java.lang.Exception] {
      val res = t.parseContent(idString)
    }
  }
}
