package org.polushin.vars.parser

import scala.util.Try

trait Parser extends AutoCloseable {

  def nextUnit(): Try[ParseUnit]

}
