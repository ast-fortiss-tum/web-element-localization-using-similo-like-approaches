package scrape

import java.awt.Toolkit
import java.awt.datatransfer.{Clipboard, DataFlavor}
import java.util.concurrent.atomic.AtomicBoolean
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.*
import scala.util.Try

case class ClipboardCollector() extends Thread:

  private val collected = ListBuffer.empty[String]
  private val running = new AtomicBoolean(true)

  private val clipboard: Clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
  private val initialValue: String = clipboardContent.getOrElse("")

  override def run(): Unit =
    while running.get do
      clipboardContent match
        case Some(value) if initialValue != value && !collected.contains(value) =>
          collected += value
          println(s"Collected: $value")
        case _ => ()
      sleepSafely(100.millis)

  private def clipboardContent: Option[String] =
    Option(clipboard.getContents(this)).flatMap: content =>
      Try(content.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]).toOption

  private def sleepSafely(duration: Duration): Unit =
    try Thread.sleep(duration.toMillis)
    catch case _: Exception => running.set(false)

  def append(value: String): Unit =
    collected += value
    println(s"Collected: $value")

  def finish(): Seq[String] =
    running.set(false)
    interrupt()
    join()
    collected.toList

object ClipboardCollector:
  def apply(): ClipboardCollector =
    val collector = new ClipboardCollector()
    collector.start()
    collector
