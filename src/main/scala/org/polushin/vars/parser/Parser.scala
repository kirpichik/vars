package org.polushin.vars.parser

import scala.util.Try

trait Parser extends AutoCloseable {

  def nextStatement(): Try[Option[Statement]]

}

class ParseException(reason: String, filename: String, line: Int)
  extends RuntimeException(s"Parse error at $filename:$line because of $reason")
