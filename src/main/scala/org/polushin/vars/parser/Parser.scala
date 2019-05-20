package org.polushin.vars.parser

import scala.util.Try

trait Parser extends AutoCloseable {

  def nextStatement(): Try[Option[Statement]]

}
