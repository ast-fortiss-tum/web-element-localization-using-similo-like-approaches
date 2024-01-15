package similo.legacy

import similo.AttributeComparison
import similo.comparing.LegacyCompare.*
import similo.von.VonSimilo

object LegacyVonSimilo
    extends VonSimilo(
      Seq(
        AttributeComparison(_.tag, 1.5, equalString),
        AttributeComparison(_.className, 0.5, distanceProcessedOption),
        AttributeComparison(_.name, 1.5, equalString.forOption),
        AttributeComparison(_.id, 1.5, equalString.forOption),
        AttributeComparison(_.href, 0.5, distanceProcessedOption),
        AttributeComparison(_.alt, 0.5, distanceProcessedOption),
        AttributeComparison(_.xpath.selenium, 0.5, distanceProcessedString),
        AttributeComparison(_.xpath.idxpath, 0.5, distanceProcessedString),
        AttributeComparison(_.isButton, 0.5, equalBoolean),
        AttributeComparison(_.location, 0.5, distanceProcessedTuple),
        AttributeComparison(_.area, 0.5, distanceProcessedInt),
        AttributeComparison(_.shape, 0.5, distanceProcessedInt),
        AttributeComparison(_.getVisibleText, 1.5, distanceProcessedOption),
        AttributeComparison(_.neighbourText, 1.5, stringSet.forOption)
      )
    )
