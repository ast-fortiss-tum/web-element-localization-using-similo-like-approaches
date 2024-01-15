import evalutation.EvaluationData.SiteData
import evalutation.EvaluationStatistics.{BySiteStats, LocatorStats}
import evalutation.{EvaluationComputing, EvaluationStatistics}
import optimization.GeneticOptimization.{createIntGenotype, fitnessByScore, fitnessByScoreFirstLast, fitnessByTopTen}
import optimization.OneAtTheTime.*
import optimization.{BruteForceOptimization, GeneticOptimization, OneAtTheTime}
import scripts.OverlapUsability.overlapMaxScoreNotTarget
import scripts.{AttributeStability, OverlapUsability}
import similo.baseline.SinglePropertyEqual
import similo.comparing.*
import similo.comparing.DimensionCompare.*
import similo.comparing.LocationCompare.*
import similo.comparing.MapCompare.*
import similo.comparing.StringCompare.*
import similo.legacy.{LegacySimilo, LegacyVonSimilo}
import similo.optimized.{OptimizedCombinedSimilo, OptimizedMinimalSimilo, OptimizedSimilo, OptimizedVonSimilo}
import similo.similo.Similo
import similo.von.VonSimilo
import similo.{AttributeComparison, Locator}
import util.Utils
import util.Load

import java.nio.file.Path

