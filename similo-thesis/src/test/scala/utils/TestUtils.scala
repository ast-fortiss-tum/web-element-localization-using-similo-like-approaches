package utils

import scala.annotation.targetName

object TestUtils:

  extension (d: Double)
    @targetName("approxEqual")
    def ~~(other: Double): Boolean =
      if (d - other).abs < 1e-3 then true
      else
        println(s"Expected $d to be approximately equal to $other")
        false

    def between(lower: Double, upper: Double): Boolean =
      if lower > upper then between(upper, lower)
      else if d >= lower && d <= upper then true
      else
        println(s"Expected $d to be between $lower and $upper")
        false
