package com.github.nizshee.program

import java.io.{File, FileNotFoundException}
import java.nio.file.{Files, Paths}

/**
  * Class representing UNIX ls command
  */
class Ls extends Program {
  /** List directories passed in arguments.
    * List current directory if no arguments.
    *
    * @param arguments directories to list
    * @param input     unused
    * @return list of files and subdirectories in specified directories
    */
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] = {
    if (arguments.isEmpty) new File(".").listFiles.map(_.getPath).toStream
    val printSourceDir = if (arguments.size == 1) false else true
    arguments.flatMap(listDir(_, printSourceDir)).toStream
  }

  private def listDir(path: String, printSourceDir: Boolean): Stream[String] = {
    val currentDir = Paths.get(System.getProperty("user.dir"))
    val dir = currentDir.relativize(Paths.get(path)).toFile
    if (!dir.exists) {
      throw new FileNotFoundException(s"$path : directory doesn't exist")
    }
    if (!dir.isDirectory) {
      throw new RuntimeException(s"$path is not a directory")
    }

    val result = if (printSourceDir) Stream(s"${dir.toString}:") else Stream.empty
    result ++ dir.listFiles.map(_.getName).toStream
  }
}
