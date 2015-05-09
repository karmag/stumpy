package karmag.stumpy

object Create {
  val nil = EdnNil()
  val true_ = EdnBool(value = true)
  val false_ = EdnBool(value = false)

  def str(string: String) = EdnString(string)
  def char(character: Char) = EdnChar(character)

  def int(value: Long) = EdnInt(value)
  def int(value: BigInt) = EdnInt(value, exact = true)
  def int(value: Long, exact: Boolean) = EdnInt(value, exact)
  def int(value: BigInt, exact: Boolean) = EdnInt(value, exact)

  def float(value: Double) = EdnFloat(value)
  def float(value: BigDecimal) = EdnFloat(value, exact = true)
  def float(value: Double, exact: Boolean) = EdnFloat(value, exact)
  def float(value: BigDecimal, exact: Boolean) = EdnFloat(value, exact)

  def sym(name: String) = EdnSymbol(name)
  def sym(ns: String, name: String) = EdnSymbol(name, Some(ns))
  def kw(name: String) = EdnKeyword(name)
  def kw(ns: String, name: String) = EdnKeyword(name, Some(ns))

  def list(values: Seq[Edn]) = EdnList(values.toList)
  def vec(values: Seq[Edn]) = EdnVector(values.toVector)
  def map(kvs: Map[Edn, Edn]) = EdnMap(kvs)
  def set(values: Seq[Edn]) = EdnSet(values.toSet)

  def tag(tag: EdnSymbol, value: Edn) = EdnTag(tag, value)
}
