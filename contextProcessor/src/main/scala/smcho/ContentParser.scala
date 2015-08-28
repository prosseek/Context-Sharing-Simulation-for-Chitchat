package smcho

/**
 * Created by smcho on 8/27/15.
 */
trait ContentParser {
  val DEFAULT_TYPE = "b"

  def assembleContent(name:String, summaryType:String, size:Int) = {
    s"$name|$summaryType|$size"
  }

  def parseContent(content: String) = {
    val summaryPattern = """([a-zA-Z0-9]+)(\|([b|l])\|([\d]+))?""".r
    content.split(":") map (v =>
      v match {
        case summaryPattern(name, option, summaryType, size) if (option != null) => (
          name,
          summaryType,
          size.toInt)
        case _ => throw new Exception(s"No match for summary $summaryPattern")
      })
  }
}
