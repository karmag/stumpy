package karmag.stumpy.clj

import java.io.PushbackReader
import java.util

object Clj {
  val EOF = new Object

  val taggedReader = new TaggedReader
  val uuidReader = new BuiltinReader(symbol("uuid"))
  val instReader = new BuiltinReader(symbol("inst"))

  def readEdn(in: PushbackReader): Object = {
    val readers = new util.HashMap[Object, Object]()
    readers.put(symbol("uuid"), uuidReader)
    readers.put(symbol("inst"), instReader)

    val options = new java.util.HashMap[Object, Object]()
    options.put(keyword("eof"), EOF)
    options.put(keyword("readers"), map(readers))
    options.put(keyword("default"), taggedReader)

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

class TaggedReader extends clojure.lang.AFn {
  override def invoke(tag: Object, value: Object): Object = {
    Tagged(tag, value)
  }
}

class BuiltinReader(val tag: clojure.lang.Symbol) extends clojure.lang.AFn {
  override def invoke(value: Object): Object = {
    Tagged(tag, value)
  }
}

case class Tagged(tag: Object, value: Object)
