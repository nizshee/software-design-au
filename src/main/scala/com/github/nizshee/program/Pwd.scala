package com.github.nizshee.program


class Pwd extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
    Stream(System.getProperty("user.dir"))
}
