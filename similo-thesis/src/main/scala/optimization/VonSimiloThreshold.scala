package optimization

import evalutation.EvaluationData
import similo.von.VonSimilo
import util.Load
import util.Utils.{get, getElement}

import scala.annotation.tailrec

object VonSimiloThreshold:

  @tailrec
  def computeBruteForce(max: Int, threshold: Double = 1.0): Double =
    val highest = getHighestVisualOverlapFor(threshold)
    println(s"$threshold:")
    highest.toSeq
      .sortBy(_._1)
      .foreach: (k, v) =>
        println(s"\t$k -> $v")
    if threshold < 0 then 0.0 else computeBruteForce(max, threshold - 0.01)

  private def getHighestVisualOverlapFor(threshold: Double): Map[Int, Int] =
    Load.getFullLoadedSuitableSites
      .flatMap: path =>
        val elements = Load.getAllElementsByDateFor(path)
        val xpaths = Load.getXPathsFor(path)
        xpaths
          .flatMap(date => date._2.map((date._1, _)))
          .map: (date, xpath) =>
            if xpath == "-" then 0
            else
              val dateElements = elements.get(date)
              val element = dateElements.getElement(xpath)
              VonSimilo.getOverlap(element, dateElements, threshold).size
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toMap
