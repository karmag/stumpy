package karmag.stumpy.read

import java.io.StringReader

import karmag.stumpy.{EdnInt, Read}
import org.scalatest.FunSuite

class ReadAll extends FunSuite {

  test("Reading stream fully") {
    assert(Read.allEdn(new StringReader("1 2 3")) ===
      Right(List(EdnInt(1), EdnInt(2), EdnInt(3))))

    assert(Read.allEdn(new StringReader("1 2 [")) ===
      Left("EOF while reading"))
  }
}
