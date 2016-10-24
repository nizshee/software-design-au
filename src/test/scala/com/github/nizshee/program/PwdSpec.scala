package com.github.nizshee.program

import org.scalatest.FlatSpec


class PwdSpec extends FlatSpec {

  val pwd = new Pwd

  "Pwd" should "return current directory" in {
    assert(pwd(Nil)(Stream("")) == Stream(System.getProperty("user.dir")))
  }
}
