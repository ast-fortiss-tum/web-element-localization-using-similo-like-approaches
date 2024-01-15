package scrape

import io.circe.generic.auto.*
import util.Utils.using

import scala.io.Source

case class Site(name: String, url: String)

object Site:

  def apply(url: String): Site = Site(url.split(".")(1), url)
