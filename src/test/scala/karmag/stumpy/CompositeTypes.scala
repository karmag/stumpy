package karmag.stumpy

import org.scalatest.FunSuite
import TestHelper._

class CompositeTypes extends FunSuite {

  test("Lists") {
    assert(edn("()") === EdnList(List()))

    assert(edn("(1 2 3)") ===
      EdnList(List(EdnInt(1), EdnInt(2), EdnInt(3))))

    assert(edn("(true false sunday)") ===
      EdnList(List(EdnBool(true), EdnBool(false), EdnSymbol("sunday"))))
  }

  test("Vectors") {
    assert(edn("[]") === EdnVector(Vector()))

    assert(edn("[1 2 3]") ===
      EdnVector(Vector(EdnInt(1), EdnInt(2), EdnInt(3))))

    assert(edn("[true false sunday]") ===
      EdnVector(Vector(EdnBool(true), EdnBool(false), EdnSymbol("sunday"))))
  }

  test("Maps") {
    assert(edn("{}") === EdnMap(Map()))

    assert(edn("""{:alpha 1 beta "-"}""") ===
      EdnMap(Map(
        EdnKeyword("alpha") -> EdnInt(1),
        EdnSymbol("beta") -> EdnString("-"))))

    assert(edn("""{1 "one", 1.0 "one.zero"}""") ===
      EdnMap(Map(
        EdnInt(1) -> EdnString("one"),
        EdnFloat(1) -> EdnString("one.zero"))))
  }

  test("Sets") {
    assert(edn("#{}") === EdnSet(Set()))
    assert(edn("#{1 2 3}") === EdnSet(Set(EdnInt(1), EdnInt(2), EdnInt(3))))
    assert(edn("#{1 1.0}") === EdnSet(Set(EdnInt(1), EdnFloat(1))))
  }
}