object Main:

  def main(args: Array[String]): Unit =

    // basicSimiloGeneticOptimizeScoreFourMonthsWithEliminated()

    // val stats = overlapMaxScoreNotTarget()
    // val zip = Utils.zip(stats.couldBeFoundBySimilo, stats.couldBeFoundByVonSimilo, stats.overlapCouldBeFoundByVonSimilo)
    // println(zip.zipWithIndex.map(t => s"${t._2},${t._1._1},${t._1._2},${t._1._3}").mkString("\n"))

    // AttributeStability.computePropertyStability()

    printWholeStats()

    // vonSimiloGeneticOptimizeTopTenGenetic()

    // basicSimiloGeneticOptimizeScoreFourMonths()
    // basicSimiloGeneticOptimizeScoreFiveYears()
    // vonSimiloGeneticOptimizeTopTenGenetic()

    // AttributeStability.compute()

    /* basicSimiloOat(
      "results/comparison_optimization/similo_oat_four_month_fitness.csv"
    )

    basicSimiloOat(
      "results/comparison_optimization/similo_oat_five_year_fitness.csv",
      mapper = EvaluationComputing.firstAndLastMapper
    ) */


  private def bruteForceEliminateProperties(): Unit =
    BruteForceOptimization(
      (values: Seq[Double]) =>
        Similo(
          AttributeComparison(_.tag, values.head, StringCompare.jaccard),
          AttributeComparison(_.className, values(1), StringCompare.equal.forOption),
          AttributeComparison(_.name, values(2), StringCompare.levenshtein.forOption),
          AttributeComparison(_.id, values(3), StringCompare.levenshtein.forOption),
          AttributeComparison(_.href, values(4), StringCompare.equal.forOption),
          AttributeComparison(_.alt, values(5), StringCompare.equal.forOption),
          AttributeComparison(_.typeName, values(6), StringCompare.equal.forOption),
          AttributeComparison(_.ariaLabel, values(7), StringCompare.jaccard.forOption),
          AttributeComparison(_.xpath.chrome, values(8), StringCompare.jaccard),
          AttributeComparison(_.xpath.idxpath, values(9), StringCompare.levenshtein),
          AttributeComparison(_.isButton, values(10), ValueCompare.equals),
          AttributeComparison(_.location, values(11), LocationCompare.distanceMediumDecay),
          AttributeComparison(_.dimension, values(12), DimensionCompare.area),
          AttributeComparison(_.getVisibleText, values(13), StringCompare.levenshtein.forOption),
          AttributeComparison(_.neighbourText, values(14), StringCompare.stringSet.forOption),
          AttributeComparison(_.attributes, values(15), MapCompare.intersectValueCompare)
        ),
      Seq(
        Seq(0.8, 0.0),
        Seq(0.0),
        Seq(2.85),
        Seq(0.5, 0.0),
        Seq(0.95, 0.0),
        Seq(1.85),
        Seq(2.75),
        Seq(0.9, 0.0),
        Seq(0.1, 0.0),
        Seq(0.45, 0.0),
        Seq(0.1, 0.0),
        Seq(1.2),
        Seq(0.35, 0.0),
        Seq(2.8),
        Seq(1.45),
        Seq(1.8)
      ), 0.0
    ).compute()

  private def vonSimiloGeneticOptimizeTopTenGenetic(): Unit =
    val inputWithComparisonOptResults = (values: Seq[Double]) =>
      VonSimilo(
        Seq(
          AttributeComparison(_.tag, values.head, StringCompare.jaroWinkler),
          AttributeComparison(_.className, values(1), StringCompare.equal.forOption),
          AttributeComparison(_.name, values(2), StringCompare.equal.forOption),
          AttributeComparison(_.id, values(3), StringCompare.equal.forOption),
          AttributeComparison(_.href, values(4), StringCompare.equal.forOption),
          AttributeComparison(_.alt, values(5), StringCompare.equal.forOption),
          AttributeComparison(_.typeName, values(6), StringCompare.equal.forOption),
          AttributeComparison(_.ariaLabel, values(7), StringCompare.jaccard.forOption),
          AttributeComparison(_.xpath.chrome, values(8), StringCompare.equal),
          AttributeComparison(_.xpath.idxpath, values(9), StringCompare.levenshtein),
          AttributeComparison(_.isButton, values(10), ValueCompare.equals),
          AttributeComparison(_.location, values(11), LocationCompare.distanceLinear),
          AttributeComparison(_.dimension, values(12), DimensionCompare.area),
          AttributeComparison(_.getVisibleText, values(13), StringCompare.levenshtein.forOption),
          AttributeComparison(_.neighbourText, values(14), StringCompare.stringSet.forOption),
          AttributeComparison(_.attributes, values(15), MapCompare.intersectValueCompare)
        )
      )
    GeneticOptimization(
      inputWithComparisonOptResults,
      fitnessByTopTen,
      createIntGenotype,
      outfile = "results/weight_optimization/von_max_top_ten_four_months.csv"
    ).compute()

  private def basicSimiloGeneticOptimizeScoreFourMonths(): Unit =
    val inputWithComparisonOptResults = (values: Seq[Double]) =>
      Similo(
        AttributeComparison(_.tag, values.head, StringCompare.jaccard),
        AttributeComparison(_.className, values(1), StringCompare.equal.forOption),
        AttributeComparison(_.name, values(2), StringCompare.levenshtein.forOption),
        AttributeComparison(_.id, values(3), StringCompare.levenshtein.forOption),
        AttributeComparison(_.href, values(4), StringCompare.equal.forOption),
        AttributeComparison(_.alt, values(5), StringCompare.equal.forOption),
        AttributeComparison(_.typeName, values(6), StringCompare.equal.forOption),
        AttributeComparison(_.ariaLabel, values(7), StringCompare.jaccard.forOption),
        AttributeComparison(_.xpath.chrome, values(8), StringCompare.jaccard),
        AttributeComparison(_.xpath.idxpath, values(9), StringCompare.levenshtein),
        AttributeComparison(_.isButton, values(10), ValueCompare.equals),
        AttributeComparison(_.location, values(11), LocationCompare.distanceMediumDecay),
        AttributeComparison(_.dimension, values(12), DimensionCompare.area),
        AttributeComparison(_.getVisibleText, values(13), StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, values(14), StringCompare.stringSet.forOption),
        AttributeComparison(_.attributes, values(15), MapCompare.intersectValueCompare)
      )
    GeneticOptimization(
      inputWithComparisonOptResults,
      fitnessByScore,
      createIntGenotype,
      outfile = "results/weight_optimization/similo_max_score_four_month.csv"
    ).compute()

  private def basicSimiloGeneticOptimizeScoreFourMonthsWithEliminated(): Unit =
    val inputWithComparisonOptResults = (values: Seq[Double]) =>
      Similo(
        AttributeComparison(_.tag, values.head, StringCompare.jaccard),
        AttributeComparison(_.name, values(1), StringCompare.levenshtein.forOption),
        AttributeComparison(_.href, values(2), StringCompare.equal.forOption),
        AttributeComparison(_.alt, values(3), StringCompare.equal.forOption),
        AttributeComparison(_.typeName, values(4), StringCompare.equal.forOption),
        AttributeComparison(_.ariaLabel, values(5), StringCompare.jaccard.forOption),
        AttributeComparison(_.xpath.idxpath, values(6), StringCompare.levenshtein),
        AttributeComparison(_.location, values(7), LocationCompare.distanceMediumDecay),
        AttributeComparison(_.dimension, values(8), DimensionCompare.area),
        AttributeComparison(_.getVisibleText, values(9), StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, values(10), StringCompare.stringSet.forOption),
        AttributeComparison(_.attributes, values(11), MapCompare.intersectValueCompare)
      )
    GeneticOptimization(
      inputWithComparisonOptResults,
      fitnessByScore,
      createIntGenotype,
      outfile = "results/weight_optimization/similo_max_score_eliminated_four_month.csv",
      props = 12
    ).compute()

  private def basicSimiloGeneticOptimizeScoreFiveYears(): Unit =
    val inputWithComparisonOptResults = (values: Seq[Double]) =>
      Similo(
        AttributeComparison(_.tag, values.head, StringCompare.jaroWinkler),
        AttributeComparison(_.className, values(1), StringCompare.equal.forOption),
        AttributeComparison(_.name, values(2), StringCompare.equal.forOption),
        AttributeComparison(_.id, values(3), StringCompare.jaccard.forOption),
        AttributeComparison(_.href, values(4), StringCompare.equal.forOption),
        AttributeComparison(_.alt, values(5), StringCompare.equal.forOption),
        AttributeComparison(_.typeName, values(6), StringCompare.equal.forOption),
        AttributeComparison(_.ariaLabel, values(7), StringCompare.jaccard.forOption),
        AttributeComparison(_.xpath.chrome, values(8), StringCompare.equal),
        AttributeComparison(_.xpath.idxpath, values(9), StringCompare.levenshtein),
        AttributeComparison(_.isButton, values(10), ValueCompare.equals),
        AttributeComparison(_.location, values(11), LocationCompare.distanceLinear),
        AttributeComparison(_.dimension, values(12), DimensionCompare.aspectRatio),
        AttributeComparison(_.getVisibleText, values(13), StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, values(14), StringCompare.stringSet.forOption),
        AttributeComparison(_.attributes, values(15), MapCompare.intersectValueCompare)
      )
    GeneticOptimization(
      inputWithComparisonOptResults,
      fitnessByScoreFirstLast,
      createIntGenotype,
      outfile = "results/weight_optimization/similo_max_score_five_year.csv"
    ).compute()

  private def calculateExtensiveStats(): Unit =
    val stats = EvaluationComputing.computeAll(OptimizedSimilo).extensiveStatsBySite
    println(BySiteStats.header)
    stats.foreach: s =>
      println(s.csv)

  private def basicSimiloOatFourMonths(): Unit =
    basicSimiloOat("results/comparison_optimization/similo_oat_four_month_fitness.csv")

  private def basicSimiloOatFiveYears(): Unit =
    basicSimiloOat(
      "results/comparison_optimization/similo_oat_five_year_fitness.csv",
      mapper = EvaluationComputing.firstAndLastMapper
    )

  private def basicSimiloOat(writeTo: String, mapper: SiteData => SiteData = identity): Unit =
    OneAtTheTime(
      Seq(
        AttributeComparisons("tag", _.tag, equal, stringComparisons, 1.5),
        AttributeComparisons("className", _.className, levenshtein.forOption, stringOptionComparisons, 0.5),
        AttributeComparisons("name", _.name, equal.forOption, stringOptionComparisons, 1.5),
        AttributeComparisons("id", _.id, equal.forOption, stringOptionComparisons, 1.5),
        AttributeComparisons("href", _.href, levenshtein.forOption, stringOptionComparisons, 0.5),
        AttributeComparisons("alt", _.alt, levenshtein.forOption, stringOptionComparisons, 0.5),
        AttributeComparisons("xpath.chrome", _.xpath.chrome, levenshtein, stringComparisons, 0.5),
        AttributeComparisons("xpath.idxpath", _.xpath.idxpath, levenshtein, stringComparisons, 0.5),
        AttributeComparisons("location", _.location, distanceLinear, locationComparisons, 0.5),
        AttributeComparisons("dimension", _.dimension, area, dimensionComparisons, 0.5),
        AttributeComparisons("visible_text", _.getVisibleText, levenshtein.forOption, stringOptionComparisons, 1.5),
        AttributeComparisons("neighbour_text", _.neighbourText, stringSet.forOption, stringOptionComparisons, 1.5),
        AttributeComparisons("type", _.typeName, equal.forOption, stringOptionComparisons),
        AttributeComparisons("aria-label", _.ariaLabel, levenshtein.forOption, stringOptionComparisons),
        AttributeComparisons("attributes", _.attributes, intersectValueCompare, mapComparisons)
      ),
      input =>
        Similo(
          Seq(
            AttributeComparison(_.isButton, 0.5, ValueCompare.equals)
          ) ++ input*
        ),
      writeTo = writeTo,
      mapper = mapper
    ).sequentialRandom(50)

  private def basicSimiloBruteForce4Months(): Unit =
    OneAtTheTime(
      Seq(
        AttributeComparisons("tag", _.tag, jaccard, Seq(jaccard, levenshtein, equal), 1.5), // jaccard
        AttributeComparisons(
          "className",
          _.className,
          levenshtein.forOption,
          Seq(levenshtein.forOption, equal.forOption),
          0.5
        ), // equal
        AttributeComparisons(
          "name",
          _.name,
          levenshtein.forOption,
          Seq(equal.forOption, levenshtein.forOption),
          1.5
        ), // levenshtein
        AttributeComparisons(
          "alt",
          _.alt,
          levenshtein.forOption,
          Seq(levenshtein.forOption, equal.forOption),
          0.5
        ), // equal
        AttributeComparisons("xpath.chrome", _.xpath.chrome, levenshtein, Seq(levenshtein, jaccard), 0.5), // jaccard
        AttributeComparisons(
          "aria-label",
          _.ariaLabel,
          levenshtein.forOption,
          Seq(jaccard.forOption, levenshtein.forOption)
        ) // jaccard
      ),
      input =>
        Similo(
          Seq(
            AttributeComparison(_.id, 1.5, levenshtein.forOption),
            AttributeComparison(_.href, 0.5, equal.forOption),
            AttributeComparison(_.xpath.idxpath, 0.5, levenshtein),
            AttributeComparison(_.isButton, 0.5, ValueCompare.equals),
            AttributeComparison(_.location, 0.5, distanceMediumDecay),
            AttributeComparison(_.dimension, 0.5, area),
            AttributeComparison(_.getVisibleText, 1.5, levenshtein.forOption),
            AttributeComparison(_.neighbourText, 1.5, stringSet.forOption),
            AttributeComparison(_.typeName, 1.0, equal.forOption),
            AttributeComparison(_.attributes, 1.0, intersectValueCompare)
          ) ++ input*
        ),
      writeTo = "results/comparison_optimization/brute_force_four_month_fitness.csv"
    ).bruteForce()

  private def basicSimiloBruteForce5Years(): Unit =
    OneAtTheTime(
      Seq(
        AttributeComparisons(
          "className",
          _.className,
          stringSet.forOption,
          Seq(stringSet.forOption, equal.forOption),
          0.5
        ), // equal
        AttributeComparisons("id", _.id, equal.forOption, Seq(equal.forOption, jaccard.forOption), 1.5), // jaccard
        AttributeComparisons(
          "alt",
          _.alt,
          levenshtein.forOption,
          Seq(levenshtein.forOption, equal.forOption),
          0.5
        ), // equal
        AttributeComparisons("xpath.idxpath", _.xpath.idxpath, equal, Seq(levenshtein, equal), 0.5), // equal
        AttributeComparisons(
          "aria-label",
          _.ariaLabel,
          levenshtein.forOption,
          Seq(jaccard.forOption, levenshtein.forOption)
        ) // jaccard
      ),
      input =>
        Similo(
          Seq(
            AttributeComparison(_.tag, 1.5, jaroWinkler),
            AttributeComparison(_.name, 1.5, equal.forOption),
            AttributeComparison(_.href, 0.5, equal.forOption),
            AttributeComparison(_.xpath.chrome, 0.5, equal),
            AttributeComparison(_.isButton, 0.5, ValueCompare.equals),
            AttributeComparison(_.location, 0.5, distanceLinear),
            AttributeComparison(_.dimension, 0.5, aspectRatio),
            AttributeComparison(_.getVisibleText, 1.5, levenshtein.forOption),
            AttributeComparison(_.neighbourText, 1.5, stringSet.forOption),
            AttributeComparison(_.typeName, 1.0, equal.forOption),
            AttributeComparison(_.attributes, 1.0, intersectValueCompare)
          ) ++ input*
        ),
      writeTo = "results/comparison_optimization/brute_force_five_year_fitness.csv",
      mapper = EvaluationComputing.firstAndLastMapper
    ).bruteForce()

  private def printHeapSizes(): Unit =
    val mb = 1024 * 1024
    val runtime = Runtime.getRuntime
    println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
    println("** Free Memory:  " + runtime.freeMemory / mb)
    println("** Max Memory:   " + runtime.maxMemory / mb)

  private def printWholeStats(): Unit =
    val locators = Seq(
      (OptimizedCombinedSimilo, "Optimized Combined"),
      (OptimizedSimilo, "Optimized Similo"),
      (OptimizedMinimalSimilo, "Optimized Minimal"),
      (OptimizedVonSimilo, "Optimized VON Similo"),
      (LegacySimilo, "Legacy Similo"),
      (LegacyVonSimilo, "Legacy VON Similo"),
      (SinglePropertyEqual.tagText, "Tag + Text"),
      (SinglePropertyEqual.idXpath, "ID-XPath"),
      (SinglePropertyEqual.xpath, "XPath"),
      (SinglePropertyEqual.id, "ID"),
    )

    println("Locator,Accuracy,Fitness,Locator Break Found,Found Overlap,Top Ten")
    val printStats: (LocatorStats, String) => Unit = (s, n) =>
      println(s"${n},${s.accuracy},${s.fitness} (${s.fitnessPercent})," +
        s"${s.locatorBreakFound},${s.foundOverlap},${s.topTen}")

    val calculateStats: Seq[Path] => Unit = paths =>
      for locator <- locators do
        val fmStats = EvaluationComputing
          .computeAll(locator._1, paths = paths).extensivePerformanceStats
        printStats(fmStats, locator._2)
      for locator <- locators do
        val fyStats = EvaluationComputing
          .computeAll(locator._1, paths = paths, mapper = EvaluationComputing.firstAndLastMapper)
          .extensivePerformanceStats
        printStats(fyStats, locator._2)

    calculateStats(Load.getFullLoadedSuitableSites)
    // println("\n\n\n")
    calculateStats(Load.crossValidate)
