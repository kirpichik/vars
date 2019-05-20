package org.polushin.vars.parser

import org.polushin.vars.Scope

import scala.util.Try

class VarsImportStatement(factory: ParserFactory, filename: String) extends Statement {

  override def applyScope(scope: Scope): Try[Scope] = ???

}
