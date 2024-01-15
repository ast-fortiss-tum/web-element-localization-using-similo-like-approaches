package de.tum.ls4.locators;

import de.tum.ls4.Javascript;
import de.tum.ls4.artifacts.Artifact;
import de.tum.ls4.artifacts.ArtifactData;
import de.tum.ls4.artifacts.ArtifactStore;
import de.tum.ls4.artifacts.Element;
import de.tum.ls4.utils.Utils;
import org.openqa.selenium.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static de.tum.ls4.utils.Utils.*;

public abstract class BySimilo<A extends Artifact> extends By {

    private static final Javascript GET_ELEMENTS = Javascript.create()
            .addFile("extractors/attributes.js")
            .addFile("extractors/css_styles.js")
            .addFile("extractors/xpath.js")
            .addFile("extractors/css_selector.js")
            .addFile("extractors/direct_text.js")
            .addFile("extractors/element.js");
    protected static final String GET_ALL_ELEMENTS = GET_ELEMENTS
            .addScript("return extractAllElements();")
            .getScript();

    protected final By by;
    protected final ArtifactStore<A> artifactStore;
    protected SearchContext context;
    protected WebDriver webDriver;
    protected JavascriptExecutor jsExecutor;
    protected String url;
    protected String identifier;
    protected ArtifactData<A> artifactData;

    public BySimilo(By by, ArtifactStore<A> artifactStore) {
        this.by = by;
        this.artifactStore = artifactStore;
    }

    /**
     * Extracts the parameters of locator used in the By instance.
     * e.g. By.id("foo") -> "foo"
     *
     * @return The parameter of the locator.
     */
    private String byIdentifier() {
        if (this.by instanceof By.Remotable) {
            Object identifier = ((By.Remotable) this.by).getRemoteParameters().value();
            if (identifier instanceof String) {
                return (String) identifier;
            } else {
                throw new IllegalArgumentException("BySimilo can only be used with " +
                        "Remotable By instances with String identifiers");
            }
        } else {
            throw new IllegalArgumentException("BySimilo can only be used with " +
                    "Remotable By instances");
        }
    }

    private double calculateAndUpdateScore(A artifact) {

        double normalizedScore = normalizedScore(this.artifactData.artifact(), artifact);
        double averageScore = this.artifactData.averageScore();
        int numberOfLocalizations = Math.max(this.artifactData.numberOfLocalizations(), 10);

        double newAverageScore = (averageScore * numberOfLocalizations + normalizedScore) / (numberOfLocalizations + 1);

        artifactStore.updateAverageScore(url, identifier, newAverageScore);

        return normalizedScore;
    }

    private void updateOrWarn(double score, A foundArtifact) {
        double difference = Math.abs(score - this.artifactData.averageScore());

        if (difference > 0.1) {
            artifactStore.addArtifactEvolution(url, identifier, foundArtifact);
        }

        if (difference > getConfig("similo.warning_threshold", 0.2)) {
            System.err.println("Warning: " + by + " in " + url + " has changed significantly!");
        }
    }

    private List<WebElement> useActiveLocator() {
        try {
            return context.findElements(artifactData.optimalBy());
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private List<WebElement> extractFromBrowser() {
        List<WebElement> initialWebElements = context.findElements(by);

        if (initialWebElements.isEmpty()) {
            throw new NoSuchElementException("No element found with "
                    + by + " in " + url + "! Could not initialize.");
        }

        return initialWebElements;
    }

    private A initialLocalisation(JavascriptExecutor jsExecutor, WebElement webElement) {
        Object obj = this.scrapeArtifact(webElement, jsExecutor);
        if (obj instanceof Map<?, ?> rawMap) {
            return constructArtifact(checkedCastToMap(rawMap));
        } else {
            throw new IllegalArgumentException("Initial localisation did not return a map");
        }
    }

    protected record PropertyComparison<T>(Function<Element, T> property,
                                           BiFunction<T, T, Double> comparison,
                                           double weight) {
        double score(Element artifact, Element element) {
            T artifactProperty = property.apply(artifact);
            T elementProperty = property.apply(element);
            return comparison.apply(artifactProperty, elementProperty) * weight;
        }
    }

    protected List<WebElement> findElementWithSimilo() {
        List<Object> allElements = checkedCastToList(jsExecutor.executeScript(GET_ALL_ELEMENTS));

        double bestScore = 0;
        WebElement bestElement = null;

        for (Object element : allElements) {
            List<?> webElements = (List<?>) element;
            Object obj = this.scrapeArtifact((WebElement) webElements.get(1), jsExecutor);
            A artifact = constructArtifact(checkedCastToMap(obj));
            double score = normalizedScore(this.artifactData.artifact(), artifact);
            if (score > bestScore) {
                bestScore = score;
                bestElement = (WebElement) webElements.get(1);
            }
        }

        if (bestElement == null) {
            throw new NoSuchElementException("No element found with "
                    + by + " in " + url + " with Similo! Could not repair broken locator.");
        }

        return List.of(bestElement);
    }

    protected abstract double normalizedScore(A artifact, A element);

    protected abstract A constructArtifact(Map<String, Object> map);

    protected abstract Object scrapeArtifact(WebElement webElement, JavascriptExecutor jsExecutor);

    @Override
    public List<WebElement> findElements(SearchContext context) {

        this.context = context;
        this.webDriver = getWebDriver(context);
        this.jsExecutor = getJavascriptExecutor(context);

        this.url = this.webDriver.getCurrentUrl();
        this.identifier = byIdentifier();

        if (Utils.getConfig("similo.deactivated",false)) {
            return context.findElements(by);
        }

        this.artifactData = artifactStore.getArtifact(url, identifier);

        if (artifactData == null) {

            artifactData = artifactStore.getArtifact(url, identifier, false);

            A artifact;
            List<WebElement> webElements;

            if (artifactData == null) {
                webElements = extractFromBrowser();
                artifact = initialLocalisation(jsExecutor, webElements.get(0));
            } else {
                webElements = useActiveLocator();
                artifact = artifactData.artifact();
            }

            double selfScore = normalizedScore(artifact, artifact);
            artifactStore.addArtifact(url, identifier, getByName(by), artifact, selfScore);

            return webElements;
        }

        List<WebElement> foundByLocator = useActiveLocator();

        if (foundByLocator == null || foundByLocator.isEmpty()) {
            foundByLocator = findElementWithSimilo();
        }

        Object foundElement = scrapeArtifact(foundByLocator.get(0), jsExecutor);
        A foundArtifact = constructArtifact(checkedCastToMap(foundElement));

        double score = calculateAndUpdateScore(foundArtifact);
        updateOrWarn(score, foundArtifact);

        return foundByLocator;
    }
}
