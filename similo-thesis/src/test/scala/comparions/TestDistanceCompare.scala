package comparions

import org.scalatest.wordspec.AnyWordSpec
import similo.comparing.LocationCompare.{distanceLargeDecay, distanceLinear, distanceMediumDecay, distanceSmallDecay}
import utils.TestUtils.{between, ~~}

class TestDistanceCompare extends AnyWordSpec:

  "Linear distance compare" should {
    "return 1 for the same point" in {
      assert(distanceLinear.compare((0, 0), (0, 0)) == 1)
    }

    "return 0 for super far away points" in {
      assert(distanceLinear.compare((0, 0), (2000, 2000)) == 0)
    }

    "return something between 0.3 and 0.4 for half away points" in {
      assert(distanceLinear.compare((0, 0), (1000, 1000)).between(0.4, 0.3))
    }

    "return something between 0.6 and 0.7 for quarter away points" in {
      assert(distanceLinear.compare((0, 0), (500, 500)).between(0.7, 0.6))
    }
  }

  "Small decay compare" should {
    "return 1 for the same point" in {
      assert(distanceSmallDecay.compare((0, 0), (0, 0)) == 1)
    }

    "return a high score for near points" in {
      assert(distanceSmallDecay.compare((0, 0), (50, 50)) > 0.9)
    }

    "return a low score for very far away points" in {
      assert(distanceSmallDecay.compare((0, 0), (2000, 2000)) < 0.1)
    }

    "return a score between 0.2 and 0.3 for halfway points" in {
      assert(distanceSmallDecay.compare((0, 0), (1000, 1000)).between(0.2, 0.3))
    }

    "return a score between 0.4 and 0.5 for quarter-way points" in {
      assert(distanceSmallDecay.compare((0, 0), (500, 500)).between(0.4, 0.5))
    }
  }

  "Medium decay compare" should {
    "return 1 for the same point" in {
      assert(distanceMediumDecay.compare((0, 0), (0, 0)) == 1)
    }

    "return a moderate score for near points" in {
      assert(distanceMediumDecay.compare((0, 0), (50, 50)) > 0.7)
    }

    "return a very low score for very far away points" in {
      assert(distanceMediumDecay.compare((0, 0), (2000, 2000)) < 0.05)
    }

    "return a score around 0 for halfway points" in {
      assert(distanceMediumDecay.compare((0, 0), (1000, 1000)) ~~ 0)
    }

    "return a score smaller than 0.05 for quarter-way points" in {
      assert(distanceMediumDecay.compare((0, 0), (500, 500)) < 0.05)
    }
  }

  "Large decay compare" should {
    "return 1 for the same point" in {
      assert(distanceLargeDecay.compare((0, 0), (0, 0)) == 1)
    }

    "return a low score for near points" in {
      assert(distanceLargeDecay.compare((0, 0), (50, 50)) < 0.5)
    }

    "return an extremely low score for very far away points" in {
      assert(distanceLargeDecay.compare((0, 0), (2000, 2000)) ~~ 0)
    }

    "return a score close to 0 for halfway points" in {
      assert(distanceLargeDecay.compare((0, 0), (1000, 1000)) ~~ 0)
    }

    "return a score slightly above 0 for quarter-way points" in {
      assert(distanceLargeDecay.compare((0, 0), (500, 500)).between(0.0, 0.05))
    }
  }
