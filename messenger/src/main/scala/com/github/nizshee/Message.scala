package com.github.nizshee


/**
  * Class for message store.
  */
sealed trait Message

object Message {
  final case class Local(msg: String) extends Message
  final case class Remote(msg: String) extends Message
  final case class System(msg: String) extends Message
}
