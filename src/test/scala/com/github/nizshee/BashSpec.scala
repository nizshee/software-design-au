package com.github.nizshee


import com.github.nizshee.argument.Argument
import com.github.nizshee.operation.Operation
import com.github.nizshee.operation.Operation.Command
import com.github.nizshee.parser.Parser
import com.github.nizshee.program.Program
import org.scalatest.FlatSpec


class BashSpec extends FlatSpec {

  val parser = new Parser {
    override def parse(line: String): Either[String, List[(Operation, List[Argument])]] =
      Right(List((Command("ls"), List(Argument.Simple(getClass.getResource("/ls").getPath)))))
  }

  "Bash" should "run program if it present" in {

    val ls = Stream("a", "b", "c")
    val bash = new Bash(parser, null, Map("ls" -> new Program {
      override def apply(arguments: List[String])(input: Stream[String]): Stream[String] = ls
    }))

    assert(bash.execute("") == ls)
  }

  it should "run new process if program not present" in {
    val ls = Stream("1", "2", "3")

    val bash = new Bash(parser, null, Map())
    assert(bash.execute("") == ls)
  }

}
