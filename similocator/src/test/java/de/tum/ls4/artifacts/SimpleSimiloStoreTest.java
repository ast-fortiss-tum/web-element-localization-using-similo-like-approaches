package de.tum.ls4.artifacts;

import de.tum.ls4.locators.LocatorUtils;
import org.junit.jupiter.api.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static de.tum.ls4.artifacts.StoreTest.*;
import static de.tum.ls4.utils.DockerUtils.cleanDatabase;
import static de.tum.ls4.utils.DockerUtils.setupDatabase;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleSimiloStoreTest {
    private static final Lock lock = new ReentrantLock();
    private final String URL_MAIN = "https://www.google.com";

    @BeforeAll
    public static void setup() {
        setupDatabase();
    }

    @BeforeEach
    void setupTest() {
        lock.lock();
        cleanDatabase();
    }

    @AfterEach
    void unlock() {
        lock.unlock();
    }

    @Test
    @Order(1)
    void testEmptyDatabaseSimilo() {
        testEmptyDatabase(SimpleSimiloStore::new, SampleElements.ELEMENTS, SampleElements.ELEMENTS_IDENTIFIERS);
    }

    @Test
    @Order(2)
    void testSimpleInsertAndRemoveSimilo() {
        testSimpleInsertAndRemove(
                SimpleSimiloStore::new,
                SampleElements.ELEMENTS,
                SampleElements.ELEMENTS_IDENTIFIERS,
                LocatorUtils::similoScore
        );
    }

    @Test
    @Order(3)
    void testElementEvolutionSimilo() {
        testElementEvolution(
                SimpleSimiloStore::new,
                SampleElements.ELEMENTS,
                SampleElements.ELEMENTS_IDENTIFIERS,
                LocatorUtils::similoScore
        );
    }

    @Test
    @Order(4)
    void testOptimizeLocatorStrategySimilo() {
        testOptimizeLocatorStrategy(
                SimpleSimiloStore::new,
                SampleElements.ELEMENTS_LOC_OPT,
                SampleElements.ELEMENTS_LOC_OPT_IDENTIFIERS,
                LocatorUtils::similoScore
        );
    }
}
