package smcho

import java.io.File

import core.ContextSummary

import scala.collection.mutable.{Map => mm}

object Database {

  private var summaries : mm[String, Summary] = _

  def getSummary(name: String) = {
    summaries.get(name)
  }

  def loadContexts(directory: String) = {
    summaries = Summary.loadContexts(directory)
  }

  /**
   * Given an id host, it returns the context that can be shared among participants
   * @param id
   * @return created_context
   */
  def getContextMessageToShare(id: Int, summaryType: String): ContextMessage = {
    val contextName = "summary" + id
    Database.getContextMessageToShare(contextName, summaryType)
  }

  def getContextMessageToShare(contextName: String, summaryType: String) = {
    val summary = Database.getSummary(contextName)
    if (summary == None) {
      throw new NoContextException(contextName)
    }
    val messageContexts = summaryToMessageContext(summary.get, contextName)
    messageContexts(summaryType)
  }

  /**
   * The summary itself does not contain its name
   * @param summary
   * @param name
   */
  def summaryToMessageContext(summary: Summary, name: String) = {
    val bf = new ContextMessage(name)
    bf.setSize(summary.getSizeLabeled())
    val lb = new ContextMessage(name)
    lb.setSize(summary.getSizeLabeled())

    val map = Map("bf" -> bf, "lb" -> lb)
    map
  }

  def getSummaries() = summaries
}