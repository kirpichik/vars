package org.polushin.vars.lexer

import scala.io.Source

class VarsLexer(source: Source) extends Lexer {

  private val buffer = source.buffered

  override def nextLexeme(): Lexeme = nextNativeToken match {
    case Character(' ') | Character('\t') => nextLexeme()
    case Character('\r') => lookupNextChar match {
      case None => Character('\r')
      case Some(c) => c match {
        case '\n' => nextChar(); NewLine
        case _ => nextLexeme()
      }
    }
    case Character('\n') => NewLine
    case lexeme => lexeme
  }

  private def nextChar() = buffer.next()

  private def lookupNextChar: Option[Char] = {
    if (buffer.hasNext)
      Some(buffer.head)
    else
      None
  }

  private def parseNumber(s: String): Lexeme = lookupNextChar match {
    case None => Number(s)
    case Some(next) => next match {
      case c if c.toString.matches("[0-9]") => parseNumber(s + nextChar())
      case _ => Number(s)
    }
  }

  private def parseIdentifier(s: String): Lexeme = lookupNextChar match {
    case None => if (s == "import") Import else Identifier(s) // There is only one keyword in the language
    case Some(next) => next match {
      case c if c.toString.matches("[_a-zA-Z0-9]") => parseIdentifier(s + nextChar())
      case _ => if (s == "import") Import else Identifier(s)
    }
  }

  private def parseOperator: Lexeme = {
    nextChar()
    Equal // There is only one operator in the language
  }

  private def nextNativeToken: Lexeme = lookupNextChar match {
    case None => EOF
    case Some(next) => next match {
      case c if c.isDigit => parseNumber(nextChar().toString)
      case c if c.toString.matches("[_a-zA-Z]") => parseIdentifier(nextChar().toString)
      case c if VarsLexer.isOperator(c) => parseOperator
      case _ => Character(nextChar())
    }
  }

  override def close(): Unit = source.close()
}

object VarsLexer {
  val operators = "="

  def isOperator(c: Char): Boolean = isOperator(c.toString)

  def isOperator(s: String): Boolean = s.matches("[" + operators + "]")
}
