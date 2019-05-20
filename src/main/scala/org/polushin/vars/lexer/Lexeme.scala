package org.polushin.vars.lexer

import org.polushin.vars.lexer.LexemeType.LexemeType

case class Lexeme(lexemeType: LexemeType, value: Option[String], filename: String, line: Int)
