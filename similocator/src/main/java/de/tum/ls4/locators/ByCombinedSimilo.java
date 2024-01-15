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
import java.util.NoSuchElementException;

import static de.tum.ls4.utils.Utils.checkedCastToList;
import static de.tum.ls4.utils.Utils.checkedCastToMap;

public class ByCombinedSimilo extends BySimilo<Overlap> {
    private static final List<BySimpleSimilo.PropertyComparison<?>> COMPARISON_SIMILO = List.of(
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
    private static final List<BySimpleSimilo.PropertyComparison<?>> COMPARISON_VON = List.of(
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
    private static final double TOTAL_WEIGHT = COMPARISON_SIMILO.stream()
            .mapToDouble(PropertyComparison::weight)
            .sum();
    private static final String SCRAPE_ELEMENT_FROM_WEB_ELEMENT = Javascript.create()
            .addFile("extractors/attributes.js")
            .addFile("extractors/css_styles.js")
            .addFile("extractors/xpath.js")
            .addFile("extractors/css_selector.js")
            .addFile("extractors/direct_text.js")
            .addFile("extractors/element.js")
            .addScript("return extractVisualOverlapFromWebElements(arguments[0]);")
            .getScript();

    ByCombinedSimilo(By by) {
        super(by, new VonSimiloStore());
    }

    private record Triple<A, B, C>(A a, B b, C c) {
    }

    @Override
    protected double normalizedScore(Overlap artifact, Overlap element) {
        List<Element> allArtifacts = artifact.allElements();
        List<Element> allElements = element.allElements();

        double maxScore = 0;
        for (Element artifactElement : allArtifacts) {
            for (Element elementElement : allElements) {
                double score = 0;
                for (PropertyComparison<?> propertyComparison : COMPARISON_VON) {
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
    protected List<WebElement> findElementWithSimilo() {
        List<Object> allElements = checkedCastToList(jsExecutor.executeScript(GET_ALL_ELEMENTS));

        List<Triple<Overlap, WebElement, Double>> allElementsWithScore = new ArrayList<>();

        for (Object element : allElements) {
            List<?> webElements = (List<?>) element;
            Object obj = this.scrapeArtifact((WebElement) webElements.get(1), jsExecutor);
            Overlap artifact = constructArtifact(checkedCastToMap(obj));
            double score = normalizedScore(this.artifactData.artifact(), artifact);
            allElementsWithScore.add(new Triple<>(artifact, (WebElement) webElements.get(1), score));
        }

        allElementsWithScore.sort((a, b) -> Double.compare(b.c(), a.c()));
        int listLength = allElementsWithScore.size();
        List<Triple<Overlap, WebElement, Double>> topTen = allElementsWithScore.subList(0, Math.min(10, listLength));

        double bestScore = 0;
        WebElement bestElement = null;

        for (Triple<Overlap, WebElement, Double> triple : topTen) {
            double score = 0;
            Element element = triple.a().target();

            for (BySimpleSimilo.PropertyComparison<?> propertyComparison : COMPARISON_SIMILO) {
                score += propertyComparison.score(this.artifactData.artifact().target(), element);
            }

            if (score > bestScore) {
                bestScore = score;
                bestElement = triple.b();
            }
        }

        if (bestElement == null) {
            throw new NoSuchElementException("No element found with "
                    + by + " in " + url + " with Similo! Could not repair broken locator.");
        }

        return List.of(bestElement);
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
