package evalutation

import elements.Element
import evalutation.Classification.ElementClassification.*
import evalutation.Classification.LocatorClassification.*
import evalutation.Classification.{ElementClassification, LocatorClassification}
import evalutation.EvaluationStatistics.MatchType.ExactMatch
import evalutation.EvaluationStatistics.{BySiteStats, ElementLocalisation, LocatorStats, MatchType, Result}
import util.Compare.compareElements
import util.Utils.roundTwoDecimals

import java.time.LocalDate

case class EvaluationStatistics(results: Seq[ElementLocalisation]):

  def append(other: EvaluationStatistics): EvaluationStatistics =
    EvaluationStatistics(results ++ other.results)

  def exactMatchPercentage: Double =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then 0.0
    else comparisons.count(_.exactMatch) / comparisons.size.toDouble

  def exactMatchPercentageClassified: Map[Classification, Double] =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then Map.empty
    else
      val classified = comparisons.groupBy(_.classification)
      classified.foreach(s => println(s"   ${s._1}: ${s._2.size}"))
      classified.map: (classification, comparisons) =>
        classification -> comparisons.count(_.exactMatch) / comparisons.size.toDouble

  def score: Double =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then 0.0
    else comparisons.map(_.score).sum

  def possibleScore: Double =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then 0.0
    else comparisons.map(_.possibleScore).sum

  def matchInFound: Double =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then 0.0
    else comparisons.count(_.found.exists(_._2 == MatchType.ExactMatch)) / comparisons.size.toDouble

  def firstInOverlap: Double =
    val comparisons = results.flatMap(_.results).filter(_.existsOnNewVersion)
    if comparisons.isEmpty then 0.0
    else
      comparisons.count(e => e.found.head._2 == MatchType.ExactMatch || e.found.head._2 == MatchType.InOverlap)
        / comparisons.size.toDouble

  def printMismatchedFirstMatch(): Unit =
    printMismachted(_.filter(_._2.found.head._2 != MatchType.ExactMatch).filter(_._2.existsOnNewVersion))

  def printMismatchedMatchInTopTen(): Unit =
    printMismachted(_.filter(_._2.found.forall(_._2 != MatchType.ExactMatch)).filter(_._2.existsOnNewVersion))

  private def printMismachted(seq: Seq[(String, Result)] => Seq[(String, Result)]): Unit =
    val flat = results.flatMap: result =>
      result.results.map((result.site, _))

    val mismatched = seq(flat)
    println(s"Found ${mismatched.size} mismatched elements!")

    mismatched.zipWithIndex.foreach: (result, i) =>
      compareElements(
        site = result._1,
        oldDate = result._2.date.minusMonths(4),
        oldElement = result._2.toFind,
        newDate = result._2.date,
        target = result._2.target,
        found = result._2.found.head._1,
        saveTo = s"comparison/mismatched",
        fileName = s"${result._1}-${result._2.date}",
        result = Some(result._2)
      )
      print(s"\r${i + 1}/${mismatched.size} comparisons created.             ")
    println("\nDone!")

  def extensiveStatsBySite: Seq[BySiteStats] =
    val siteResults = results
      .groupBy(_.site)
      .map: siteResults =>
        extensiveStats(siteResults._1, siteResults._2.flatMap(_.results))
      .toSeq

    siteResults :+ extensiveStats("all", results.flatMap(_.results))

  private def extensiveStats(site: String, results: Seq[Result]): BySiteStats =
    val nonEmptyResult = results.filter(_.existsOnNewVersion)
    val numberResults = nonEmptyResult.size.toDouble
    BySiteStats(
      site = site,
      initialElements = nonEmptyResult.count(_.date == LocalDate.of(2019, 1, 1)),
      elementsOverAllVersions = nonEmptyResult.count(_.date == LocalDate.of(2023, 9, 1)),
      elementPairs = nonEmptyResult.size,
      sameLocator = nonEmptyResult.count(_.classification.locator == LocatorClassification.UNCHANGED),
      changedXpath = nonEmptyResult.count(_.classification.locator == XPATH_CHANGED),
      changedLocators = nonEmptyResult.count(_.classification.locator == LOCATOR_CHANGED),
      fitness = nonEmptyResult.map(_.score).sum,
      accuracy = nonEmptyResult.count(_.exactMatch) / numberResults,
      locatorChangeAccuracy = nonEmptyResult.count(r => r.exactMatch && r.classification.locator == LOCATOR_CHANGED) /
        nonEmptyResult.count(_.classification.locator == LOCATOR_CHANGED).toDouble,
      amongTopTen = nonEmptyResult.count(_.found.exists(_._2 == MatchType.ExactMatch)) / numberResults
    ).toPercents
  
  def extensivePerformanceStats: LocatorStats =
    val fitness = score
    val noneEmptyResults = results.flatMap(_.results).filter(_.existsOnNewVersion)
    val locatorChange = noneEmptyResults.filter(_.classification.locator == LOCATOR_CHANGED)
    LocatorStats(
      accuracy = roundTwoDecimals(exactMatchPercentage * 100),
      fitness = fitness,
      fitnessPercent = roundTwoDecimals((fitness / possibleScore) * 100),
      topTen = roundTwoDecimals(matchInFound * 100),
      foundOverlap = roundTwoDecimals(this.firstInOverlap * 100),
      locatorBreakFound = roundTwoDecimals((locatorChange.count(_.exactMatch)
        / locatorChange.size.toDouble) * 100)
    )

