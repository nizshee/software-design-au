package com.github.nizshee

import java.io.{ByteArrayInputStream, IOException}

import com.github.nizshee.executor.{Context, Executor}
import com.github.nizshee.operation.Operation.{Application, Command}
import com.github.nizshee.parser.Parser
import com.github.nizshee.program.Program

import scala.sys.process._

/** Class that parses and executes sequence of parameters.
  *
  * @param parser the parser that will be used for parse
  * @param err the error stream
  * @param programs set of programs that class can execute
  */
class Bash(parser: Parser, err: java.io.OutputStream, programs: Map[String, Program]) extends Executor {

  var environment: Map[String, String] = Map()

  override def execute(line: String): Stream[String] = {
    parser.parse(line) match {
      case Left(msg) =>
        err.write(msg.getBytes)
        Stream.empty
      case Right(result) =>
        result.foldLeft(Stream.empty: Stream[String]) { case (prev, (operation, arguments)) =>
          operation match {
            case Application(name) if arguments.isEmpty =>
              environment += name -> ""
              prev
            case Application(name) if arguments.length == 1 =>
              environment += name -> arguments.head.stringify(Context(environment, this))
              prev
            case Application(name) =>
              err.write(s"Command '$arguments' not found.".toByte)
              prev
            case Command(name) =>
              val args = arguments.map(_.stringify(Context(environment, this)))
              try {
                resolution(name)(args)(prev)
              } catch {
                case e: Exception =>
                  err.write(e.getMessage.getBytes)
                  Stream()
              }
          }
        }
    }
  }

  private def resolution(name: String): Program = programs.getOrElse(name, executeNative(name))

  private def executeNative(command: String): Program = new Program {
    override def apply(arguments: List[String])(input: Stream[String]): Stream[String] =
      try {
        val bytes = input.mkString("\n").getBytes
        val inputStream = new ByteArrayInputStream(bytes)
        var out: Stream[String] = Stream()
        ((command :: arguments) #< inputStream) !
          ProcessLogger(txt => out = out :+ txt, txt => err.write(txt.getBytes))
        out
      } catch {
        case e: RuntimeException =>
          err.write(e.getMessage.getBytes)
          Stream.empty
        case e: IOException =>
          err.write(e.getMessage.getBytes)
          Stream.empty
      }
  }

}
