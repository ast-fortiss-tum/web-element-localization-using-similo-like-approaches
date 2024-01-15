package similo.optimized

import similo.AttributeComparison
import similo.comparing.*
import similo.similo.Similo


object OptimizedMinimalSimilo
  extends Similo(
    AttributeComparison(_.tag, 0.8, StringCompare.jaccard),
    AttributeComparison(_.name, 2.85, StringCompare.levenshtein.forOption),
    AttributeComparison(_.href, 0.65, StringCompare.equal.forOption),
    AttributeComparison(_.alt, 1.75, StringCompare.equal.forOption),
    AttributeComparison(_.typeName, 0.6, StringCompare.equal.forOption),
    AttributeComparison(_.ariaLabel, 0.95, StringCompare.jaccard.forOption),
    AttributeComparison(_.xpath.idxpath, 0.35, StringCompare.levenshtein),
    AttributeComparison(_.location, 0.95, LocationCompare.distanceMediumDecay),
    AttributeComparison(_.dimension, 0.35, DimensionCompare.area),
    AttributeComparison(_.getVisibleText, 2.70, StringCompare.levenshtein.forOption),
    AttributeComparison(_.neighbourText, 1.45, StringCompare.stringSet.forOption),
    AttributeComparison(_.attributes, 1.95, MapCompare.intersectValueCompare)
  )