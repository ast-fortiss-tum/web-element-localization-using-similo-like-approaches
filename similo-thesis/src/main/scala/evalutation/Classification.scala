package evalutation

import evalutation.Classification.{ElementClassification, LocatorClassification}

case class Classification(element: ElementClassification, locator: LocatorClassification):
  def deleted: Boolean = element == ElementClassification.DELETED || locator == LocatorClassification.DELETED
  override def toString: String = s"${element.symbol}|${locator.symbol}"

object Classification:

  def load(string: String): (Classification, String) =
    val Array(element, locator, xpath) = string.split("\\|")
    (Classification(elementEnum(element), locatorEnum(locator)), xpath)

  private def locatorEnum(symbol: String): LocatorClassification =
    LocatorClassification.values
      .find(_.symbol == symbol)
      .getOrElse(throw new IllegalArgumentException(s"Unknown locator classification: $symbol"))

  private def elementEnum(symbol: String): ElementClassification =
    ElementClassification.values
      .find(_.symbol == symbol)
      .getOrElse(throw new IllegalArgumentException(s"Unknown element classification: $symbol"))

  def apply(string: String): Classification =
    val Array(element, locator) = string.split("\\|")
    Classification(elementEnum(element), locatorEnum(locator))

  def initial: Classification = Classification(ElementClassification.INITIAL, LocatorClassification.INITIAL)
  def deleted: Classification = Classification(ElementClassification.DELETED, LocatorClassification.DELETED)

  enum ElementClassification(val symbol: String):
    case SAME extends ElementClassification("same   ")
    case MINOR_CHANGE extends ElementClassification("minor  ")
    case MAJOR_CHANGE extends ElementClassification("major  ")
    case DELETED extends ElementClassification("deleted")
    case INITIAL extends ElementClassification("initial")

  enum LocatorClassification(val symbol: String):
    case UNCHANGED extends LocatorClassification("same   ")
    case XPATH_CHANGED extends LocatorClassification("xchange")
    case LOCATOR_CHANGED extends LocatorClassification("lchange")
    case DELETED extends LocatorClassification("deleted")
    case INITIAL extends LocatorClassification("initial")
