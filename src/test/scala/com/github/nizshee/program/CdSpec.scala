package com.github.nizshee.program

import java.io.FileNotFoundException
import java.nio.file.Paths

import org.scalatest.FlatSpec

/**
  * Created by fb on 10/24/16.
  */
class CdSpec extends FlatSpec {

  val cd = new Cd

  it should "change current directory to home" in {
    val baseDir = System.getProperty("user.dir")
    assert(System.getProperty("user.home") != baseDir)

    cd(List.empty)(Stream.empty)

    assert(System.getProperty("user.home") == System.getProperty("user.dir"))

    System.setProperty("user.dir", baseDir)
  }

  it should "change current directory to specified" in {
    val baseDir = System.getProperty("user.dir")
    val cdPath = getClass.getResource("/ls").getPath

    cd(List(cdPath))(Stream.empty)

    assert(System.getProperty("user.dir") == Paths.get(cdPath).toString)

    System.setProperty("user.dir", baseDir)
  }

  it should "throw if new directory doesn't exists" in {
    assertThrows[FileNotFoundException] {
      cd(List("/nonexistent"))(Stream.empty)
    }
  }
}
