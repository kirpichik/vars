package org.polushin.vars.parser

import org.polushin.vars.{RecursiveImportException, Scope}

import scala.util.{Failure, Success, Try}

class VarsImportStatement(factory: ParserFactory, importName: String, filename: String, line: Int) extends Statement {

  override def applyScope(scope: Scope, depth: List[String]): Try[Scope] = {
    if (isRecursiveImport(depth))
      Failure(new RecursiveImportException(filename, line))
    else factory.parseFile(importName) match {
      case Failure(exception) => Failure(exception)
      case Success(parser) => withResources(parser)(parser => applyParserScope(parser, scope, importName :: depth))
    }
  }

  private def applyParserScope(parser: Parser, scope: Scope, depth: List[String]): Try[Scope] = {
    parser.nextStatement() match {
      case Failure(exception) => Failure(exception)
      case Success(optStatement) => optStatement match {
        case None => Success(scope)
        case Some(statement) => statement.applyScope(scope, depth) match {
          case Success(_) => applyParserScope(parser, scope, depth)
          case Failure(exception) => Failure(exception)
        }
      }
    }
  }

  private def isRecursiveImport(depth: List[String]): Boolean = depth.contains(importName)

}
