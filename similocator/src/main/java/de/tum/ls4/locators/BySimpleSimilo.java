package de.tum.ls4.locators;

import de.tum.ls4.Javascript;
import de.tum.ls4.artifacts.Element;
import de.tum.ls4.artifacts.SimpleSimiloStore;
import de.tum.ls4.locators.comparisons.DimensionComparisons;
import de.tum.ls4.locators.comparisons.LocationComparisons;
import de.tum.ls4.locators.comparisons.MapComparisons;
import de.tum.ls4.locators.comparisons.StringComparisons;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class BySimpleSimilo extends BySimilo<Element> {
    private static final String SCRAPE_ELEMENT_FROM_WEB_ELEMENT = Javascript.create()
            .addFile("extractors/attributes.js")
            .addFile("extractors/css_styles.js")
            .addFile("extractors/xpath.js")
            .addFile("extractors/css_selector.js")
            .addFile("extractors/direct_text.js")
            .addFile("extractors/element.js")
            .addScript("return extract_element_from_web_element(arguments[0]);")
            .getScript();
    private static final List<PropertyComparison<?>> PROPERTY_COMPARISONS = List.of(
            new PropertyComparison<>(Element::tag, StringComparisons::jaccard, 0.8),
            new PropertyComparison<>(Element::name, StringComparisons::levenshtein, 2.85),
            new PropertyComparison<>(Element::id, StringComparisons::levenshtein, 0.5),
            new PropertyComparison<>(Element::href, StringComparisons::equality, 0.95),
            new PropertyComparison<>(Element::alt, StringComparisons::equality, 1.85),
            new PropertyComparison<>(Element::typeName, StringComparisons::equality, 2.75),
            new PropertyComparison<>(Element::ariaLabel, StringComparisons::jaccard, 0.9),
            new PropertyComparison<>(Element::xpathChrome, StringComparisons::jaccard, 0.1),
            new PropertyComparison<>(Element::xpathId, StringComparisons::levenshtein, 0.45),
            new PropertyComparison<>(Element::location, LocationComparisons::distanceDecayMedium, 1.2),
            new PropertyComparison<>(Element::dimensions, DimensionComparisons::area, 0.35),
            new PropertyComparison<>(Element::visibleText, StringComparisons::levenshtein, 2.8),
            new PropertyComparison<>(Element::neighborText, StringComparisons::stringSet, 1.45),
            new PropertyComparison<>(Element::attributes, MapComparisons::intersectValueCompare, 1.8)
    );
    private static final double TOTAL_WEIGHT = PROPERTY_COMPARISONS.stream()
            .mapToDouble(PropertyComparison::weight)
            .sum();

    public BySimpleSimilo(By by) {
        super(by, new SimpleSimiloStore());
    }

    @Override
    protected double normalizedScore(Element artifact, Element element) {
        double score = 0;
        for (PropertyComparison<?> propertyComparison : PROPERTY_COMPARISONS) {
            score += propertyComparison.score(artifact, element);
        }
        return score / TOTAL_WEIGHT;
    }

    @Override
    protected Object scrapeArtifact(WebElement webElement, JavascriptExecutor jsExecutor) {
        return jsExecutor.executeScript(SCRAPE_ELEMENT_FROM_WEB_ELEMENT, webElement);
    }

    @Override
    protected Element constructArtifact(Map<String, Object> map) {
        return new Element(map);
    }
}
