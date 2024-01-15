package similo.combined

import elements.Element
import similo.similo.Similo
import similo.von.VonSimilo
import similo.{AttributeComparison, Locator}

case class SimiloCombined(
  vonComparisons: Seq[AttributeComparison[_]],
  similoComparisons: Seq[AttributeComparison[_]],
  consideration: Int = 10
) extends Locator:

  type S = SimiloCombinedState

  private val similo = Similo(similoComparisons*)
  private val vonSimilo = VonSimilo(vonComparisons)

  override def init(element: Element, elements: Seq[Element]): SimiloCombinedState =
    SimiloCombinedState(similo.init(element, elements), vonSimilo.init(element, elements))

  override def locate(state: SimiloCombinedState, elements: Seq[Element]): Seq[(Double, Element)] =
    val preSelect = vonSimilo.locateSorted(state.vonSimiloState, elements)
    val (top, low) = (preSelect.take(consideration), preSelect.drop(consideration))
    val similoSelect = similo.locateSorted(state.similoState, top.map(_._2))
    similoSelect ++ low.map(e => (0.0, e._2))

  override def normalizer: Double = similoComparisons.map(_.weight).sum

object SimiloCombined:

  def apply(similoComparisons: Seq[AttributeComparison[_]]): SimiloCombined =
    new SimiloCombined(similoComparisons, similoComparisons)
