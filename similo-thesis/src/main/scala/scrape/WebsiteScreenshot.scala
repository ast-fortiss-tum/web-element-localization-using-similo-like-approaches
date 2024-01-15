package scrape

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.{JavascriptExecutor, OutputType, WebDriver}
import scrape.SetupDrivers.loadFullPage
import util.Load.{getSitesFor, getUrlsFor}

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.file.{Files, Path, Paths}
import javax.imageio.ImageIO
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine

object WebsiteScreenshot:

  def screenshotViewport(webdriver: WebDriver, saveTo: String): Unit =
    screenshotViewport(webdriver, Paths.get(saveTo))

  private def screenshotViewport(webdriver: WebDriver, saveTo: Path): Unit =
    val bytes = webdriver match
      case driver: FirefoxDriver => driver.getScreenshotAs(OutputType.BYTES)
      case driver: ChromeDriver  => driver.getScreenshotAs(OutputType.BYTES)
      case _                     => throw new Exception("Unsupported driver")
    writeScreenshot(bytes, saveTo)

  def screenshotFullPage(driver: WebDriver, saveTo: String): Unit =
    screenshotFullPage(driver, Paths.get(saveTo))

  def screenshotFullPage(driver: WebDriver, saveTo: Path): Unit =
    val bytes = driver match
      case driver: FirefoxDriver => driver.getFullPageScreenshotAs(OutputType.BYTES)
      case driver: ChromeDriver  => screenshotFullPageChrome(driver)
      case _                     => throw new Exception("Unsupported driver")
    writeScreenshot(bytes, saveTo)

  private def writeScreenshot(bytes: Array[Byte], saveTo: Path): Unit =
    val parent = saveTo.getParent
    parent match
      case null => ()
      case _    => Files.createDirectories(parent)
    Files.write(saveTo, bytes)

  private def screenshotFullPageChrome(driver: ChromeDriver): Array[Byte] =
    val executor = driver.asInstanceOf[JavascriptExecutor]

    var totalHeight = executor
      .executeScript("""
      |return Math.max(
      |  document.body.scrollHeight, document.documentElement.scrollHeight,
      |  document.body.offsetHeight, document.documentElement.offsetHeight,
      |  document.body.clientHeight
      |);
      |""".stripMargin)
      .asInstanceOf[Long]

    // Calculate the viewport height
    val viewportHeight = executor.executeScript("return window.innerHeight;").asInstanceOf[Long]

    val imageParts = ListBuffer[BufferedImage]()
    while totalHeight > 0 do
      val screenshot = driver.getScreenshotAs(OutputType.BYTES)
      var image = ImageIO.read(new ByteArrayInputStream(screenshot))

      executor.executeScript("window.scrollBy(0, arguments[0]);", viewportHeight)
      totalHeight -= viewportHeight
      if totalHeight < 0 then
        image = image.getSubimage(0, image.getHeight + totalHeight.toInt, image.getWidth, -totalHeight.toInt)

      imageParts += image
    combineScreenshots(imageParts.toList)

  private def combineScreenshots(images: List[BufferedImage]): Array[Byte] =
    val completeHeight = images.map(_.getHeight).sum
    val finalImage = new BufferedImage(images.head.getWidth, completeHeight, BufferedImage.TYPE_INT_RGB)
    var y = 0
    images.foreach { part =>
      finalImage.createGraphics().drawImage(part, 0, y, null)
      y += part.getHeight
    }
    val output = new ByteArrayOutputStream()
    ImageIO.write(finalImage, "png", output)
    output.toByteArray

  def screenshotAllFullScreen(driver: FirefoxDriver): Unit =
    getSitesFor.foreach(screenshotFullPage(driver, _))

  def screenshotFullScreen(driver: FirefoxDriver, path: Path, force: Boolean = false, confirm: Boolean = true): Unit =
    getUrlsFor(path).toSeq
      .sortBy(_._1)
      .foreach: (date, url) =>
        val screenshotPath = path.resolve(date.toString).resolve("screenshot.png")
        if !Files.exists(screenshotPath) || force then
          println(s"[i] Getting ${path.subpath(1, 2)} to screenshot on $date.")
          driver.get(url)
          loadFullPage(driver)
          if confirm then readLine(s"[i] Is the page fully loaded? Press enter to continue...")
          else Thread.sleep(10000)
          screenshotFullPage(driver, screenshotPath)
          println(s"[i] Screenshot saved to $screenshotPath.")
