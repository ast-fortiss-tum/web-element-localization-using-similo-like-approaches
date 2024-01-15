package de.tum.ls4.locators;

import de.tum.ls4.artifacts.Element;
import de.tum.ls4.artifacts.Overlap;

public class LocatorUtils {
    public static double similoScore(Element element, Element artifact) {
        BySimpleSimilo locator = new BySimpleSimilo(null);
        return locator.normalizedScore(artifact, element);
    }

    public static double vonSimiloScore(Overlap overlap, Overlap artifact) {
        ByVonSimilo locator = new ByVonSimilo(null);
        return locator.normalizedScore(artifact, overlap);
    }
}
