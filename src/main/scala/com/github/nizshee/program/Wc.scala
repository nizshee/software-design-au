package com.github.nizshee.program


class Wc extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
    Stream(input.size.toString)
}
