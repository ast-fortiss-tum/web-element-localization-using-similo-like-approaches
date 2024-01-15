package similo

import elements.Element
import comparing.ValueCompare

case class AttributeComparison[T](extract: Element => T, weight: Double, comparison: ValueCompare[T]):

  def score(e1: Element, e2: Element): Double = comparison.compare(extract(e1), extract(e2)) * weight

  override def toString: String = s"($weight, $comparison)"

object AttributeComparison:

  def any[T](extract: Element => Any, weight: Double, comparison: ValueCompare[?]): AttributeComparison[T] =
    AttributeComparison[T](extract.andThen(_.asInstanceOf[T]), weight, comparison.asInstanceOf[ValueCompare[T]])
