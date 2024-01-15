package scripts

import io.circe.generic.auto.*
import io.circe.jawn.decode
import sttp.client4.*

import scala.util.*

object FetchTagInterchangeability:

  val backend: WebSocketSyncBackend = DefaultSyncBackend()
  private val apiKey = "key" // TODO sys.env("OPENAI_API_KEY")
  private val tags = Seq(
    "div",
    "a",
    "span",
    "li",
    "p",
    "h3",
    "h2",
    "button",
    "svg",
    "td",
    "input",
    "h1",
    "h4",
    "label",
    "tr",
    "h5",
    "th",
    "select"
  )

  private def makeGpt4Request(leftString: String, rightString: String): Either[String | Exception, String] =
    val prompt = s"How interchangeably can the HTML tags '$leftString' and '$rightString' be used on a scale " +
      s"0 to 100, considering CSS styling and JavaScript functionality. Answer with the number."

    val jsonPayload = s"""{
         |"model": "gpt-4",
         |"messages": [
         |  {
         |    "role": "user",
         |    "content": "$prompt"
         |  }
         |],
         |"temperature": 0,
         |"max_tokens": 5,
         |"top_p": 1,
         |"frequency_penalty": 0,
         |"presence_penalty": 0
         |}""".stripMargin

    val request = basicRequest
      .post(uri"https://api.openai.com/v1/chat/completions")
      .header("Authorization", s"Bearer $apiKey")
      .header("Content-Type", "application/json")
      .body(jsonPayload)

    request
      .send(backend)
      .body
      .flatMap: value =>
        decode[ApiResponse](value).map(_.choices.head.message.content)

  private case class ApiResponse(id: String, `object`: String, created: Long, model: String, choices: Seq[Choice])

  private case class Choice(index: Int, message: Message, finish_reason: String)

  private case class Message(role: String, content: String)
  tags.combinations(2).foreach {
    case Seq(left, right) =>
      val response = makeGpt4Request(left, right).fold(_.toString, identity)
      println(s"$left - $right: $response")
  }
