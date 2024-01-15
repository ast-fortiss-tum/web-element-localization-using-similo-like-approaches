package scrape

import elements.Element
import io.circe.*
import io.circe.generic.auto.*
import similo.legacy.LegacySimilo
import util.Utils.{get, read, write}
import util.{Csv, Draw, Load, Utils}

import java.nio.file.{Path, Paths}
import java.time.LocalDate
import scala.annotation.tailrec
import scala.io.StdIn.readLine

/**
 * This class is used to categorize specific elements on a website over the evolution of the website.
 *
 * @param site The name of the website.
 * @param folder The file to save the elements to.
 * @param allElements The elements that have already been scraped.
 */
case class SetElementsInteractive(site: String, folder: Path, allElements: Seq[(LocalDate, Seq[Element])]):

  private val urls: Map[LocalDate, String] = Load.getUrlsFor(folder)
  private val file: Path = folder.resolve("xpaths.csv")

  def run(): Unit =
    if !initializedCsv then
      val initialXPaths = readInInitialXPaths().map(e => allElements.head._2.find(_.xpath.equals(e)).get)
      initialCsv(allElements.head._1, initialXPaths)
    VisualSelectionValidation.create(site, allElements.head._1)
    classifyOverTime()

  @tailrec
  private def classifyOverTime(): Unit =
    val (oldDate, oldXpaths) = getLastCsv
    allElements.find(_._1.isAfter(oldDate)) match
      case Some((newDate, newElements)) =>
        val oldElements = oldXpaths.map: xpath =>
          if xpath.trim.toLowerCase == "-" then Element.empty
          else
            allElements
              .get(oldDate)
              .find(_.xpath.equals(xpath))
              .getOrElse(throw Exception(s"Could not find $xpath in $oldDate"))
        val classified = classifyNewElements(newDate, oldElements, newElements)
        appendCsv(newDate, classified)
        VisualSelectionValidation.create(site, newDate)
        readLine("[i] Please have a look at the new validation and press enter to continue.")
        classifyOverTime()
      case None => ()

  private def getLastCsv: (LocalDate, Seq[String]) =
    val csv = Csv(file).columns.last
    (LocalDate.parse(csv.header), csv.values)

  private def appendCsv(date: LocalDate, elements: Seq[Element]): Unit =
    val csv = read(file).split("\n")
    val newCsv = csv.zip(date.toString +: elements.map(_.xpath.chrome)).map(_ + ";" + _).mkString("\n")
    write(file, newCsv)

  private def initialCsv(date: LocalDate, elements: Seq[Element]): Unit =
    val csv = s"$date\n${elements.map(_.xpath.chrome).mkString("\n")}"
    write(file, csv)

  private def initializedCsv: Boolean =
    val csv = read(file)
    csv.nonEmpty && csv.contains("\n")

  private def classifyNewElements(date: LocalDate, oldElements: Seq[Element], newElements: Seq[Element]): Seq[Element] =
    val preLocate = oldElements.map: oldElement =>
      if oldElement.isEmpty then Left(oldElement)
      else
        canBeFound(oldElement, newElements) match
          case Some(value) => Left(value)
          case None        => Right(oldElement)

    val notFound = preLocate.filter(_.isRight).map(_.getOrElse(throw Exception("This should not happen")))
    if notFound.nonEmpty then
      val datePrior = getDatePrior(date)
      val screenshot = notFound.foldLeft(Draw(site, datePrior))(_.highlightElementAndNumber(_))
      screenshot.write(s"comparison/$site/$site-$date.png")
      println(s"[i] The elements to classify are on ${urls(date)}.")
      println(s"[i] Please classify the elements in comparison/$site-$datePrior.png.")
      println(s"[i] Its ${notFound.size} elements in total. Enter to continue.")
      notFound.zipWithIndex.foreach: (element, index) =>
        println(s"[i] ${index + 1}. ${element.xpath.chrome}")
      val collector = ClipboardCollector()
      readDeleteValue(collector)
      val collected = collector
        .finish()
        .map: xpath =>
          if xpath.trim.toLowerCase.startsWith("delete") then Element.empty
          else newElements.find(_.xpath.equals(xpath)).getOrElse(throw Exception(s"Could not find $xpath in $date"))
      merge(preLocate, collected)
    else preLocate.map(_.swap.getOrElse(throw Exception("This should not happen")))

  @tailrec
  private def readDeleteValue(collector: ClipboardCollector): Unit =
    readLine().trim match
      case "" => ()
      case _ =>
        collector.append("delete")
        readDeleteValue(collector)

  private def getDatePrior(date: LocalDate): LocalDate =
    allElements.findLast(_._1.isBefore(date)).map(_._1).getOrElse(throw Exception("This should not happen"))

  private def merge(preSelect: Seq[Either[Element, Element]], xpaths: Seq[Element]): Seq[Element] =
    preSelect match
      case head +: tail if head.isLeft =>
        head.swap.getOrElse(throw Exception("This should not happen"))
          +: merge(tail, xpaths)
      case head +: tail if head.isRight =>
        xpaths.head
          +: merge(tail, xpaths.tail)
      case _ => Seq.empty

  private def canBeFound(oldElement: Element, newElements: Seq[Element]): Option[Element] =
    LegacySimilo.normalizedLocate(oldElement, newElements).headOption.filter(_._1 > 0.45).map(_._2)

  /**
   * Reads in the initial xPaths from the user. The user is asked to visit the wayback machine url and copy the xPaths
   * of the elements he wants to categorize. The program then checks if the xPaths are valid and if not asks the user
   * to input the correct xPath. The user can also input 'delete' to delete the element. The user can stop the
   * collection by pressing enter.
   *
   * @return The xPaths of the elements the user wants to categorize.
   */
  private def readInInitialXPaths(): Seq[String] =
    val (date, elements) = allElements.reduce((a, b) => if a._1.isBefore(b._1) then a else b)
    println(s"[i] Please visit ${urls(date)} and copy the xPath of the elements you want to categorize.")
    print("[i] Starting background collection. Enter to stop.\n>>> ")
    val collector = ClipboardCollector()
    readLine()
    val xPaths = collector.finish()
    xPaths.map: xPath =>
      if elements.exists(_.xpath.equals(xPath)) then xPath
      else readInValidXPath(xPath, elements)

  @tailrec
  private def readInValidXPath(xpath: String, elements: Seq[Element]): String =
    if elements.exists(_.xpath.equals(xpath)) then xpath
    else
      print(s"[i] Not found. Please input the correct xPath for $xpath.\n>>>")
      readInValidXPath(readLine().trim, elements)

object SetElementsInteractive:

  def apply(name: String): SetElementsInteractive =
    val folder = Paths.get(s"sites/$name")
    val elements = Load.getAllElementsByDateFor(folder)
    new SetElementsInteractive(name, folder, elements)
