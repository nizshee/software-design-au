package com.github.nizshee.parser

import com.github.nizshee.argument.Argument
import com.github.nizshee.operation.Operation

/** Interface of parser for input strings. */
trait Parser {
  type CommandAndArgs = (Operation, List[Argument])

  /** Method for parsing input line.
    *
    * @param line the line that should be parsed
    * @return either parsed string or error message
    */
  def parse(line: String): Either[String, List[CommandAndArgs]]
}
