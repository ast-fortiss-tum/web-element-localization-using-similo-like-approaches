package similo.comparing

import com.github.benmanes.caffeine.cache.LoadingCache
import org.apache.commons.text.similarity.{JaccardSimilarity, JaroWinklerSimilarity, LevenshteinDistance}
import util.Utils

object StringCompare:
  
  val equal: ValueCompare[String] =
    ValueCompare[String]("equal", (left, right) => if left == right then 1 else 0)

  val equalIgnoreCase: ValueCompare[String] =
    ValueCompare[String]("equalIgnoreCase", (left, right) => if left.equalsIgnoreCase(right) then 1 else 0)

  val levenshtein: ValueCompare[String] =
    ValueCompare[String]("levenshtein", elseSetEqual(levenshteinDistance), cache = true)

  val jaccard: ValueCompare[String] =
    ValueCompare[String]("jaccard", elseSetEqual(jaccardSimilarity), cache = true)

  val jaroWinkler: ValueCompare[String] =
    ValueCompare[String]("jaroWinkler", elseSetEqual(jaroWinklerSimilarity), cache = true)

  val stringSet: ValueCompare[String] =
    ValueCompare[String]("stringSet", stringSetSimilarity, cache = true)

  private def equalIgnoreCase(left: String, right: String): Double =
    if left.equalsIgnoreCase(right) then 1 else 0

  private def levenshteinDistance(left: String, right: String): Double =
    val distance = new LevenshteinDistance(50).apply(left, right)
    val maxLen = Math.min(Math.max(left.length, right.length), 50)
    if maxLen == 0 then 1 else 1 - distance.toDouble / maxLen

  private def jaccardSimilarity(left: String, right: String): Double =
    new JaccardSimilarity().apply(left, right)

  private def jaroWinklerSimilarity(left: String, right: String): Double =
    new JaroWinklerSimilarity().apply(left, right)

  private def elseSetEqual(fn: (String, String) => Double): (String, String) => Double =
    (left, right) =>
      if left.length > 50 || right.length > 50
      then stringSetSimilarity(left, right)
      else fn(left, right)

  private def stringSetSimilarity(left: String, right: String): Double =
    val leftSet = left.split("( |\n)").toSet
    val rightSet = right.split("( |\n)").toSet
    val intersection = leftSet.intersect(rightSet)
    val union = leftSet.union(rightSet)
    intersection.size.toDouble / union.size

  private def optionSaveCompare(fn: (String, String) => Double): (Option[String], Option[String]) => Double =
    (left, right) =>
      (left, right) match
        case (Some(left), Some(right)) => fn(left, right)
        case _                         => 0

