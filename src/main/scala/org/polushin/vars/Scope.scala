package org.polushin.vars

trait Scope {

  type ConflictingDefinition = (DefineInfo, DefineInfo)

  def isDefined(name: String): Boolean

  def define(defineInfo: DefineInfo): Option[ConflictingDefinition]

}
