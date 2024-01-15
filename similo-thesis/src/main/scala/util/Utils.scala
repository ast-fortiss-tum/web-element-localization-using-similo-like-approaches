package util

import com.github.benmanes.caffeine.cache.{CacheLoader, Caffeine, LoadingCache}
import elements.Element

import java.nio.file.{Files, Path}
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import scala.io.Source
import scala.jdk.CollectionConverters.*

object Utils:

  def using[T](source: Source)(eval: Source => T): T =
    try eval(source)
    finally source.close()

  def read(file: String): Option[String] =
    using(() => Source.fromFile(file))(None)(s => Some(s.mkString))

  def ressource(file: String): Option[String] =
    using(() => Source.fromResource(file))(None)(s => Some(s.mkString))

  def write(file: String, content: String): Unit =
    write(Path.of(file), content)

  def write(file: Path, content: String): Unit =
    createDirs(file)
    Files.writeString(file, content)

  def createDirs(path: String): Unit =
    createDirs(Path.of(path))

  def createDirs(path: Path): Unit =
    if path.toString.split("/").last.contains(".") then Files.createDirectories(path.getParent)
    else Files.createDirectories(path)

  extension (option: Option[String])

    def equalsIgnoreCase(other: String): Boolean = option match
      case Some(value) => value.equalsIgnoreCase(other)
      case None        => false

    def equalsIgnoreCase(other: Option[String]): Boolean = (option, other) match
      case (Some(value), Some(otherValue)) => value.equalsIgnoreCase(otherValue)
      case _                               => false

    def indexOf(other: String): Int = option match
      case Some(value) => value.indexOf(other)
      case None        => -1

    def is(tag: String): Boolean = option match
      case Some(value) => value.equalsIgnoreCase(tag)
      case None        => false

  extension (string: String)
    infix def is(tag: String): Boolean = string match
      case null => false
      case _    => string.equalsIgnoreCase(tag)

  def append(file: String, content: String): Unit =
    val add = if content.startsWith("\n") then content else "\n" + content
    Files.writeString(Path.of(file), add, java.nio.file.StandardOpenOption.APPEND)

  def daysBetween(start: LocalDate, end: LocalDate): Int =
    Math.abs(ChronoUnit.DAYS.between(start, end).toInt)

  def read(path: Path): String =
    using(() => Source.fromFile(path.toFile))("")(_.mkString)

  def using[T](source: () => Source)(default: T)(eval: Source => T): T =
    val open =
      try source()
      catch case _: Exception => return default

    try eval(open)
    catch case _: Exception => default
    finally open.close()

  def walk(path: String): Seq[Path] =
    walk(Path.of(path))

  def walk(path: Path): Seq[Path] =
    Files.walk(path).toList.asScala.toSeq.drop(1)

  def fromXpath(xpath: String, elements: Seq[Element]): Element =
    if xpath == "-" then Element.empty
    else elements.find(_.xpath.equals(xpath)).getOrElse(throw new Exception(s"Element $xpath not found!"))

  def reverseSequence[T](seq: Seq[Seq[T]]): Seq[Seq[T]] = seq match
    case Nil          => Nil
    case Seq(last)    => last.map(Seq(_))
    case head :: tail => head.zip(reverseSequence(tail)).map(_ +: _)

  def removeWayBackUrls(string: String): String =
    string.replaceAll("https?://web\\.archive\\.org/web/\\d{14}(..?_)?/", "")

  extension [T](seq: Seq[(LocalDate, Seq[T])])
    def get(date: LocalDate): Seq[T] =
      seq.find(_._1 == date).map(_._2).getOrElse(throw Exception(s"This should not happen: $date"))

  extension (seq: Seq[Element])
    def getElement(xpath: String): Element =
      if xpath == "-" then Element.empty
      else seq.find(_.xpath.equals(xpath)).get

  extension [T](seq: Seq[Option[T]]) def unwrapDiscard: Seq[T] = seq.filter(_.isDefined).map(_.get)

  def zip[A, B, C](seqOne: Seq[A], seqTwo: Seq[B], seqThree: Seq[C]): Seq[(A, B, C)] =
    seqOne.zip(seqTwo).zip(seqThree).map((ab, c) => (ab._1, ab._2, c))

  def time[T](block: => T): T =
    val before = System.currentTimeMillis
    val result = block
    val after = System.currentTimeMillis
    println("Elapsed time: " + ((after - before) / 1000.0) + "s")
    result

  def time[T](block: => T, customPrint: String => Unit): T =
    val before = System.currentTimeMillis
    val result = block
    val after = System.currentTimeMillis
    customPrint(s"${(after - before) / 1000.0}s")
    result

  def asJava[I, O](lambda: I => O): java.util.function.Function[I, O] = (i: I) => lambda(i)

  def createCache[K, V](fn: K => V): LoadingCache[K, V] =
    Caffeine.newBuilder().build(key => fn(key))

  def roundTwoDecimals(d: Double): Double = Math.floor(d * 100) / 100.0

  def noneEmptyEqual(left: Option[String], right: Option[String]): Boolean = (left, right) match
    case (Some(l), Some(r)) => l.equalsIgnoreCase(r)
    case _                  => false

  def noneEmptyEqual(left: String, right: String): Boolean = (left, right) match
    case (null, null) | (null, _) | (_, null) => false
    case ("", "")                             => false
    case (l, r)                               => l.equalsIgnoreCase(r)