object EvaluationStatistics:

  case class LocatorStats(
                           accuracy: Double,
                           fitness: Double,
                           fitnessPercent: Double,
                           topTen: Double,
                           foundOverlap: Double,
                           locatorBreakFound: Double
                         )


  def empty: EvaluationStatistics = EvaluationStatistics(Seq.empty)

  def classificationScore(classification: Classification): Double = classification match
    case Classification(SAME, UNCHANGED)               => 0
    case Classification(SAME, XPATH_CHANGED)           => 3
    case Classification(SAME, LOCATOR_CHANGED)         => 6
    case Classification(MINOR_CHANGE, UNCHANGED)       => 2
    case Classification(MINOR_CHANGE, XPATH_CHANGED)   => 6
    case Classification(MINOR_CHANGE, LOCATOR_CHANGED) => 12
    case Classification(MAJOR_CHANGE, UNCHANGED)       => 4
    case Classification(MAJOR_CHANGE, XPATH_CHANGED)   => 12
    case Classification(MAJOR_CHANGE, LOCATOR_CHANGED) => 24
    case _                                             => 0

  case class BySiteStats(
    site: String,
    initialElements: Int,
    elementsOverAllVersions: Int,
    elementPairs: Int,
    sameLocator: Int,
    changedXpath: Int,
    changedLocators: Int,
    fitness: Double,
    accuracy: Double,
    locatorChangeAccuracy: Double,
    amongTopTen: Double
  ):
    def csv: String = this.productIterator.mkString(",")

    def toPercents: BySiteStats = copy(
      fitness = roundTwoDecimals(fitness * 100),
      accuracy = roundTwoDecimals(accuracy * 100),
      locatorChangeAccuracy = roundTwoDecimals(locatorChangeAccuracy * 100),
      amongTopTen = roundTwoDecimals(amongTopTen * 100)
    )

  case class ElementLocalisation(
    site: String,
    results: Seq[Result]
  )

  case class Result(
    date: LocalDate,
    classification: Classification,
    toFind: Element,
    target: Element,
    found: Seq[(Element, MatchType, Double)]
  ):

    def exactMatch: Boolean = found.headOption match
      case Some(found) if !target.isEmpty => found._2 == ExactMatch
      case _                              => false

    def isForDeletedElement: Boolean = toFind.isEmpty

    def existsOnNewVersion: Boolean = !target.isEmpty

    def score: Double = matchScore * classificationScore //  * entropy

    private def matchScore: Double =
      if found.head._2 == MatchType.ExactMatch then 4
      else if found.head._2 == MatchType.InOverlap then 1
      else 0

    def classificationScore: Double = EvaluationStatistics.classificationScore(classification)

    def possibleScore: Double = 4 * classificationScore

    private def entropy: Double =
      val values = found.map(_._3).filter(_ > 0.0)
      val sum = values.sum
      val factor = 1.0 / sum
      val entropy = values.map(_ * factor).map(x => x * Math.log(x)).sum * -1
      entropy * -1 + 3.3

  enum MatchType:
    case ExactMatch, InOverlap, NoMatch

  object BySiteStats:
    def header: String = "site,initialElements,elements,elementPairs,sameLocator,changedXpath," +
      "changedLocators,fitness,accuracy,locatorChangeAccuracy,amongTopTen"
