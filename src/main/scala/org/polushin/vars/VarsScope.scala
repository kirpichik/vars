package org.polushin.vars

import scala.collection.mutable

class VarsScope extends Scope {

  private val definitions = mutable.Map[String, DefineInfo]()

  override def isDefined(name: String): Boolean = definitions.contains(name)

  override def define(defineInfo: DefineInfo): Option[(DefineInfo, DefineInfo)] = definitions.get(defineInfo.name) match {
    case None => definitions += (defineInfo.name -> defineInfo); None
    case Some(other) => Some((defineInfo, other))
  }

  override def getDefinitions: Iterable[String] = definitions.keys
}
