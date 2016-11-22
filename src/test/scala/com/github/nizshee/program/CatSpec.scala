package com.github.nizshee.program

import org.scalatest.FlatSpec

class CatSpec extends FlatSpec {

  val cat = new Cat

  "Cat" should "return input if no arguments" in {
    val input1 = List("aA")
    val output1 = cat.apply(List())(input1.toStream).toList

    assert(input1 == output1)

    val input2 = List("aA", "bB", "cC")
    val output2 = cat.apply(List())(input2.toStream).toList

    assert(input2 == output2)
  }

  it should "return content of file listed in arguments" in {
    val name1 = getClass.getResource("/a.txt").getPath
    val name2 = getClass.getResource("/b.txt").getPath

    val content1 = List("1", "2", "3")
    val content2 = List("a", "b", "c", "d")

    val output1 = cat.apply(List(name1))(Stream("something")).toList

    assert(content1 == output1)

    val output2 = cat.apply(List(name2, name1))(Stream("some", "thing")).toList

    assert(content2 ++ content1 == output2)
  }
}
