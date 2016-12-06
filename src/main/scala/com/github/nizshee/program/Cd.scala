package com.github.nizshee.program

import java.io.{File, FileNotFoundException}
import java.nio.file.{Path, Paths}

/**
  * Class representing UNIX cd command
  */
class Cd extends Program {
  /** Changes current working directory to one that specified in arguments.
    * Changes current working directory to user home if no arguments.
    *
    * @param arguments string that command can use
    * @param input     unused
    * @return empty stream
    */
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] = {
    val dir = if (arguments.isEmpty) System.getProperty("user.home")
              else getCurrentDir.resolve(Paths.get(arguments.head)).normalize.toAbsolutePath.toString
    val dirf = new File(dir)
    if (!dirf.exists || !dirf.isDirectory) {
      throw new FileNotFoundException(s"$dir: directory doesn't exist")
    }
    System.setProperty("user.dir", dir)
    Stream.empty[String]
  }

  private def getCurrentDir: Path =
    Paths.get(System.getProperty("user.dir"))
}
