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

  // todo:: no error checking when the directory does not exist
  def load(contexts: Seq[String]) = { // directoryName: String, hostName: String) = {
    new ContextMessage(1, 2, "hello")
  }
}