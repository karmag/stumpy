package karmag.stumpy.clj

import java.io.PushbackReader
import java.util

object Clj {
  val EOF = new Object

  def readEdn(in: PushbackReader): Object = {
    val readers = new util.HashMap[Object, Object]()
    readers.put(symbol("uuid"), (value: Object) => Tagged(symbol("uuid"), value))
    readers.put(symbol("inst"), (value: Object) => Tagged(symbol("inst"), value))

    val options = new java.util.HashMap[Object, Object]()
    options.put(keyword("eof"), EOF)
    options.put(keyword("readers"), map(readers))
    options.put(keyword("default"),
      (tag: Object, value: Object) => Tagged(tag, value))

    val reader = new clojure.edn$read
    reader.invoke(map(options), in)
  }

  def map(data: util.HashMap[Object, Object]): clojure.lang.IPersistentMap = {
    clojure.lang.PersistentArrayMap.create(data)
  }

  def keyword(str: String): clojure.lang.Keyword = {
    clojure.lang.Keyword.intern(str)
  }

  def symbol(str: String): clojure.lang.Symbol = {
    clojure.lang.Symbol.intern(str)
  }

  def isKeyword(obj: Object) = obj.isInstanceOf[clojure.lang.Keyword]
  def isSymbol(obj: Object) = obj.isInstanceOf[clojure.lang.Symbol]
}

case class Tagged(tag: Object, value: Object)
