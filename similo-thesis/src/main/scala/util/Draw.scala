package util

import elements.Element
import org.openqa.selenium.Rectangle

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Font, Graphics2D}
import java.io.File
import java.time.LocalDate
import javax.imageio.ImageIO
import scala.math

case class Draw(image: BufferedImage, path: String = "screenshot.png"):

  private final val WIDTH = 3840
  private final val HEIGHT = 1940

  private final val BORDER = 0

  private var indexed = 1

  def write(path: String = path): Unit = ImageIO.write(image, "png", new File(path))

  def highlightElementAndNumber(element: Element): Draw =
    highlightElementAndNumber(indexed, element)
    indexed += 1
    this

  def highlightElementAndNumber(number: Int, element: Element): Draw =
    highlightElement(element)
    labelElement(number.toString, element)
    this

  def highlightElement(element: Element): Draw =
    highlightElement(element.x, element.y, element.width, element.height)

  def highlightElement(x: Int, y: Int, width: Int, height: Int, offset: Int = BORDER): Draw =
    // Load the image and create the graphics object
    val graphics = image.getGraphics.asInstanceOf[Graphics2D]

    // Set the color and draw the rectangle
    graphics.setColor(Color.RED)
    graphics.setStroke(BasicStroke(5))
    graphics.drawRect(x * 2 - offset, y * 2 - offset, 2 * width + 2 * offset, 2 * height + 2 * offset)
    graphics.dispose()

    // Return Draw to allow chaining
    this

  def labelElement(label: String, element: Element): Draw =
    labelElement(label, element.x * 2, element.y * 2, element.height * 2, element.width * 2)

  private def labelElement(label: String, x: Int, y: Int, height: Int, width: Int, offset: Int = 5): Draw =
    // Load the image and create the graphics object
    val graphics = image.getGraphics

    // Set the color and draw the rectangle
    graphics.setColor(Color.RED)
    graphics.setFont(new Font("TimesRoman", Font.PLAIN, 40))
    if y < 40 then graphics.drawString(label, x + width / 2, y + height + offset + 40)
    else graphics.drawString(label, x + width / 2, y - offset - 10)
    graphics.dispose()

    // Return Draw to allow chaining
    this

  def highlightElementAndNumber(tuple: (Int, Element)): Draw =
    highlightElementAndNumber(tuple._1, tuple._2)

  def highlightElement(rect: Rectangle): Draw =
    highlightElement(rect.getX, rect.getY, rect.getWidth, rect.getHeight)

  def highlightElement(rect: Rectangle, offset: Int): Draw =
    highlightElement(rect.getX, rect.getY, rect.getWidth, rect.getHeight, offset)

  def labelElement(label: String, rect: Rectangle): Draw =
    labelElement(label, rect.getX, rect.getY, rect.getHeight, rect.getWidth)

  def labelElement(label: String, rect: Rectangle, offset: Int): Draw =
    labelElement(label, rect.getX, rect.getY, offset, rect.getHeight, rect.getWidth)

  def cropToElement(rect: Rectangle): Draw = cropToElement(rect.getY)

  def cropToElement(y: Int): Draw =
    // Calculate new starting point
    val height = image.getHeight
    val pageHeight = math.min(height, HEIGHT)

    val newY =
      if (y - pageHeight / 2) < 0 then 0 // to close to top
      else if (y + pageHeight / 2) >= height then height - pageHeight // to close to bottom
      else y - pageHeight / 2 // in the middle

    // Crop the image and return as a new Draw
    Draw(image.getSubimage(0, newY, WIDTH, pageHeight), path)

  def cropToElements(rects: Seq[Int]): Draw = ??? // TODO

object Draw:

  def apply(site: String, date: LocalDate): Draw =
    Draw(s"sites/$site/${date.toString}/screenshot.png")

  def apply(path: String): Draw = new Draw(readImage(path), path)

  private def readImage(path: String): BufferedImage = ImageIO.read(new File(path))
