package org.polushin.vars.parser

import org.polushin.vars.Scope

import scala.util.Try

class VarsAssignStatement(value: String, isIdentifier: Boolean) extends Statement {
  override def applyScope(scope: Scope): Try[Scope] = ???
}
