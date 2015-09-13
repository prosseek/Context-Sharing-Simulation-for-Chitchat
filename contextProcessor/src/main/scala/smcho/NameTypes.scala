package smcho

import scala.collection.mutable.{Map => mm}

object NameTypes {
  val map = mm[String, NameTypes]()

  def nameSort(name: String) = {
    name.split(":").toSet.toList.sorted.mkString(":")
  }

  def split(name: String) = {
    (name.dropRight(1), name.takeRight(1))
  }

  def set(nameTypes: String, value: NameTypes) = {
    map(nameSort(nameTypes)) = value
  }

  def get(nameTypes: String) = {
    map.get(nameSort(nameTypes)) match {
      case Some(p) => p
      case None => null
    }
  }
}

/**
 * Created by smcho on 9/12/15.
 */
case class NameTypes(nameTypes:String, summaries:mm[String, Summary]) {

}
