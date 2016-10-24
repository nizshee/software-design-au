package com.github.nizshee.program

import org.scalatest.FlatSpec


class ExitSpec extends FlatSpec {

  val exit = new Exit

  "Exit" should "return empty string" in {
    assert(exit.apply(List("anything"))(Stream("anything")) == Stream())
  }

}
