package evalutation

import elements.Element
import util.Load.{getFullLoadedSuitableSites, getSitesFor}
import util.Utils.*
import util.{Load, Utils}

import java.nio.file.{Files, Path}
import java.time.LocalDate

object EvaluationData:

  def getData: Seq[SiteData] = getFullLoadedSuitableSites.map(getSiteData)

  def getSiteData(site: Path): SiteData =
    val dates = Load.getDatesFor(site)
    val xpaths = Load.getClassificationFor(site)
    val elements = Load.getAllElementsByDateFor(site)
    val pairs = dates.init.zip(dates.tail)
    SiteData(
      site.toString.split("/").last,
      pairs.map((oldDate, newDate) =>
        getDateDate(
          newDate,
          xpaths.get(oldDate),
          xpaths.get(newDate),
          elements.get(oldDate),
          elements.get(newDate)
        )
      )
    )

  private def getDateDate(
    date: LocalDate,
    oldXpaths: Seq[(Classification, String)],
    newXpaths: Seq[(Classification, String)],
    allOldElements: Seq[Element],
    allNewElements: Seq[Element]
  ): DateData =
    val oldElements = oldXpaths.map((_, xpath) => fromXpath(xpath, allOldElements))
    val classification = newXpaths.map(_._1)
    val validation = newXpaths.map(x => allNewElements.getElement(x._2))
    DateData(
      date = date,
      oldElements = allOldElements,
      newElements = allNewElements,
      location = zip(oldElements, classification, validation).map(ElementPair(_, _, _))
    )

  private def hasBeenClassified(path: Path): Boolean =
    Files.exists(path.resolve("classified.csv"))

  case class SiteData(site: String, data: Seq[DateData])

  case class DateData(date: LocalDate, oldElements: Seq[Element], newElements: Seq[Element], location: Seq[ElementPair])

  case class ElementPair(oldElement: Element, classification: Classification, target: Element)
