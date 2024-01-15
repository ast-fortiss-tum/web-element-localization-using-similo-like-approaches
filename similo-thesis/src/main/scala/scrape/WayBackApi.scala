package scrape

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import sttp.client4.*
import util.Setup.logger

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object WayBackApi:

  private val backend = DefaultSyncBackend()
  private val wayBackUrl = "https://archive.org/wayback/available"
  private val wayBackDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
  private val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36"

  def closestSnapshot(url: String, date: LocalDate): String =
    val uri = uri"$wayBackUrl?url=$url&timestamp=${date.format(wayBackDateFormat)}120000"
    logger.info(s"Getting closest wayback url for $url on $date: $uri")

    val wayBackResponse = basicRequest.get(uri).header("User-Agent", userAgent).send(backend).body

    val responseObject = for
      response <- wayBackResponse
      json <- decode[WebArchive](response)
    yield json

    responseObject.fold(
      error => throw new Exception(s"Error getting closest wayback url for $url: $error"),
      _.archived_snapshots.closest.url.withoutWayBackBanner.replace("http://", "https://")
    )

  extension (url: String)
    def withoutWayBackBanner: String =
      url.replace("/http", "if_/http")

  private case class Snapshot(
    available: Boolean,
    url: String,
    timestamp: String,
    status: String
  )

  private case class ArchivedSnapshots(closest: Snapshot)

  private case class WebArchive(archived_snapshots: ArchivedSnapshots)
