package similo.comparing

object LocationCompare:

  private final val HEIGHT = 1080
  private final val WIDTH = 1920
  private final val MAX_DISTANCE = Math.sqrt(Math.pow(HEIGHT, 2) + Math.pow(WIDTH, 2))

  val distanceLinear: ValueCompare[(Int, Int)] =
    ValueCompare("distanceLinear", (left, right) => Math.max(0, 1 - distance(left, right) / MAX_DISTANCE))

  val distanceSmallDecay: ValueCompare[(Int, Int)] =
    ValueCompare("distanceSmallDecay", (left, right) => distanceDecay(left, right, 0.001))

  val distanceMediumDecay: ValueCompare[(Int, Int)] =
    ValueCompare("distanceMediumDecay", (left, right) => distanceDecay(left, right, 0.005))

  val distanceLargeDecay: ValueCompare[(Int, Int)] =
    ValueCompare("distanceLargeDecay", (left, right) => distanceDecay(left, right, 0.01))

  val distanceManhattan: ValueCompare[(Int, Int)] = ValueCompare(
    "distanceManhattan",
    (left, right) => Math.max(0, 1 - (Math.abs(left._1 - right._1) + Math.abs(left._2 - right._2)) / (MAX_DISTANCE))
  )

  private def distanceDecay(left: (Int, Int), right: (Int, Int), lambda: Double): Double =
    Math.exp(-lambda * distance(left, right))

  private def distance(left: (Int, Int), right: (Int, Int)): Double =
    Math.sqrt(Math.pow(left._1 - right._1, 2) + Math.pow(left._2 - right._2, 2))
