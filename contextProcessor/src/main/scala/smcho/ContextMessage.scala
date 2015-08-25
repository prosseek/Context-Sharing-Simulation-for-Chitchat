package smcho

import java.io.File
import java.nio.file.{Files, Paths}

import core.ContextSummary

/** ContextMessage is a class to transform between a set of contexts to Message (Simulator)
  *
  * @constructor create a database with owner id
  */

case class ContextMessage(var host1: Int, var host2: Int, var size:Int, var time: Double, val id: String) {
  def this(id:String) = this(-1, -1, 0, 0.0, id)

  def setSize(size: Int) = this.size = size
  def setTime(time: Double) = this.time = time
  def setHost1(host: Int) = this.host1 = host
  def setHost2(host: Int) = this.host2 = host

  def getSize() = this.size
  def getId() = this.id
  def getTime() = this.time
  def getHost1() = this.host1
  def getHost2() = this.host2
  override def toString() = s"($host1 => $host2)[$size]:$id"
}

//object ContextMessage {
//  def create(host1: Int, host2: Int, size:Int, id : String) =
//  {
//    new ContextMessage(host1 = host1, host2 = host2, size = size, id = id)
//  }
//}