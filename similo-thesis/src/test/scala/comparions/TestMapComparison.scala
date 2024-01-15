package comparions

import org.scalatest.wordspec.AnyWordSpec
import similo.comparing.MapCompare.intersectValueCompare

class TestMapComparison extends AnyWordSpec:

  "Map comparison" should {
    "be able to compare two maps" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("a" -> "1", "b" -> "2")
      assert(intersectValueCompare.compare(map1, map2) == 1)
    }

    "be able to compare two maps with different values" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("a" -> "1", "b" -> "3")
      assert(intersectValueCompare.compare(map1, map2) == 0.5)
    }

    "be able to compare two maps with different keys" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("a" -> "1", "c" -> "2")
      assert(intersectValueCompare.compare(map1, map2) == 0.5)
    }

    "be able to compare two maps with different keys and values" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("a" -> "1", "c" -> "3")
      assert(intersectValueCompare.compare(map1, map2) == 0.5)
    }

    "be able to compare maps with no overlap" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("c" -> "1", "d" -> "2")
      assert(intersectValueCompare.compare(map1, map2) == 0)
    }

    "be able to compare maps with no overlap and different values" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("c" -> "1", "d" -> "3")
      assert(intersectValueCompare.compare(map1, map2) == 0)
    }

    "be able to compare maps with same keys but different values" in {
      val map1 = Map("a" -> "1", "b" -> "2")
      val map2 = Map("a" -> "2", "b" -> "1")
      assert(intersectValueCompare.compare(map1, map2) == 0)
    }

  }
