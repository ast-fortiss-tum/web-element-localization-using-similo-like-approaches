package similo.comparing

import legacy.LegacyComparisons.{compareDistance, compareEqual, compareIntegerDistance, containsWord}

object LegacyCompare:

  val distanceProcessedString: ValueCompare[String] = ValueCompare("editDistance", compareDistanceProcessed)
  val distanceProcessedOption: ValueCompare[Option[String]] = ValueCompare("editDistance", compareDistanceProcessed)
  val equalString: ValueCompare[String] = ValueCompare("equal", compareEqualProcessed)
  val equalBoolean: ValueCompare[Boolean] = ValueCompare("equal", compareEqualProcessed)
  val distanceProcessedInt: ValueCompare[Int] = ValueCompare("editDistance", compareIntegerDistanceProcessed)
  val distanceProcessedTuple: ValueCompare[(Int, Int)] = ValueCompare("editDistance", compareDistanceProcessed)
  val stringSet: ValueCompare[String] = ValueCompare("stringSet", stringSetCompare)

  def compareEqualProcessed(t1: Boolean, t2: Boolean): Double =
    if t1 == t2 then 1 else 0

  def compareEqualProcessed(t1: String, t2: String): Double =
    compareEqualProcessed(Option(t1), Option(t2))

  def compareEqualProcessed(t1: Option[String], t2: Option[String]): Double =
    if t1.isDefined && t2.isDefined && t1.nonEmpty && t2.nonEmpty then compareEqual(t1.get, t2.get, 1)
    else 0

  def compareDistanceProcessed(t1: String, t2: String): Double =
    compareDistanceProcessed(Option(t1), Option(t2))

  def compareDistanceProcessed(t1: Option[String], t2: Option[String]): Double =
    if t1.isDefined && t2.isDefined && t1.nonEmpty && t2.nonEmpty then
      val trimmedT1 = if t1.get.length > 50 then t1.get.substring(0, 49) else t1.get
      val trimmedT2 = if t2.get.length > 50 then t2.get.substring(0, 49) else t2.get
      compareDistance(trimmedT1, trimmedT2, 100) / 100.0
    else 0

  def compareIntegerDistanceProcessed(t1: Int, t2: Int): Double =
    compareIntegerDistance(t1, t2, 100) / 100.0

  def compareDistanceProcessed(t1: (Int, Int), t2: (Int, Int)): Double =
    val dx = t1._1 - t2._1
    val dy = t1._2 - t2._2
    val pixelDistance = Math.sqrt(dx * dx + dy * dy).toInt
    Math.max(100 - pixelDistance, 0).toDouble / 100.0

  def stringSetCompare(t1: String, t2: String): Double =
    if t1.isEmpty || t2.isEmpty then return 0

    val words1 = t1.split("\\s+")
    val words2 = t2.split("\\s+")

    var existsCount = 0
    val wordCount = Math.max(t1.length - words1.length + 1, t2.length - words2.length + 1)
    for word1 <- words1 do if containsWord(word1, words2) then existsCount += word1.length
    val score = Math.min((existsCount * 100.0) / wordCount, 100)
    return score / 100.0
