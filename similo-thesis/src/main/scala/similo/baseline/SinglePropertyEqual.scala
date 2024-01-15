package similo.baseline

import elements.Element
import similo.baseline.SinglePropertyEqual.SinglePropertyEqualState
import similo.{Locator, State}

case class SinglePropertyEqual(compare: Element => String | Option[String]*) extends Locator:

  override type S = SinglePropertyEqualState

  override def init(element: Element, elements: Seq[Element]): SinglePropertyEqualState =
    SinglePropertyEqualState(extract(element))

  private def extract(element: Element): Seq[String] = compare.map: extract =>
    extract(element) match
      case s: String         => s
      case o: Option[String] => o.getOrElse("")

  override def locate(state: SinglePropertyEqualState, elements: Seq[Element]): Seq[(Double, Element)] =
    val element = elements.find(e => baseLineIsElement(e, state))
    if element.isDefined then Seq((1, element.get))
    else Seq((0, elements.head))

  private def baseLineIsElement(element: Element, baseLine: SinglePropertyEqualState): Boolean =
    extract(element).zip(baseLine.values).forall(_.toLowerCase == _.toLowerCase)

  override def normalizer: Double = 1

object SinglePropertyEqual:

  def id: SinglePropertyEqual = SinglePropertyEqual(_.id)

  def tagText: SinglePropertyEqual = SinglePropertyEqual(_.tag, _.getVisibleText)

  def className: SinglePropertyEqual = SinglePropertyEqual(_.className)
  
  def xpath: SinglePropertyEqual = SinglePropertyEqual(_.xpath.chrome)
  
  def idXpath: SinglePropertyEqual = SinglePropertyEqual(_.xpath.idxpath)

  case class SinglePropertyEqualState(values: Seq[String]) extends State
