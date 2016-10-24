package com.github.nizshee.program

/** Interface that represents command to execute. */
trait Program {
  /** Method to execute command.
    *
    * @param arguments string that command can use
    * @param input the input data that is output of previous command
    * @return output
    */
  def apply(arguments: List[String])(input: Stream[String]): Stream[String]
}
