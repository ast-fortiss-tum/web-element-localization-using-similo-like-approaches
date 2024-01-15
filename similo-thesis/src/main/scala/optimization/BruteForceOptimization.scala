package optimization

import evalutation.EvaluationComputing
import similo.Locator

case class BruteForceOptimization[T](
  inputSimilo: Seq[T] => Locator,
  possibleValues: Seq[Seq[T]],
  unit: T
):

  def compute(): Unit =
    val results = combinations.map { combination =>
      val locator = inputSimilo(combination)
      val score = EvaluationComputing.computeAll(locator).score
      val nrUnit = combination.count(_ == unit)
      println(s"$score," + combination.mkString(",") + s",$nrUnit")
      (combination, score)
    }
    val best = results.maxBy(_._2)
    println(s"Best combination: ${best._1}")
    println(s"Best score: ${best._2}")


  private def combinations: Seq[Seq[T]] =
    possibleValues.foldLeft(Seq(Seq.empty[T])) { (acc, values) =>
      acc.flatMap { combination =>
        values.map { value =>
          combination :+ value
        }
      }
    }
