package com.github.nizshee.program


/** Concatenate arguments and redirect to output. */
class Echo extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
    Stream(arguments mkString " ")

}
