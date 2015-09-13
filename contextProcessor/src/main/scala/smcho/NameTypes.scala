package smcho

import scala.collection.mutable.{Map => mm}

object NameTypes {
  val cacheMap = mm[String, Seq[NameType]]()

  def size() = cacheMap.size

  def split(names:String) = {
    names.split(":").toSet.toList
  }

  def nameSort(name: String) = {
    name.split(":").toSet.toList.sorted.mkString(":")
  }

  /**
   * Given names, return a list of
   * @param nameTypes
   * @return
   */
  def getNameTypeSeq(nameTypes:String, summaries:mm[String, Summary]) = {
    (scala.collection.mutable.ListBuffer[NameType]() /: split(nameTypes)) {
      (acc, value) => acc += NameType(value, summaries)
    }
  }

  def add(nameTypes:String, summaries:mm[String, Summary]) = {
    val sortedName = nameSort(nameTypes)
    if (!cacheMap.contains(sortedName)) {
      cacheMap(sortedName) = getNameTypeSeq(sortedName, summaries)
    }
    cacheMap(sortedName)
  }

  def set(nameTypes: String, value: Seq[NameType]) = {
    cacheMap(nameSort(nameTypes)) = value
  }

  def get(nameTypes: String) = {
    cacheMap.get(nameSort(nameTypes)) match {
      case Some(p) => p
      case None => null
    }
  }
}

/**
 * Created by smcho on 9/12/15.
 */
case class NameTypes(nameTypesString:String, summaries:mm[String, Summary]) {
  // Add nameTypes to the cache
  val nameTypeStrings = NameTypes.split(nameTypesString)
  val nameTypes: Seq[NameType] = NameTypes.add(nameTypesString, summaries)

  def size() = {
    nameTypes.size
  }

  /**
   * Returns a set of NameType objects that nameType sring represents
   */
  def get(nameTypeString:String) : NameType = {
    if (nameTypeStrings.contains(nameTypeString)) {
      nameTypes foreach { nameType =>
        //println(nameType.name)
        if (nameType.name == nameTypeString)
          return nameType
      }
      null
    }
    else
      null
  }
}
