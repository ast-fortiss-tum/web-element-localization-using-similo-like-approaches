package similo.comparing

import com.github.benmanes.caffeine.cache.LoadingCache
import util.Utils

case class ValueCompare[T](
  name: String,
  fn: (T, T) => Double,
  empty: Option[T => Boolean] = None,
  cache: Boolean = false
):

  private val valueCache: LoadingCache[(T, T), Double] =
    Utils.createCache((key: (T, T)) => fn(key._1, key._2))

  def saveCompare(a: T, b: T): Double = strongEmpty match
    case Some(f) => if f(a) || f(b) then 0.0 else compare(a, b)
    case None    => compare(a, b)

  private def strongEmpty: Option[T => Boolean] = empty match
    case Some(f) => Some((t: T) => if t == null then true else f(t))
    case None    => None

  override def toString: String = name

  def forOption: ValueCompare[Option[T]] = ValueCompare[Option[T]](name + " (option)", (a, b) => compareOption(a, b))

  private def compareOption(a: Option[T], b: Option[T]): Double = (a, b) match
    case (Some(a), Some(b)) => compare(a, b)
    case _                  => 0.0

  def compare(a: T, b: T): Double = if cache then valueCache.get((a, b)) else fn(a, b)

object ValueCompare:

  val equals: ValueCompare[Boolean] = ValueCompare[Boolean]("equals", (a, b) => if a == b then 1.0 else 0.0)
