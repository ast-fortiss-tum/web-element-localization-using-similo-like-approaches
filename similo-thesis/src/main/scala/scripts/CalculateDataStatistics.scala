package scripts

import evalutation.Classification
import evalutation.EvaluationStatistics.classificationScore
import util.Load

object CalculateDataStatistics:

  def elementClassificationDistribution(): Map[Classification, Int] =
    Load.getFullLoadedSuitableSites
      .flatMap(Load.getClassificationFor)
      .flatMap(_._2)
      .groupBy(_._1)
      .view
      .mapValues(_.size)
      .toMap

  def maximalPossibleScore(): Double =
    Load.getFullLoadedSuitableSites
      .flatMap(Load.getClassificationFor)
      .flatMap(_._2)
      .groupBy(_._1)
      .map(kv => classificationScore(kv._1) * 4 * kv._2.size)
      .sum
