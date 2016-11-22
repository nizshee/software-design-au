package com.github.nizshee.program


/** Forward empty output. */
class Exit extends Program {
  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] = Stream()
}
