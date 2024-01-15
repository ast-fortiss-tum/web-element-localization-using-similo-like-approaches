package de.tum.ls4;

import de.tum.ls4.locators.Similocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com/");

        // Click cookie consent accept button.
        driver.findElement(
                        By.xpath("/html/body/div[2]/div[3]/div[3]/span/div/div/div/div[3]/div[1]/button[1]/div"))
                .click();

        By locator = Similocator.similo(
                By.xpath("/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/textarea"));
        WebElement element = driver.findElement(locator);

        element.sendKeys("It works!");

        Thread.sleep(10000);

        driver.quit();
    }
}
