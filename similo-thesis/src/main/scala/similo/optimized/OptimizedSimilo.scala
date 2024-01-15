package similo.optimized

import similo.AttributeComparison
import similo.comparing.*
import similo.similo.Similo

object OptimizedSimilo
    extends Similo(
      AttributeComparison(_.tag, 0.8, StringCompare.jaccard),
      AttributeComparison(_.name, 2.85, StringCompare.levenshtein.forOption),
      AttributeComparison(_.id, 0.5, StringCompare.levenshtein.forOption),
      AttributeComparison(_.href, 0.95, StringCompare.equal.forOption),
      AttributeComparison(_.alt, 1.85, StringCompare.equal.forOption),
      AttributeComparison(_.typeName, 2.75, StringCompare.equal.forOption),
      AttributeComparison(_.ariaLabel, 0.9, StringCompare.jaccard.forOption),
      AttributeComparison(_.xpath.chrome, 0.1, StringCompare.jaccard),
      AttributeComparison(_.xpath.idxpath, 0.45, StringCompare.levenshtein),
      AttributeComparison(_.location, 1.2, LocationCompare.distanceMediumDecay),
      AttributeComparison(_.dimension, 0.35, DimensionCompare.area),
      AttributeComparison(_.getVisibleText, 2.80, StringCompare.levenshtein.forOption),
      AttributeComparison(_.neighbourText, 1.45, StringCompare.stringSet.forOption),
      AttributeComparison(_.attributes, 1.8, MapCompare.intersectValueCompare)
    )
