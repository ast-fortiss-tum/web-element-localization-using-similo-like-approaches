package scrape

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxDriverLogLevel, FirefoxOptions, FirefoxProfile}
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.{JavascriptExecutor, WebDriver}

import java.util.logging.Level
import scala.annotation.tailrec

object SetupDrivers:

  def initFirefoxDriver(): FirefoxDriver =
    System.setProperty("webdriver.firefox.logfile", "/dev/null")
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF)
    java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF)

    try System.setProperty("webdriver.gecko.driver", "driver/firefox/geckodriver")
    catch
      case e: Exception =>
        println("Please run the setup.sh script to download the corresponding chromedriver for your OS.")
        System.exit(1)

    val profile = new FirefoxProfile()
    profile.setPreference("browser.logfile", "/dev/null")

    val options = new FirefoxOptions().setProfile(profile)
    // Uncomment the following line if you want to run Firefox in headless mode
    // options.addArguments("--headless")

    options.setLogLevel(FirefoxDriverLogLevel.FATAL)

    val driver: FirefoxDriver = new FirefoxDriver(options)
    driver.manage.window.maximize()
    driver

  def initChromeDriver(): ChromeDriver =
    // Set path to chromedriver
    System.setProperty("webdriver.chrome.silentOutput", "true")
    java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF)

    try System.setProperty("webdriver.chrome.driver", "driver/chrome/chromedriver")
    catch
      case e: Exception =>
        println("Please run the setup.sh script to download the corresponding chromedriver for your OS.")
        System.exit(1)

    val options = new ChromeOptions()
    options.addArguments("--remote-allow-origins=*")
    // Uncomment the following line if you want to run Chrome in headless mode
    // options.addArguments("--headless")

    val driver: ChromeDriver = new ChromeDriver(options)
    driver.manage.window.maximize()
    driver

  def zoom(driver: WebDriver, to: Int): Unit =
    driver.asInstanceOf[JavascriptExecutor].executeScript(s"document.body.style.zoom='$to%'")

  @tailrec
  def loadFullPage(driver: WebDriver): Unit =
    val js = driver.asInstanceOf[JavascriptExecutor]
    val total = scrollHeight(js)
    scrollStepwise(js, total)
    Thread.sleep(5000)
    scrollToTop(js)
    if scrollHeight(js) != total then loadFullPage(driver)

  def scrollHeight(js: JavascriptExecutor): Long =
    js.executeScript("return document.body.scrollHeight").asInstanceOf[Long]

  @tailrec
  private def scrollStepwise(js: JavascriptExecutor, total: Long): Unit =
    if total > 0 then
      scrollBy(js, 1000)
      Thread.sleep(1000)
      scrollStepwise(js, total - 1000)

  private def scrollTo(js: JavascriptExecutor, y: Int): Unit =
    js.executeScript(s"window.scrollTo(0, $y)")

  private def scrollToTop(js: JavascriptExecutor): Unit =
    scrollTo(js, 0)

  private def scrollBy(js: JavascriptExecutor, y: Int): Unit =
    js.executeScript(s"window.scrollBy(0, $y)")
