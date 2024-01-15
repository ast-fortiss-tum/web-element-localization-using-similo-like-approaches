package similo.comparing

object DimensionCompare:

  val area: ValueCompare[(Int, Int)] = ValueCompare("area", (a, b) => ratio(a._1 * a._2, b._1 * b._2))

  val perimeter: ValueCompare[(Int, Int)] =
    ValueCompare("perimeter", (a, b) => ratio(a._1 * 2 + a._2 * 2, b._1 * 2 + b._2 * 2))

  val aspectRatio: ValueCompare[(Int, Int)] = ValueCompare(
    "aspectRatio",
    (a, b) =>
      if a._1 == 0 || b._1 == 0 || a._2 == 0 || b._2 == 0 then 0.0
      else ratio(a._1.toDouble / a._2.toDouble, b._1.toDouble / b._2.toDouble)
  )

  private def ratio(a: Int, b: Int): Double =
    ratio(a.toDouble, b.toDouble)

  private def ratio(a: Double, b: Double): Double =
    if a == 0.0 || b == 0.0 then 0.0
    else math.min(a, b) / math.max(a, b)
