package evalutation

import elements.Element
import evalutation.Classification.{ElementClassification, LocatorClassification}
import util.Load.{getAllElementsByDateFor, getSitesFor}
import util.Utils.*
import util.{Csv, Load, Utils}

import java.nio.file.{Files, Path}
import java.time.LocalDate

object ComputeClassification:

  def computeClassification(): Unit =
    getSitesFor
      .map(_.resolve("xpaths.csv"))
      .filter: file =>
        Files.exists(file)
      .foreach(computeForXpaths)

  def computeForXpaths(paths: Path): Unit =
    println(s"Computing classification for $paths")
    val folder = paths.getParent
    val elements = getAllElementsByDateFor(folder)
    val xPaths = Csv(paths)
    val columns = xPaths.columns.columns.map: column =>
      val date = LocalDate.parse(column.header)
      column.values.map(fromXpath(_, elements.get(date)))
    // classified is a list of columns, each column is a list of tuples (classification, element) for the tailing
    // columns without dates.
    val classified = columns.init.zip(columns.tail).map((bc, nc) => classifyColumns(bc, nc))
    val total = classifyInitial(columns.head) +: classified
    write(folder.resolve("classified.csv"), putTogether(total, xPaths.headers.values))

  private def putTogether(classified: Seq[Seq[(Classification, Element)]], headers: Seq[String]): String =
    headers.mkString(";") + "\n" +
      reverseSequence(classified).map(_.map(stringClassification).mkString(";")).mkString("\n")

  private def stringClassification(tuple: (Classification, Element)): String =
    s"${tuple._1}|${tuple._2.xpath.chrome}"

  private def classifyColumns(before: Seq[Element], now: Seq[Element]): Seq[(Classification, Element)] =
    before.zip(now).map((be, ne) => (classifyElements(be, ne), ne))

  def classifyElements(before: Element, now: Element): Classification =
    Classification(elementChange(before, now), locatorChange(before, now))

  private def elementChange(before: Element, now: Element): ElementClassification =
    if now.isEmpty then ElementClassification.DELETED
    else if before.equals(now) then ElementClassification.SAME
    else if before.tolerantEquals(now) then ElementClassification.MINOR_CHANGE
    else ElementClassification.MAJOR_CHANGE

  private def locatorChange(before: Element, now: Element): LocatorClassification =
    if now.isEmpty then LocatorClassification.DELETED
    else
      val sameTotalXPath = before.xpath.chrome == now.xpath.chrome
      val sameId = noneEmptyEqual(before.id, now.id)
      val sameIdXPath = before.xpath.idxpath == now.xpath.idxpath
      (sameTotalXPath, sameId, sameIdXPath) match
        case (true, _, _)          => LocatorClassification.UNCHANGED
        case (false, false, false) => LocatorClassification.LOCATOR_CHANGED
        case (false, _, _)         => LocatorClassification.XPATH_CHANGED

  private def classifyInitial(initial: Seq[Element]): Seq[(Classification, Element)] =
    initial.map((Classification.initial, _))
