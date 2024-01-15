package util

import com.github.benmanes.caffeine.cache.LoadingCache
import elements.Element
import elements.Element.parseElements
import evalutation.Classification
import scrape.Site
import util.Utils.{read, walk}

import java.io.File
import java.nio.file.{Files, Path}
import java.time.LocalDate

object Load:

  val crossValidate: Seq[Path] = Seq(
    // "sites/zz_aol",
    "sites/zz_cnn",
    "sites/zz_foxnews",
    "sites/zz_instagram",
    // "sites/zz_imdb"
    "sites/zz_fandom"
  ).map(Path.of(_))
  private val elementsCache: LoadingCache[Path, Seq[Element]] =
    Utils.createCache((path: Path) => getDateElementsForFromSystem(path))
  private val elementsDateCache: LoadingCache[Path, Seq[(LocalDate, Seq[Element])]] =
    Utils.createCache((path: Path) => getAllElementsForFromSystem(path))

  def getDatesFor(site: Path): Seq[LocalDate] =
    walk(site)
      .filter(Files.isDirectory(_))
      .map(_.toString.split(File.separatorChar).last)
      .map(LocalDate.parse(_))
      .sorted

  def getClassificationFor(site: Path): Seq[(LocalDate, Seq[(Classification, String)])] =
    val path = ensureFileName(site, "classified.csv")
    val csv = Csv(path)
    csv.columns.columns.map: column =>
      (LocalDate.parse(column.header), column.values.map(Classification.load))

  private def ensureFileName(path: Path, shouldEndWith: String): Path =
    val startsSite = if path.startsWith("sites") then path else Path.of("sites").resolve(path)
    if startsSite.endsWith(shouldEndWith) then startsSite
    else startsSite.resolve(shouldEndWith)

  def getXPathsFor(site: String): Seq[(LocalDate, Seq[String])] =
    Load.getXPathsFor(Path.of(site))

  def getXPathsFor(site: Path): Seq[(LocalDate, Seq[String])] =
    val path = ensureFileName(site, "xpaths.csv")
    val csv = Csv(path)
    csv.columns.columns.map: column =>
      (LocalDate.parse(column.header), column.values)

  def getFullLoadedSuitableSites: Seq[Path] =
    Load.getSitesFor.filter: site =>
      Files.exists(site.resolve("xpaths.csv")) &&
        Files.exists(site.resolve("classified.csv"))

  def getSitesFor: Seq[Path] =
    walk("sites")
      .filter(Files.isDirectory(_))
      .filter(_.getParent.endsWith("sites"))
      .filterNot(_.getFileName.toString.startsWith("zz_"))
      .filterNot(_.getFileName.toString.startsWith("."))

  def getUrlsFor(path: Path): Map[LocalDate, String] =
    val full = ensureFileName(path, "urls.csv")
    if Files.notExists(full) then throw new Exception(s"File $full does not exist!")
    else
      read(full)
        .split("\n")
        .tail
        .filter(_.nonEmpty)
        .map: line =>
          val Array(date, url) = line.split(";")
          LocalDate.parse(date) -> url
        .toMap

  def getAllElementsByDateFor(path: String): Seq[(LocalDate, Seq[Element])] =
    getAllElementsByDateFor(Path.of(path))

  def getAllElementsByElementFor(path: String): Seq[Seq[(LocalDate, Element)]] =
    getAllElementsByElementFor(Path.of(path))

  def getAllElementsByElementFor(path: Path): Seq[Seq[(LocalDate, Element)]] =
    Utils.reverseSequence(getAllElementsByDateFor(path).map(t => t._2.map(e => (t._1, e))))

  def getAllElementsByDateFor(path: Path): Seq[(LocalDate, Seq[Element])] =
    elementsDateCache.get(path)

  def getDateElementsFor(path: String): Seq[Element] = Load.getDateElementsFor(Path.of(path))

  def getDateElementsFor(path: Path): Seq[Element] = elementsCache.get(path)

  def getSitesFromFile: Seq[Site] =
    val sites = read(Path.of("sites/sites.txt"))
    sites.split("\n").map(Site(_)).toSeq

  private def getAllElementsForFromSystem(path: Path): Seq[(LocalDate, Seq[Element])] =
    val totalPath =
      if path.startsWith("sites") || path.startsWith("src") then path
      else Path.of("sites").resolve(path)
    walk(totalPath)
      .filter(Files.isDirectory(_))
      .map((dir: Path) => (dateFromPath(dir), getDateElementsFor(dir)))
      .sortBy(_._1)

  private def dateFromPath(path: Path): LocalDate =
    LocalDate.parse(path.getFileName.toString)

  private def getDateElementsForFromSystem(path: Path): Seq[Element] =
    val elements = read(path.resolve("elements.json"))
    parseElements(elements)
