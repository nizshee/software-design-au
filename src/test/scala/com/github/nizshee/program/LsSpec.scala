package com.github.nizshee.program

import java.io.FileNotFoundException
import java.nio.file.Paths

import org.scalatest.FlatSpec

class LsSpec extends FlatSpec {

  val ls = new Ls

  it should "list files in one directory" in {
    val dir = getClass.getResource("/").getPath

    val output = ls(List(dir))(Stream.empty).toList
    val result = List("ls", "com", "a.txt", "b.txt")

    assert(output == result)
  }

  it should "list files in several directories" in {
    val currentDir = Paths.get(getClass.getResource("/").getPath)
    val lsDir = Paths.get(getClass.getResource("/ls").getPath)

    val output = ls(List(currentDir.toString, lsDir.toString))(Stream.empty).toList
    val result = List("target/scala-2.11/test-classes:", "ls", "com", "a.txt", "b.txt") ++
                 List("target/scala-2.11/test-classes/ls:", "2", "3", "1")

    assert(output == result)
  }

  it should "throw exception if file doesn't exists" in {
    assertThrows[FileNotFoundException] {
      ls(List("/nonexistent"))(Stream.empty)
    }
  }
}
