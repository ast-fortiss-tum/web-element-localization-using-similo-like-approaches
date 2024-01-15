package scripts

import evalutation.EvaluationComputing
import similo.AttributeComparison
import similo.comparing.{DimensionCompare, LocationCompare, StringCompare}
import similo.similo.Similo
import util.Utils.{roundTwoDecimals, time}
import util.{Load, Utils}

object GeneticBruteForce:

  def TwoDimHeatMap(): Unit =
    val combinations = for
      a <- 0 to 60
      b <- 0 to 60
    yield (a * 0.05, b * 0.05)

    def inputSimilo(tuple: (Double, Double)): Similo =
      Similo(
        AttributeComparison(_.tag, tuple._1, StringCompare.equal),
        AttributeComparison(_.className, tuple._2, StringCompare.levenshtein.forOption),
        AttributeComparison(_.name, 1.5, StringCompare.equal.forOption),
        AttributeComparison(_.id, 1.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.href, 0.5, StringCompare.equal.forOption),
        AttributeComparison(_.alt, 0.5, StringCompare.equal.forOption),
        AttributeComparison(_.xpath.chrome, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.xpath.idxpath, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.cssSelector, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.location, 0.5, LocationCompare.distanceLargeDecay),
        AttributeComparison(_.dimension, 0.5, DimensionCompare.perimeter),
        AttributeComparison(_.getVisibleText, 1.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, 1.5, StringCompare.levenshtein.forOption)
      )

    Utils.write("results/weight_opt/2d_heat_map.csv", "tag,class,score")
    var i = 0
    for combination <- combinations do
      val similo = inputSimilo(combination)
      val result = time(EvaluationComputing.computeAll(similo, Load.getFullLoadedSuitableSites))
      val res = s"${roundTwoDecimals(combination._1)},${roundTwoDecimals(combination._2)},${result.score}\n"
      Utils.append("results/weight_opt/2d_heat_map.csv", res)
      println(s"Done ${i}/${combinations.size}")
      i += 1
      print(res)

  def ThreeDimHeatMap(): Unit =
    val combinations = for
      a <- 0 to 15
      b <- 0 to 15
      c <- 0 to 15
    yield (a * 0.2, b * 0.2, c * 0.2)

    def inputSimilo(tuple: (Double, Double, Double)): Similo =
      Similo(
        AttributeComparison(_.tag, 1.5, StringCompare.equal),
        AttributeComparison(_.className, 0.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.name, tuple._1, StringCompare.equal.forOption),
        AttributeComparison(_.id, tuple._2, StringCompare.levenshtein.forOption),
        AttributeComparison(_.href, tuple._3, StringCompare.equal.forOption),
        AttributeComparison(_.alt, 0.5, StringCompare.equal.forOption),
        AttributeComparison(_.xpath.chrome, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.xpath.idxpath, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.cssSelector, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.location, 0.5, LocationCompare.distanceLargeDecay),
        AttributeComparison(_.dimension, 0.5, DimensionCompare.perimeter),
        AttributeComparison(_.getVisibleText, 1.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, 1.5, StringCompare.levenshtein.forOption)
      )

    Utils.write("results/weight_opt/3d_heat_map.csv", "name,id,href,score")
    var i = 0
    for combination <- combinations do
      val similo = inputSimilo(combination)
      val result = time(EvaluationComputing.computeAll(similo, Load.getFullLoadedSuitableSites))
      val res =
        s"${roundTwoDecimals(combination._1)},${roundTwoDecimals(combination._2)},${roundTwoDecimals(combination._3)},${result.score}\n"
      Utils.append("results/weight_opt/3d_heat_map.csv", res)
      println(s"Done ${i}/${combinations.size}")
      i += 1
      print(res)

  def OneDimHeatMap(): Unit =
    val combinations =
      for a <- 0 to 15
      yield a * 0.2

    def inputSimilo(tuple: Double): Similo =
      Similo(
        AttributeComparison(_.tag, 1.5, StringCompare.equal),
        AttributeComparison(_.className, 0.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.name, 1.5, StringCompare.equal.forOption),
        AttributeComparison(_.id, tuple, StringCompare.levenshtein.forOption),
        AttributeComparison(_.href, 0.5, StringCompare.equal.forOption),
        AttributeComparison(_.alt, 0.5, StringCompare.equal.forOption),
        AttributeComparison(_.xpath.chrome, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.xpath.idxpath, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.cssSelector, 0.5, StringCompare.levenshtein),
        AttributeComparison(_.location, 0.5, LocationCompare.distanceLargeDecay),
        AttributeComparison(_.dimension, 0.5, DimensionCompare.perimeter),
        AttributeComparison(_.getVisibleText, 1.5, StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, 1.5, StringCompare.levenshtein.forOption)
      )

    Utils.write("results/weight_opt/1d_heat_map.csv", "id,score")
    var i = 0
    for combination <- combinations do
      val similo = inputSimilo(combination)
      val result = time(EvaluationComputing.computeAll(similo, Load.getFullLoadedSuitableSites))
      val res = s"${roundTwoDecimals(combination)},${result.score}\n"
      Utils.append("results/weight_opt/1d_heat_map.csv", res)
      println(s"Done ${i}/${combinations.size}")
      i += 1
      print(res)
