package smcho

import java.io.File

import smcho.ContextMessage

import scala.collection.mutable.ListBuffer

object DatabaseWithStrategy {
  val contextName = "is"
  def apply(strategy: String) = new DatabaseWithStrategy(strategy)
  def apply() = new DatabaseWithStrategy("smcho.SimpleShareLogic")

  def load(directory: String, storage:Storage, hostSize:String) = {

    def getHostAddressFromSummaryName(summaryName: String): Int = {
      val idPattern = """[^\d]+(\d+)""".r
      val m = idPattern.findFirstMatchIn(summaryName)
      m match {
        case Some(s) => Integer.valueOf(s.group(1))
        case None => throw new Exception(s"not correct summary name $summaryName")
      }
    }

    /**
     * Returns in what group index belongs to items
     * For example with inputs: (1,5,8), 4
     * (1,5,8) is accumulation of group1 (1), group2 (4), group3 (3)
     * index 0 => smaller than 1, so in group 1
     * index 1 => larger than 1 smaller than 5, so in group 2
     * index 4 => likewise in group 2
     *
     * @param items
     * @param index
     */
    def getGroupIndex(items:Seq[Int], index:Int): Int = {
      items.zipWithIndex foreach { case (v, i) =>
        // println(v,i)
        if (v > index) return (i+1)
      }
      return items.size + 1 // This is an error condition
    }

    // 1. load all the contexts in a directory
    /*
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
    */

    // 2. make all the default context by copying default.txt up to the size of hostSize
    if (hostSize.length() > 0) {
      val sizes = hostSize.split(",") map {_.toInt}
      val totalSize = (0 /: sizes){(acc, value) => acc + value}
      // {1,2,3} => {1, 3, 6}
      val accumulation = (ListBuffer[Int]() /: sizes) { (acc, v) => acc += (acc.sum + v)}
      // For example with {1,2,3} => totalSize of 6
      // we iterate 0 .. 5
      Range(0, totalSize).foreach { i =>
        val fileName = contextName + i + ".txt"
        // If the context is not provided
        if (!storage.exists(fileName)) {
          // get what group 'i' is in
          val groupIndex = getGroupIndex(accumulation, i)
          var defaultGroupFilePath = directory + "/" + s"default${groupIndex}.txt"
          if (!(new File(defaultGroupFilePath).exists())) {
            defaultGroupFilePath = directory + "/" + "default.txt"
            if (!(new File(defaultGroupFilePath).exists())) {
              // No default file, raise an error to stop
              throw new Exception(s"No default file in directory ${directory}")
            }
          }
          // defaultGroupFilePath now contains the file path of default file
          val s = Summary.loadContext(directory, fileName, defaultGroupFilePath, "b")
          val contextMessage = ContextMessage(s.toString())
          val host = i
          storage.add(host, contextMessage)
          //storage.addToSummaryMap(fileName.replace(".txt",""), s)
        }
      }
    }
  }
}

class DatabaseWithStrategy(val strategy: String) extends Database with LoadClass {
  val storage = Storage()
  val shareLogic: ShareLogic = loadObject(strategy).asInstanceOf[ShareLogic]

  // <editor-fold desc="API">

  // returns ContextMassage to share for host
  override def get(host: Int) : ContextMessage = {
    val summaries = shareLogic.get(host, storage)
    val content = ContextMessage.summariesToContent(summaries)
    val c= ContextMessage(content)
    c.setSize(ContextMessage.getTotalTime(summaries))
    c
  }

  // Given a directory, load all the contexts into database
  override def load(directory: String, hostSize: String) = {
    DatabaseWithStrategy.load(directory, storage, hostSize)
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