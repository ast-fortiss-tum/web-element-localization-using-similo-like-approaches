package similo.comparing

object MapCompare:

  val intersectValueCompare: ValueCompare[Map[String, String]] =
    ValueCompare[Map[String, String]](
      "intersectValueCompare",
      (a, b) =>
        val maxSize = Math.max(a.size, b.size)
        val intersectKeys = a.keySet.intersect(b.keySet)
        val filter = intersectKeys.filter(key => a(key) == b(key))
        if maxSize == 0 then 0.0
        else filter.size.toDouble / maxSize
    )

  val intersectKeyCompare: ValueCompare[Map[String, String]] =
    ValueCompare[Map[String, String]](
      "intersectKeyCompare",
      (a, b) =>
        val aKeys = a.keySet
        val bKeys = b.keySet
        val unionKeys = aKeys.union(bKeys)
        val intersectKeys = aKeys.intersect(bKeys)
        if unionKeys.isEmpty then 0.0
        else intersectKeys.size.toDouble / unionKeys.size
    )
