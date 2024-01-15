package similo.von

import elements.Element
import similo.von.VonSimilo.{combinations, getOverlap}
import similo.{AttributeComparison, Locator}

import java.awt.Rectangle

case class VonSimilo(
  comparisons: Seq[AttributeComparison[_]],
  threshold: Double = 0.85,
  maxOverlap: Int = Int.MaxValue
) extends Locator:

  override type S = VonSimiloState

  override def init(element: Element, elements: Seq[Element]): VonSimiloState =
    val overlap = getOverlap(element, elements)
    if overlap.isEmpty then VonSimiloState(Seq(element))
    else VonSimiloState(overlap)

  override def locate(state: VonSimiloState, elements: Seq[Element]): Seq[(Double, Element)] =
    elements.map: element =>
      val overlap = getOverlap(element, elements)
      if overlap.size > maxOverlap then (0.0, element)
      else
        val scores = combinations(state.overlap, overlap).map: (target, candidate) =>
          comparisons.map(_.score(target, candidate)).sum
        (scores.max, element)

  override def normalizer: Double = comparisons.map(_.weight).sum

object VonSimilo:

  def getOverlap(target: Element, candidates: Seq[Element], threshold: Double = 0.85): Seq[Element] =
    candidates.filter(areOverlap(target, _, threshold)) match
      case Nil     => Seq(target)
      case overlap => overlap

  def areOverlap(target: Element, candidate: Element, threshold: Double): Boolean =
    val targetArea = Rectangle(target.x, target.y, target.width, target.height)
    val candidateArea = Rectangle(candidate.x, candidate.y, candidate.width, candidate.height)
    val intersection = targetArea.intersection(candidateArea)
    val union = targetArea.union(candidateArea)
    val divider = union.width.toDouble * union.height.toDouble
    if divider == 0 then false
    else
      val ratio = (intersection.width.toDouble * intersection.height.toDouble) / divider
      ratio >= threshold && targetArea.contains(candidateArea.getCenterX, candidateArea.getCenterY)

  def combinations(targets: Seq[Element], candidates: Seq[Element]): Seq[(Element, Element)] =
    targets.flatMap: target =>
      candidates.map: candidate =>
        (target, candidate)
