package karmag.stumpy.read

import karmag.stumpy.{EdnInt, EdnString, EdnSymbol, EdnTag}
import karmag.stumpy.read.TestHelper._
import org.scalatest.FunSuite

class Tags extends FunSuite {

  test("Generic tags") {
    assert(edn("#hello world") ===
      EdnTag(EdnSymbol("hello"), EdnSymbol("world")))

    assert(edn("#my/tag 10") ===
      EdnTag(EdnSymbol("tag", Some("my")), EdnInt(10)))

    assert(edn("#first #second val") ===
      EdnTag(EdnSymbol("first"), EdnTag(EdnSymbol("second"), EdnSymbol("val"))))
  }

  test("Built in tags") {
    assert(edn("""#inst "2000-01-01"""") ===
      EdnTag(EdnSymbol("inst"), EdnString("2000-01-01")))

    assert(edn("""#uuid "abc-123"""") ===
      EdnTag(EdnSymbol("uuid"), EdnString("abc-123")))
  }
}
