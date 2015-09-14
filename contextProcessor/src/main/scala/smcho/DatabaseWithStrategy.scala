package smcho

object DatabaseWithStrategy {
  def apply(strategy: String, directory:String) = new DatabaseWithStrategy(strategy, directory)
}

class DatabaseWithStrategy(val strategy: String, val directory:String) extends Database with LoadClass {
  val storage = Storage(directory)
  val shareLogic: ShareLogic = loadObject(strategy).asInstanceOf[ShareLogic]

  override def get(host: Int) : ContextMessage = {
    val summaries = shareLogic.get(host, storage)
    null
  }
  // add received ContextMessage to host
  override def add(host: Int, contextMessage: ContextMessage) = {
    storage.add(host, contextMessage)
  }

  override def getSize(nameTypesString: String): Int = {
    NameTypes.size(nameTypesString, storage.summariesMap)
  }
}