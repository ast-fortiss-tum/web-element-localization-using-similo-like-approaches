package scrape

import elements.Element
import util.Load.getXPathsFor
import util.Utils.get
import util.{Draw, Load, Utils}

import java.time.LocalDate

object VisualSelectionValidation:

  def create(site: String): Unit =
    Utils.createDirs(s"comparison/$site/verify/")
    val elements = Load.getAllElementsByDateFor(site)
    getXPathsFor(site).foreach: (date, xpaths) =>
      create(site, date, xpaths, elements)

  private def create(
    site: String,
    date: LocalDate,
    xpaths: Seq[String],
    elements: Seq[(LocalDate, Seq[Element])]
  ): Unit =
    val raw = Draw(s"sites/$site/${date.toString}/screenshot.png")
    getElementsForXPaths(date, xpaths, elements)
      .foldLeft(raw)((draw, element) => draw.highlightElementAndNumber(element))
      .write(s"comparison/$site/verify/$date.png")

  private def getElementsForXPaths(
    date: LocalDate,
    xpaths: Seq[String],
    elements: Seq[(LocalDate, Seq[Element])]
  ): Seq[(Int, Element)] =
    val dateElements = elements.get(date)
    xpaths.zipWithIndex
      .filter(_._1 != "-")
      .map(xp =>
        (
          xp._2 + 1,
          dateElements
            .find(e => e.xpath.equals(xp._1))
            .getOrElse(throw new NoSuchElementException(s"Could not find element for xpath: ${xp._1} in $date"))
        )
      )

  def create(site: String, date: LocalDate): Unit =
    Utils.createDirs(s"comparison/$site/verify/")
    val elements = Load.getAllElementsByDateFor(site)
    val xpathElements = getXPathsFor(site).get(date)
    create(site, date, xpathElements, elements)
