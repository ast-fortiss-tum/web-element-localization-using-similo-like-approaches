package scripts

import evalutation.EvaluationData
import evalutation.EvaluationData.ElementPair
import jdk.jshell.execution.Util
import similo.legacy.LegacySimilo
import similo.optimized.OptimizedSimilo
import util.Utils
import util.Utils.unwrapDiscard

object SelfScoresStats:

  def computeWithSelf(): Unit =
    val res = loadOnlyPairs.map: data =>
      (data.target.tag, LegacySimilo.score(data.target, data.target))
    Utils.write("results/self_scores/self_scores.csv", "tag,score")
    res.foreach: (tag, score) =>
      Utils.append("results/self_scores/self_scores.csv", s"\n$tag,$score")

  def computeWithNextIfSameTag(): Unit =
    val res = loadOnlyPairs.map: data =>
      if data.oldElement.tag == data.target.tag then
        Some((data.target.tag, LegacySimilo.score(data.target, data.oldElement)))
      else None
    Utils.write("results/self_scores/self_scores_next.csv", "tag,score")
    res.unwrapDiscard.foreach: (tag, score) =>
      Utils.append("results/self_scores/self_scores_next.csv", s"\n$tag,$score")

  private def loadOnlyPairs: Seq[ElementPair] =
    EvaluationData.getData.flatMap(_.data).flatMap(_.location)
