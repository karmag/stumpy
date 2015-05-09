package karmag.stumpy

import karmag.stumpy.TestHelper._
import org.scalatest.FunSuite

class SimpleTypes extends FunSuite {

  test("Nil, true, false") {
    assert(edn("nil") === EdnNil())
    assert(edn("true") === EdnBool(true))
    assert(edn("false") === EdnBool(false))
  }

  test("Strings and characters") {
    assert(edn(""""hello world"""") === EdnString("hello world"))
    assert(edn("\\c") === EdnChar('c'))
    assert(edn("\\newline") === EdnChar('\n'))
    assert(edn("\\u1234") === EdnChar('\u1234'))
  }

  test("Symbols") {
    assert(edn("sym") === EdnSymbol("sym"))
    assert(edn("namespace/sym") === EdnSymbol("sym", Some("namespace")))
  }

  test("Keywords") {
    assert(edn(":key") === EdnKeyword("key"))
    assert(edn(":namespace/key") == EdnKeyword("key", Some("namespace")))
  }

  test("Integers") {
    assert(edn("12345") === EdnInt(12345))
    assert(edn("12345N") === EdnInt(12345, true))
  }

  test("Floating point numbers") {
    assert(edn("123.45") === EdnFloat(123.45))
    assert(edn("123.45M") === EdnFloat(123.45, true))
  }
}
