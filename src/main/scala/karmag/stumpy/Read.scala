package karmag.stumpy

import java.io.{Reader, PushbackReader}

import clojure.core$not_empty
import karmag.stumpy.clj.{Tagged, Clj}

object Read {
  private val endOfTheLine = "Nothing to read"

  def edn(in: Reader): Either[String, Edn] = {
    try {
      val parseResult = Clj.readEdn(new PushbackReader(in))
      if (parseResult == Clj.EOF)
        Left(endOfTheLine)
      else
        Right(Translate.translate(parseResult))
    } catch {
      case e: Exception => Left(e.getMessage)
    }
  }

  def allEdn(in: Reader)(implicit acc: Seq[Edn] = Nil): Either[String, Seq[Edn]] = {
    val result = edn(in)
    result match {
      case Left("Nothing to read") => Right(acc)
      case Left(msg) => Left(msg)
      case Right(edn) => allEdn(in)(acc ++ List(edn))
    }
  }
}

private object Translate {
  def translate(obj: Object): Edn = {
    if (obj == null)
      EdnNil()
    else obj match {
      case value: java.lang.Boolean => EdnBool(value)
      case value: String => EdnString(value)
      case value: Character => EdnChar(value)
      case value: java.lang.Long => EdnInt(value.toLong)
      case value: clojure.lang.BigInt => EdnInt(value.toBigInteger, exact = true)
      case value: java.lang.Double => EdnFloat(value.toDouble)
      case value: java.math.BigDecimal => EdnFloat(value, exact = true)
      case value: clojure.lang.Symbol => toSymbol(value)
      case value: clojure.lang.Keyword => toKeyword(value)
      case value: clojure.lang.IPersistentList => toList(value)
      case value: clojure.lang.IPersistentVector => toVector(value)
      case value: clojure.lang.IPersistentMap => toMap(value)
      case value: clojure.lang.IPersistentSet => toSet(value)
      case value: karmag.stumpy.clj.Tagged => toTagged(value)
      case _ =>
        throw new scala.RuntimeException("Can't translate type: " + obj.getClass)
    }
  }

  def toSymbol(obj: clojure.lang.Symbol): EdnSymbol = {
    val ns = obj.getNamespace
    EdnSymbol(obj.getName, if (ns == null) None else Some(ns))
  }

  def toKeyword(obj: clojure.lang.Keyword): EdnKeyword = {
    val ns = obj.getNamespace
    EdnKeyword(obj.getName, if (ns == null) None else Some(ns))
  }

  def toList(obj: clojure.lang.IPersistentList): EdnList = {
    var items = obj.asInstanceOf[clojure.lang.ISeq]
    var data: List[Edn] = List[Edn]()

    while(notEmpty.invoke(items) != null) {
      val item = items.first()
      data = data :+ translate(item)
      items = items.next()
    }

    EdnList(data)
  }

  def toVector(obj: clojure.lang.IPersistentVector): EdnVector = {
    var items = obj.asInstanceOf[clojure.lang.IPersistentStack]
    var vec: Vector[Edn] = Vector[Edn]()

    while (notEmpty.invoke(items) != null) {
      val item = items.peek()
      vec = translate(item) +: vec
      items = items.pop()
    }

    EdnVector(vec)
  }

  def toMap(obj: clojure.lang.IPersistentMap): EdnMap = {
    var keys = new clojure.core$keys().invoke(obj).asInstanceOf[clojure.lang.ISeq]
    var data: Map[Edn, Edn] = Map[Edn, Edn]()

    while (notEmpty.invoke(keys) != null) {
      val key = keys.first()
      data = data + (translate(key) -> translate(get.invoke(obj, key)))
      keys = keys.next()
    }
    EdnMap(data)
  }

  def toSet(obj: clojure.lang.IPersistentSet): EdnSet = {
    var items = obj.seq()
    var data: Set[Edn] = Set[Edn]()

    while (notEmpty.invoke(items) != null) {
      val item = items.first()
      data += translate(item)
      items = items.next()
    }

    EdnSet(data)
  }

  def toTagged(tagged: Tagged): Edn = tagged match {
    case Tagged(tag, value) =>
      EdnTag(translate(tag).asInstanceOf[EdnSymbol], translate(value))
  }

  val get = new clojure.core$get()
  val notEmpty = new core$not_empty()
}
