package elements

case class Xpath(chrome: String, selenium: String, idxpath: String):

  def equals(other: Xpath): Boolean =
    chrome == other.chrome && selenium == other.selenium && idxpath == other.idxpath

  def equals(other: String): Boolean =
    chrome == other || selenium == other || idxpath == other

object Xpath:

  def empty: Xpath = Xpath("-", "-", "-")
