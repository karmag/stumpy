package karmag.stumpy

import org.scalatest.FunSuite
import karmag.stumpy.Query._

class QueryTest extends FunSuite {

  val person = EdnMap(Map(
    EdnKeyword("name") -> EdnString("karl"),
    EdnKeyword("has-computer") -> EdnBool(value = true)))

  val dog = EdnMap(Map(
    EdnKeyword("breed") -> EdnString("small"),
    EdnKeyword("likes") ->
      EdnSet(Set(EdnString("cars"), EdnString("balls")))))

  val creatures = EdnList(List(person, dog))

  val tagged = EdnTag(EdnSymbol("tag"), creatures)

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

  def isEdnString(edn: Edn) = {
    edn match {
      case _: EdnString => true
      case _ => false
    }
  }

  test("Search") {
    assert(search(creatures, isEdnString).toSet ===
      Set(EdnString("karl"), EdnString("small"), EdnString("cars"), EdnString("balls")))

    assert(search(tagged, isEdnString).toSet ===
      Set(EdnString("karl"), EdnString("small"), EdnString("cars"), EdnString("balls")))
  }
}
