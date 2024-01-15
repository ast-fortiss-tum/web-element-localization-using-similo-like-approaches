package similo

import elements.Element

trait Locator:

  type S <: State

  def locate(state: S, elements: Seq[Element]): Seq[(Double, Element)]

  def locateSortedNormalized(state: S, elements: Seq[Element]): Seq[(Double, Element)] =
    locateSorted(state, elements).map((score, element) => (score / normalizer, element))

  def locateSorted(state: S, elements: Seq[Element]): Seq[(Double, Element)] =
    locate(state, elements).sortBy(_._1).reverse

  def init(element: Element, elements: Seq[Element]): S

  def normalizer: Double
