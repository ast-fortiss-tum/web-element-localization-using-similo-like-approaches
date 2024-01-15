package scrape

import elements.Element
import io.circe.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.openqa.selenium.remote.RemoteWebDriver
import scrape.WayBackApi.withoutWayBackBanner
import util.Load
import util.Setup.logger
import util.Utils.{append, read, write}

import java.nio.file.{Files, Paths}
import java.time.LocalDate
import java.util.concurrent.TimeoutException
import scala.annotation.tailrec
import scala.io.StdIn.readLine

object LoadAllWebsites:

  private final val URLS_CSV_HEADER = "date;url"

  def loadAll(driver: RemoteWebDriver, start: LocalDate): Unit =
    for
      site <- Load.getSitesFromFile
      date <- Iterator.iterate(start)(_.plusMonths(4)).takeWhile(_.isBefore(LocalDate.of(2023, 12, 12)))
    do load(site, date, driver)

  private def load(site: Site, date: LocalDate, driver: RemoteWebDriver): Unit =
    val folder = s"sites/${site.name}/${date.toString}"
    if Files.exists(Paths.get(folder)) then logger.info(s"Skipping ${site.name} on $date, already loaded!")
    else
      logger.info(s"Loading ${site.name} on $date")
      loadCorrectWebsite(driver, site.url, date, site.name)

      readLine("[i] Press enter when the website is fully loaded.")

      logger.info(s"Collecting elements from ${site.name} on $date")
      val allElements = Element.extractAll(driver)
      val processed = allElements.map(_.addNeighborTexts(allElements))
      val json = processed.asJson.spaces2

      logger.info(s"Writing ${processed.size} elements to $folder")
      val path = Paths.get(s"$folder/elements.json")
      Files.createDirectories(path.getParent)
      Files.writeString(path, json)

      logger.info(s"Writing screenshot to $folder")
      WebsiteScreenshot.screenshotFullPage(driver, s"$folder/screenshot.png")

  private def loadCorrectWebsite(driver: RemoteWebDriver, url: String, date: LocalDate, name: String): Unit =
    val urlsPath = s"sites/$name/urls.csv"
    val workingUrls = read(urlsPath) match
      case Some(value) => value
      case None        => write(urlsPath, URLS_CSV_HEADER); URLS_CSV_HEADER

    val urls = workingUrls.split("\n").filter(_.contains(";")).map(_.split(";")).map(a => (a(0), a(1)))

    urls.find(_._1 == date.toString) match
      case Some(value) => loadCatchTimeout(driver, value._2)
      case None =>
        try determineCorrectWebsite(driver, url, date, urlsPath)
        catch
          case _: Exception =>
            println("[i] Something went wrong, please manually enter the URL of the website.")
            manuallyEnterUrl(driver, date, urlsPath, readLine().withoutWayBackBanner)

  private def loadCatchTimeout(driver: RemoteWebDriver, url: String): Unit =
    try driver.get(url)
    catch
      case _: TimeoutException =>
        println("[i] Timeout, please confirm that the website is mostly loaded before continuing.")

  @tailrec
  private def determineCorrectWebsite(driver: RemoteWebDriver, url: String, date: LocalDate, urlsPath: String): Unit =
    val closest = WayBackApi.closestSnapshot(url, date)
    loadCatchTimeout(driver, closest)
    readLine("[i] Is this the correct website? (y/n) Or just manually enter an URL!\n") match
      case "y" => append(urlsPath, s"\n${date.toString};$closest")
      case "n" => determineCorrectWebsite(driver, url, date.plusDays(1), urlsPath)
      case url => manuallyEnterUrl(driver, date, urlsPath, url.withoutWayBackBanner)

  @tailrec
  private def manuallyEnterUrl(driver: RemoteWebDriver, date: LocalDate, urlsPath: String, url: String): Unit =
    loadCatchTimeout(driver, url)
    readLine("[i] Is this the correct website? (y) Or just manually enter an URL!\n") match
      case "y" => append(urlsPath, s"\n${date.toString};${url.withoutWayBackBanner}")
      case url => manuallyEnterUrl(driver, date, urlsPath, url.withoutWayBackBanner)
