package org.polushin.vars.lexer

import scala.util.Try

trait Lexer extends AutoCloseable {

  def nextLexeme(): Try[Lexeme]

}
