package org.polushin.vars

import org.polushin.vars.Scope.ConflictingDefinition

trait Scope {

  def isDefined(name: String): Boolean

  def define(defineInfo: DefineInfo): Option[ConflictingDefinition]

}

object Scope {
  type ConflictingDefinition = (DefineInfo, DefineInfo)
}

class UnknownIdentifierException(name: String, filename: String, line: Int)
  extends RuntimeException(s"Unknown identifier $name at $filename:$line")

class ConflictingIdentifiersException(conflict: ConflictingDefinition)
  extends RuntimeException(s"Conflicting definition ${conflict._1.name} " +
    s"at ${conflict._1.filename}:${conflict._1.line} and ${conflict._2.filename}:${conflict._2.line}")
