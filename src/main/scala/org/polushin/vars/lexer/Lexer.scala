package org.polushin.vars.lexer

trait Lexer extends AutoCloseable {

  def nextLexeme(): Lexeme

}
