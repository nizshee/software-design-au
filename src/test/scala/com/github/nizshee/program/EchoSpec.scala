package com.github.nizshee.program

import org.scalatest.FlatSpec


class EchoSpec extends FlatSpec {

  val echo = new Echo

  "Echo" should "return empty string if no arguments" in {
    assert(echo.apply(List())(Stream("anything")) == Stream(""))
  }

  it should "return string with arguments otherwise" in {
    assert(echo.apply(List("aA"))(Stream("anything")) == Stream("aA"))
    assert(echo.apply(List("aA", "bB"))(Stream("anything")) == Stream("aA bB"))
  }
}
