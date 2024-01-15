package optimization

import elements.Element
import evalutation.EvaluationData.SiteData
import evalutation.{EvaluationComputing, EvaluationData, EvaluationStatistics}
import optimization.OneAtTheTime.*
import similo.comparing.DimensionCompare.*
import similo.comparing.LocationCompare.*
import similo.comparing.MapCompare.{intersectKeyCompare, intersectValueCompare}
import similo.comparing.StringCompare.*
import similo.comparing.ValueCompare
import similo.similo.Similo
import similo.von.VonSimilo
import similo.{AttributeComparison, Locator}
import util.Utils

import java.time.LocalDateTime
import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters.*
import scala.util.Random

case class OneAtTheTime[L <: Locator](
  attributeComparisons: Seq[AttributeComparisons[_]],
  locatorInstanz: Seq[AttributeComparison[Any]] => L,
  fitness: EvaluationStatistics => Double = _.score,
  writeTo: String = s"results/comparison_opt/${LocalDateTime.now()}.csv",
  mapper: SiteData => SiteData = identity
):

  private type NextIndex = (Int, Seq[Option[(String, AttributeComparison[Any])]]) => Int

  def sequentialLinear(iterations: Int): Unit =
    Utils.write(writeTo, "property,algo,score," + attributeComparisons.map(_.name).mkString(","))
    for i <- 1 to iterations do sequential((i, _) => i + 1, 0)

  def sequentialRandom(iterations: Int): Unit =
    Utils.write(writeTo, "property,algo,score," + attributeComparisons.map(_.name).mkString(","))
    for _ <- 1 to iterations do sequential((_, seq) => nextSlot(seq), Random.nextInt(attributeComparisons.size))

  def bruteForce(): Unit =
    Utils.write(writeTo, "score," + attributeComparisons.map(_.name).mkString(","))

    val attrComparisons = attributeComparisons.map: ac =>
      ac.algorithms.map(algo => AttributeComparison(ac.property, ac.weight, algo))
    val allPossibilities = createAllPossibilities(attrComparisons)

    for possibility <- allPossibilities do
      val similo = locatorInstanz(possibility.asInstanceOf[Seq[AttributeComparison[Any]]])
      val result = EvaluationComputing.computeAll(similo, mapper = mapper)
      val score = fitness(result)
      Utils.append(writeTo, s"$score,${possibility.map(_.comparison.name).mkString(",")}")

  private def getBestLocator(locators: Seq[(L, AttributeComparison[Any])]): (AttributeComparison[Any], Double) =
    locators.par
      .map: locator =>
        val result = EvaluationComputing.computeAll(locator._1, mapper = mapper)
        (locator._2, fitness(result))
      .maxBy(_._2)

  private def sequential(chooseNext: NextIndex, init: Int): Seq[(String, ValueCompare[Any])] =
    val allEmpty = Seq.fill(attributeComparisons.size)(None)
    keepingProgress(allEmpty, init, chooseNext)

  @tailrec
  private def keepingProgress(
    alreadySet: Seq[Option[(String, AttributeComparison[Any])]],
    next: Int,
    chooseNext: NextIndex
  ): Seq[(String, ValueCompare[Any])] =
    val zipped = alreadySet
      .zip(attributeComparisons)
      .map: (op, cmp) =>
        if op.isDefined then op.get._2 else cmp.defaultCompare

    val before = zipped.take(next)
    val after = zipped.drop(next + 1)
    val current = attributeComparisons(next)

    val locators = current.allCompares.map: compare =>
      val comparisons = before ++ Seq(compare) ++ after
      val locator = locatorInstanz(comparisons)
      (locator, compare)

    val best = getBestLocator(locators)
    val updatedSet = alreadySet.updated(next, Some((current.name, best._1)))

    val others = zipped.map(_.comparison.name).mkString(",")
    Utils.append(writeTo, s"${attributeComparisons(next).name},${best._1.comparison},${best._2},$others")

    if updatedSet.forall(_.isDefined) then updatedSet.map(t => (t.get._1, t.get._2.comparison))
    else keepingProgress(updatedSet, chooseNext(next, updatedSet), chooseNext)

  private def nextSlot[T](seq: Seq[Option[T]]): Int =
    val remaining = seq.zipWithIndex.filter(_._1.isEmpty).map(_._2)
    Random.shuffle(remaining).head

