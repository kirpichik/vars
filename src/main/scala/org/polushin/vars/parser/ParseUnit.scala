package org.polushin.vars.parser

import org.polushin.vars.DefineInfo

import scala.util.Try

trait ParseUnit {

  def collectDefinitions(): Try[Seq[DefineInfo]]

}
