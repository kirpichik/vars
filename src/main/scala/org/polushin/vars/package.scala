package org.polushin

import org.polushin.vars.Scope.ConflictingDefinition

package object vars {

  // TODO - improve with depth stack

  class UnknownIdentifierException(name: String, filename: String, line: Int)
    extends RuntimeException(s"Unknown identifier $name at $filename:$line")

  class ConflictingIdentifiersException(conflict: ConflictingDefinition)
    extends RuntimeException(s"Conflicting definition ${conflict._1.name} " +
      s"at ${conflict._1.filename}:${conflict._1.line} and ${conflict._2.filename}:${conflict._2.line}")

  class RecursiveImportException(filename: String, line: Int)
    extends RuntimeException(s"Recursive import at $filename:$line")

}
