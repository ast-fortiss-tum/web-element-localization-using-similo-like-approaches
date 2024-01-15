package similo.baseline

import elements.Element
import similo.baseline.Color.ColorState
import similo.{Locator, State}

case class Color() extends Locator:

  override type S = ColorState

  override def init(element: Element, elements: Seq[Element]): ColorState = ???

  override def locate(state: ColorState, elements: Seq[Element]): Seq[(Double, Element)] = ???

  override def normalizer: Double = ???

object Color:

  case class ColorState() extends State
