package org.polushin.vars.parser

import org.polushin.vars.DefineInfo
import org.polushin.vars.lexer._

import scala.util.Try

class VarsParser(factory: ParserFactory, lexer: Lexer, filename: String) extends Parser {

  private var currentLine = 1

  override def nextStatement(): Try[Option[Statement]] = Try {
    val statement = parseStatement()
    statement match {
      case Some(_) =>
        lexer.nextLexeme() match {
          case NewLine => currentLine += 1; statement
          case EOF => statement
          case _ => throw new ParseException("Expected new line or EOF at the end of the statement", filename, currentLine)
        }
      case None => None
    }
  }

  private def parseStatement(): Option[Statement] = lexer.nextLexeme() match {
    case Import => Some(parseImportStatement())
    case Identifier(varName) => Some(parseAssignStatement(varName))
    case NewLine => currentLine += 1; parseStatement()
    case EOF => None
    case lexeme => throw new ParseException(s"Unexpected lexeme: ${lexeme.getClass.getSimpleName}", filename, currentLine)
  }

  private def parseImportStatement(): Statement = lexer.nextLexeme() match {
    case Identifier(id) => new VarsImportStatement(factory, id, filename, currentLine)
    case _ => throw new ParseException("Expected filename after import keyword", filename, currentLine)
  }

  private def parseAssignStatement(varName: String): Statement = lexer.nextLexeme() match {
    case Equal => lexer.nextLexeme() match {
      case Identifier(id) => new VarsAssignStatement(DefineInfo(varName, filename, currentLine), id, true)
      case Number(num) => new VarsAssignStatement(DefineInfo(varName, filename, currentLine), num, false)
      case _ => throw new ParseException("Expected identifier or number in assign statement", filename, currentLine)
    }
    case _ => throw new ParseException("Expected '=' in assign statement", filename, currentLine)
  }

  override def close(): Unit = lexer.close()
}
