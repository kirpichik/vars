package org.polushin.vars.lexer

object LexemeType extends Enumeration {

  type LexemeType = Value

  val
  Invalid, // Invalid lexeme
  EOF, // End of file
  Import, // "import" keyword
  Identifier, // file name or var name
  Equal, // "=" sign
  Number // some number
  = Value

}
