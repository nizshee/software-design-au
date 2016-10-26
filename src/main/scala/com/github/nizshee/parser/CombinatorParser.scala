package com.github.nizshee.parser

import com.github.nizshee.argument.Argument
import com.github.nizshee.operation.Operation

import scala.util.parsing.combinator._

/** Realization of Parser interface on Scala Parser Combinators */
class CombinatorParser extends RegexParsers with Parser {

  val validTokens = "\\w\\.\\/"

  override def parse(line: String): Either[String, List[CommandAndArgs]] = parseAll(pipe, line) match {
    case Success(result, _) => Right(result)
    case NoSuccess(msg, _) => Left(msg)
  }

  def pipe: Parser[List[CommandAndArgs]] = repsep(commandAndArg, "|")
  def commandAndArg: Parser[CommandAndArgs] = operation ~ arguments ^^ {
    case operation ~ arguments =>(operation, arguments)
  }

  def argument: Parser[Argument] = simple | simple1 | substitution | substitution1
  def arguments: Parser[List[Argument]] = rep(argument)
  
  def simple: Parser[Argument.Simple] = s"[$validTokens]+".r ^^ Argument.Simple
  def simple1: Parser[Argument.Simple] = "'" ~ s"[$validTokens$$ ]+".r ~ "'" ^^ {
    case _ ~ text ~ _ => Argument.Simple(text)
  }

  def variable: Parser[Argument.Substitution.Variable] = "$" ~ s"[$validTokens]+".r ^^ {
    case _ ~ name => Argument.Substitution.Variable(name)
  }

  def variable1: Parser[Argument.Substitution.Variable] = "${" ~ s"[$validTokens]*".r ~ "}" ^^ {
    case _ ~ name ~ _ => Argument.Substitution.Variable(name)
  }

  def text: Parser[Argument.Substitution.Text] = s"[$validTokens ]+".r ^^ Argument.Substitution.Text
  def parts: Parser[List[Argument.Substitution.Part]] = rep(variable | variable1 | text)
  def substitution: Parser[Argument.Substitution] = ("\"" ~ parts ~ "\"") ^^ {
    case _ ~ parts ~ _ => Argument.Substitution(parts)
  }
  def substitution1: Parser[Argument.Substitution] = "$" ~ s"[$validTokens]+".r ^^ {
    case _ ~ name => Argument.Substitution(List(Argument.Substitution.Variable(name)))
  }

  def operation: Parser[Operation] = application | command
  def command: Parser[Operation.Command] = s"[$validTokens]+".r ^^ Operation.Command

  def application: Parser[Operation.Application] = s"[$validTokens]+=".r ^^ (name => Operation.Application(name dropRight 1))

}
