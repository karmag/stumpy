package karmag.stumpy

// base
sealed class Edn()

// primitives
case class EdnNil() extends Edn
case class EdnBool(value: Boolean) extends Edn
case class EdnString(value: String) extends Edn
case class EdnChar(value: Char) extends Edn
case class EdnInt(value: BigInt, exact: Boolean = false) extends Edn
case class EdnFloat(value: BigDecimal, exact: Boolean = false) extends Edn

// types
case class EdnSymbol(name: String, ns: Option[String] = None) extends Edn
case class EdnKeyword(name: String, ns: Option[String] = None) extends Edn

// containers
case class EdnList(data: List[Edn]) extends Edn
case class EdnVector(data: Vector[Edn]) extends Edn
case class EdnMap(data: Map[Edn, Edn]) extends Edn
case class EdnSet(data: Set[Edn]) extends Edn

// meta
case class EdnTag(tag: EdnSymbol, edn: Edn) extends Edn
