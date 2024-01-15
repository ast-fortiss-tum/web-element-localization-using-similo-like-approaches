package scripts

import util.{Load, Utils}

object AttributeFrequency:

  private type Frequencies = Map[String, ValueFrequency]

  def compute(): Unit =
    val elements = Load.getFullLoadedSuitableSites.flatMap(Load.getAllElementsByDateFor).flatMap(_._2)
    val result = elements.foldLeft(Map.empty[String, ValueFrequency]): (acc, element) =>
      element.attributes.foldLeft(acc): (acc, attribute) =>
        val newAcc = insertAttribute(attribute, acc)
        insertAttribute(("total", "total"), newAcc)
    val processed = result.toSeq.sortBy(_._2.nonEmptyString).reverse.take(11)
    val csv = s"Property,Empty,Non Empty\n" + processed
      .map: (key, value) =>
        s"$key,${value.emptyString},${value.nonEmptyString}"
      .mkString("\n")
    Utils.write("results/attribute_stats/attribute_frequency.csv", csv)

  private def insertAttribute(attribute: (String, String), acc: Frequencies): Frequencies =
    val key = attribute._1
    attribute._2 match
      case "" => updateMapping(key, acc, _.addEmpty())
      case _  => updateMapping(key, acc, _.addNonEmpty())

  private def updateMapping(key: String, acc: Frequencies, update: ValueFrequency => ValueFrequency): Frequencies =
    acc.updatedWith(key) {
      case Some(value) => Some(update(value))
      case None        => Some(update(ValueFrequency(0, 0)))
    }

  case class ValueFrequency(emptyString: Int, nonEmptyString: Int):
    def addEmpty(): ValueFrequency = ValueFrequency(emptyString + 1, nonEmptyString)
    def addNonEmpty(): ValueFrequency = ValueFrequency(emptyString, nonEmptyString + 1)
    def total: Int = emptyString + nonEmptyString
