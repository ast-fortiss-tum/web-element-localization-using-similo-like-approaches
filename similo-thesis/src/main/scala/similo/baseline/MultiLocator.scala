package similo.baseline

import elements.Element
import similo.baseline.MultiLocator.MultiLocatorState
import similo.{Locator, State}

case class MultiLocator() extends Locator:

  override type S = MultiLocatorState

  override def init(element: Element, elements: Seq[Element]): MultiLocatorState = ???

  override def locate(state: MultiLocatorState, elements: Seq[Element]): Seq[(Double, Element)] = ???

  override def normalizer: Double = ???

object MultiLocator:

  case class MultiLocatorState() extends State
