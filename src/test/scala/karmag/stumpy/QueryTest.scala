package karmag.stumpy

import org.scalatest.FunSuite
import karmag.stumpy.Query._

class QueryTest extends FunSuite {

  val person = EdnMap(Map(
    EdnKeyword("name") -> EdnString("karl"),
    EdnKeyword("has-computer") -> EdnBool(true)))

  val dog = EdnMap(Map(
    EdnKeyword("breed") -> EdnString("small"),
    EdnKeyword("likes") ->
      EdnSet(Set(EdnString("cars"), EdnString("balls")))))

  val creatures = EdnList(List(person, dog))

  test("Lookup") {
    assert(lookup(person, EdnKeyword("name")).head === EdnString("karl"))

    assert(lookup(creatures,
      EdnInt(1), EdnKeyword("likes"), EdnString("cars")).head ===
      EdnString("cars"))

    assert(lookup(EdnVector(Vector(creatures)),
      EdnInt(0), EdnInt(0)).head ===
      person)

    assert(lookup(creatures, EdnInt(10)) === None)
  }
}
