package scripts

import elements.Element
import evalutation.EvaluationData
import evalutation.EvaluationData.ElementPair
import util.Utils
import util.Utils.{reverseSequence, roundTwoDecimals}

object AttributeStability:

  private type Counter = Map[String, AttributeStabilityCount]

  def computeAttributeStability(): Unit =
    compute(_.attributes, "attribute_stability")

  def computePropertyStability(): Unit =
    compute(extractProperties, "property_stability")

  def compute(properties: Element => Map[String, String], file: String): Unit =
    val all = EvaluationData.getData.map(_.data.map(_.location)).flatMap(reverseSequence).flatten
    val result = all.foldLeft(Map.empty[String, AttributeStabilityCount])(processElementPair(_, _, properties))
    val processed = result.toSeq.sortBy(_._2.same).reverse.take(11)
    val leftColumn = Seq("Stability", "Exists", "Same", "Different", "Removed", "Added", "Stability")
    val csv = processed.foldLeft(leftColumn): (acc, pair) =>
      Seq(
        acc.head + "," + pair._1,
        acc(1) + "," + pair._2.there,
        acc(2) + "," + pair._2.same,
        acc(3) + "," + pair._2.different,
        acc(4) + "," + pair._2.removed,
        acc(5) + "," + pair._2.added,
        acc(6) + "," + roundTwoDecimals(pair._2.same.toDouble / pair._2.there.toDouble)
      )
    Utils.write(s"results/attribute_stats/$file.csv", csv.mkString("\\\\ \n"))

  private def processElementPair(counter: Counter, pair: ElementPair, properties: Element => Map[String, String]): Counter =
    if pair.target.isEmpty then counter
    else
      val oldAttributes = properties(pair.oldElement)
      val newAttributes = properties(pair.target)
      val allAttributes = oldAttributes.keySet ++ newAttributes.keySet
      allAttributes.foldLeft(counter): (acc, attribute) =>
        val old = oldAttributes.get(attribute)
        val newA = newAttributes.get(attribute)
        (old, newA) match
          case (Some(o), Some(n)) =>
            if o == n then
              acc.updatedWith(attribute)(_.orElse(Some(AttributeStabilityCount.empty)).map(_.incrementSame))
            else acc.updatedWith(attribute)(_.orElse(Some(AttributeStabilityCount.empty)).map(_.incrementDifferent))
          case (Some(_), None) =>
            acc.updatedWith(attribute)(_.orElse(Some(AttributeStabilityCount.empty)).map(_.incrementRemoved))
          case (None, Some(_)) =>
            acc.updatedWith(attribute)(_.orElse(Some(AttributeStabilityCount.empty)).map(_.incrementAdded))
          case (None, None) => acc

  private def extractProperties(element: Element): Map[String, String] = Map(
    "Tag" -> element.tag,
    "XPath" -> element.xpath.chrome,
    "ID-XPath" -> element.xpath.idxpath,
    "Is button" -> element.isButton.toString,
    "Location" -> element.location.toString,
    "Dimensions" -> element.dimension.toString,
    "Text" -> element.getVisibleText.getOrElse(""),
    "Neighbour Text" -> element.neighbourText.getOrElse("")
  )

  case class AttributeStabilityCount(there: Int, same: Int, different: Int, removed: Int, added: Int):
    def incrementSame: AttributeStabilityCount = copy(same = same + 1, there = there + 1)
    def incrementDifferent: AttributeStabilityCount = copy(different = different + 1, there = there + 1)
    def incrementRemoved: AttributeStabilityCount = copy(removed = removed + 1, there = there + 1)
    def incrementAdded: AttributeStabilityCount = copy(added = added + 1, there = there + 1)

  private object AttributeStabilityCount:
    def empty: AttributeStabilityCount = AttributeStabilityCount(0, 0, 0, 0, 0)
