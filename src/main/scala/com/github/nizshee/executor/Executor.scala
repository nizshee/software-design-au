package com.github.nizshee.executor

/** Interface to execute strings. */
trait Executor {
  /** Method to execute string.
    *
    * @param line the string to be execute
    * @return result after execution
    */
  def execute(line: String): Stream[String]
}
