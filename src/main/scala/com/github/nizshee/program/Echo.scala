package com.github.nizshee.program


class Echo extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
    Stream(arguments mkString " ")

}
