package org.polushin.vars.parser

import scala.util.Try

trait ParserFactory {

  def parseFile(filename: String): Try[Parser]

}
