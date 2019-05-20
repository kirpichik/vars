package org.polushin.vars

import org.polushin.vars.Scope.ConflictingDefinition

trait Scope {

  def isDefined(name: String): Boolean

  def define(defineInfo: DefineInfo): Option[ConflictingDefinition]

  def getDefinitions: Iterable[String]

}

object Scope {
  type ConflictingDefinition = (DefineInfo, DefineInfo)
}