object OneAtTheTime:

  val stringComparisons: Seq[ValueCompare[String]] =
    Seq(equal, equalIgnoreCase, levenshtein, jaroWinkler, jaccard, stringSet)
  val stringOptionComparisons: Seq[ValueCompare[Option[String]]] =
    stringComparisons.map(_.forOption)
  val dimensionComparisons: Seq[ValueCompare[(Int, Int)]] =
    Seq(area, perimeter, aspectRatio)
  val dimensionOptionComparisons: Seq[ValueCompare[Option[(Int, Int)]]] =
    dimensionComparisons.map(_.forOption)
  val locationComparisons: Seq[ValueCompare[(Int, Int)]] =
    Seq(distanceLinear, distanceSmallDecay, distanceMediumDecay, distanceLargeDecay, distanceManhattan)
  val locationOptionComparisons: Seq[ValueCompare[Option[(Int, Int)]]] =
    locationComparisons.map(_.forOption)
  val mapComparisons: Seq[ValueCompare[Map[String, String]]] =
    Seq(intersectKeyCompare, intersectValueCompare)

  def similoBruteForce: OneAtTheTime[Similo] = OneAtTheTime(
    Seq(
      AttributeComparisons("tag", _.tag, equal, Seq(equal, jaroWinkler), 1.5), // jaccard
      AttributeComparisons(
        "className",
        _.className,
        levenshtein.forOption,
        Seq(equal.forOption, levenshtein.forOption),
        0.5
      ), // levenshtein
      AttributeComparisons("name", _.name, equal.forOption, Seq(equal.forOption, levenshtein.forOption), 1.5), // equal
      AttributeComparisons(
        "id",
        _.id,
        equal.forOption,
        Seq(equal.forOption, levenshtein.forOption, jaccard.forOption),
        1.5
      ), // levenshtein
      AttributeComparisons("xpath.idxpath", _.xpath.idxpath, levenshtein, Seq(equal, levenshtein), 0.5), // levenshtein
      AttributeComparisons(
        "aria-label",
        _.ariaLabel,
        jaccard.forOption,
        Seq(equal.forOption, jaccard.forOption, levenshtein.forOption)
      ), // jacard
      AttributeComparisons("dimension", _.dimension, area, Seq(area, aspectRatio), 0.5), // area
      AttributeComparisons(
        "visible_text",
        _.getVisibleText,
        levenshtein.forOption,
        Seq(equalIgnoreCase.forOption, levenshtein.forOption),
        1.5
      ) // levenshtein
    ),
    in =>
      Similo(
        (Seq(
          AttributeComparison(_.isButton, 0.5, ValueCompare.equals),
          AttributeComparison(_.attributes, 1, intersectValueCompare),
          AttributeComparison(_.href, 0.5, equal.forOption),
          AttributeComparison(_.alt, 0.5, equal.forOption),
          AttributeComparison(_.typeName, 0.5, equal.forOption),
          AttributeComparison(_.xpath.chrome, 0.5, equal),
          AttributeComparison(_.neighbourText, 1.5, levenshtein.forOption),
          AttributeComparison(_.location, 0.5, distanceLinear)
        ) ++ in)*
      )
  )

  def vonSimiloOneAtTheTime(opt: EvaluationStatistics => Double): OneAtTheTime[VonSimilo] = OneAtTheTime(
    Seq(
      AttributeComparisons("tag", _.tag, equal, stringComparisons, 1.5),
      AttributeComparisons("className", _.className, levenshtein.forOption, stringOptionComparisons, 0.5),
      AttributeComparisons("name", _.name, equal.forOption, stringOptionComparisons, 1.5),
      AttributeComparisons("id", _.id, equal.forOption, stringOptionComparisons, 1.5),
      AttributeComparisons("href", _.href, levenshtein.forOption, stringOptionComparisons, 0.5),
      AttributeComparisons("alt", _.alt, levenshtein.forOption, stringOptionComparisons, 0.5),
      AttributeComparisons("xpath.chrome", _.xpath.chrome, levenshtein, stringComparisons, 0.5),
      AttributeComparisons("xpath.idxpath", _.xpath.idxpath, levenshtein, stringComparisons, 0.5),
      AttributeComparisons("is_button", _.isButton, ValueCompare.equals, Seq(ValueCompare.equals), 0.5),
      AttributeComparisons("location", _.location, distanceLinear, locationComparisons, 0.5),
      AttributeComparisons("dimension", _.dimension, area, dimensionComparisons, 0.5),
      AttributeComparisons("visible_text", _.getVisibleText, levenshtein.forOption, stringOptionComparisons, 1.5),
      AttributeComparisons("neighbour_text", _.neighbourText, stringSet.forOption, stringOptionComparisons, 1.5),
      AttributeComparisons("type", _.typeName, equal.forOption, stringOptionComparisons),
      AttributeComparisons("aria-label", _.ariaLabel, levenshtein.forOption, stringOptionComparisons),
      AttributeComparisons("attributes", _.attributes, intersectValueCompare, Seq(intersectValueCompare))
    ),
    VonSimilo(_, 0.8, 12),
    opt
  )

  def createAllPossibilities[T](seq: Seq[Seq[T]]): Seq[Seq[T]] =
    seq match
      case Seq() => Seq(Seq())
      case head +: tail =>
        val rest = createAllPossibilities(tail)
        for
          h <- head
          r <- rest
        yield h +: r

  private def splitAtEach[T](seq: Seq[T]): Seq[(Seq[T], T, Seq[T])] =
    seq.zipWithIndex.map: (t, i) =>
      val (before, after) = seq.splitAt(i)
      (before, t, after.tail)

  case class AttributeComparisons[T](
    name: String,
    property: Element => T,
    default: ValueCompare[T],
    algorithms: Seq[ValueCompare[T]],
    weight: Double = 1
  ):

    def defaultCompare: AttributeComparison[Any] = AttributeComparison.any(property, weight, default)

    def allCompares: Seq[AttributeComparison[Any]] =
      algorithms.map: algorithm =>
        AttributeComparison.any(property, weight, algorithm)
