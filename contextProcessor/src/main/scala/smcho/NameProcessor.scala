package smcho

/**
 * Created by smcho on 8/24/15.
 */
case class Context(val contextName:String, val host1:Int, val host2:Int, val size:Int)

case class NameProcessor(val inputString: String) {

  /**
   *
   * @return Array[Context]
   */
  def toSeq() = {
    inputString.split(":") map NameProcessor.stringToContext
  }

  def getTotalSize() = {
    (0 /: toSeq) ((acc, value) => acc + value.size)
  }
}

object NameProcessor {

  /**
   * Context(is1,null,null,0)
   * Context(is132,3,4,100)

   * @param input
   * @return
   */
  def stringToContext(input:String) = {
    val pattern = """([a-zA-Z]+\d+)(\((\d+)->(\d+)\[(\d+)\]\))?""".r

    input match {
      case pattern(fileName, addresses, host1, host2, size) =>
        Context(fileName,
                if (host1 == null) -1 else host1.toInt,
                if (host2 == null) -1 else host2.toInt,
                if (size == null) 0 else size.toInt)
      case _ => throw new Exception("No match")
    }
  }
}
