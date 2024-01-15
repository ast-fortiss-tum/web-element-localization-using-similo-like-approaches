package elements

/**
 * Representing the DOM tree of elements.
 *
 * @param element
 * @param children
 */
case class Dom(element: Element, children: List[Dom] = List.empty):

  def insert(insert: Element): Dom =
    recursiveInsert(insert, insert.xpath.chrome.split("/").filter(_.nonEmpty).toSeq)

  private def recursiveInsert(insert: Element, path: Seq[String]): Dom = path match
    case Seq(tag) =>
      if element.tag == tag then copy(children = children :+ Dom(insert))
      else throw new Exception(s"Cannot insert $insert into $element")
    case tag :: tail => copy(children = children.map(_.recursiveInsert(insert, tail)))
    case _           => throw new Exception(s"Cannot insert $insert into $element")

object Dom:

  def apply(elements: Seq[Element]): Dom =
    elements.foldLeft(Dom(Element.justTag("html")))(_ insert _)
