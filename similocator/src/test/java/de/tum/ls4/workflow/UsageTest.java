package de.tum.ls4.workflow;

import de.tum.ls4.Javascript;
import de.tum.ls4.locators.Similocator;
import de.tum.ls4.utils.DockerUtils;
import de.tum.ls4.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static de.tum.ls4.workflow.SampleLocators.GOOGLE_DATA;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsageTest {
    private static final Javascript GET_XPATHS = Javascript.create()
            .addFile("extractors/xpath.js")
            .addScript("return chromeXPathExtractor(arguments[0]);");
    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
            driver = new ChromeDriver();
        }
        DockerUtils.setupDatabase();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    private static String filePath(Path site) {
        Path pathToIndex = site.resolve("index.html");
        return "file://" + pathToIndex.toAbsolutePath();
    }

    @BeforeEach
    void setupTest() {
        DockerUtils.cleanDatabase();
    }

    @Test
    @Order(1)
    void testGoogleSimilo() {
        testWebsite(GOOGLE_DATA, Similocator::similo, 0.6);
    }

    @Test
    @Order(2)
    void testGoogleVonSimilo() {
        testWebsite(GOOGLE_DATA, Similocator::von, 0.6);
    }

    @Test
    @Order(3)
    void testGoogleCombinedSimilo() {
        testWebsite(GOOGLE_DATA, Similocator::combined, 0.6);
    }

    protected void testWebsite(
            SampleLocators.SiteData siteData,
            Function<By, By> wrapper,
            double threshold
    ) {
        // Load the initial website into the live folder
        LocalDate initialDate = siteData.dates().get(0);
        Path liveFolder = FileUtils.copySiteToLiveFolder(siteData.site(), initialDate);
        List<String> initialXpaths = siteData.xpaths().get(siteData.dates().get(0));

        driver.get(filePath(liveFolder));
        for (String xpath : initialXpaths) {
            driver.findElement(wrapper.apply(By.xpath(xpath)));
        }

        // Subsequent locations
        for (LocalDate date : siteData.dates().subList(1, siteData.dates().size())) {

            liveFolder = FileUtils.copySiteToLiveFolder(siteData.site(), date);
            driver.get(filePath(liveFolder));

            List<String> xpaths = siteData.xpaths().get(date);
            int found = 0;

            for (int i = 0; i < xpaths.size(); i++) {
                // Try locating this element with Similo
                WebElement candidate = driver.findElement(wrapper.apply(By.xpath(initialXpaths.get(i))));

                // Get the xpath of the candidate element
                String candidateXpath = (String) GET_XPATHS.executeOn((JavascriptExecutor) driver, candidate);

                if (candidateXpath.equals(xpaths.get(i))) {
                    found++;
                }
            }

            assertTrue((double) found / xpaths.size() > threshold);
        }
    }
}
