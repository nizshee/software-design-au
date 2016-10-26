package com.github.nizshee.program

import org.scalatest.FlatSpec


class GrepSpec extends FlatSpec {

  val grep = new Grep
  val input = Stream("aA", "bB", "cC", "BB", "dD", "w", "ww")

  "Grep" should "find lines with regex" in {
    assert(grep(List("b"))(input) == Stream("bB"))
  }

  it should "find more lines with key" in {
    assert(grep(List("b", "-A", "1"))(input) == Stream("bB", "cC"))
  }

  it should "find by regex" in {
    assert(grep(List("^c"))(input) == Stream("cC"))
    assert(grep(List("c$"))(input) == Stream())
  }

  it should "ignore case" in {
    assert(grep(List("b", "-i"))(input) == Stream("bB", "BB"))
  }

  it should "find words" in {
    assert(grep(List("w"))(input) == Stream("w", "ww"))
    assert(grep(List("w", "-w"))(input) == Stream("w"))
  }
}
