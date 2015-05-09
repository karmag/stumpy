package karmag.stumpy.create

import karmag.stumpy._
import karmag.stumpy.Create._
import org.scalatest.FunSuite

class CreateTest extends FunSuite {

  test("Creation") {
    assert(nil === EdnNil())
    assert(true_ === EdnBool(true))
    assert(false_ === EdnBool(false))

    assert(str("qwe") === EdnString("qwe"))
    assert(char('a') === EdnChar('a'))
  }

  test("Integers") {
    assert(int(10) === EdnInt(10, false))
    assert(int(11, true) === EdnInt(11, true))

    assert(int(BigInt(100)) == EdnInt(100, true))
    assert(int(BigInt(200), false) === EdnInt(200, false))
  }

  test("Float point numbers") {
    assert(float(1.5) === EdnFloat(1.5))
    assert(float(2.5, true) === EdnFloat(2.5, true))

    assert(float(BigDecimal(5.5)) === EdnFloat(5.5, true))
    assert(float(BigDecimal(7.5), false) === EdnFloat(7.5, false))
  }

  test("Symbols, keywords") {
    assert(sym("alpha") === EdnSymbol("alpha"))
    assert(sym("ns", "alpha") === EdnSymbol("alpha", Some("ns")))

    assert(kw("beta") === EdnKeyword("beta"))
    assert(kw("ns", "beta") === EdnKeyword("beta", Some("ns")))
  }

  test("Collections") {
    val items = List(kw("keyword"), sym("symbol"))

    assert(list(items) === EdnList(items.toList))
    assert(vec(items) === EdnVector(items.toVector))
    assert(set(items) === EdnSet(items.toSet))
    assert(map(Map(kw("a") -> int(1), sym("b") -> float(2))) ===
      EdnMap(Map(
        EdnKeyword("a") -> EdnInt(1),
        EdnSymbol("b") -> EdnFloat(2))))
  }

  test("Tag") {
    assert(tag(sym("asd"), true_) ===
      EdnTag(EdnSymbol("asd"), EdnBool(value = true)))
  }
}
