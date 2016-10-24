package com.github.nizshee.argument

import com.github.nizshee.executor.Context

/** Interface represent argument for command in interpreter. */
sealed trait Argument {

  /** Return string that will be argument for command in interpreter.
    *
    * @param context the context that can be use to explore environment.
    * @return string
    */
  def stringify(context: Context): String
}

/** Factory for creating [[com.github.nizshee.argument.Argument]] instances. */
object Argument {

  /** Argument that represent simple line.
    *
    * @param line some string
    * @return argument
    */
  case class Simple(line: String) extends Argument {
    override def stringify(context: Context): String = line
  }

  /** Argument that represent text with variables
    *
    * @param parts parts of string that can be either variable or text
    */
  case class Substitution(parts: List[Substitution.Part]) extends Argument {
    override def stringify(context: Context): String = parts.map {
      case Substitution.Variable(name) => context.environment.getOrElse(name, "")
      case Substitution.Text(text) => text
    } mkString ""
  }

  object Substitution {
    sealed trait Part
    case class Variable(name: String) extends Part
    case class Text(text: String) extends Part
  }
}


