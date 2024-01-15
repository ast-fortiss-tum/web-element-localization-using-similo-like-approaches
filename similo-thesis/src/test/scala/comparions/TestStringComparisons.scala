package comparions

import org.scalatest.wordspec.AnyWordSpec
import similo.comparing.StringCompare.{jaccard, jaroWinkler, levenshtein}
import utils.TestUtils.~~

class TestStringComparisons extends AnyWordSpec:

  "Normalized Levenshtein distance" should {
    "be 1 for empty strings" in {
      assert(levenshtein.compare("", "") == 1.0)
    }
    "be 0 for strings with 1 character" in {
      assert(levenshtein.compare("a", "") == 0.0)
      assert(levenshtein.compare("", "a") == 0.0)
    }
    "be 0.5 for strings with 2 characters" in {
      assert(levenshtein.compare("a", "b") == 0.0)
      assert(levenshtein.compare("ab", "a") == 0.5)
      assert(levenshtein.compare("a", "ab") == 0.5)
    }
    "have appropriate values for strings with 2 characters" in {
      assert(levenshtein.compare("ab", "cd") == 0.0)
      assert(levenshtein.compare("ab", "ac") == 0.5)
      assert(levenshtein.compare("ab", "bc") == 0.0)
    }
    "have appropriate values for strings with 3 characters" in {
      assert(levenshtein.compare("abc", "def") == 0.0)
      assert(levenshtein.compare("abc", "ade") ~~ (1.0 / 3.0))
      assert(levenshtein.compare("abc", "bde") == 0.0)
      assert(levenshtein.compare("abc", "abd") ~~ (2.0 / 3.0))
      assert(levenshtein.compare("abc", "bbc") ~~ (2.0 / 3.0))
      assert(levenshtein.compare("abc", "bcc") ~~ (1.0 / 3.0))
    }
    "have appropriate values for strings with 4 characters" in {
      assert(levenshtein.compare("abcd", "efgh") == 0.0)
      assert(levenshtein.compare("abcd", "efgd") == 0.25)
      assert(levenshtein.compare("abcd", "efcd") == 0.5)
      assert(levenshtein.compare("abcd", "ebcd") == 0.75)
      assert(levenshtein.compare("abcd", "abcd") == 1.0)
    }
  }

  "Jaro Winkler distance" should {
    "be 1 for identical strings" in {
      assert(jaroWinkler.compare("", "") == 1.0)
      assert(jaroWinkler.compare("a", "a") == 1.0)
    }
    "be less than 1 for non-identical strings" in {
      assert(jaroWinkler.compare("a", "") < 1.0)
      assert(jaroWinkler.compare("", "a") < 1.0)
    }
    "be greater than 0 for strings with some similarities" in {
      assert(jaroWinkler.compare("a", "b") == 0.0)
      assert(jaroWinkler.compare("ab", "a") > 0.0)
      assert(jaroWinkler.compare("a", "ab") > 0.0)
    }
    "have appropriate values for strings with 2 characters" in {
      assert(jaroWinkler.compare("ab", "cd") == 0.0)
      assert(jaroWinkler.compare("ab", "ac") > 0.0)
      assert(jaroWinkler.compare("ab", "bc") == 0.0)
    }
    "have appropriate values for strings with 3 characters" in {
      assert(jaroWinkler.compare("abc", "def") == 0.0)
      assert(jaroWinkler.compare("abc", "ade") > 0.0)
      assert(jaroWinkler.compare("abc", "bde") == 0.0)
      assert(jaroWinkler.compare("abc", "abd") > 0.0)
      assert(jaroWinkler.compare("abc", "bbc") > 0.0)
      assert(jaroWinkler.compare("abc", "bcc") > 0.0)
    }
    "have appropriate values for strings with 4 characters" in {
      assert(jaroWinkler.compare("abcd", "efgh") == 0.0)
      assert(jaroWinkler.compare("abcd", "efgd") > 0.0)
      assert(jaroWinkler.compare("abcd", "efcd") > 0.0)
      assert(jaroWinkler.compare("abcd", "ebcd") > 0.0)
      assert(jaroWinkler.compare("abcd", "abcd") == 1.0)
    }
  }

  "Jaccard Similarity" should {
    "be 1 for identical strings" in {
      assert(jaccard.compare("", "") == 1.0)
      assert(jaccard.compare("a", "a") == 1.0)
    }
    "be less than 1 for non-identical strings" in {
      assert(jaccard.compare("a", "") < 1.0)
      assert(jaccard.compare("", "a") < 1.0)
    }
    "be greater than 0 for strings with some similarities" in {
      assert(jaccard.compare("a", "b") == 0.0)
      assert(jaccard.compare("ab", "a") > 0.0)
      assert(jaccard.compare("a", "ab") > 0.0)
    }
    "have appropriate values for strings with 2 characters" in {
      assert(jaccard.compare("ab", "cd") == 0.0)
      assert(jaccard.compare("ab", "ac") > 0.0)
      assert(jaccard.compare("ab", "bc") > 0.0)
    }
    "have appropriate values for strings with 3 characters" in {
      assert(jaccard.compare("abc", "def") == 0.0)
      assert(jaccard.compare("abc", "ade") > 0.0)
      assert(jaccard.compare("abc", "bde") > 0.0)
      assert(jaccard.compare("abc", "abd") > 0.0)
      assert(jaccard.compare("abc", "bbc") > 0.0)
      assert(jaccard.compare("abc", "bcc") > 0.0)
    }
    "have appropriate values for strings with 4 characters" in {
      assert(jaccard.compare("abcd", "efgh") == 0.0)
      assert(jaccard.compare("abcd", "efgd") > 0.0)
      assert(jaccard.compare("abcd", "efcd") > 0.0)
      assert(jaccard.compare("abcd", "ebcd") > 0.0)
      assert(jaccard.compare("abcd", "abcd") == 1.0)
    }
  }
