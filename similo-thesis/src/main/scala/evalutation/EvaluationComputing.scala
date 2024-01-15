package evalutation

import elements.Element
import evalutation.EvaluationData.{DateData, ElementPair, SiteData, getSiteData}
import evalutation.EvaluationStatistics.{ElementLocalisation, MatchType, Result}
import similo.Locator
import similo.von.VonSimilo
import util.Load.getFullLoadedSuitableSites
import util.Utils.{reverseSequence, unwrapDiscard}

import java.nio.file.Path
import scala.collection.parallel.CollectionConverters.*

object EvaluationComputing:

  def computeAll(
    locator: Locator,
    paths: Seq[Path] = getFullLoadedSuitableSites,
    mapper: SiteData => SiteData = identity
  ): EvaluationStatistics =
    paths.par.map(computeSingle(locator, _, mapper)).fold(EvaluationStatistics.empty)(_ append _)

  private def computeSingle(locator: Locator, path: Path, mapper: SiteData => SiteData): EvaluationStatistics =
    EvaluationComputing(locator, path, mapper).compute()

  def firstAndLastMapper(siteData: SiteData): SiteData =
    val newDate = siteData.data.last.date
    val oldElements = siteData.data.head.oldElements
    val newElements = siteData.data.last.newElements
    val newLocations = siteData.data.head.location
      .zip(siteData.data.last.location)
      .map: (oldPair, newNew) =>
        val target = oldPair.oldElement
        val toFind = newNew.target
        val classification = ComputeClassification.classifyElements(target, toFind)
        ElementPair(target, classification, toFind)
      .filter(_.classification != Classification.deleted)
    SiteData(siteData.site, Seq(DateData(newDate, oldElements, newElements, newLocations)))

case class EvaluationComputing(locator: Locator, site: Path, mapper: SiteData => SiteData):

  def compute(): EvaluationStatistics =
    val data = mapper(getSiteData(site))
    val results = data.data.map: dateData =>
      dateData.location.map: elementData =>
        if elementData.oldElement.isEmpty then None
        else Some(determineResult(dateData, elementData))

    val resultsTransformed = reverseSequence(results).map(_.unwrapDiscard)
    // println(s"[i] Parsing statistics for $site.")
    EvaluationStatistics(resultsTransformed.map(ElementLocalisation(data.site, _)))

  private def determineResult(dateData: DateData, elementData: ElementPair): Result =
    val state = locator.init(elementData.oldElement, dateData.oldElements)
    val located = locator.locateSortedNormalized(state, dateData.newElements)
    val overlap = VonSimilo.getOverlap(elementData.target, dateData.newElements)
    Result(
      date = dateData.date,
      elementData.classification,
      toFind = elementData.oldElement,
      target = elementData.target,
      located
        .take(10)
        .map(res =>
          (
            res._2,
            determineMatchType(elementData.target, overlap, res._2),
            res._1
          )
        )
    )

  private def determineMatchType(target: Element, overlap: Seq[Element], found: Element): MatchType =
    if found.xpath.chrome == target.xpath.chrome then MatchType.ExactMatch
    else if overlap.contains(found) then MatchType.InOverlap
    else MatchType.NoMatch
