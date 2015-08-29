package smcho

object DatabaseWithStrategy {
  def apply(strategy: String) = new DatabaseWithStrategy(strategy)
  def apply() = new DatabaseWithStrategy("smcho.SimpleShareLogic")

  def load(directory: String, storage:Storage) = {

    def getHostAddressFromSummaryName(summaryName: String): Int = {
      val idPattern = """[^\d]+(\d+)""".r
      val m = idPattern.findFirstMatchIn(summaryName)
      m match {
        case Some(s) => Integer.valueOf(s.group(1))
        case None => throw new Exception(s"not correct summary name $summaryName")
      }
    }

    val summaries = Summary.loadContexts(directory)

    // from the loaded contexts, setup the initial contexts into the history
    summaries foreach {
      case (summaryName, summary) => {
        val host = getHostAddressFromSummaryName(summaryName)
        val contextMessage = ContextMessage(summary.toString())
        storage.add(host, contextMessage)
        storage.addToSummaryMap(summaryName, summary)
      }
    }
  }
}

class DatabaseWithStrategy(val strategy: String) extends Database with LoadClass {

  var storage = Storage()
  val shareLogic: ShareLogic = loadObject(strategy).asInstanceOf[ShareLogic]

  // <editor-fold desc="API">

  // returns ContextMassage to share for host
  override def get(host: Int) : Set[Summary] = {
    shareLogic.get(host, storage)
  }

  // Given a directory, load all the contexts into database
  override def load(directory: String) = {
    DatabaseWithStrategy.load(directory, storage)
  }

  // add received ContextMessage to host
  override def add(host: Int, contextMessage: ContextMessage) = {
    storage.add(host, contextMessage)
  }

  override def reset() = {
    storage.reset()
  }
  // </editor-fold>
  //
  override def getStorage(): Storage = storage
}