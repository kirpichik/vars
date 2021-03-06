package org.polushin.vars.parser

import org.polushin.vars.Scope

import scala.util.Try

trait Statement {

  def applyScope(scope: Scope, depth: List[(String, Int)]): Try[Scope]

}
