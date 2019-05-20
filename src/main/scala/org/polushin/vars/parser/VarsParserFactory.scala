package org.polushin.vars.parser

import org.polushin.vars.lexer.VarsLexer

import scala.io.Source
import scala.util.Try

class VarsParserFactory extends ParserFactory {

  override def parseFile(filename: String): Try[Parser] = Try {
    new VarsParser(this, new VarsLexer(Source.fromFile(filename + ".vars")), filename)
  }

}
