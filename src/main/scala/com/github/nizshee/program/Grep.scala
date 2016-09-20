package com.github.nizshee.program

import scala.io.Source


class Grep extends Program {
  import Grep._

  override def apply(arguments: List[String])(input: Stream[String]): Stream[String] = {
    parser.parse(arguments, Config()) match {
      case Some(config) =>
        val in = config.filename.map(Source.fromFile(_).getLines.toStream).getOrElse(input)
        val regex = if (config.caseSensitive) config.regex.r else ("(?i)" + config.regex).r
        var linesLeft = 0
        in.filter(str => {
          if (regex.findFirstMatchIn(str).nonEmpty) {
            linesLeft = config.linesCount
            true
          } else if (linesLeft > 0) {
            linesLeft -= 1
            true
          } else false
        })
      case None =>
        Stream()
    }
  }
}

object Grep {
  private case class Config(
                             linesCount: Int = 0,
                             caseSensitive: Boolean = true,
                             fullWorld: Boolean = false,
                             regex: String = "",
                             filename: Option[String] = None
                           )

  private val parser = new scopt.OptionParser[Config]("grep") {

    opt[Int]('A', "after-context").action( (count, config) =>
      config.copy(linesCount = count)
    )

    opt[Unit]('i', "ignore-case").action( (_, config) =>
      config.copy(caseSensitive = false)
    )

    opt[Unit]('w', "full-word").action( (_, config) =>
      config.copy(fullWorld = true)
    )

    arg[String]("<regex>").required().action( (regex, config) =>
      config.copy(regex = regex)
    )

    arg[String]("<filename>").optional().action( (filename, config) =>
      config.copy(filename = Some(filename))
    )

    override def terminate(exitState: Either[String, Unit]): Unit = ()

    override def showUsageOnError: Boolean = false
  }
}
