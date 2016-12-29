package com.github.nizshee

import com.github.nizshee.parser.CombinatorParser
import com.github.nizshee.program._

import scala.io.StdIn


object Main {

  def main(args: Array[String]): Unit = {

    val parser = new CombinatorParser
    val bash = new Bash(parser, System.err,
      Map(
        "echo" -> new Echo,
        "cat" -> new Cat,
        "wc" -> new Wc,
        "pwd" -> new Pwd,
        "exit" -> new Exit,
        "grep" -> new Grep
      )
    )

    var input = ""
    val escapePhrases = List("exit", "", null)
    while (true) {
      input = StdIn.readLine("$")
      if (escapePhrases contains input) return
      val result = bash.execute(input)
      println(result mkString "\n")
    }
  }
}
