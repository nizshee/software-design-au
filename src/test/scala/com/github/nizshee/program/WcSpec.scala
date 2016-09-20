package com.github.nizshee.program

import org.scalatest.FlatSpec


class WcSpec extends FlatSpec {

  val wc = new Wc

  "Wc" should "return count of line" in {
    val input = Stream("1", "2", "3", "4")
    assert(wc(Nil)(input) == Stream("4"))
  }
}
