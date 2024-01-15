package util

import elements.Element
import evalutation.EvaluationStatistics.Result
import similo.comparing.{DimensionCompare, LocationCompare, MapCompare, StringCompare}
import similo.legacy.LegacySimilo
import similo.optimized.OptimizedSimilo

import java.nio.file.{Files, Paths}
import java.time.LocalDate

object Compare:

  def compareElements(
    site: String,
    oldDate: LocalDate,
    oldElement: Element,
    newDate: LocalDate,
    target: Element,
    found: Element,
    saveTo: String = "comparison",
    fileName: String = "element-comparison",
    result: Option[Result] = None
  ): Unit =

    Files.createDirectories(Paths.get(saveTo))

    if target.xpath.chrome == "/html/body/div/div/div/div/main/div/div/div/div[1]/div/div[3]/div[4]/span" ||
      target.xpath.chrome == "/html/body/div[1]/div[3]/div/div[1]/nav[2]/div[2]/div/ul[1]/li[2]/button"
    then
      val oldE = oldElement
      val newE = target
      val foundE = found
      val oldNewScore = OptimizedSimilo.score(oldE, newE)
      val oldFoundScore = OptimizedSimilo.score(oldE, foundE)

      this.printElement(oldE)
      this.printElement(newE)
      this.printElement(foundE)

      this.printScore(oldE, newE)
      this.printScore(oldE, foundE)

      println("")

    val table = s"""
     | |Attribute|Old Element|Target|Found instead|
     | | --- | --- | --- | --- |
     | |Tag|${oldElement.tag}|${target.tag}|${found.tag}|
     | |Class|${clean(oldElement.className)}|${clean(target.className)}|${clean(found.className)}|
     | |Name|${clean(oldElement.name)}|${clean(target.name)}|${clean(found.name)}|
     | |Id|${clean(oldElement.id)}|${clean(target.id)}|${clean(found.id)}|
     | |Href|${clean(oldElement.href)}|${clean(target.href)}|${clean(found.href)}|
     | |Alt|${clean(oldElement.alt)}|${clean(target.alt)}|${clean(found.alt)}|
     | |Xpath|${clean(oldElement.xpath.chrome)}|${clean(target.xpath.chrome)}|${clean(found.xpath.chrome)}|
     | |Id Xpath|${clean(oldElement.xpath.idxpath)}|${clean(target.xpath.idxpath)}|${clean(found.xpath.idxpath)}|
     | |Css Selector|${clean(oldElement.cssSelector)}|${clean(target.cssSelector)}|${clean(found.cssSelector)}|
     | |Is Button|${oldElement.isButton}|${target.isButton}|${found.isButton}|
     | |Location|${oldElement.location}|${target.location}|${found.location}|
     | |Dimension|${oldElement.dimension}|${target.dimension}|${found.dimension}|
     | |Area|${oldElement.area}|${target.area}|${found.area}|
     | |Text|${clean(oldElement.getVisibleText)}|${clean(target.getVisibleText)}|${clean(found.getVisibleText)}|
     | |Neighbour Text|${clean(oldElement.neighbourText)}|${clean(target.neighbourText)}|${clean(found.neighbourText)}|
     |""".stripMargin

    val hash = Math.abs(table.hashCode)
    val imageLeft = s"images/labeled-left-$hash.png"
    val imageRight = s"images/labeled-right-$hash.png"

    Utils.createDirs(s"$saveTo/images")

    Draw(s"sites/$site/${oldDate.toString}/screenshot.png").highlightElement(oldElement).write(s"$saveTo/$imageLeft")

    Draw(s"sites/$site/${newDate.toString}/screenshot.png")
      .highlightElement(target)
      .labelElement("Target", target)
      .highlightElement(found)
      .labelElement("Found instead", found)
      .write(s"$saveTo/$imageRight")

    val imageEmbedded = s"""
        |<div style="display: flex;">
        |    <img src="$imageLeft" alt="Description of Image 1" style="width: 50%; margin-right: 10px;"/>
        |    <img src="$imageRight" alt="Description of Image 2" style="width: 50%;"/>
        |</div>
    """.stripMargin

    var markdown = s"# Element Comparison\n$imageEmbedded\n $table"

    if result.isDefined then
      markdown += s"\n## Result\n\n"
      markdown += s"Target score: ${LegacySimilo.score(result.get.target, result.get.toFind)}\n\n"
      markdown += s"Found score: ${LegacySimilo.score(result.get.target, result.get.found.head._1)}\n\n"

      markdown += s"\n## Meta data\n\n"
      markdown += s"Date: ${result.get.date}\n\n"
      markdown += s"Target: ${result.get.target.xpath.chrome}\n\n"
      markdown += s"To Find: ${result.get.toFind.xpath.chrome}\n\n"
      markdown += s"Found: ${result.get.found.head._1.xpath.chrome}\n\n"

    Files.writeString(Paths.get(s"$saveTo/$fileName-$hash.md"), markdown)

  private def clean(op: Option[String]): String = op.map(clean).getOrElse("null")

  private def clean(s: String): String =
    val replaced = s.replace("\n", " ").replace("\r", "").replace("\t", "")
    if replaced.length > 50 then replaced.substring(0, 47) + "..." else replaced

  private def printElement(element: Element): Unit =
    println("-------ELEMENT------")
    println(s"Tag: ${element.tag}")
    println(s"Class: ${element.className}")
    println(s"Name: ${element.name}")
    println(s"Id: ${element.id}")
    println(s"Href: ${element.href}")
    println(s"Alt: ${element.alt}")
    println(s"Type: ${element.typeName}")
    println(s"Aria Label: ${element.ariaLabel}")
    println(s"Xpath: ${element.xpath.chrome}")
    println(s"Id Xpath: ${element.xpath.idxpath}")
    println(s"Is Button: ${element.isButton}")
    println(s"Location: ${element.location}")
    println(s"Dimension: ${element.dimension}")
    println(s"Text: ${element.getVisibleText}")
    println(s"Neighbour Text: ${element.neighbourText}")
    println(s"Attributes: ${element.attributes}")
    println(s"-------------------")

  private def printScore(oldElement: Element, newElement: Element): Unit =
    val tagScore = StringCompare.jaccard.compare(oldElement.tag, newElement.tag)
    println(s"tag;${oldElement.tag};${newElement.tag};$tagScore;${tagScore * 0.55}")

    val classScore = StringCompare.equal.forOption.compare(oldElement.className, newElement.className)
    println(s"class;${oldElement.className};${newElement.className};$classScore;${classScore * 1.5}")

    val nameScore = StringCompare.levenshtein.forOption.compare(oldElement.name, newElement.name)
    println(s"name;${oldElement.name};${newElement.name};$nameScore;${nameScore * 2.25}")

    val idScore = StringCompare.levenshtein.forOption.compare(oldElement.id, newElement.id)
    println(s"id;${oldElement.id};${newElement.id};$idScore;${idScore * 1.25}")

    val hrefScore = StringCompare.equal.forOption.compare(oldElement.href, newElement.href)
    println(s"href;${oldElement.href};${newElement.href};$hrefScore;${hrefScore * 1.5}")

    val altScore = StringCompare.equal.forOption.compare(oldElement.alt, newElement.alt)
    println(s"alt;${oldElement.alt};${newElement.alt};$altScore;${altScore * 0.95}")

    val typeScore = StringCompare.equal.forOption.compare(oldElement.typeName, newElement.typeName)
    println(s"type;${oldElement.typeName};${newElement.typeName};$typeScore;${typeScore * 0.4}")

    val ariaLabelScore = StringCompare.jaccard.forOption.compare(oldElement.ariaLabel, newElement.ariaLabel)
    println(s"aria-label;${oldElement.ariaLabel};${newElement.ariaLabel};$ariaLabelScore;${ariaLabelScore * 1.4}")

    val xpathScore = StringCompare.levenshtein.compare(oldElement.xpath.chrome, newElement.xpath.chrome)
    println(s"xpath;${oldElement.xpath.chrome};${newElement.xpath.chrome};$xpathScore;${xpathScore * 0.35}")

    val idXpathScore = StringCompare.levenshtein.compare(oldElement.xpath.idxpath, newElement.xpath.idxpath)
    println(s"id-xpath;${oldElement.xpath.idxpath};${newElement.xpath.idxpath};$idXpathScore;${idXpathScore * 0.25}")

    val isButtonScore = if oldElement.isButton == newElement.isButton then 1.0 else 0.0
    println(s"is_button;${oldElement.isButton};${newElement.isButton};$isButtonScore;${isButtonScore * 0.1}")

    val locationScore = LocationCompare.distanceMediumDecay.compare(oldElement.location, newElement.location)
    println(s"location;${oldElement.location};${newElement.location};$locationScore;${locationScore * 1.15}")

    val dimensionScore = DimensionCompare.area.compare(oldElement.dimension, newElement.dimension)
    println(s"dimension;${oldElement.dimension};${newElement.dimension};$dimensionScore;${dimensionScore * 1.0}")

    val textScore = StringCompare.levenshtein.forOption.compare(oldElement.getVisibleText, newElement.getVisibleText)
    println(s"text;${oldElement.getVisibleText};${newElement.getVisibleText};$textScore;${textScore * 2.85}")

    val neighbourTextScore = StringCompare.jaccard.forOption.compare(oldElement.neighbourText, newElement.neighbourText)
    println(
      s"neighbour;${oldElement.neighbourText};${newElement.neighbourText};$neighbourTextScore;${neighbourTextScore * 1.4}"
    )

    val attributeScore = MapCompare.intersectValueCompare.compare(oldElement.attributes, newElement.attributes)
    println(s"attributes;${oldElement.attributes};${newElement.attributes};$attributeScore;${attributeScore * 1.65}")

    val totalScoreSimilo = OptimizedSimilo.score(oldElement, newElement)
    val totalScoreSum = tagScore * 0.55 +
      classScore * 1.5 +
      nameScore * 2.25 +
      idScore * 1.25 +
      hrefScore * 1.5 +
      altScore * 0.95 +
      typeScore * 0.4 +
      ariaLabelScore * 1.4 +
      xpathScore * 0.35 +
      idXpathScore * 0.25 +
      isButtonScore * 0.1 +
      locationScore * 1.15 +
      dimensionScore * 1.0 +
      textScore * 2.85 +
      neighbourTextScore * 1.4 +
      attributeScore * 1.65
    println(s"Total Score Similo: $totalScoreSimilo")
    println(s"Total Score Sum: $totalScoreSum")

    println("")

  private def drawAndSave(element: Element, site: String, date: LocalDate, saveTo: String): Unit =
    Draw(s"sites/$site/${date.toString}/screenshot.png").highlightElement(element).write(saveTo)
