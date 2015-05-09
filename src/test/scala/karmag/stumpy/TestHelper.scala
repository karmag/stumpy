package karmag.stumpy

import java.io.StringReader

import org.scalatest.FunSuite

object TestHelper extends FunSuite {
  def edn(str: String): Edn = {
    Read.edn(new StringReader(str)) match {
      case Left(msg) => fail(s"Failed to parse '$str' -> '$msg'")
      case Right(edn) => edn
    }
  }
}
