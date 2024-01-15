package util

import util.Csv.reverse
import util.Utils.read

import java.nio.file.{Files, Path}
import scala.util.{Failure, Success, Try}

case class Csv(file: Path, delimiter: Char = ';', withHeader: Boolean = true):

  private var content = open

  def apply(line: Int): Row =
    Row(content(line + 1))

  def columns: Columns = Columns(reverse(content).map(Column.apply))

  def flush(): Unit = Files.writeString(file, content.map(_.mkString(delimiter.toString)).mkString("\n"))

  def appendColumn(header: String, values: Seq[String]): Unit = ???

  def appendRow(values: Seq[String]): Unit =
    content = content :+ values

  def headers: Row = Row(content.head)

  def isEmpty: Boolean = content.isEmpty

  private def open: Seq[Seq[String]] =
    Try(read(file)) match
      case Failure(exception) => Seq.empty
      case Success(value) =>
        value.split("\n").filter(!_.isBlank).map(_.trim.split(delimiter).filter(!_.isBlank).map(_.trim).toSeq).toSeq

  case class Rows(private val rows: Seq[Row])

  case class Row(private val _values: Seq[String]):
    def size: Int = _values.size
    def values: Seq[String] = _values
    def apply(i: Int): String = _values(i)
    override def toString: String = _values.mkString(", ")

  case class Columns(private val _columns: Seq[Column]):
    def last: Column = _columns.last
    def columns: Seq[Column] = _columns
    def apply(i: Int): Column = if i >= _columns.size then Column(Seq.empty) else _columns(i)

  case class Column(private val _values: Seq[String]):
    def apply(i: Int): String = _values(i + 1)
    def header: String = _values.head
    def values: Seq[String] = _values.tail
    def where(f: String => Boolean): Option[Row] = _values.indexWhere(f) match
      case -1 => None
      case i  => Some(Row(content(i)))

object Csv:

  def reverse(in: Seq[Seq[String]]): Seq[Seq[String]] =
    if in.isEmpty then Seq.empty
    else
      in.foldLeft(Seq.fill(in.head.size)(Seq.empty[String])): (acc, row) =>
        acc.zip(row).map(_ :+ _)

  def apply(file: String): Csv = Csv(Path.of(file))
