package de.tum.ls4.locators;

import de.tum.ls4.Javascript;
import de.tum.ls4.artifacts.Element;
import de.tum.ls4.artifacts.Overlap;
import de.tum.ls4.artifacts.VonSimiloStore;
import de.tum.ls4.locators.comparisons.DimensionComparisons;
import de.tum.ls4.locators.comparisons.LocationComparisons;
import de.tum.ls4.locators.comparisons.MapComparisons;
import de.tum.ls4.locators.comparisons.StringComparisons;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.tum.ls4.utils.Utils.checkedCastToList;
import static de.tum.ls4.utils.Utils.checkedCastToMap;

public class ByVonSimilo extends BySimilo<Overlap> {
    private static final String SCRAPE_ELEMENT_FROM_WEB_ELEMENT = Javascript.create()
            .addFile("extractors/attributes.js")
            .addFile("extractors/css_styles.js")
            .addFile("extractors/xpath.js")
            .addFile("extractors/css_selector.js")
            .addFile("extractors/direct_text.js")
            .addFile("extractors/element.js")
            .addScript("return extractVisualOverlapFromWebElements(arguments[0]);")
            .getScript();
    private static final List<BySimpleSimilo.PropertyComparison<?>> PROPERTY_COMPARISONS = List.of(
            new BySimpleSimilo.PropertyComparison<>(Element::tag, StringComparisons::jaccard, 0.8),
            new BySimpleSimilo.PropertyComparison<>(Element::name, StringComparisons::levenshtein, 2.85),
            new BySimpleSimilo.PropertyComparison<>(Element::id, StringComparisons::levenshtein, 0.5),
            new BySimpleSimilo.PropertyComparison<>(Element::href, StringComparisons::equality, 0.95),
            new BySimpleSimilo.PropertyComparison<>(Element::alt, StringComparisons::equality, 1.85),
            new BySimpleSimilo.PropertyComparison<>(Element::typeName, StringComparisons::equality, 2.75),
            new BySimpleSimilo.PropertyComparison<>(Element::ariaLabel, StringComparisons::jaccard, 0.9),
            new BySimpleSimilo.PropertyComparison<>(Element::xpathChrome, StringComparisons::jaccard, 0.1),
            new BySimpleSimilo.PropertyComparison<>(Element::xpathId, StringComparisons::levenshtein, 0.45),
            new BySimpleSimilo.PropertyComparison<>(Element::location, LocationComparisons::distanceDecayMedium, 1.2),
            new BySimpleSimilo.PropertyComparison<>(Element::dimensions, DimensionComparisons::area, 0.35),
            new BySimpleSimilo.PropertyComparison<>(Element::visibleText, StringComparisons::levenshtein, 2.8),
            new BySimpleSimilo.PropertyComparison<>(Element::neighborText, StringComparisons::stringSet, 1.45),
            new BySimpleSimilo.PropertyComparison<>(Element::attributes, MapComparisons::intersectValueCompare, 1.8)
    );
    private static final double TOTAL_WEIGHT = PROPERTY_COMPARISONS.stream()
            .mapToDouble(PropertyComparison::weight)
            .sum();

    public ByVonSimilo(By by) {
        super(by, new VonSimiloStore());
    }

    @Override
    protected double normalizedScore(Overlap artifact, Overlap element) {
        List<Element> allArtifacts = artifact.allElements();
        List<Element> allElements = element.allElements();

        double maxScore = 0;
        for (Element artifactElement : allArtifacts) {
            for (Element elementElement : allElements) {
                double score = 0;
                for (PropertyComparison<?> propertyComparison : PROPERTY_COMPARISONS) {
                    score += propertyComparison.score(artifactElement, elementElement);
                }
                score /= TOTAL_WEIGHT;
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }

        return maxScore;
    }

    @Override
    protected Object scrapeArtifact(WebElement webElement, JavascriptExecutor jsExecutor) {
        return jsExecutor.executeScript(SCRAPE_ELEMENT_FROM_WEB_ELEMENT, webElement);
    }

    @Override
    protected Overlap constructArtifact(Map<String, Object> map) {
        Element element = new Element(checkedCastToMap(map.get("target")));
        List<Object> overlapsObjects = checkedCastToList(map.get("overlap"));
        List<Element> overlaps = new ArrayList<>();
        for (Object overlapObject : overlapsObjects) {
            overlaps.add(new Element(checkedCastToMap(overlapObject)));
        }
        return new Overlap(element, overlaps);
    }
}
