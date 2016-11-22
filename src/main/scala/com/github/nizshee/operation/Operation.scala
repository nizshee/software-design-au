package com.github.nizshee.operation

/** Class that represents either command execution or
  * changing environment variable after parsing.
  */
sealed trait Operation

/** Factory for creating Operation instances. */
object Operation {
  /** Class that represents change of environment variable. */
  case class Application(name: String) extends Operation

  /** Class that represents command execution. */
  case class Command(name: String) extends Operation
}