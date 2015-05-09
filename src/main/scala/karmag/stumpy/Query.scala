package karmag.stumpy

object Query {

  /**
   * Lookup attempts to follow the given path and returns the
   * object at the end of the path.
   *
   * The key is used to perform a fetch from the current EDN
   * object. If the fetch is successful the fetched object becomes
   * the current object and the next key is used for that object.
   * This will recursively traverse the data until the final key
   * finds an object or a fetch fails.
   *
   * The EDN structures supported are:
   * <pre>
   *     - EdnMap    - key is used as key
   *     - EdnSet    - key is used to lookup itself
   *     - EdnList   - key must be a EdnInt, used as index
   *     - EdnVector - key must be a EdnInt, used as index
   *     - EdnTag    - key matching on tag
   * </pre>
   *
   * @param edn The starting point
   * @param keys The path as a sequence of keys
   * @return The object at the end of the path
   */
  def lookup(edn: Edn, keys: Edn*): Option[Edn] = {
    keys.size match {
      case 0 => None
      case 1 => singleLookup(edn, keys.head)
      case _ => singleLookup(edn, keys.head) match {
        case Some(x) => lookup(x, keys.drop(1):_*)
        case _ => None
      }
    }
  }

  private def singleLookup(edn: Edn, key: Edn): Option[Edn] = {
    edn match {
      case map: EdnMap => map.data.get(key)
      case set: EdnSet => set.data.find(_ == key)
      case lst: EdnList => key match {
        case int: EdnInt => lst.data.drop(int.value.toInt).headOption
        case _ => None
      }
      case vec: EdnVector => key match {
        case int: EdnInt => vec.data.drop(int.value.toInt).headOption
        case _ => None
      }
      case tag: EdnTag => if (key == tag.tag) Some(tag.edn) else None
      case _ => None
    }
  }

  /**
   * Iterate over all EDN objects. Returns all objects satisfying
   * the predicate function.
   *
   * For maps the function is applied to values, keys are not checked.
   *
   * @param edn Root EDN object
   * @param f Predicate function used for filtering
   * @return All EDN objects satisfying the predicate function
   */
  def search(edn: Edn, f: Edn => Boolean): Seq[Edn] = {
    val self = if (f(edn)) List(edn) else Nil

    val found = edn match {
      case map: EdnMap => map.data.values.map(search(_, f))
      case set: EdnSet => set.data.map(search(_, f))
      case lst: EdnList => lst.data.map(search(_, f))
      case vec: EdnVector => vec.data.map(search(_, f))
      case tag: EdnTag => List(search(tag.edn, f))
      case _ => Nil
    }

    self ++ found.fold(Nil)(_ ++ _)
  }
}
