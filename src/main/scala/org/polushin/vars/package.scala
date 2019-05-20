package org.polushin

import org.polushin.vars.Scope.ConflictingDefinition

package object vars {

  private def buildDepthStack(depth: List[(String, Int)]): String = {
    depth.filter(_._2 != 0).foldLeft("")((result, item) => {
      val (name, line) = item
      s"In file '$name' included at line $line\n" + result
    })
  }

  class UnknownIdentifierException(name: String, depth: List[(String, Int)], filename: String, line: Int)
    extends RuntimeException(UnknownIdentifierException.message(name, depth, filename, line))

  object UnknownIdentifierException {
    private def message(name: String, depth: List[(String, Int)], filename: String, line: Int): String = {
      buildDepthStack(depth) + s"Unknown identifier '$name' in file '$filename' at line $line"
    }
  }

  class ConflictingIdentifiersException(conflict: ConflictingDefinition, depth: List[(String, Int)])
    extends RuntimeException(ConflictingIdentifiersException.message(conflict, depth))

  object ConflictingIdentifiersException {
    private def message(conflict: ConflictingDefinition, depth: List[(String, Int)]): String = {
      val (first, second) = conflict
      buildDepthStack(depth) + s"Conflicting definition '${first.name}' " +
        s"in file '${first.filename}' at line ${first.line} and in file '${second.filename}' at line ${second.line}"
    }
  }

  class RecursiveImportException(importName: String, filename: String, line: Int, depth: List[(String, Int)])
    extends RuntimeException(RecursiveImportException.message(importName, filename, line, depth))

  object RecursiveImportException {
    private def message(importName: String, filename: String, line: Int, depth: List[(String, Int)]): String = {
      buildDepthStack(depth) + s"Recursive import '$importName' in '$filename' at line $line"
    }
  }
}
