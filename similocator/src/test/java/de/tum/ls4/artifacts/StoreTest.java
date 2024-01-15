package de.tum.ls4.artifacts;

import org.openqa.selenium.By;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {
    private static final String URL_MAIN = "https://www.google.com";

    static <T extends Artifact> void testEmptyDatabase(
            Supplier<ArtifactStore<T>> storeSupplier,
            List<T> artifacts,
            List<String> identifiers
    ) {
        ArtifactStore<T> store = storeSupplier.get();

        By by = store.getOptimalLocator(URL_MAIN, identifiers.get(0));
        assertNull(by);

        ArtifactData<T> element = store.getArtifact(URL_MAIN, identifiers.get(0));
        assertNull(element);

        assertThrows(Exception.class, () -> store.addArtifactEvolution(URL_MAIN, identifiers.get(0), artifacts.get(0)));
        assertDoesNotThrow(() -> store.updateOptimalLocator(URL_MAIN, identifiers.get(0)));
        assertDoesNotThrow(() -> store.removeArtifact(URL_MAIN, identifiers.get(0)));
    }

    static <A extends Artifact> void testSimpleInsertAndRemove(
            Supplier<ArtifactStore<A>> storeSupplier,
            List<A> artifacts,
            List<String> identifiers,
            BiFunction<A, A, Double> comparison
    ) {

        ArtifactStore<A> store = storeSupplier.get();
        A artifact = artifacts.get(0);
        double score = comparison.apply(artifact, artifact);
        String identifier = identifiers.get(0);

        store.addArtifact(URL_MAIN, identifier, "xpath", artifact, score);

        // Locator works as expected
        By by = store.getOptimalLocator(URL_MAIN, identifier);
        assertNotNull(by);
        assertInstanceOf(By.ByXPath.class, by);
        assertEquals("xpath", ((By.ByXPath) by).getRemoteParameters().using());
        assertEquals(identifier, ((By.ByXPath) by).getRemoteParameters().value());

        // Element is stored correctly
        ArtifactData<A> element = store.getArtifact(URL_MAIN, identifier);
        assertNotNull(element);
        assertEquals(artifact, element.artifact());
        assertEquals(element.averageScore(), score, 0.001);

        // Element can be removed
        store.removeArtifact(URL_MAIN, identifier);
        assertNull(store.getArtifact(URL_MAIN, identifier));

        // Locator is removed
        assertNull(store.getOptimalLocator(URL_MAIN, identifier));
    }

    static <T extends Artifact> void testElementEvolution(
            Supplier<ArtifactStore<T>> storeSupplier,
            List<T> artifacts,
            List<String> identifiers,
            BiFunction<T, T, Double> comparison
    ) {

        ArtifactStore<T> store = storeSupplier.get();

        T artifact1 = artifacts.get(2);
        double sefScore1 = comparison.apply(artifact1, artifact1);
        String identifier1 = identifiers.get(2);

        T artifact2 = artifacts.get(3);
        T artifact3 = artifacts.get(4);
        T artifact4 = artifacts.get(5);

        store.addArtifact(URL_MAIN, identifier1, "xpath", artifact1, sefScore1);

        // Element is stored correctly
        ArtifactData<T> element = store.getArtifact(URL_MAIN, identifier1);
        assertNotNull(element);
        assertEquals(artifact1, element.artifact());
        assertEquals(element.averageScore(), sefScore1, 0.001);

        // Element can be evolved
        store.addArtifactEvolution(URL_MAIN, identifier1, artifact2);
        element = store.getArtifact(URL_MAIN, identifier1);
        assertNotNull(element);
        assertEquals(artifact2, element.artifact());
        assertEquals(element.averageScore(), sefScore1, 0.001);

        // Element can be evolved again
        store.addArtifactEvolution(URL_MAIN, identifier1, artifact3);
        element = store.getArtifact(URL_MAIN, identifier1);
        assertNotNull(element);
        assertEquals(artifact3, element.artifact());
        assertEquals(element.averageScore(), sefScore1, 0.001);

        // Element can be evolved again
        store.addArtifactEvolution(URL_MAIN, identifier1, artifact4);
        element = store.getArtifact(URL_MAIN, identifier1);
        assertNotNull(element);
        assertEquals(artifact4, element.artifact());
        assertEquals(element.averageScore(), sefScore1, 0.001);

        // Evolved element can be removed
        store.removeArtifact(URL_MAIN, identifier1);
        assertNull(store.getArtifact(URL_MAIN, identifier1));

        // Evolved element locator is removed
        assertNull(store.getOptimalLocator(URL_MAIN, identifier1));
    }

    static <T extends Artifact> void testOptimizeLocatorStrategy(
            Supplier<ArtifactStore<T>> storeSupplier,
            List<T> artifacts,
            List<String> identifiers,
            BiFunction<T, T, Double> comparison
    ) {

        ArtifactStore<T> store = storeSupplier.get();

        store.addArtifact(URL_MAIN, identifiers.get(0), "xpath", artifacts.get(0));

        // Locator works as expected
        By by = store.getOptimalLocator(URL_MAIN, identifiers.get(0));
        assertNotNull(by);
        assertInstanceOf(By.ByXPath.class, by);
        assertEquals("xpath", ((By.ByXPath) by).getRemoteParameters().using());
        assertEquals(identifiers.get(0), ((By.ByXPath) by).getRemoteParameters().value());

        // Element is stored correctly
        ArtifactData<T> element = store.getArtifact(URL_MAIN, identifiers.get(0));
        assertNotNull(element);
        assertEquals(artifacts.get(0), element.artifact());
        assertEquals(element.averageScore(), 0.0, 0.001);

        // Add changed locators and update optimal locator
        store.addArtifactEvolution(URL_MAIN, identifiers.get(0), artifacts.get(1));
        store.updateOptimalLocator(URL_MAIN, identifiers.get(0));

        // Locator works as expected
        by = store.getOptimalLocator(URL_MAIN, identifiers.get(0));
        assertNotNull(by);
        assertInstanceOf(By.ById.class, by);
        assertEquals("id", ((By.ById) by).getRemoteParameters().using());
        assertEquals("div-id", ((By.ById) by).getRemoteParameters().value());

        // Element is stored correctly
        element = store.getArtifact(URL_MAIN, identifiers.get(0));
        assertNotNull(element);
        assertEquals(artifacts.get(1), element.artifact());
        assertEquals(element.averageScore(), 0.0, 0.001);

        // Add changed locators and update optimal locator
        store.addArtifactEvolution(URL_MAIN, identifiers.get(0), artifacts.get(2));
        store.updateOptimalLocator(URL_MAIN, identifiers.get(0));

        // Element is stored correctly
        element = store.getArtifact(URL_MAIN, identifiers.get(0));
        assertNotNull(element);
        assertEquals(artifacts.get(2), element.artifact());
        assertEquals(element.averageScore(), 0.0, 0.001);

        // Add changed locators and update optimal locator
        store.addArtifactEvolution(URL_MAIN, identifiers.get(0), artifacts.get(3));
        store.updateOptimalLocator(URL_MAIN, identifiers.get(0));

        // Locator works as expected
        by = store.getOptimalLocator(URL_MAIN, identifiers.get(0));
        assertNotNull(by);
        assertInstanceOf(By.ByXPath.class, by);
        assertEquals("xpath", ((By.ByXPath) by).getRemoteParameters().using());
        assertEquals("/html/body/div/div[4]/span/div", ((By.ByXPath) by).getRemoteParameters().value());

        // Element is stored correctly
        element = store.getArtifact(URL_MAIN, identifiers.get(0));
        assertNotNull(element);
        assertEquals(artifacts.get(3), element.artifact());
        assertEquals(element.averageScore(), 0.0, 0.001);

        // Remove element
        store.removeArtifact(URL_MAIN, identifiers.get(0));
        assertNull(store.getArtifact(URL_MAIN, identifiers.get(0)));
        assertNull(store.getOptimalLocator(URL_MAIN, identifiers.get(0)));
    }
}
