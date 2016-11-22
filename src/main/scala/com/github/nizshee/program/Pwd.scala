package com.github.nizshee.program


/** Redirect to output current directory path. */
class Pwd extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
    Stream(System.getProperty("user.dir"))
}
