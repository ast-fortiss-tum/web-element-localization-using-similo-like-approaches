package similo.similo

import elements.Element
import similo.{AttributeComparison, Locator}

case class Similo(comparisons: AttributeComparison[?]*) extends Locator:

  override type S = SimiloState

  def score(left: Element, right: Element): Double =
    score(SimiloState(left), right)

  def locateSorted(element: Element, elements: Seq[Element]): Seq[(Double, Element)] =
    locate(element, elements).sortBy(_._1).reverse

  def locate(element: Element, elements: Seq[Element]): Seq[(Double, Element)] =
    locate(SimiloState(element), elements)

  override def locate(state: SimiloState, elements: Seq[Element]): Seq[(Double, Element)] =
    elements.map(e => (score(state, e), e)).sortBy(_._1).reverse

  def normalizedScore(left: Element, right: Element): Double =
    normalizedScore(SimiloState(left), right)

  def normalizedScore(state: SimiloState, element: Element): Double =
    score(state, element) / normalizer

  private def score(state: SimiloState, element: Element): Double =
    comparisons.map(_.score(state.element, element)).sum

  def normalizedLocate(element: Element, elements: Seq[Element]): Seq[(Double, Element)] =
    normalizedLocate(SimiloState(element), elements)

  def normalizedLocate(state: SimiloState, elements: Seq[Element]): Seq[(Double, Element)] =
    locateSorted(state, elements).map((score, element) => (score / normalizer, element))

  override def normalizer: Double = comparisons.map(_.weight).sum

  override def init(element: Element, elements: Seq[Element]): SimiloState = SimiloState(element)
