package com.github.nizshee.parser

import com.github.nizshee.argument.Argument.{Simple, Substitution}
import com.github.nizshee.argument.Argument.Substitution.{Text, Variable}
import com.github.nizshee.operation.Operation.{Application, Command}
import org.scalatest.FlatSpec


class CombinatorParserSpec extends FlatSpec {
  val parser: Parser = new CombinatorParser

  "Parser" should "fail on wrong output with error message" in {
    assert(parser.parse("\"").isLeft)
    assert(parser.parse("a==").isLeft)
  }

  it should "parse command and arguments" in {
    assert(parser.parse("aA") ==
      Right(
        List(
          Command("aA") -> List()
        )
      )
    )
    assert(parser.parse("aA bB cC dD eE fF") ==
      Right(
        List(
          Command("aA") -> List(Simple("bB"), Simple("cC"), Simple("dD"), Simple("eE"), Simple("fF"))
        )
      )
    )
    assert(parser.parse("aA bB 'cC dD' eE") ==
      Right(
        List(
          Command("aA") -> List(Simple("bB"), Simple("cC dD"), Simple("eE"))
        )
      )
    )
  }

  it should "parse substitutions" in {
    assert(parser.parse("aA $bB cC") ==
      Right(
        List(
          Command("aA") -> List(Substitution(List(Variable("bB"))), Simple("cC"))
        )
      )
    )
    assert(parser.parse("aA bB $cC") ==
      Right(
        List(
          Command("aA") -> List(Simple("bB"), Substitution(List(Variable("cC"))))
        )
      )
    )
    assert(parser.parse("aA \"$bB $cC\"") ==
      Right(
        List(
          Command("aA") -> List(Substitution(List(Variable("bB"), Variable("cC"))))
        )
      )
    )
    assert(parser.parse("aA \"bB $cC dD $eE\"") ==
      Right(
        List(
          Command("aA") -> List(Substitution(List(Text("bB "), Variable("cC"), Text("dD "), Variable("eE"))))
        )
      )
    )
  }

  it should "parse applications" in {
    assert(parser.parse("aA=bB") ==
      Right(
        List(
          Application("aA") -> List(Simple("bB"))
        )
      )
    )
    assert(parser.parse("aA=") ==
      Right(
        List(
          Application("aA") -> List()
        )
      )
    )
    assert(parser.parse("aA=bB cC dD") ==
      Right(
        List(
          Application("aA") -> List(Simple("bB"), Simple("cC"), Simple("dD"))
        )
      )
    )
  }

  it should "parse pipes" in {
    assert(parser.parse("aA | bB | cC") ==
      Right(
        List(
          Command("aA") -> List(),
          Command("bB") -> List(),
          Command("cC") -> List()
        )
      )
    )
    assert(parser.parse("aA bB | cC dD") ==
      Right(
        List(
          Command("aA") -> List(Simple("bB")),
          Command("cC") -> List(Simple("dD"))
        )
      )
    )
  }
}
