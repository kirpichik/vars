package org.polushin.vars.parser

import org.polushin.vars.{ConflictingIdentifiersException, DefineInfo, Scope, UnknownIdentifierException}

import scala.util.{Failure, Success, Try}

class VarsAssignStatement(define: DefineInfo, value: String, isIdentifier: Boolean) extends Statement {

  override def applyScope(scope: Scope, depth: List[(String, Int)]): Try[Scope] = {
    scope.define(define) match {
      case None => if (isIdentifier && !scope.isDefined(value)) {
        Failure(new UnknownIdentifierException(value, depth, define.filename, define.line))
      } else
        Success(scope)
      case Some(conflict) => Failure(new ConflictingIdentifiersException(conflict, depth))
    }
  }

}
