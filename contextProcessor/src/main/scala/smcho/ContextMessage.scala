package smcho

import java.io.File
import java.nio.file.{Files, Paths}

import core.ContextSummary

/** A database that stores contexts exchanged
  *
  * @constructor create a database with owner id
  */

class ContextMessage(val fromAddress: Int, val toAddress: Int, val id: String) {
  var size = 0;
  var contextSummaries : Set[ContextSummary] = _

  def setSize(size: Int) = this.size = size;
  def getSize() = this.size;
  def getMessage() = this.id
  def getFromAddress() = this.fromAddress
  def getToAddress() = this.toAddress
  def getId() = ""
  override def toString() = s"($fromAddress => $toAddress):$id"
}

object ContextMessage {

  val contextDirectory: String = "contexts"

  def create(host1: Int, host2: Int, message : String) =
  {
    new ContextMessage(fromAddress = host1, toAddress = host2, id = message)
  }

  /**
   * Load ContextSummaries from a directory
   *
   * The contexts should be in
   *     #workingdirectory/directoryName/hostName
   * Or
   *     absolutePath/hostName
   *
   * The hostNames are number of the hostid
   *
   * @param directoryName
   * @param hostName
   * @return
   */
  // todo:: no error checking when the directory does not exist
  def load(directoryName: String, hostName: String) = {
    var basedirectory : String = ""
    // todo:: file path name processing - "/" may not work with Windows
    if (directoryName.startsWith("/")) {
      // absolute path
      basedirectory = directoryName
    }
    else {
      // relative path
      basedirectory = new File(".").getAbsolutePath() + "/" + ContextMessage.contextDirectory + "/" + directoryName;
    }
    val contextDirectory = basedirectory + "/" + hostName
    if (Files.exists(Paths.get(contextDirectory))) {
      println("YES")
    }

    new ContextMessage(1, 2, "hello")
  }
}