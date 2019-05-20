package org.polushin.vars.lexer

abstract class Lexeme

// End of file
case object EOF extends Lexeme

// New line
case object NewLine extends Lexeme

// Import keyword
case object Import extends Lexeme

// Filename or variable name
case class Identifier(word: String) extends Lexeme

// Equal sign
case object Equal extends Lexeme

// Number value
case class Number(word: String) extends Lexeme

// Single character (special type for unknown symbols)
case class Character(c: Char) extends Lexeme
