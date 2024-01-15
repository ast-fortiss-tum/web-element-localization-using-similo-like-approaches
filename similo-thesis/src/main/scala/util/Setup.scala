package util

import ch.qos.logback.classic.Logger
import com.typesafe.config.{Config, ConfigFactory}
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.slf4j.LoggerFactory
import scrape.SetupDrivers

object Setup:

  lazy val firefox: FirefoxDriver = SetupDrivers.initFirefoxDriver()

  lazy val chrome: ChromeDriver = SetupDrivers.initChromeDriver()

  lazy val logger: Logger =
    val log = LoggerFactory.getLogger("").asInstanceOf[Logger]
    // log.setLevel(config.getInt("loglevel"))
    log

  lazy val config: Config = ConfigFactory.load()
