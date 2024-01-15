package similo.optimized

import similo.AttributeComparison
import similo.comparing.*
import similo.von.VonSimilo

object OptimizedVonSimilo
    extends VonSimilo(
      Seq(
        AttributeComparison(_.tag, 0.8, StringCompare.jaroWinkler),
        AttributeComparison(_.className, 0.35, StringCompare.equal.forOption),
        AttributeComparison(_.name, 0.35, StringCompare.equal.forOption),
        AttributeComparison(_.id, 2.9, StringCompare.equal.forOption),
        AttributeComparison(_.href, 1.9, StringCompare.equal.forOption),
        AttributeComparison(_.alt, 1.55, StringCompare.equal.forOption),
        AttributeComparison(_.typeName, 1.25, StringCompare.equal.forOption),
        AttributeComparison(_.ariaLabel, 2.1, StringCompare.jaccard.forOption),
        AttributeComparison(_.xpath.chrome, 0.95, StringCompare.equal),
        AttributeComparison(_.xpath.idxpath, 0.7, StringCompare.levenshtein),
        AttributeComparison(_.isButton, 0.0, ValueCompare.equals),
        AttributeComparison(_.location, 2.4, LocationCompare.distanceLinear),
        AttributeComparison(_.dimension, 1.85, DimensionCompare.area),
        AttributeComparison(_.getVisibleText, 2.55, StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, 1.95, StringCompare.stringSet.forOption),
        AttributeComparison(_.attributes, 0.85, MapCompare.intersectValueCompare)
      )
    )
