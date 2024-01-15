package scripts

import evalutation.EvaluationData
import similo.legacy.{LegacySimilo, LegacyVonSimilo}
import similo.von.VonSimilo

import scala.annotation.targetName

object OverlapUsability:

  def overlapMaxScoreNotTarget(): OverlapUsabilityStats =
    val all = EvaluationData.getData.flatMap: siteData =>
      siteData.data.flatMap: dateData =>
        dateData.location.map: locationData =>
          if locationData.classification.deleted then OverlapUsabilityStats(Seq(), Seq(), Seq())
          else
            val oldTarget = locationData.oldElement
            val newTarget = locationData.target
            val oldOverlap = VonSimilo.getOverlap(oldTarget, dateData.oldElements)
            val newOverlap = VonSimilo.getOverlap(newTarget, dateData.newElements)
            val oldOverlapWithoutTarget = oldOverlap.filter(_ != oldTarget)
            val newOverlapWithoutTarget = newOverlap.filter(_ != newTarget)
            val scoreTargets = LegacySimilo.normalizedScore(oldTarget, newTarget)
            val scoreOverlaps = VonSimilo
              .combinations(oldOverlapWithoutTarget, newOverlapWithoutTarget)
              .map(LegacySimilo.normalizedScore(_, _))
              .maxOption
              .getOrElse(0.0)
            val maxNoneOverlapOnSite = oldOverlapWithoutTarget
              .map(LegacySimilo.normalizedLocate(_, dateData.newElements).find(r => !newOverlap.contains(r._2)).get)
              .maxByOption(_._1)
              .map(_._1)
              .getOrElse(0.0)

            val o = LegacyVonSimilo.init(oldTarget, dateData.oldElements)
            val vonSimilo = LegacyVonSimilo.locateSorted(o, dateData.newElements).head
            val similo = LegacySimilo.locate(oldTarget, dateData.newElements).head

            OverlapUsabilityStats(
              if similo._2 == newTarget then Seq(1) else Seq(0),
              if vonSimilo._2 == newTarget then Seq(1) else Seq(0),
              if newOverlap.contains(vonSimilo._2) then Seq(1) else Seq(0)
            )

    all.reduce(_ ++ _)

  case class OverlapUsabilityStats(
    couldBeFoundBySimilo: Seq[Int],
    couldBeFoundByVonSimilo: Seq[Int],
    overlapCouldBeFoundByVonSimilo: Seq[Int]
  ):

    @targetName("combine")
    def ++(other: OverlapUsabilityStats): OverlapUsabilityStats =
      OverlapUsabilityStats(
        couldBeFoundBySimilo ++ other.couldBeFoundBySimilo,
        couldBeFoundByVonSimilo ++ other.couldBeFoundByVonSimilo,
        overlapCouldBeFoundByVonSimilo ++ other.overlapCouldBeFoundByVonSimilo
      )
